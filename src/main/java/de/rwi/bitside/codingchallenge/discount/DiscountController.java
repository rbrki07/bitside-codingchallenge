package de.rwi.bitside.codingchallenge.discount;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/discounts")
public class DiscountController {

    @Autowired
    private DiscountService discountService;

    @GetMapping
    List<Discount> getDiscounts() {
        return discountService.getDiscounts();
    }

    @GetMapping("/{id}")
    ResponseEntity<Discount> getDiscountById(@PathVariable Long id) {
        return ResponseEntity.ok(discountService.getDiscountById(id));
    }

    @PostMapping
    ResponseEntity<Void> createDiscount(@Valid @RequestBody Discount discount) {
        var createdDiscount = discountService.createDiscount(discount);
        var url = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .build(createdDiscount.getId());
        return ResponseEntity.created(url).build();
    }

    @PutMapping("/{id}")
    ResponseEntity<Void> updateDiscount(@PathVariable Long id, @Valid @RequestBody Discount discount) {
        discountService.updateDiscount(id, discount);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteDiscount(@PathVariable Long id) {
        discountService.deleteDiscount(id);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(DiscountNotFoundException.class)
    ResponseEntity<Void> handle(DiscountNotFoundException e) {
        return ResponseEntity.notFound().build();
    }
}
