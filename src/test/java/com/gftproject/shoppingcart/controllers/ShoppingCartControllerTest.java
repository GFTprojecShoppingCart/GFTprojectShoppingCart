package com.gftproject.shoppingcart.controllers;

import com.gftproject.shoppingcart.CartsData;
import com.gftproject.shoppingcart.model.Status;
import com.gftproject.shoppingcart.services.ShoppingCartService;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.DeleteMapping;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ShoppingCartController.class)
class ShoppingCartControllerTest {

    @MockBean
    private ShoppingCartService service;

    @Autowired
    private MockMvc mvc;

    @Test
    void findAllByEmptyStatus() throws Exception {

        given(service.findAllByStatus(any())).willReturn(CartsData.getMockCarts());
        given(service.findAll()).willReturn(CartsData.getMockCarts());

//        Status.SUBMITTED;

        mvc.perform(get("/carts/").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(service).findAll();
        verify(service, never()).findAllByStatus(any());
    }

    @Test
    void findAllByStatus() throws Exception {

        given(service.findAllByStatus(any())).willReturn(CartsData.getMockCarts());
        given(service.findAll()).willReturn(CartsData.getMockCarts());

        mvc.perform(get("/carts/").contentType(MediaType.APPLICATION_JSON).param("status", String.valueOf(Status.SUBMITTED)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(service).findAllByStatus(any());
        verify(service, never()).findAll();
    }

    @Test
    void createShoppingCart() throws Exception {
        given(service.createCart(any())).willReturn(CartsData.createCart001().orElseThrow());

        mvc.perform(post("/carts/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(service).createCart(any());

    }

    @Test
    void submitCart() throws Exception {
        given(service.submitCart(any())).willReturn(CartsData.createSampleCart());

        mvc.perform(put("/carts/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(service).submitCart(any());
    }


    @Test
    void deleteShoppingCart() throws Exception {

        //deleteCart001 CartsData.java method?
        given(service.deleteCart(any())).willReturn(CartsData.createCart001().orElseThrow());

        //Content Type not set?
        mvc.perform(delete("/carts/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(service).deleteCart(any());
    }
}