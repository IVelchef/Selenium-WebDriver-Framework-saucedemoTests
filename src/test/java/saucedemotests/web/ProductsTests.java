package saucedemotests.web;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import saucedemotests.core.SauceDemoBaseWebTest;


public class ProductsTests extends SauceDemoBaseWebTest {
    public final String BACKPACK_TITLE = "Sauce Labs Backpack";
    public final String SHIRT_TITLE = "Sauce Labs Bolt T-Shirt";

    @BeforeEach
    public void beforeTest(){

        authenticateWithUser( "standard_user" , "secret_sauce");
        inventoryPage.resetShoppingCart();
    }

    @Test
    public void productAddedToShoppingCart_when_addToCart(){


        inventoryPage.addProductsByTitle( BACKPACK_TITLE, SHIRT_TITLE);

        inventoryPage.clickShoppingCartLink();

        var items = shoppingCartPage.getShoppingCartItems();

        Assertions.assertEquals(2, items.size(), "Items count not as expected");
        Assertions.assertEquals(BACKPACK_TITLE, items.get(0).getText(), "Item title not as expected");
        Assertions.assertEquals(SHIRT_TITLE, items.get(1).getText(), "Item title not as expected");

        inventoryPage.logout();


    }

    @Test
    public void userDetailsAdded_when_checkoutWithValidInformation(){


        inventoryPage.addProductsByTitle(BACKPACK_TITLE, SHIRT_TITLE);


        inventoryPage.clickShoppingCartLink();

        shoppingCartPage.clickCheckout();

        checkoutYourInformationPage.fillShippingDetails("Fname", "lname", "zip");
        checkoutYourInformationPage.clickContinue();

        var items = shoppingCartPage.getShoppingCartItems();
        Assertions.assertEquals(2, items.size(), "Items count not as expected");

        var totalText = checkoutOverviewPage.getTotalLabelText();
        totalText = totalText.replace("Total: $", ""); // Премахваме 'Total: $'
        double actualTotal = Double.parseDouble(totalText);

        double expectedPrice = 29.99 + 15.99 + 3.68;

        Assertions.assertEquals(2, items.size(), "Items count not as expected");
        Assertions.assertEquals(BACKPACK_TITLE, items.get(0).getText(), "Item title not as expected");
        Assertions.assertEquals(SHIRT_TITLE, items.get(1).getText(), "Item title not as expected");
        Assertions.assertEquals(expectedPrice, actualTotal, "Items total price not as expected");

        inventoryPage.logout();


    }

    @Test
    public void orderCompleted_when_addProduct_and_checkout_withConfirm(){



        inventoryPage.addProductsByTitle(BACKPACK_TITLE, SHIRT_TITLE);

        inventoryPage.clickShoppingCartLink();

        shoppingCartPage.clickCheckout();

        checkoutYourInformationPage.fillShippingDetails("Fname", "lname", "zip");
        checkoutYourInformationPage.clickContinue();

        var itemsInCart = shoppingCartPage.getShoppingCartItems();
        Assertions.assertEquals(2, itemsInCart.size(), "Items count not as expected in cart before checkout");

        checkoutOverviewPage.clickFinish();

        var confirmationMessage = checkoutCompletePage.getConfirmationMessage();
        Assertions.assertTrue(confirmationMessage.contains("Thank you for your order!"), "Order was not completed successfully");

        inventoryPage.clickShoppingCartLink();
        var cartItemsAfterOrder = shoppingCartPage.getShoppingCartItems();
        Assertions.assertEquals(0, cartItemsAfterOrder.size(), "Shopping cart is not empty after order completion");

        inventoryPage.logout();
    }
}