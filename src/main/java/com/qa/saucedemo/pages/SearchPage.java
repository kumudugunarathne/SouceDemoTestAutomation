package com.qa.saucedemo.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class SearchPage {

    private final Page page;

    public SearchPage(Page page) {
        this.page = page;
    }

    public SearchPage searchUser(String name) {
        page.fill("#search-box", name);
        page.click("#search-button");
        return this;
    }

    public boolean validateUserDetails(String expectedName, String expectedEmail) {
        Locator nameElement = page.locator(".user-name");
        Locator emailElement = page.locator(".user-email");

        return nameElement.isVisible() &&
                emailElement.isVisible() &&
                nameElement.textContent().equalsIgnoreCase(expectedName) &&
                emailElement.textContent().equalsIgnoreCase(expectedEmail);
    }
}
