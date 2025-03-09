package com.qa.saucedemo.tests;
import com.microsoft.playwright.*;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.testng.Assert;
import org.testng.annotations.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;


public class VisualPerformanceTest {

    private static Playwright playwright;
    private static Browser browser;
    private static Page page;
    private static BrowserContext context;

    private static final String BASELINE_IMAGE = "baseline/product-listing.png";
    private static final String CURRENT_IMAGE = "screenshots/current-product-listing.png";
    private static final String PRODUCT_LISTING_URL = "https://your-ui-url.com/product-listing"; // Replace with actual URL

    @BeforeClass
    public void setup() {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME); // Load OpenCV
        playwright = Playwright.create();
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
        context = browser.newContext(new Browser.NewContextOptions().setViewportSize(1280, 720));
        page = context.newPage();
    }

    @Test
    public void testVisualAndPerformance() {
        long startTime = System.currentTimeMillis();

        // Implementing retry mechanism (Max 3 attempts)
        int maxRetries = 3;
        for (int i = 0; i < maxRetries; i++) {
            try {
                page.navigate(PRODUCT_LISTING_URL, new Page.NavigateOptions().setTimeout(5000));
                break; // If successful, exit the loop
            } catch (PlaywrightException e) {
                System.out.println("Network issue, retrying... (" + (i + 1) + "/" + maxRetries + ")");
                if (i == maxRetries - 1) {
                    Assert.fail("Failed to load page after retries due to network issues.");
                }
            }
        }

        long endTime = System.currentTimeMillis();
        long loadTime = endTime - startTime;
        System.out.println("Page Load Time: " + loadTime + " ms");

        // Fail test if load time exceeds 3000ms (3 seconds)
        Assert.assertTrue(loadTime <= 3000, "Page load time exceeded 3 seconds!");

        // Capture screenshot of current state
        page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get(CURRENT_IMAGE)));

        // Compare screenshot with baseline image
        boolean match = compareImages(BASELINE_IMAGE, CURRENT_IMAGE);

        // Fail the test if the images do not match
        Assert.assertTrue(match, "Visual regression detected! UI has changed.");
    }

    /**
     * Method to compare baseline and current screenshots using OpenCV.
     * @param baselineImagePath Path of the baseline image.
     * @param currentImagePath Path of the newly captured screenshot.
     * @return true if images match, false otherwise.
     */
    private boolean compareImages(String baselineImagePath, String currentImagePath) {
        Mat baselineImage = Imgcodecs.imread(baselineImagePath);
        Mat currentImage = Imgcodecs.imread(currentImagePath);

        if (baselineImage.empty() || currentImage.empty()) {
            System.out.println("Error loading images for comparison.");
            return false;
        }

        // Convert images to grayscale for better comparison
        Mat baselineGray = new Mat();
        Mat currentGray = new Mat();
        Imgproc.cvtColor(baselineImage, baselineGray, Imgproc.COLOR_BGR2GRAY);
        Imgproc.cvtColor(currentImage, currentGray, Imgproc.COLOR_BGR2GRAY);

        // Compute absolute difference
        Mat diff = new Mat();
        Core.absdiff(baselineGray, currentGray, diff);
        Imgproc.threshold(diff, diff, 25, 255, Imgproc.THRESH_BINARY);

        int nonZeroCount = Core.countNonZero(diff);
        System.out.println("Difference Pixel Count: " + nonZeroCount);

        // If significant differences exist, return false
        return nonZeroCount < 500; // Adjust threshold as needed
    }

    @AfterClass
    public void tearDown() {
        if (browser != null) {
            browser.close();
        }
        if (playwright != null) {
            playwright.close();
        }
    }


}
