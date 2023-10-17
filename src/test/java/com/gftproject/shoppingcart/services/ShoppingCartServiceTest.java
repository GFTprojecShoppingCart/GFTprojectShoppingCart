package com.gftproject.shoppingcart.services;

import com.gftproject.shoppingcart.model.Cart;
import com.gftproject.shoppingcart.model.Product;
import com.gftproject.shoppingcart.model.Status;
import com.gftproject.shoppingcart.repositories.ShoppingCartRepository;
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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


class ShoppingCartServiceTest {
    @InjectMocks
    ShoppingCartServiceImpl service;

    @Mock
    ShoppingCartRepository cartRepository;

    @Mock
    CartComputationsService computationsService;

    @Mock
    ProductServiceImpl productService;

    @Mock
    UserServiceImpl userService;


    private List<Cart> carts;

    @BeforeEach
    void setUp() {
        // Instantiate Shopping cart Service and instantiate con new mock de repository
        MockitoAnnotations.openMocks(this);
        service = new ShoppingCartServiceImpl(cartRepository, computationsService, productService, userService);
        carts = Arrays.asList(Optional.of(createCart001()).orElseThrow(), createCart002(), createCart003());
    }

    @Test
    @DisplayName("Gets a full list of carts")
    void getAllCarts() {
        //Given
        when(cartRepository.findAll()).thenReturn(carts);

        //when
        List<Cart> allCarts = service.findAll();

        //then
        assertNotNull(allCarts);
        assertEquals(3, allCarts.size());
        verify(cartRepository).findAll();
    }

    @Test
    @DisplayName("WHEN a status is provided, THEN a filtered list of carts should be provided")
    void getCartsByStatus() {
        //Given
        when(cartRepository.findAllByStatus(any())).thenReturn(carts);

        //when
        List<Cart> allCarts = service.findAllByStatus(Status.DRAFT);

        //then
        assertThat(allCarts).isNotNull().hasSize(3);

        verify(cartRepository).findAllByStatus(Status.DRAFT);
    }

    @Test
    @DisplayName("Submit a cart")
    void submitCart() {
        when(cartRepository.findById(any())).thenReturn(Optional.of(createCart001()));
        when(cartRepository.save(any())).thenReturn(createCart001());
        //when(computationsService.computeFinalValues(any()));

        Cart submittedCart = service.submitCart(1L);

        // Verify that the service method correctly calls the repository
        verify(cartRepository).findById(1L);
        verify(cartRepository).save(any());

        assertThat(submittedCart).isNotNull();
        assertThat(submittedCart.getFinalPrice()).isNotZero();
        assertThat(submittedCart.getStatus()).isEqualTo(Status.SUBMITTED);
        assertThat(submittedCart.getId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("Add product to cart and check stock")
    void addProductWithQuantity() {
        Cart cart = new Cart(1L, new ArrayList<>(), new HashMap<>(), 1L, Status.DRAFT, new BigDecimal(14), BigDecimal.ZERO);
        Product product = new Product(1L, new BigDecimal(3), new BigDecimal("0.5"), 5);
        when(cartRepository.findById(any())).thenReturn(Optional.of(cart));
        when(computationsService.checkStock(cart.getProducts(), List.of(product))).thenReturn(List.of(1L, 2L));
        when(cartRepository.save(any())).thenReturn(cart);
        Cart updatedCart = service.addProductToCartWithQuantity(1L, product, 5);
        assertNotNull(updatedCart);

        assertEquals(1L, updatedCart.getId());
        // Verificamos que se haya guardado en el repositorio
        verify(cartRepository).save(cart);
    }

    @Test
    void updateProductsFromCarts() {
        // Create sample data for testing
        Product product1 = new Product(1L, new BigDecimal("10.0"), new BigDecimal("0.5"), 10);
        Product product2 = new Product(2L, new BigDecimal("20.0"), new BigDecimal("0.8"), 15);

        Product product1_updated = new Product(2L, new BigDecimal("20.0"), new BigDecimal("0.8"), 15);
        Product product2_updated = new Product(2L, new BigDecimal("25.0"), new BigDecimal("0.8"), 5);

        Cart cart1 = new Cart(1L, new ArrayList<>(), new HashMap<>(), 1L, Status.SUBMITTED, new BigDecimal("0.0"), new BigDecimal("0.0"));
        Cart cart2 = new Cart(2L, new ArrayList<>(), new HashMap<>(), 2L, Status.DRAFT, new BigDecimal("0.0"), new BigDecimal("0.0"));

        cart1.getProducts().put(product1.getId(), 3);
        cart1.getProducts().put(product2.getId(), 2);
        cart2.getProducts().put(product1.getId(), 1);

        List<Product> updatedProducts = new ArrayList<>();
        updatedProducts.add(product1_updated);
        updatedProducts.add(product2_updated);

        // Mock repository behavior
        when(cartRepository.findCartsByProductIds(Mockito.anyList())).thenReturn(List.of(cart1, cart2));
        when(cartRepository.save(any(Cart.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Test the service method
        List<Cart> updatedCartIds = service.updateProductsFromCarts(updatedProducts);

        // Assertions
        assertEquals(2, updatedCartIds.size());
        assertEquals(1L, updatedCartIds.get(0).getId());
        assertEquals(2L, updatedCartIds.get(1).getId());

        // Verify that the save method was called on the repository
        Mockito.verify(cartRepository, Mockito.times(2)).save(any(Cart.class));

        verify(cartRepository).save(any());
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





