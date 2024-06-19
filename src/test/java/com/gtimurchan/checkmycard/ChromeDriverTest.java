package com.gtimurchan.checkmycard;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import static org.junit.Assert.assertTrue;

public class ChromeDriverTest {

    private WebDriver driver;

    @Before
    public void setUp() {
        // Set path to ChromeDriver executable
//        System.setProperty("webdriver.chrome.driver", Const.PATH_TO_CHROME_DRIVER);
//
//        // Initialize ChromeDriver
//        driver = new ChromeDriver();
    }

    @Test
    public void testGoogleSearch() {
        // Open Google
//        driver.get("https://www.google.com");
//
//        // Perform a search
//        driver.findElement(By.name("q")).sendKeys("Selenium");
//        driver.findElement(By.name("q")).submit();
//
//        // Wait for the results page to load
//        WebDriverWait wait = new WebDriverWait(driver, 10);
//        wait.until(ExpectedConditions.titleContains("Selenium"));
//
//        // Assert that the title contains the search term
//        assertTrue(driver.getTitle().contains("Selenium"));
    }

    @After
    public void tearDown() {
        // Quit the browser
//        if (driver != null) {
//            driver.quit();
//        }
    }
}