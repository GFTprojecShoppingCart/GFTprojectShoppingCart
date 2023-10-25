package com.gftproject.shoppingcart.services;

import com.gftproject.shoppingcart.ProductData;
import com.gftproject.shoppingcart.exceptions.NotEnoughStockException;
import com.gftproject.shoppingcart.exceptions.ProductNotFoundException;
import com.gftproject.shoppingcart.exceptions.UserNotFoundException;
import com.gftproject.shoppingcart.model.*;
import com.gftproject.shoppingcart.repositories.CartProductsRepository;
import com.gftproject.shoppingcart.repositories.CartRepository;
import com.gftproject.shoppingcart.repositories.CountryRepository;
import com.gftproject.shoppingcart.repositories.PaymentRepository;
import org.antlr.v4.runtime.misc.Pair;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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
    UserService userService;

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
        when(computationsService.computeFinalWeightAndPrice(anyList(), anyList())).thenReturn(new Pair<>(new BigDecimal(4), new BigDecimal(5)));
        when(computationsService.applyTaxes(any(), anyDouble(), anyDouble(), anyDouble())).thenReturn(new BigDecimal(6));
        when(userService.getUserById(any())).thenReturn(new User(1L, "SPAIN", "VISA")); // Need to talk with user microservice
        when(computationsService.computeFinalWeightAndPrice(anyList(), anyList())).thenReturn(new Pair<>(new BigDecimal(3), new BigDecimal(25)));
        when(countryRepository.findById(any())).thenReturn(Optional.of(new Country("Stony", 1.5)));
        when(paymentRepository.findById(any())).thenReturn(Optional.of(new Payment("VISA", 2.5)));
        // Return the cart (the argument of the function) instead of execute the save in the repository.
        // As we change status, price and weight in the cart (the argument of the function) we can check the method works because the cart is changing. 
        //Now we can see if the cart change the STATUS and get the created pair
        when(cartRepository.save(any(Cart.class))).thenAnswer(invocation -> invocation.getArgument(0)); // Return submitted cart

        Cart submittedCart = service.submitCart(1L);

        // Verify that the service method correctly calls the repository
        verify(cartRepository).findById(1L);
        verify(userService).getUserById(submittedCart.getUserId());
        verify(computationsService).computeFinalWeightAndPrice(anyList(), anyList());

        assertThat(submittedCart).isNotNull();
        assertThat(submittedCart.getFinalWeight()).isNotZero().isEqualTo(new BigDecimal(3));
        assertThat(submittedCart.getFinalPrice()).isNotZero().isEqualTo(new BigDecimal(6));
        assertThat(submittedCart.getStatus()).isEqualTo(Status.SUBMITTED);
        assertThat(submittedCart.getId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("GIVEN a cart Id with products without stock  WHEN cart is submitted  THEN error is shown")
    void submitCartNoStock() throws UserNotFoundException {
        when(cartRepository.findById(any())).thenReturn(Optional.of(createCart001()));
        when(computationsService.computeFinalWeightAndPrice(anyList(), anyList())).thenReturn(new Pair<>(new BigDecimal(4), new BigDecimal(5)));
        when(userService.getUserById(any())).thenReturn(new User(1L, "SPAIN", "VISA")); // Need to talk with user microservice
        when(cartProductsRepository.findAllByCartId(anyLong())).thenReturn(List.of(ProductData.createCartProductFalse()));
        when(countryRepository.findById(any())).thenReturn(Optional.of(new Country("Stony", 1.5)));
        when(paymentRepository.findById(any())).thenReturn(Optional.of(new Payment("VISA", 2.5)));

        assertThrows(NotEnoughStockException.class, () -> {
            service.submitCart(1L); // Submit the cart
        });
    }

    @Test
    @DisplayName("GIVEN a cart Id with products without stock  WHEN cart is submitted  THEN error is shown")
    void submitCartIncompleteUser() throws UserNotFoundException {
        when(cartRepository.findById(any())).thenReturn(Optional.of(createCart001()));
        when(computationsService.computeFinalWeightAndPrice(anyList(), anyList())).thenReturn(new Pair<>(new BigDecimal(4), new BigDecimal(5)));
        when(userService.getUserById(any())).thenReturn(new User(1L, "SPAIN", "VISA")); // Need to talk with user microservice
        when(cartProductsRepository.findAllByCartId(anyLong())).thenReturn(List.of(ProductData.createCartProductFalse()));
        when(countryRepository.findById(any())).thenReturn(Optional.empty());
        when(paymentRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> {
            service.submitCart(1L); // Submit the cart
        });
    }

    @Test
    @DisplayName("GIVEN a cart Id and products with quantity WHEN addProductToCartWithQuantity THEN add product to cart and check stock")
    void addProductToCartWithQuantity() throws ProductNotFoundException, NotEnoughStockException {
        Cart cart = new Cart(1L, 1L, Status.DRAFT, new BigDecimal(14), BigDecimal.ZERO);
        ProductDTO product = new ProductDTO(1L, new BigDecimal(3), 5, new BigDecimal(4));

        when(cartRepository.findById(any())).thenReturn(Optional.of(cart));
        when(cartRepository.save(any())).thenReturn(cart);
        when(productService.getProductById(any())).thenReturn(product);

        Cart updatedCart = service.addProductToCartWithQuantity(1L, 5L, 5L, 5);

        assertThat(updatedCart).isNotNull();
        assertThat(updatedCart.getId()).isEqualTo(1L);
        verify(cartRepository).save(cart);
    }

    @Test
    @DisplayName("GIVEN a cart Id and products with quantity WHEN addProductToCartWithQuantity THEN add product to cart and check stock")
    void addProductToCartWithQuantityNoCart() throws ProductNotFoundException, NotEnoughStockException {
        Cart cart = new Cart(4L, 1L, Status.DRAFT, new BigDecimal(14), BigDecimal.ZERO);
        ProductDTO product = new ProductDTO(1L, new BigDecimal(3), 5, new BigDecimal(4));

        when(cartRepository.findById(any())).thenReturn(Optional.empty());
        when(cartRepository.save(any())).thenReturn(cart);
        when(productService.getProductById(any())).thenReturn(product);

        Cart newCart = service.addProductToCartWithQuantity(1L, 4L, 5L, 5);

        assertThat(newCart).isNotNull();
        assertThat(newCart.getId()).isEqualTo(4L);

        verify(cartRepository).save(cart);
    }

    @Test
    @DisplayName("GIVEN a cart Id and products with quantity WHEN addProductToCartWithQuantity THEN add product to cart and check stock")
    void addProductToCartWithQuantityNoStock() throws ProductNotFoundException, NotEnoughStockException {
        Cart cart = new Cart(4L, 1L, Status.DRAFT, new BigDecimal(14), BigDecimal.ZERO);
        ProductDTO product = new ProductDTO(1L, new BigDecimal(3), 5, new BigDecimal(4));

        when(cartRepository.findById(any())).thenReturn(Optional.of(cart));
        when(cartRepository.save(any())).thenReturn(cart);
        when(productService.getProductById(any())).thenReturn(product);

        assertThrows(NotEnoughStockException.class, () -> {
            service.addProductToCartWithQuantity(1L, 5L, 5L, 10); // Submit the cart
        });

    }

    @Test
    @DisplayName("GIVEN userId, cartId, and productId WHEN product is deleted from cart THEN response is OK")
    void deleteProductFromCart() {
        Long cartId = 1L;
        Long productId = 1L;
        Cart cart = new Cart();
        CartProduct product = new CartProduct(cart, productId, true, 1);
        when(cartRepository.findById(cartId)).thenReturn(Optional.of(cart));
        when(cartProductsRepository.findByCartAndProduct(cart, productId)).thenReturn(product);

        service.deleteProductFromCart(cartId, productId);

        verify(cartProductsRepository).delete(product);
    }

    @Test
    @DisplayName("GIVEN a list of updated products WHEN we submit the data to the service THEN the products in carts will be updated in database")
    void updateProductsFromCarts() {
        // Create sample data for testing
        List<ProductDTO> updatedProducts = new ArrayList<>();
        updatedProducts.add(new ProductDTO(1L, new BigDecimal("12.99"), 20, new BigDecimal("3")));
        updatedProducts.add(new ProductDTO(2L, new BigDecimal("19.99"), 2, new BigDecimal("5")));
        updatedProducts.add(new ProductDTO(3L, new BigDecimal("10.0"), 5, new BigDecimal("1.0")));

        // Mock repository behavior
        when(cartProductsRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(cartProductsRepository.findByProductIn(any())).thenReturn(List.of(ProductData.createCartProduct001()));

        service.updateProductsFromCarts(updatedProducts);
        // Assertions
        verify(cartProductsRepository, times(1)).save(any(CartProduct.class));
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





