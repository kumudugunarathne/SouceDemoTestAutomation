package com.qa.saucedemo.pages;

import com.microsoft.playwright.ElementHandle;
import com.microsoft.playwright.Page;

import java.util.List;
import java.util.Random;

public class LoginPage {

    Page page;

    private String usernameField = "id=user-name";
    private String passwordField = "id=password";
    private String loginButton = "id=login-button";
    private String errorMessageLabel = "//h3[@data-test='error']";
    private String productImages = "//div[@class='inventory_item_img']";
    //Page instructor
    public LoginPage (Page page){
        this.page=page;
    }

    public void enterUsername(String username) {
        page.fill(usernameField,username);
    }

    public void enterPassword(String password) {
        page.fill(passwordField,password);
    }

    public void clickLoginButton() {
        page.click(loginButton);
    }

    public HomePage logIntoHomePage(String username, String password){
        enterUsername(username);
        enterPassword(password);
        clickLoginButton();
        return new HomePage(page);
    }


    public boolean isRandomImageBroken() {
        List<ElementHandle> images = page.locator(productImages).elementHandles();  // Get all images

        if (images.isEmpty()) {
            System.out.println("No images found on the page.");
            return true;
        }

        // Randomly select an image
        Random random = new Random();
        int randomIndex = random.nextInt(images.size());
        ElementHandle randomImage = images.get(randomIndex);

        // Try to get the src attribute (if present)
        String src = randomImage.getAttribute("src");

        System.out.println("Checking randomly selected image at index: " + randomIndex + " | src: " + src);

        // If src is null or empty, the image is broken
        return (src == null || src.isEmpty());
    }


    public double getPageLoadTime() {
        Object loadTime = page.evaluate("() => performance.timing.loadEventEnd - performance.timing.navigationStart");
        return Double.parseDouble(loadTime.toString());
    }

    public String getErrorMessage() {
        return page.locator(errorMessageLabel).textContent();
    }









}
