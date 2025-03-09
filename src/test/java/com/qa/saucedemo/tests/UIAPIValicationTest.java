package com.qa.saucedemo.tests;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.playwright.*;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.List;
import java.util.Random;

public class UIAPIValicationTest {

    public static Playwright playwright;
    private static Browser browser;
    private static Page page;
    public static APIRequestContext requestContext;
    private static ObjectMapper objectMapper = new ObjectMapper();

    @BeforeClass
    public void setup() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
        page = browser.newPage();
        requestContext = playwright.request().newContext();
    }

    @Test
    public void testApiAndUiValidation() throws IOException {
        // 1. Make a GET request to fetch users from the API
        APIResponse response = requestContext.get("https://reqres.in/api/users?page=2");

        if (response.status() != 200) {
            Assert.fail("API Request failed! Status Code: " + response.status());
        }

        // 2. Parse JSON response
        JsonNode responseBody = objectMapper.readTree(response.text());
        List<JsonNode> users = responseBody.get("data").findValues("id");

        if (users.isEmpty()) {
            Assert.fail("No users found in API response!");
        }

        // 3. Pick a random user
        Random random = new Random();
        JsonNode randomUser = responseBody.get("data").get(random.nextInt(users.size()));

        String firstName = randomUser.get("first_name").asText();
        String lastName = randomUser.get("last_name").asText();
        String email = randomUser.get("email").asText();
        String avatar = randomUser.get("avatar").asText();

        System.out.println("Random User Selected: " + firstName + " " + lastName);

        // 4. Navigate to UI and simulate search
        page.navigate("https://your-ui-url.com"); // Replace with actual URL
        page.fill("#searchBox", firstName);
        page.click("#searchButton");

        // 5. Validate UI elements against API data
        String uiFirstName = page.textContent("#firstName"); // Replace with actual selectors
        String uiLastName = page.textContent("#lastName");
        String uiEmail = page.textContent("#email");
        String uiAvatar = page.getAttribute("#avatar", "src");

        Assert.assertEquals(uiFirstName, firstName, "First Name mismatch!");
        Assert.assertEquals(uiLastName, lastName, "Last Name mismatch!");
        Assert.assertEquals(uiEmail, email, "Email mismatch!");
        Assert.assertEquals(uiAvatar, avatar, "Avatar URL mismatch!");

        System.out.println("UI matches API data successfully!");
    }




}
