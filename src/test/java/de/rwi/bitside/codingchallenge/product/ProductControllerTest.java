package de.rwi.bitside.codingchallenge.product;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

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

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@WebMvcTest(controllers = ProductController.class)
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @Test
    void shouldListProductsWhenCorrespondingEndpointIsCalled() throws Exception {
        var product1 = createProduct(1L, "A0001", new BigDecimal("12.99"));
        var product2 = createProduct(2L, "A0002", new BigDecimal("3.99"));

        when(productService.getProducts()).thenReturn(List.of(product1, product2));

        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$.[0].id", is(product1.getId().intValue())))
                .andExpect(jsonPath("$.[1].id", is(product2.getId().intValue())));

        verify(productService).getProducts();
    }

    @Test
    void shouldShowProductWhenCorrespondingEndpointIsCalled() throws Exception {
        var product = createProduct(1L, "A0001", new BigDecimal("12.99"));

        when(productService.getProductById(1L)).thenReturn(product);

        mockMvc.perform(get("/api/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(product.getId().intValue())))
                .andExpect(jsonPath("$.code", is(product.getCode())))
                .andExpect(jsonPath("$.price", is(product.getPrice().doubleValue())))
                .andExpect(jsonPath("$.createdAt", is(product.getCreatedAt().toString())))
                .andExpect(jsonPath("$.updatedAt", is(product.getUpdatedAt().toString())));

        verify(productService).getProductById(1L);
    }

    @Test
    void shouldReturn404WhenProductIsNotFound() throws Exception {
        when(productService.getProductById(1L)).thenThrow(new ProductNotFoundException());

        mockMvc.perform(get("/api/products/1"))
                .andExpect(status().isNotFound());

        verify(productService).getProductById(1L);
    }

    @Test
    void shouldCreateProductWhenCorrespondingEndpointIsCalled() throws Exception {
        var product = createProduct(1L, "A0001", new BigDecimal("12.99"));

        when(productService.createProduct(any(Product.class))).thenReturn(product);

        mockMvc.perform(post("/api/products").contentType(MediaType.APPLICATION_JSON)
                .content("{\"code\":\"A0001\",\"price\":12.99}"))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "http://localhost/api/products/1"));

        verify(productService).createProduct(any(Product.class));
    }

    @Test
    void shouldUpdateProductWhenCorrespondingEndpointIsCalled() throws Exception {
        var product = createProduct(1L, "A0001", new BigDecimal("12.99"));

        when(productService.updateProduct(eq(1L), any(Product.class))).thenReturn(product);

        mockMvc.perform(put("/api/products/1").contentType(MediaType.APPLICATION_JSON)
                .content("{\"code\":\"A0001\",\"price\":12.99}"))
                .andExpect(status().isNoContent());

        verify(productService).updateProduct(eq(1L), any(Product.class));
    }

    @Test
    void shouldDeleteProductWhenCorrespondingEndpointIsCalled() throws Exception {
        mockMvc.perform(delete("/api/products/1"))
                .andExpect(status().isNoContent());

        verify(productService).deleteProduct(1L);
    }

    private Product createProduct(Long id, String code, BigDecimal price) {
        var product = new Product();
        product.setId(id);
        product.setCode(code);
        product.setPrice(price);
        product.setCreatedAt(LocalDateTime.of(2024, 1, 2, 3, 4, 5));
        product.setUpdatedAt(LocalDateTime.of(2024, 1, 2, 3, 4, 5));
        return product;
    }
}
