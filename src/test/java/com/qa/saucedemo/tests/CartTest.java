package com.qa.saucedemo.tests;

import com.github.javafaker.Faker;
import com.microsoft.playwright.Page;
import com.qa.saucedemo.base.BaseTest;
import com.qa.saucedemo.pages.CartPage;
import com.qa.saucedemo.pages.CheckoutPage;
import com.qa.saucedemo.pages.HomePage;
import com.qa.saucedemo.pages.LoginPage;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

public class CartTest extends BaseTest {

    HomePage homePage;
    CartPage cartPage;
    Page page;
    private LoginPage loginPage;
    private CheckoutPage checkoutPage;
    private Faker faker;

    @Test(dataProvider = "userData")
    void testCheckoutWorkflowWithValidation(String username, String password, String role) {
        if(role.equals("standard_user")){
            this.homePage = super.loginPage.logIntoHomePage(username,password);
            List<Map<String, String>> selectedItems = this.homePage.addRandomProductsToCart(3);
            this.homePage.navigateToCart();
            this.cartPage = new CartPage(super.page);

            this.checkoutPage = new CheckoutPage(super.page);
            this.checkoutPage.proceedToCheckout();

            faker = new Faker();

            String firstName = faker.name().firstName();
            String lastName = faker.name().lastName();
            String invalidZipCode = "";
            String validZipCode = faker.address().zipCode();

            //checkoutPage.proceedToCheckout().enterCheckoutDetails(firstName, lastName, invalidZipCode).fixZipCode(validZipCode);
            this.checkoutPage.enterCheckoutDetails(firstName, lastName, invalidZipCode).fixZipCode(validZipCode);

            Assert.assertTrue(this.checkoutPage.verifyTotalPrice(), "Total price does not match!");
            this.checkoutPage.captureScreenshot().completeOrder();
            Assert.assertTrue(this.checkoutPage.verifyOrderConfirmation(), "Order confirmation message is missing!");
        }



    }



}
