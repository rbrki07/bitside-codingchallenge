package de.rwi.bitside.codingchallenge.basket;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/baskets")
public class BasketController {

    @Autowired
    private BasketService basketService;

    @GetMapping
    List<Basket> getBaskets() {
        return basketService.getBaskets();
    }

    @GetMapping("/{id}")
    ResponseEntity<Basket> getBasketById(@PathVariable Long id) {
        return ResponseEntity.ok(basketService.getBasketById(id));
    }

    @GetMapping("/{id}/total")
    ResponseEntity<BigDecimal> calculateTotal(@PathVariable Long id) {
        return ResponseEntity.ok(basketService.calculateTotal(id));
    }

    @PostMapping
    ResponseEntity<Void> createBasket(@Valid @RequestBody Basket basket) {
        var createdBasket = basketService.createBasket(basket);
        var url = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .build(createdBasket.getId());
        return ResponseEntity.created(url).build();
    }

    @PatchMapping("/{id}/products/{productId}")
    ResponseEntity<Void> addProduct(@PathVariable Long id, @PathVariable Long productId) {
        basketService.addProduct(id, productId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/scan/{productCode}")
    ResponseEntity<Void> scanProduct(@PathVariable Long id, @PathVariable String productCode) {
        basketService.addProduct(id, productCode);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}/products/{productId}")
    ResponseEntity<Void> removeProduct(@PathVariable Long id, @PathVariable Long productId) {
        basketService.removeProduct(id, productId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/discounts/{discountId}")
    ResponseEntity<Void> addDiscount(@PathVariable Long id, @PathVariable Long discountId) {
        basketService.addDiscount(id, discountId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}/discounts/{discountId}")
    ResponseEntity<Void> removeDiscount(@PathVariable Long id, @PathVariable Long discountId) {
        basketService.removeDiscount(id, discountId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteBasket(@PathVariable Long id) {
        basketService.deleteBasket(id);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(BasketNotFoundException.class)
    ResponseEntity<Void> handle(BasketNotFoundException e) {
        return ResponseEntity.notFound().build();
    }
}
