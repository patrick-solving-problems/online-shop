package de.rode.onlineshop;

import de.rode.onlineshop.exceptions.WrongPasswordException;
import de.rode.onlineshop.model.Customer;
import de.rode.onlineshop.model.Discount;
import de.rode.onlineshop.model.Product;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class DiscountService {

    private List<Discount> discounts = new ArrayList<>();

    /**
     * Admin only function to add Discounts
     */
    public void addDiscount(final Discount discount, final String password) throws WrongPasswordException {
        if (password.equals("123")) {
            this.discounts.add(discount);
            log.info("Discount {} added.", discount);
        } else {
            throw new WrongPasswordException();
        }
    }

    /**
     * Admin only function to remove all Discounts
     */
    public void removeAllDiscounts(final String password) throws WrongPasswordException {
        if (password.equals("123")) {
            this.discounts = new ArrayList<>();
            log.info("All discounts deleted");
        } else {
            throw new WrongPasswordException();
        }
    }

    /**
     * Will apply Product- and ProductGroup-Discounts
     */
    public double realPrice(final Product product) {
        double price = product.getPrice();
        for (Discount discount : discounts) {
            if (product.equals(discount.getProduct()) ||
                    discount.getProductGroup() != null && discount.getProductGroup().getProducts().contains(product)) {
                price = applyDiscount(discount, price);
            }
        }
        return price;
    }

    /**
     * Will apply CustomerGroup-Discounts
     */
    public double realPrice(final Customer customer, double price) {
        for (Discount discount : discounts) {
            if (discount.getCustomerGroup() != null && discount.getCustomerGroup().getCustomers().contains(customer)) {
                price = applyDiscount(discount, price);
            }
        }
        return price;
    }

    private double applyDiscount(final Discount discount, double price) {
        if (discount.getAbsolute() != null) {
            price -= discount.getAbsolute();
        }
        if (discount.getPercentage() != null) {
            price *= (100.0 - discount.getPercentage()) / 100.0;
        }
        return price;
    }

}
