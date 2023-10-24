package com.gftproject.shoppingcart.services;

import com.gftproject.shoppingcart.ProductData;
import com.gftproject.shoppingcart.exceptions.NotEnoughStockException;
import com.gftproject.shoppingcart.exceptions.ProductNotFoundException;
import com.gftproject.shoppingcart.exceptions.UserNotFoundException;
import com.gftproject.shoppingcart.model.Cart;
import com.gftproject.shoppingcart.model.ProductDTO;
import com.gftproject.shoppingcart.model.Status;
import com.gftproject.shoppingcart.model.User;
import com.gftproject.shoppingcart.repositories.CartProductsRepository;
import com.gftproject.shoppingcart.repositories.CartRepository;
import com.gftproject.shoppingcart.repositories.CountryRepository;
import com.gftproject.shoppingcart.repositories.PaymentRepository;
import org.antlr.v4.runtime.misc.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.*;

import static com.gftproject.shoppingcart.CartsData.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;


class ShoppingCartServiceTest {
    @InjectMocks
    ShoppingCartServiceImpl service;

    @Mock
    CartRepository cartRepository;

    @Mock
    CountryRepository countryRepository;

    @Mock
    PaymentRepository paymentRepository;

    @Mock
    CartComputationsService computationsService;

    @Mock
    ProductServiceImpl productService;

    @Mock
    UserServiceImpl userService;

    @Mock
    CartProductsRepository cartProductsRepository;


    private List<Cart> carts;

    @BeforeEach
    void setUp() {
        // Instantiate Shopping cart Service and instantiate con new mock de repository
        MockitoAnnotations.openMocks(this);
        service = new ShoppingCartServiceImpl(cartRepository, computationsService, productService, userService, countryRepository, paymentRepository, cartProductsRepository);
        carts = Arrays.asList(Optional.of(createCart001()).orElseThrow(), createCart002(), createCart003());
    }

    @Test
    @DisplayName("GIVEN an userId WHEN a cart is created THEN returns the created cart associated to the user")
    void createCart() throws UserNotFoundException {
        Long userId = 1L;
        Cart expectedCart = new Cart();
        expectedCart.setUserId(userId);

        when(cartRepository.save(any(Cart.class))).thenReturn(expectedCart);

        Cart createdCart = service.createCart(userId);

        verify(cartRepository, times(1)).save(any(Cart.class));
        assertThat(createdCart.getUserId()).isEqualTo(userId);
    }

    @Test
    @DisplayName("GIVEN the method is called WHEN findAll THEN provide a list of all carts")
    void findAll() {
        //Given
        when(cartRepository.findAll()).thenReturn(carts);

        //when
        List<Cart> allCarts = service.findAll();

        //then
        assertThat(allCarts).isNotNull().hasSize(3);
        verify(cartRepository).findAll();
    }

    @Test
    @DisplayName("GIVEN a status WHEN the method is called THEN a filtered list of carts should be provided")
    void findAllByStatus() {
        //Given
        when(cartRepository.findAllByStatus(any())).thenReturn(carts);

        //when
        List<Cart> allCarts = service.findAllByStatus(Status.DRAFT);

        //then
        assertThat(allCarts).isNotNull().hasSize(3);

        verify(cartRepository).findAllByStatus(Status.DRAFT);
    }

    @Test
    @DisplayName("GIVEN a cart Id  WHEN cart is submitted  THEN status is submitted")
    void submitCartStock() throws ProductNotFoundException, NotEnoughStockException, UserNotFoundException {
        when(cartRepository.findById(any())).thenReturn(Optional.of(createCart001()));
//        when(productService.getProductsByIds(any())).thenReturn(getWarehouseStock());
        when(userService.getUserById(any())).thenReturn(new User(1L, "SPAIN", "VISA")); // Need to talk with user microservice
        when(computationsService.computeFinalWeightAndPrice(anyList(), anyList())).thenReturn(new Pair<>(new BigDecimal(3), new BigDecimal(25)));
        // Return the cart (the argument of the function) instead of execute the save in the repository.
        // As we change status, price and weight in the cart (the argument of the function) we can check the method works because the cart is changing. 
        //Now we can see if the cart change the STATUS and get the created pair
        when(cartRepository.save(any(Cart.class))).thenAnswer(invocation -> invocation.getArgument(0)); // Return submitted cart
        
        //TODO mock the country and payment repository to avoid NotFoundError
        Cart submittedCart = service.submitCart(1L);

        // Verify that the service method correctly calls the repository
        verify(cartRepository).findById(1L);
        //verify(cartRepository).save(any());
        verify(userService).getUserById(submittedCart.getUserId());
        verify(computationsService).computeFinalWeightAndPrice(anyList(), anyList());

        assertThat(submittedCart).isNotNull();
        assertThat(submittedCart.getFinalPrice()).isNotZero().isEqualTo(new BigDecimal(25));
        assertThat(submittedCart.getStatus()).isEqualTo(Status.SUBMITTED);
        assertThat(submittedCart.getId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("GIVEN a cart Id with products without stock  WHEN cart is submitted  THEN error is shown")
    void submitCartNoStock() throws ProductNotFoundException, UserNotFoundException {
        when(cartRepository.findById(any())).thenReturn(Optional.of(createCart001()));
//        when(productService.getProductsByIds(any())).thenReturn(getWarehouseStock());

    
        //TODO mock all the repositories to test only testSumbit
        // Act and Assert
        assertThrows(NotEnoughStockException.class, () -> {
            service.submitCart(1L); // Submit the cart
        });

        // Verify that the service method correctly calls the repository and other services
        verify(cartRepository).findById(1L);
//        verify(productService).getProductsByIds(anyList());
        verify(userService).getUserById(any());

        // Verify that the cart remains in "DRAFT" status
        //assertEquals(Status.DRAFT, cart.getStatus());
    }

    @Test
    @DisplayName("GIVEN a cart Id and products with quantity WHEN addProductToCartWithQuantity THEN add product to cart and check stock")
    void addProductToCartWithQuantity() throws ProductNotFoundException, NotEnoughStockException {
        Cart cart = new Cart(1L, 1L, new ArrayList<>(), Status.DRAFT, new BigDecimal(14), BigDecimal.ZERO);
        ProductDTO product = new ProductDTO(1L, new BigDecimal(3), 5, new BigDecimal(4));

        when(cartRepository.findById(any())).thenReturn(Optional.of(cart));
        when(cartRepository.save(any())).thenReturn(cart);
        when(productService.getProductById(any())).thenReturn(product);

        Cart updatedCart = service.addProductToCartWithQuantity(1L, 5L, 5L, 5);

        assertThat(updatedCart).isNotNull();
        assertThat(updatedCart.getId()).isEqualTo(1L);
//        assertThat(updatedCart.getProductList()).containsKey(5L);
        verify(cartRepository).save(cart);
    }

    @Test
    @DisplayName("GIVEN a list of updated products WHEN we receive updated products THEN upda")
    void updateProductsFromCarts() {
        // Create sample data for testing

        Cart cart1 = new Cart(1L, 1L, new ArrayList<>(),Status.DRAFT, new BigDecimal("0.0"), new BigDecimal("0.0"));
        Cart cart2 = new Cart(2L, 2L, new ArrayList<>(),Status.DRAFT, new BigDecimal("0.0"), new BigDecimal("0.0"));

        // Mock repository behavior
        when(cartRepository.findCartsByProductIds(anyList())).thenReturn(List.of(cart1, cart2));
        when(cartRepository.save(any(Cart.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Test the service method
        service.updateProductsFromCarts(ProductData.getWarehouseStock());

        // Assertions
        verify(cartRepository, Mockito.times(2)).save(any(Cart.class));
        verify(cartRepository, times(2)).save(any());
    }

    @Test
    @DisplayName("GIVEN cartId WHEN deleteCart is executed THEN Delete a cart object")
    void deleteCart() {
        // Given
        doNothing().when(cartRepository).deleteById(1L);

        // When
        service.deleteCart(1L);

        // Then
        verify(cartRepository).deleteById(1L);
    }
}





