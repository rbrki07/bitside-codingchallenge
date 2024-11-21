package de.rwi.bitside.codingchallenge.discount;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DiscountService {

    @Autowired
    private DiscountRepository discountRepository;

    public List<Discount> getDiscounts() {
        return discountRepository.findAll();
    }

    public Discount getDiscountById(Long id) {
        return discountRepository.findById(id).orElseThrow(() -> new DiscountNotFoundException());
    }

    public Discount createDiscount(Discount discount) {
        return discountRepository.save(discount);
    }

    public Discount updateDiscount(Long id, Discount discount) {
        var existingDiscount = discountRepository.findById(id).orElseThrow(() -> new DiscountNotFoundException());
        existingDiscount.setType(discount.getType());
        existingDiscount.setProductCode(discount.getProductCode());
        return discountRepository.save(existingDiscount);
    }

    public void deleteDiscount(Long id) {
        discountRepository.deleteById(id);
    }
}
