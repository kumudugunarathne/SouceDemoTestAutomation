package com.qa.saucedemo.pages;

import com.microsoft.playwright.ElementHandle;
import com.microsoft.playwright.Page;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CartPage {

    Page page;

    public CartPage(Page page){
        this.page = page;
    }

    public List<Map<String, String>> getCartItems() {
        List<ElementHandle> cartNames = page.querySelectorAll("//div[@class='inventory_item_name']");
        List<ElementHandle> cartPrices = page.querySelectorAll("//div[@class='inventory_item_price']");
        List<Map<String, String>> cartItems = new ArrayList<>();

        for (int i = 0; i < cartNames.size(); i++) {
            cartItems.add(Map.of(
                    "name", cartNames.get(i).textContent(),
                    "price", cartPrices.get(i).textContent()
            ));
        }
        return cartItems;
    }

    public String removeMostExpensiveItem() {
        List<Map<String, String>> cartItems = getCartItems();
        cartItems.sort((a, b) -> Double.compare(
                Double.parseDouble(b.get("price").replace("$", "")),
                Double.parseDouble(a.get("price").replace("$", ""))
        ));
        String mostExpensiveItem = cartItems.get(0).get("name");
        page.locator("//div[text()='"+mostExpensiveItem+"']/../../../div[@class='cart_item_label']/div[2]/button").click();
        return mostExpensiveItem;
    }

    public boolean isItemInCart(String itemName) {
        List<ElementHandle> updatedCartNames = page.querySelectorAll(".inventory_item_name");
        for (ElementHandle nameElement : updatedCartNames) {
            if (nameElement.textContent().equals(itemName)) {
                return true;
            }
        }
        return false;
    }

    public void navigateToCheckOutPage(){

    }


}
