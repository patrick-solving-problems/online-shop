package de.rode.onlineshop;

import de.rode.onlineshop.model.Customer;
import de.rode.onlineshop.model.Position;
import de.rode.onlineshop.model.Product;
import de.rode.onlineshop.model.ShoppingCart;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;

@Slf4j
@AllArgsConstructor
public class ShoppingService {

    private final DiscountService discountService;

    public Customer register(final String name) {
        final Customer customer = Customer.builder()
                .name(name)
                .build();
        log.info("New customer {} created.", customer);
        return customer;
    }

    public ShoppingCart login(final Customer customer) {
        final ShoppingCart shoppingCart = ShoppingCart.builder()
                .customer(customer)
                .positions(new ArrayList<>())
                .build();
        log.info("Customer {} successfully logged in. Initialized empty ShoppingCart.", customer);
        return shoppingCart;
    }

    public void addToCart(final ShoppingCart shoppingCart, final Product product, final int amount) {
        for (Position position : shoppingCart.getPositions()) {
            if (position.getProduct().equals(product)) {
                position.setAmount(position.getAmount() + amount);
                log.info("Added additional {} of product {} for users shopping cart.", amount, product.getName());
                return;
            }
        }
        shoppingCart.getPositions().add(Position.builder()
                .product(product)
                .amount(amount)
                .build());
        log.info("Added {} of product {} for users shopping cart.", amount, product.getName());
    }

    public double value(final ShoppingCart shoppingCart) {
        double sum = 0.0;
        for (Position position : shoppingCart.getPositions()) {
            sum += discountService.realPrice(position.getProduct()) * position.getAmount();
        }
        double realPrice = discountService.realPrice(shoppingCart.getCustomer(), sum);
        log.info("Calculated the price {} for the whote shopping cart", realPrice);
        return realPrice;
    }

}
