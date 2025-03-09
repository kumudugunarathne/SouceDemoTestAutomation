package com.qa.saucedemo.base;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.Page;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import com.qa.saucedemo.factory.PlaywrightFactory;
import com.qa.saucedemo.pages.CartPage;
import com.qa.saucedemo.pages.HomePage;
import com.qa.saucedemo.pages.LoginPage;
import com.qa.saucedemo.util.UserData;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import static com.qa.saucedemo.tests.UIAPIValicationTest.playwright;

public class BaseTest {

    PlaywrightFactory pf;
    public Page page;
    protected Properties properties;
    protected LoginPage loginPage;

    @Parameters({"browser"})
    @BeforeTest
    public  void setup(String browserName){
        pf=new PlaywrightFactory();
        properties = pf.init_prop();
        if(browserName!=null){
            properties.setProperty("browser",browserName);
        }
        page = pf.initBrowser(properties);
        loginPage = new LoginPage(page);
        APIRequestContext requestContext;
        requestContext = playwright.request().newContext();
    }

    @DataProvider(name = "userData")
    public Object[][] getData() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();

        // Read JSON file and map to Java object array
        UserData[] users = objectMapper.readValue(new File("src/main/resources/config/userdata.json"), UserData[].class);

        // Convert array to Object[][] for TestNG DataProvider
        Object[][] testData = new Object[users.length][3];
        for (int i = 0; i < users.length; i++) {
            testData[i][0] = users[i].getUsername();
            testData[i][1] = users[i].getPassword();
            testData[i][2] = users[i].getRole();
        }
        return testData;
    }


    @AfterTest
    public void tearDown(){
        page.context().browser().close();
    }
}
