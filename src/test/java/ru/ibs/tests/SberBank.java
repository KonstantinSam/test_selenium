package ru.ibs.tests;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.rules.Timeout;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class SberBank {
    WebDriver driver;
    WebDriverWait wait;

    @Before
    public void b() {
        java.lang.System.setProperty("webdriver.firefox.driver", "\"C:\\Users\\ESamoncheva\\Documents\\firefox_driver\\geckodriver.exe\"");
        driver = new FirefoxDriver();
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(10));
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        wait = new WebDriverWait(driver, Duration.ofSeconds(10), Duration.ofSeconds(2000));

        driver.manage().window().maximize();
        driver.get("https://www.sberbank.ru/ru/person");
    }

    @Test
    public void test() throws InterruptedException {
        //driver.navigate().to("\"https://www.sberbank.ru/ru/person\""); - с сохранением истории
        //driver.getTitle(); -
        //driver.findElement();
        // -> //div[contains(text(),'Для семьи')] поиск пути в Elements devTools
//        Thread.sleep(10000); // timer
//        wait.until(ExpectedConditions.visibilityOfAllElements( ))
        WebElement baseMenu = driver.findElement(By.xpath("//span[contains(text(), 'Меню')]/.."));
        Assert.assertTrue("No click", baseMenu.getAttribute("aria-label").contains("Открыть главное меню"));
        baseMenu.click();
    }

    @After
    public void af(){
        driver.quit();
    }

}
