package de.rwi.bitside.codingchallenge.discount;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = DiscountController.class)
public class DiscountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DiscountService discountService;

    @Test
    void shouldListDiscountsWhenCorrespondingEndpointIsCalled() throws Exception {
        var discount1 = createDiscount(1L, DiscountType.TEN_PERCENT_OFF, "A0001");
        var discount2 = createDiscount(2L, DiscountType.BUY_1_GET_1_FREE, "A0002");

        when(discountService.getDiscounts()).thenReturn(List.of(discount1, discount2));

        mockMvc.perform(get("/api/discounts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$.[0].id", is(discount1.getId().intValue())))
                .andExpect(jsonPath("$.[1].id", is(discount2.getId().intValue())));

        verify(discountService).getDiscounts();
    }

    @Test
    void shouldShowDiscountWhenCorrespondingEndpointIsCalled() throws Exception {
        var discount = createDiscount(1L, DiscountType.TEN_PERCENT_OFF, "A0001");

        when(discountService.getDiscountById(1L)).thenReturn(discount);

        mockMvc.perform(get("/api/discounts/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(discount.getId().intValue())))
                .andExpect(jsonPath("$.type", is(discount.getType().name())))
                .andExpect(jsonPath("$.productCode", is(discount.getProductCode())))
                .andExpect(jsonPath("$.createdAt", is(discount.getCreatedAt().toString())))
                .andExpect(jsonPath("$.updatedAt", is(discount.getUpdatedAt().toString())));

        verify(discountService).getDiscountById(1L);
    }

    @Test
    void shouldReturn404WhenDiscountIsNotFound() throws Exception {
        when(discountService.getDiscountById(1L)).thenThrow(new DiscountNotFoundException());

        mockMvc.perform(get("/api/discounts/1"))
                .andExpect(status().isNotFound());

        verify(discountService).getDiscountById(1L);
    }

    @Test
    void shouldCreateDiscountWhenCorrespondingEndpointIsCalled() throws Exception {
        var discount = createDiscount(1L, DiscountType.TEN_PERCENT_OFF, "A0001");

        when(discountService.createDiscount(any(Discount.class))).thenReturn(discount);

        mockMvc.perform(post("/api/discounts").contentType(MediaType.APPLICATION_JSON)
                .content("{\"type\":\"TEN_PERCENT_OFF\",\"productCode\":\"A0001\"}"))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "http://localhost/api/discounts/1"));

        verify(discountService).createDiscount(any(Discount.class));
    }

    @Test
    void shouldUpdateDiscountWhenCorrespondingEndpointIsCalled() throws Exception {
        var discount = createDiscount(1L, DiscountType.TEN_PERCENT_OFF, "A0001");

        when(discountService.updateDiscount(eq(1L), any(Discount.class))).thenReturn(discount);

        mockMvc.perform(put("/api/discounts/1").contentType(MediaType.APPLICATION_JSON)
                .content("{\"type\":\"TEN_PERCENT_OFF\",\"productCode\":\"A0001\"}"))
                .andExpect(status().isNoContent());

        verify(discountService).updateDiscount(eq(1L), any(Discount.class));
    }

    @Test
    void shouldDeleteDiscountWhenCorrespondingEndpointIsCalled() throws Exception {
        mockMvc.perform(delete("/api/discounts/1"))
                .andExpect(status().isNoContent());

        verify(discountService).deleteDiscount(1L);
    }

    private Discount createDiscount(Long id, DiscountType type, String productCode) {
        var discount = new Discount();
        discount.setId(id);
        discount.setType(type);
        discount.setProductCode(productCode);
        discount.setCreatedAt(LocalDateTime.of(2024, 1, 2, 3, 4, 5));
        discount.setUpdatedAt(LocalDateTime.of(2024, 1, 2, 3, 4, 5));
        return discount;
    }
}
