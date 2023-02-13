package de.rode.onlineshop;

import com.google.common.collect.Lists;
import de.rode.onlineshop.exceptions.WrongPasswordException;
import de.rode.onlineshop.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.ThrowableAssert.catchThrowable;

public class ShoppingServiceTest {

    private final DiscountService discountService = new DiscountService();
    private final ShoppingService shoppingService = new ShoppingService(discountService);

    private final Customer pedro = Customer.builder()
            .name("Pedro")
            .build();
    private final Customer eliezer = Customer.builder()
            .name("Eliezer")
            .build();
    private final Customer patrick = Customer.builder()
            .name("Patrick")
            .build();

    private final Product playstation = Product.builder()
            .name("Sony Playstation")
            .price(500.0)
            .build();
    private final Product gamingPc = Product.builder()
            .name("Alienware Gaming PC")
            .price(2000.0)
            .build();
    private final Product spaceship = Product.builder()
            .name("Spaceship")
            .price(1_000_000_000.0)
            .build();

    private final ProductGroup productGroup = ProductGroup.builder()
            .products(Lists.newArrayList(playstation, gamingPc))
            .build();

    @BeforeEach
    private void init() throws WrongPasswordException {
        discountService.removeAllDiscounts("123");
    }

    @Test
    public void correctValueTestWithProductAbsoluteDiscount() throws WrongPasswordException {
        // prepare
        discountService.addDiscount(
                Discount.builder()
                        .product(playstation)
                        .absolute(50.0)
                        .build(),
                "123"
        );
        ShoppingCart shoppingCart = shoppingService.login(pedro);
        shoppingService.addToCart(shoppingCart, playstation, 1);

        // act
        double value = shoppingService.value(shoppingCart);

        // assert
        assertThat(value).isEqualTo(450.0);
    }

    @Test
    public void correctValueTestWithProductPercentageDiscount() throws WrongPasswordException {
        // prepare
        discountService.addDiscount(
                Discount.builder()
                        .product(spaceship)
                        .percentage(99.0)
                        .build(),
                "123"
        );
        ShoppingCart shoppingCart = shoppingService.login(eliezer);
        shoppingService.addToCart(shoppingCart, spaceship, 3);

        // act
        double value = shoppingService.value(shoppingCart);

        // assert
        assertThat(value).isEqualTo(30_000_000.0);
    }

    @Test
    public void correctValueTestWithProductGroupPercentageDiscount() throws WrongPasswordException {
        // prepare
        discountService.addDiscount(
                Discount.builder()
                        .productGroup(ProductGroup.builder()
                                .products(Lists.newArrayList(playstation, gamingPc))
                                .build())
                        .percentage(20.0)
                        .build(),
                "123"
        );
        ShoppingCart shoppingCart = shoppingService.login(patrick);
        shoppingService.addToCart(shoppingCart, playstation, 2);
        shoppingService.addToCart(shoppingCart, gamingPc, 2);

        // act
        double value = shoppingService.value(shoppingCart);

        // assert
        assertThat(value).isEqualTo(4000.0);
    }

    @Test
    public void correctValueTestWithCustomerGroupAbsoluteDiscount() throws WrongPasswordException {
        // prepare
        discountService.addDiscount(
                Discount.builder()
                        .customerGroup(CustomerGroup.builder()
                                .customers(Lists.newArrayList(pedro, eliezer))
                                .build())
                        .absolute(100.0)
                        .build(),
                "123"
        );
        ShoppingCart shoppingCart = shoppingService.login(pedro);
        shoppingService.addToCart(shoppingCart, playstation, 1);
        shoppingService.addToCart(shoppingCart, playstation, 1);

        // act
        double value = shoppingService.value(shoppingCart);

        // assert
        assertThat(value).isEqualTo(900.0);
    }

    @Test
    public void newCustomerTest() throws WrongPasswordException {
        // act
        shoppingService.register("Manuel");
    }

    @Test
    public void wrongPasswordTest1() throws WrongPasswordException {
        // act
        Throwable throwable = catchThrowable(() -> discountService.removeAllDiscounts("456"));
        assertThat(throwable).hasMessage(null);
    }


    @Test
    public void wrongPasswordTest2() throws WrongPasswordException {
        // act
        Throwable throwable = catchThrowable(() -> discountService.addDiscount(Discount.builder().build(), "456"));
        assertThat(throwable).hasMessage(null);
    }

}
