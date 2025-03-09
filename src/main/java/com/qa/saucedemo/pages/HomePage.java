package com.qa.saucedemo.pages;

import com.microsoft.playwright.ElementHandle;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;
import java.util.*;

import java.util.*;

public class HomePage {

    Page page;

    private String labelProducts = "//div[@class='product_label']";
    private String menuButton = "//div[@class='bm-burger-button']";
    private String labelLogout = "id=logout_sidebar_link";
    private String sortingDropdown = "//select[@class='product_sort_container']";
    private String getItemPrices = "//div[@class='inventory_item_price']";
    private String addToCartButtons = "//button[@class='btn_primary btn_inventory']";
    private String cartButton = "//div[@id='shopping_cart_container']";

    public HomePage(Page page){
        this.page = page;
    }

    public boolean isLoginSuccessful() {
        Locator element = page.locator(menuButton); // Replace with actual selector
        element.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        return page.locator(labelProducts).isVisible();
    }

    public void sortByPriceLowToHigh() {
        page.selectOption(sortingDropdown, "Price (low to high)");
    }

    public boolean isSortedByPrice() {
        List<ElementHandle> priceElements = page.querySelectorAll(getItemPrices);
        List<Double> prices = new ArrayList<>();
        for (ElementHandle priceElement : priceElements) {
            prices.add(Double.parseDouble(priceElement.textContent().replace("$", "")));
        }
        List<Double> sortedPrices = new ArrayList<>(prices);
        Collections.sort(sortedPrices);
        return prices.equals(sortedPrices);
    }

    public List<Map<String, String>> addRandomProductsToCart(int count) {
        List<ElementHandle> addToCartButtons = page.querySelectorAll("//button[@class='btn_primary btn_inventory']");
        List<ElementHandle> itemNames = page.querySelectorAll("//div[@class='inventory_item_name']");
        List<ElementHandle> itemPrices = page.querySelectorAll("//div[@class='inventory_item_price']");

        if (addToCartButtons.size() < count) {
            throw new IllegalStateException("Not enough products available to add to cart.");
        }

        Set<Integer> selectedIndexes = new HashSet<>();
        while (selectedIndexes.size() < count) {
            int randomIndex = new Random().nextInt(addToCartButtons.size());
            selectedIndexes.add(randomIndex);
        }

        List<Map<String, String>> selectedItems = new ArrayList<>();
        for (int index : selectedIndexes) {
            addToCartButtons.get(index).click();
            selectedItems.add(Map.of(
                    "name", itemNames.get(index).textContent(),
                    "price", itemPrices.get(index).textContent().replace("$","")
            ));
        }
        return selectedItems;
    }

    public void navigateToCart() {
        page.click(cartButton);
    }

}
