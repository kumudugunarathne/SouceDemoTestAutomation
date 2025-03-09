package com.qa.saucedemo.tests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.playwright.Page;
import com.qa.saucedemo.base.BaseTest;
import com.qa.saucedemo.pages.CartPage;
import com.qa.saucedemo.pages.HomePage;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class ProductsTest extends BaseTest {

    HomePage homePage;
    CartPage cartPage;
    Page page;


    @Test(priority = 1,dataProvider = "userData")
    void testSortingAndCartValidation(String username, String password, String role) throws IOException {
        if(role.equals("standard_user")){
            this.homePage = super.loginPage.logIntoHomePage(username,password);
            this.homePage.sortByPriceLowToHigh();
            Assert.assertTrue(this.homePage.isSortedByPrice(), "Products are not sorted correctly!");

            List<Map<String, String>> selectedItems = this.homePage.addRandomProductsToCart(3);
            this.homePage.navigateToCart();
            this.cartPage = new CartPage(super.page);
            Assert.assertEquals(selectedItems,cartPage.getCartItems(), "Cart items do not match selected products!");

            String removedItem = cartPage.removeMostExpensiveItem();
            Assert.assertFalse(cartPage.isItemInCart(removedItem), "Most expensive item was not removed!");

            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> logData = Map.of(
                    "selectedItems", selectedItems,
                    "finalCart", cartPage.getCartItems(),
                    "removedItem", removedItem
            );

            String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(logData);

            try (FileWriter file = new FileWriter("test-log.json")) {
                file.write(logData.toString());
            }
        }

    }


}
