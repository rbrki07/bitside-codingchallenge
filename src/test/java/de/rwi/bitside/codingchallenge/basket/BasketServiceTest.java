package de.rwi.bitside.codingchallenge.basket;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import de.rwi.bitside.codingchallenge.discount.Discount;
import de.rwi.bitside.codingchallenge.discount.DiscountService;
import de.rwi.bitside.codingchallenge.discount.DiscountType;
import de.rwi.bitside.codingchallenge.product.Product;
import de.rwi.bitside.codingchallenge.product.ProductService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@ExtendWith(MockitoExtension.class)
class BasketServiceTest {

    @Mock
    private BasketRepository basketRepository;

    @Mock
    private ProductService productService;

    @Mock
    private DiscountService discountService;

    @InjectMocks
    private BasketService basketService;

    final Long basketId = 1L;

    @Test
    void shouldCalculateCorrectTotalWhenNoProductInBasketWithoutDiscounts() {
        var basket = createBasket(Collections.emptyList(), Collections.emptySet());

        given(basketRepository.findById(basketId)).willReturn(java.util.Optional.of(basket));

        var total = basketService.calculateTotal(basketId);

        assertThat(total).isEqualTo(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP));
    }

    @Test
    void shouldCalculateCorrectTotalWhenOneProductInBasketWithoutDiscounts() {
        var product = new Product();
        product.setCode("A0001");
        product.setPrice(BigDecimal.valueOf(12.99));

        var basket = createBasket(new ArrayList<>(Arrays.asList(product)), Collections.emptySet());

        given(basketRepository.findById(basketId)).willReturn(java.util.Optional.of(basket));

        var total = basketService.calculateTotal(basketId);

        assertThat(total).isEqualTo(BigDecimal.valueOf(12.99));
    }

    @Test
    void shouldCalculateCorrectTotalWhenTwoProductsInBasketWithoutDiscounts() {
        var product1 = new Product();
        product1.setCode("A0001");
        product1.setPrice(BigDecimal.valueOf(12.99));
        var product2 = new Product();
        product2.setCode("A0002");
        product2.setPrice(BigDecimal.valueOf(3.99));

        var basket = createBasket(new ArrayList<>(Arrays.asList(product1, product2)), Collections.emptySet());

        given(basketRepository.findById(basketId)).willReturn(java.util.Optional.of(basket));

        var total = basketService.calculateTotal(basketId);

        assertThat(total).isEqualTo(BigDecimal.valueOf(16.98));
    }

    @Test
    void shouldCalculateCorrectTotalWhenOneProductInBasketWithTenPercentOffDiscount() {
        var product = new Product();
        product.setCode("A0001");
        product.setPrice(BigDecimal.valueOf(12.99));

        var discount = new Discount();
        discount.setType(DiscountType.TEN_PERCENT_OFF);
        discount.setProductCode("A0001");

        var basket = createBasket(new ArrayList<>(Arrays.asList(product)), Collections.singleton((discount)));

        given(basketRepository.findById(basketId)).willReturn(java.util.Optional.of(basket));

        var total = basketService.calculateTotal(basketId);

        assertThat(total).isEqualTo(BigDecimal.valueOf(11.69));
    }

    @Test
    void shouldCalculateCorrectTotalWhenTwoProductsInBasketWithTenPercentOffDiscount() {
        var product1 = new Product();
        product1.setCode("A0001");
        product1.setPrice(BigDecimal.valueOf(12.99));
        var product2 = new Product();
        product2.setCode("A0001");
        product2.setPrice(BigDecimal.valueOf(12.99));

        var discount = new Discount();
        discount.setType(DiscountType.TEN_PERCENT_OFF);
        discount.setProductCode("A0001");

        var basket = createBasket(new ArrayList<>(Arrays.asList(product1, product2)),
                Collections.singleton((discount)));

        given(basketRepository.findById(basketId)).willReturn(java.util.Optional.of(basket));

        var total = basketService.calculateTotal(basketId);

        assertThat(total).isEqualTo(BigDecimal.valueOf(23.38));
    }

    @Test
    void shouldCalculateCorrectTotalWhenThreeProductsInBasketWithTenPercentOffDiscount() {
        var product1 = new Product();
        product1.setCode("A0001");
        product1.setPrice(BigDecimal.valueOf(12.99));
        var product2 = new Product();
        product2.setCode("A0002");
        product2.setPrice(BigDecimal.valueOf(3.99));
        var product3 = new Product();
        product3.setCode("A0002");
        product3.setPrice(BigDecimal.valueOf(3.99));

        var discount = new Discount();
        discount.setType(DiscountType.TEN_PERCENT_OFF);
        discount.setProductCode("A0001");

        var basket = createBasket(new ArrayList<>(Arrays.asList(product1, product2, product3)),
                Collections.singleton((discount)));

        given(basketRepository.findById(basketId)).willReturn(java.util.Optional.of(basket));

        var total = basketService.calculateTotal(basketId);

        assertThat(total).isEqualTo(BigDecimal.valueOf(19.67));
    }

    @Test
    void shouldCalculateCorrectTotalWhenOneProductInBasketWithBuyOneGetOneFreeDiscount() {
        var product = new Product();
        product.setCode("A0002");
        product.setPrice(BigDecimal.valueOf(3.99));

        var discount = new Discount();
        discount.setType(DiscountType.BUY_1_GET_1_FREE);
        discount.setProductCode("A0002");

        var basket = createBasket(new ArrayList<>(Arrays.asList(product)), Collections.singleton((discount)));

        given(basketRepository.findById(basketId)).willReturn(java.util.Optional.of(basket));

        var total = basketService.calculateTotal(basketId);

        assertThat(total).isEqualTo(BigDecimal.valueOf(3.99));
    }

    @Test
    void shouldCalculateCorrectTotalWhenTwoProductsInBasketWithBuyOneGetOneFreeDiscount() {
        var product1 = new Product();
        product1.setCode("A0002");
        product1.setPrice(BigDecimal.valueOf(3.99));
        var product2 = new Product();
        product2.setCode("A0002");
        product2.setPrice(BigDecimal.valueOf(3.99));

        var discount = new Discount();
        discount.setType(DiscountType.BUY_1_GET_1_FREE);
        discount.setProductCode("A0002");

        var basket = createBasket(new ArrayList<>(Arrays.asList(product1, product2)),
                Collections.singleton((discount)));

        given(basketRepository.findById(basketId)).willReturn(java.util.Optional.of(basket));

        var total = basketService.calculateTotal(basketId);

        assertThat(total).isEqualTo(BigDecimal.valueOf(3.99));
    }

    @Test
    void shouldCalculateCorrectTotalWhenThreeProductsInBasketWithBuyOneGetOneFreeDiscount() {
        var product1 = new Product();
        product1.setCode("A0002");
        product1.setPrice(BigDecimal.valueOf(3.99));
        var product2 = new Product();
        product2.setCode("A0002");
        product2.setPrice(BigDecimal.valueOf(3.99));
        var product3 = new Product();
        product3.setCode("A0002");
        product3.setPrice(BigDecimal.valueOf(3.99));

        var discount = new Discount();
        discount.setType(DiscountType.BUY_1_GET_1_FREE);
        discount.setProductCode("A0002");

        var basket = createBasket(new ArrayList<>(Arrays.asList(product1, product2, product3)),
                Collections.singleton((discount)));

        given(basketRepository.findById(basketId)).willReturn(java.util.Optional.of(basket));

        var total = basketService.calculateTotal(basketId);

        assertThat(total).isEqualTo(BigDecimal.valueOf(7.98));
    }

    @Test
    void shouldCalculateCorrectTotalWhenFourProductsInBasketWithBuyOneGetOneFreeDiscount() {
        var product1 = new Product();
        product1.setCode("A0002");
        product1.setPrice(BigDecimal.valueOf(3.99));
        var product2 = new Product();
        product2.setCode("A0002");
        product2.setPrice(BigDecimal.valueOf(3.99));
        var product3 = new Product();
        product3.setCode("A0002");
        product3.setPrice(BigDecimal.valueOf(3.99));
        var product4 = new Product();
        product4.setCode("A0002");
        product4.setPrice(BigDecimal.valueOf(3.99));

        var discount = new Discount();
        discount.setType(DiscountType.BUY_1_GET_1_FREE);
        discount.setProductCode("A0002");

        var basket = createBasket(new ArrayList<>(Arrays.asList(product1, product2, product3, product4)),
                Collections.singleton((discount)));

        given(basketRepository.findById(basketId)).willReturn(java.util.Optional.of(basket));

        var total = basketService.calculateTotal(basketId);

        assertThat(total).isEqualTo(BigDecimal.valueOf(7.98));
    }

    @Test
    void shouldCalculateCorrectTotalWhenFiveProductsInBasketWithBuyOneGetOneFreeDiscount() {
        var product1 = new Product();
        product1.setCode("A0002");
        product1.setPrice(BigDecimal.valueOf(3.99));
        var product2 = new Product();
        product2.setCode("A0002");
        product2.setPrice(BigDecimal.valueOf(3.99));
        var product3 = new Product();
        product3.setCode("A0002");
        product3.setPrice(BigDecimal.valueOf(3.99));
        var product4 = new Product();
        product4.setCode("A0002");
        product4.setPrice(BigDecimal.valueOf(3.99));
        var product5 = new Product();
        product5.setCode("A0002");
        product5.setPrice(BigDecimal.valueOf(3.99));

        var discount = new Discount();
        discount.setType(DiscountType.BUY_1_GET_1_FREE);
        discount.setProductCode("A0002");

        var basket = createBasket(new ArrayList<>(Arrays.asList(product1, product2, product3, product4, product5)),
                Collections.singleton((discount)));

        given(basketRepository.findById(basketId)).willReturn(java.util.Optional.of(basket));

        var total = basketService.calculateTotal(basketId);

        assertThat(total).isEqualTo(BigDecimal.valueOf(11.97));
    }

    @Test
    void shouldCalculateCorrectTotalWhenSixProductsInBasketWithMultipleDiscounts() {
        var product1 = new Product();
        product1.setCode("A0001");
        product1.setPrice(BigDecimal.valueOf(12.99));
        var product2 = new Product();
        product2.setCode("A0001");
        product2.setPrice(BigDecimal.valueOf(12.99));
        var product3 = new Product();
        product3.setCode("A0002");
        product3.setPrice(BigDecimal.valueOf(3.99));
        var product4 = new Product();
        product4.setCode("A0002");
        product4.setPrice(BigDecimal.valueOf(3.99));
        var product5 = new Product();
        product5.setCode("A0002");
        product5.setPrice(BigDecimal.valueOf(3.99));
        var product6 = new Product();
        product6.setCode("A0003");
        product6.setPrice(BigDecimal.valueOf(9.99));

        var discount1 = new Discount();
        discount1.setType(DiscountType.TEN_PERCENT_OFF);
        discount1.setProductCode("A0001");
        var discount2 = new Discount();
        discount2.setType(DiscountType.BUY_1_GET_1_FREE);
        discount2.setProductCode("A0002");

        var basket = createBasket(
                new ArrayList<>(Arrays.asList(product1, product2, product3, product4, product5, product6)),
                Set.of(discount1, discount2));

        given(basketRepository.findById(basketId)).willReturn(java.util.Optional.of(basket));

        var total = basketService.calculateTotal(basketId);

        assertThat(total).isEqualTo(BigDecimal.valueOf(41.35));
    }

    private Basket createBasket(List<Product> products, Set<Discount> discounts) {
        var basket = new Basket();
        basket.setProducts(products);
        basket.setDiscounts(discounts);
        return basket;
    }
}
