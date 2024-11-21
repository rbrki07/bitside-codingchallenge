package de.rwi.bitside.codingchallenge.basket;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = BasketController.class)
public class BasketControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BasketService basketService;

    @Test
    void shouldListBasketsWhenCorrespondingEndpointIsCalled() throws Exception {
        var basket1 = createBasket(1L);
        var basket2 = createBasket(2L);

        when(basketService.getBaskets()).thenReturn(List.of(basket1, basket2));

        mockMvc.perform(get("/api/baskets"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$.[0].id", is(basket1.getId().intValue())))
                .andExpect(jsonPath("$.[1].id", is(basket2.getId().intValue())));

        verify(basketService).getBaskets();
    }

    @Test
    void shouldShowBasketWhenCorrespondingEndpointIsCalled() throws Exception {
        var basket = createBasket(1L);

        when(basketService.getBasketById(1L)).thenReturn(basket);

        mockMvc.perform(get("/api/baskets/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(basket.getId().intValue())))
                .andExpect(jsonPath("$.createdAt", is(basket.getCreatedAt().toString())))
                .andExpect(jsonPath("$.updatedAt", is(basket.getUpdatedAt().toString())));

        verify(basketService).getBasketById(1L);
    }

    @Test
    void shouldReturn404WhenBasketIsNotFound() throws Exception {
        when(basketService.getBasketById(1L)).thenThrow(new BasketNotFoundException());

        mockMvc.perform(get("/api/baskets/1"))
                .andExpect(status().isNotFound());

        verify(basketService).getBasketById(1L);
    }

    @Test
    void shouldCalculateTotalWhenCorrespondingEndpointIsCalled() throws Exception {
        when(basketService.calculateTotal(1L)).thenReturn(BigDecimal.valueOf(12.99));

        mockMvc.perform(get("/api/baskets/1/total"))
                .andExpect(status().isOk())
                .andExpect(content().string("12.99"));

        verify(basketService).calculateTotal(1L);
    }

    @Test
    void shouldCreateBasketWhenCorrespondingEndpointIsCalled() throws Exception {
        var basket = createBasket(1L);

        when(basketService.createBasket(any(Basket.class))).thenReturn(basket);

        mockMvc.perform(post("/api/baskets").contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "http://localhost/api/baskets/1"));

        verify(basketService).createBasket(any(Basket.class));
    }

    @Test
    void shouldAddProductToBasketWhenCorrespondingEndpointIsCalled() throws Exception {
        mockMvc.perform(patch("/api/baskets/1/products/1"))
                .andExpect(status().isNoContent());

        verify(basketService).addProduct(1L, 1L);
    }

    @Test
    void shouldScanProductToBasketWhenCorrespondingEndpointIsCalled() throws Exception {
        mockMvc.perform(patch("/api/baskets/1/scan/A0001"))
                .andExpect(status().isNoContent());

        verify(basketService).addProduct(1L, "A0001");
    }

    @Test
    void shouldRemoveProductFromBasketWhenCorrespondingEndpointIsCalled() throws Exception {
        mockMvc.perform(delete("/api/baskets/1/products/1"))
                .andExpect(status().isNoContent());

        verify(basketService).removeProduct(1L, 1L);
    }

    @Test
    void shouldAddDiscountToBasketWhenCorrespondingEndpointIsCalled() throws Exception {
        mockMvc.perform(patch("/api/baskets/1/discounts/1"))
                .andExpect(status().isNoContent());

        verify(basketService).addDiscount(1L, 1L);
    }

    @Test
    void shouldRemoveDiscountFromBasketWhenCorrespondingEndpointIsCalled() throws Exception {
        mockMvc.perform(delete("/api/baskets/1/discounts/1"))
                .andExpect(status().isNoContent());

        verify(basketService).removeDiscount(1L, 1L);
    }

    @Test
    void shouldDeleteBasketWhenCorrespondingEndpointIsCalled() throws Exception {
        mockMvc.perform(delete("/api/baskets/1"))
                .andExpect(status().isNoContent());

        verify(basketService).deleteBasket(1L);
    }

    private Basket createBasket(Long id) {
        var basket = new Basket();
        basket.setId(id);
        basket.setCreatedAt(LocalDateTime.of(2024, 1, 2, 3, 4, 5));
        basket.setUpdatedAt(LocalDateTime.of(2024, 1, 2, 3, 4, 5));
        return basket;
    }
}
