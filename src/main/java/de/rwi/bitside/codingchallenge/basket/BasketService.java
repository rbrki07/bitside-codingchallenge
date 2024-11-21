package de.rwi.bitside.codingchallenge.basket;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.rwi.bitside.codingchallenge.discount.DiscountService;
import de.rwi.bitside.codingchallenge.product.Product;
import de.rwi.bitside.codingchallenge.product.ProductService;

@Service
public class BasketService {

    @Autowired
    private BasketRepository basketRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private DiscountService discountService;

    public List<Basket> getBaskets() {
        return basketRepository.findAll();
    }

    public Basket getBasketById(Long id) {
        return basketRepository.findById(id).orElseThrow(() -> new BasketNotFoundException());
    }

    public Basket createBasket(Basket basket) {
        return basketRepository.save(basket);
    }

    public void deleteBasket(Long id) {
        basketRepository.deleteById(id);
    }

    public void addProduct(Long basketId, Long productId) {
        var product = productService.getProductById(productId);
        var basket = basketRepository.findById(basketId).orElseThrow(() -> new BasketNotFoundException());
        basket.addProduct(product);
        basketRepository.save(basket);
    }

    public void addProduct(Long basketId, String productCode) {
        var product = productService.getProductByCode(productCode);
        var basket = basketRepository.findById(basketId).orElseThrow(() -> new BasketNotFoundException());
        basket.addProduct(product);
        basketRepository.save(basket);
    }

    public void removeProduct(Long basketId, Long productId) {
        var product = productService.getProductById(productId);
        var basket = basketRepository.findById(basketId).orElseThrow(() -> new BasketNotFoundException());
        basket.removeProduct(product);
        basketRepository.save(basket);
    }

    public void addDiscount(Long basketId, Long discountId) {
        var discount = discountService.getDiscountById(discountId);
        var basket = basketRepository.findById(basketId).orElseThrow(() -> new BasketNotFoundException());
        basket.addDiscount(discount);
        basketRepository.save(basket);
    }

    public void removeDiscount(Long basketId, Long discountId) {
        var discount = discountService.getDiscountById(discountId);
        var basket = basketRepository.findById(basketId).orElseThrow(() -> new BasketNotFoundException());
        basket.removeDiscount(discount);
        basketRepository.save(basket);
    }

    public BigDecimal calculateTotal(Long basketId) {
        var total = BigDecimal.ZERO;
        var basket = basketRepository.findById(basketId).get();
        var products = basket.getProducts();
        var discounts = basket.getDiscounts();
        // add prices of discountable products
        for (var discount : discounts) {
            var discountableProducts = products.stream()
                    .filter(product -> product.getCode().equals(discount.getProductCode())).toList();
            var subTotal = discountableProducts.stream().map(Product::getPrice).reduce(BigDecimal.ZERO,
                    BigDecimal::add);
            var discountValue = BigDecimal.ZERO;
            switch (discount.getType()) {
                case TEN_PERCENT_OFF:
                    discountValue = subTotal.multiply(BigDecimal.valueOf(discount.getType().getPercentage() / 100));
                    break;
                case BUY_1_GET_1_FREE:
                    discountValue = subTotal.divide(BigDecimal.valueOf(discountableProducts.size()))
                            .multiply(BigDecimal.valueOf(discountableProducts.size() / 2));
                    break;
            }
            subTotal = subTotal.subtract(discountValue);
            total = total.add(subTotal);
            products.removeAll(discountableProducts);
        }
        // add prices of non-discountable products
        total = total.add(products.stream().map(Product::getPrice).reduce(BigDecimal.ZERO, BigDecimal::add));
        return total.setScale(2, RoundingMode.HALF_UP);
    }
}
