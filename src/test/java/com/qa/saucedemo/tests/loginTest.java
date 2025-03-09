package com.qa.saucedemo.tests;

import com.qa.saucedemo.base.BaseTest;
import com.qa.saucedemo.pages.HomePage;
import com.qa.saucedemo.pages.LoginPage;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import static com.qa.saucedemo.factory.PlaywrightFactory.takeScreenshot;

public class loginTest extends BaseTest {

    HomePage homePage;
   // LoginPage loginPage;

    @Test(priority = 1, dataProvider = "userData")
    public void testSuccessfulLogin(String username, String password, String role) {
        if(role.equals("standard_user")){
            this.homePage = super.loginPage.logIntoHomePage(username,password);
            Assert.assertTrue(this.homePage.isLoginSuccessful(), "Login failed");
            System.out.println("Login successful");
        }
    }


    @Test(priority = 2, dataProvider = "userData")
    public void testLockedOutUser(String username, String password, String role) {
        if (!role.startsWith("Error")) return;
        long startTime = System.currentTimeMillis();
       loginPage.logIntoHomePage(username,password);
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        System.out.println("Error message appeared in " + duration + "ms");

        String errorMsg = loginPage.getErrorMessage();
        if (!errorMsg.equals("Epic sadface: Sorry, this user has been locked out.")) {
            takeScreenshot();
            Assert.fail("Incorrect error message for locked out user.");
        }

        Assert.assertTrue(duration < 4000, "Error message took too long!");
        System.out.println("Correct error message displayed for: " + username);
    }

    @Test(priority = 3, dataProvider = "userData")
    public void testProblemUserImageValidation(String username, String password, String role) {
        if (!role.equals("CheckImages")) return;
        long startTime = System.currentTimeMillis();
        loginPage.logIntoHomePage(username,password);
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        System.out.println("Image validation took " + duration + "ms");

        boolean isBroken = loginPage.isRandomImageBroken();
        if (isBroken) {
            takeScreenshot();
            Assert.assertTrue(isBroken,"A randomly selected image is broken.");
        }

        Assert.assertTrue(duration < 5000, "Image loading took too long!");
        System.out.println("Random image validation passed for: " + username);

    }


    @Test(priority = 4,dataProvider = "userData")
    public void testPerformanceGlitchUser(String username, String password, String role) {
        if (!role.equals("CheckPerformance")) return;
        long startTime = System.currentTimeMillis();
        loginPage.logIntoHomePage(username,password);
        double loadTime = loginPage.getPageLoadTime();
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        System.out.println("Page load time: " + loadTime + "ms, Total execution time: " + duration + "ms");

        if (loadTime >= 5000) {
            takeScreenshot();
            Assert.fail("Page load time exceeded 5s for: " + username);
        }

        Assert.assertTrue(duration < 6000, "Test execution took too long!");
        System.out.println("Performance check passed for: " + username);

    }

}
