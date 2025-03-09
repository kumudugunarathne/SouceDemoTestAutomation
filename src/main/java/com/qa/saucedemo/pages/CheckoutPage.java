package com.qa.saucedemo.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.qa.saucedemo.factory.PlaywrightFactory;

public class CheckoutPage {

    Page page;

    private String firstNameField = "id=first-name";
    private String lastNameField = "id=last-name";
    private String zipCodeField = "id=postal-code";
    private String continueButton = "//input[@class='btn_primary cart_button']";
    private String checkoutButton = "//a[@class='btn_action checkout_button']";
    private String finishButton = "//a[@class='btn_action cart_button']";

    private String completeHeader = "//h2[@class='complete-header']";

    public CheckoutPage(Page page){
        this.page = page;
    }

    public CheckoutPage proceedToCheckout() {
        //page.click(".shopping_cart_link");
        page.click(checkoutButton);
        return this;
    }

    public CheckoutPage enterCheckoutDetails(String firstName, String lastName, String zipCode) {
        page.fill(firstNameField, firstName);
        page.fill(lastNameField, lastName);
        page.fill(zipCodeField, zipCode);
        return this;
    }

    public boolean validateErrorMessage(String expectedError) {
        Locator errorMsg = page.locator("h3[data-test='error']");
        return errorMsg.isVisible() && errorMsg.textContent().equals(expectedError);
    }

    public CheckoutPage fixZipCode(String zipCode) {
        page.fill(zipCodeField, zipCode);
        page.click(continueButton);
        return this;
    }

    public boolean verifyTotalPrice() {
        Locator subtotal = page.locator(".summary_subtotal_label");
        Locator tax = page.locator(".summary_tax_label");
        Locator total = page.locator(".summary_total_label");

        double subtotalValue = Double.parseDouble(subtotal.textContent().replace("Item total: $", ""));
        double taxValue = Double.parseDouble(tax.textContent().replace("Tax: $", ""));
        double expectedTotal = subtotalValue + taxValue;
        double actualTotal = Double.parseDouble(total.textContent().replace("Total: $", ""));

        return actualTotal == expectedTotal;
    }

    public CheckoutPage captureScreenshot() {
        PlaywrightFactory.takeScreenshot();
        return this;
    }

    public CheckoutPage completeOrder() {
        page.click(finishButton);
        return this;
    }

    public boolean verifyOrderConfirmation() {
        return page.locator(completeHeader).isVisible();
    }




}
