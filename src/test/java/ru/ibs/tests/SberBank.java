package ru.ibs.tests;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.rules.Timeout;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
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

        FirefoxOptions options = new FirefoxOptions(); // настройки браузера
        options.setPageLoadStrategy(PageLoadStrategy.EAGER); // - пишем в настройки браузера, что не ждем полной загрузки страницы - начинаем тестировать когда загрузился визуально

        driver = new FirefoxDriver(options);
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(60));
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(100));

        wait = new WebDriverWait(driver, Duration.ofSeconds(10), Duration.ofSeconds(30000));

        driver.manage().window().maximize(); // делаем на весь экран браузер
        driver.get("https://demoqa.com/automation-practice-form"); // переходим на тестируемый сайт
    }

    @Test
    public void auth() {
        //негативный тест авторизации
        int randomLoginPart = (int) (Math.random() * 10001);
        driver.get("https://demoqa.com/login"); //
        WebElement loginButton = driver.findElement(By.xpath("//*[@id=\"login\"]"));
        WebElement toRegisterFormButton = driver.findElement(By.xpath("//*[@id=\"newUser\"]"));

        fillInputField(driver.findElement(By.xpath("//*[@id=\"userName\"]")), "test"+ randomLoginPart);
        fillInputField(driver.findElement(By.xpath("//*[@id=\"password\"]")), "test"+ randomLoginPart);

        loginButton.click();

        WebElement authError = driver.findElement(By.xpath("//*[@id=\"name\"]"));
        waitUtilElementToBeVisible(authError);

        //проверка на существование логина
        Assert.assertTrue("Авторизация прошла успешно", authError.getText().contains("Invalid username or password!"));

        //----попытка регистрации----
        waitUtilElementToBeVisibleBack(driver.findElement(By.xpath("//*[@id=\"newUser\"]"))).click();

        waitUtilElementToBeVisible(driver.findElement(By.xpath("//*[@id=\"firstname\"]")));

        fillInputField(driver.findElement(By.xpath("//*[@id=\"firstname\"]")), "test"+ randomLoginPart);
        fillInputField(driver.findElement(By.xpath("//*[@id=\"lastname\"]")), "test"+ randomLoginPart);
        fillInputField(driver.findElement(By.xpath("//*[@id=\"userName\"]")), "test"+ randomLoginPart);
        fillInputField(driver.findElement(By.xpath("//*[@id=\"password\"]")), "test"+ randomLoginPart);

        WebElement registerButton = driver.findElement(By.xpath("//*[@id=\"register\"]"));
        registerButton.click();

        authError = driver.findElement(By.xpath("//*[@id=\"name\"]"));
        waitUtilElementToBeVisible(authError);

        //негативный тест регистрации - дальше не пускает капча
        Assert.assertTrue("КАПЧА ПУСТИЛА????", authError.getText().contains("Please verify reCaptcha to register!"));

        //--------
        // назад к авторизации (позитивный сценарий)
        WebElement backToLoginButton = driver.findElement(By.xpath("//*[@id=\"gotologin\"]"));
        waitUtilElementToBeVisible(backToLoginButton);
        backToLoginButton.click();

        waitUtilElementToBeVisible(driver.findElement(By.xpath("//*[@id=\"userName\"]")));

        // заранее зарегистрированный пользователь
        fillInputField(driver.findElement(By.xpath("//*[@id=\"userName\"]")), "Test6190%");
        fillInputField(driver.findElement(By.xpath("//*[@id=\"password\"]")), "Test6190%");


        waitUtilElementToBeVisibleBack(driver.findElement(By.xpath("//*[@id=\"login\"]"))).click();

        // отобразился профиль пользователя книжного магазина
        Assert.assertTrue("нет ошибки - пользователь существует?", waitUtilElementToBeVisibleBack(driver
                .findElement(By.xpath("//*[@id=\"userName-value\"]"))).getText().contains("Test6190%"));
    }

    @Test
    public void findTitleTest() throws InterruptedException {
        WebElement baseMenu = null;
        baseMenu = driver.findElement(By.xpath("//h1[contains(text(), 'Practice Form')]"));
        Assert.assertTrue("No lable: 'Practice Form'", baseMenu.getText().contains("Practice Form"));
    }

    @Test
    public void formPositiveTest() throws InterruptedException {
        String fieldXPath = "//input[@id='%s']";

        WebElement name = driver.findElement(By.xpath("//*[@id=\"firstName\"]"));
        fillInputField(name, "Иванов");
        // Assert.assertTrue("No green", name.getCssValue("border-color").equals("var(--bs-form-valid-border-color)"));

        fillInputField(driver.findElement(By.xpath("//*[@id=\"lastName\"]")), "Иванов");
        fillInputField(driver.findElement(By.xpath("//*[@id=\"userEmail\"]")), "ivanov@mail.ru");

        WebElement gender = null;
        gender = driver.findElement(By.xpath("//*[@id=\"gender-radio-1\"]"));
        gender.click();

        fillInputField(driver.findElement(By.xpath("//*[@id=\"userNumber\"]")), "1234567890");

        WebElement hobbies = null;
        hobbies = driver.findElement(By.xpath("//*[@id=\"hobbies-checkbox-3\"]"));
        hobbies.click();
        //chooseAnyOption(By.id("state"));
        //chooseAnyOption(By.id("city"));

        selectOption(By.id("state"), "Haryana");
        selectOption(By.id("city"), "Karnal");

        WebElement submit = null;
        submit = driver.findElement(By.xpath("//*[@id=\"submit\"]"));
        submit.click();

        WebElement success = null;
        success = driver.findElement(By.xpath("//*[@id=\"example-modal-sizes-title-lg\"]"));
        Assert.assertTrue("No success", success.getText().contains("Thanks for submitting the form"));
    }

    @Test
    public void formNegativeTest() throws InterruptedException {
        String fieldXPath = "//input[@id='%s']";
        fillInputField(driver.findElement(By.xpath("//*[@id=\"firstName\"]")), "Иван");

        String oldColor = driver.findElement(By.xpath("//*[@id=\"lastName\"]"))
                .getCssValue("border-color");

        WebElement submit = null;
        submit = driver.findElement(By.xpath("//*[@id=\"submit\"]"));
        scrollToElementJs(submit);
        waitUtilElementToBeClickable(submit);
        Thread.sleep(10000);
        submit.click();

        waitUtilElementToBeVisible(driver.findElement(By.xpath("//*[@id=\"lastName\"]")));
        Thread.sleep(10000);

        Assert.assertNotEquals("Цвет не поменялся", driver.findElement(By.xpath("//*[@id=\"lastName\"]"))
                .getCssValue("border-color"), oldColor);
        Assert.assertNotEquals("Цвет не поменялся", driver.findElement(By.xpath("//*[@id=\"gender-radio-1\"]"))
                .getCssValue("border-color"),oldColor);
        Assert.assertNotEquals("Цвет не поменялся", driver.findElement(By.xpath("//*[@id=\"gender-radio-2\"]"))
                .getCssValue("border-color"),oldColor);
        Assert.assertNotEquals("Цвет не поменялся", driver.findElement(By.xpath("//*[@id=\"gender-radio-3\"]"))
                .getCssValue("border-color"),oldColor);
        Assert.assertNotEquals("Цвет не поменялся", driver.findElement(By.xpath("//*[@id=\"userNumber\"]"))
                .getCssValue("border-color"),oldColor);
    }

    @After
    public void af(){
        driver.quit();
    }

    /**
     * Скрол до элемента на js коде
     *
     * @param element - веб элемент до которого нужно проскролить
     */
    private void scrollToElementJs(WebElement element) {
        JavascriptExecutor javascriptExecutor = (JavascriptExecutor) driver;
        javascriptExecutor.executeScript("arguments[0].scrollIntoView(true);", element);
    }

    /**
     * Явное ожидание того что элемент станет кликабельный
     *
     * @param element - веб элемент до которого нужно проскролить
     */
    private void waitUtilElementToBeClickable(WebElement element) {
        wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    /**
     * Явное ожидание того что элемент станет видемым
     *
     * @param locator - локатор до веб элемент который мы ожидаем найти и который виден на странице
     */
    private void waitUtilElementToBeVisible(By locator) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    /**
     * Явное ожидание того что элемент станет видемым
     *
     * @param element - веб элемент который мы ожидаем что будет  виден на странице
     */
    private void waitUtilElementToBeVisible(WebElement element) {
        wait.until(ExpectedConditions.visibilityOf(element));
    }

    private WebElement waitUtilElementToBeVisibleBack(WebElement element) {
        return wait.until(ExpectedConditions.visibilityOf(element));
    }

    /**
     * Заполнение полей определённым значений
     *
     * @param element - веб элемент (поле какое-то) которое планируем заполнить)
     * @param value - значение которы мы заполняем веб элемент (поле какое-то)
     */
    private void fillInputField(WebElement element, String value) {
        scrollToElementJs(element);
        waitUtilElementToBeClickable(element);
        element.click();
        element.clear();
        element.sendKeys(value);
        boolean checkFlag = wait.until(ExpectedConditions.attributeContains(element, "value", value));
        Assert.assertTrue("Поле было заполнено некорректно", checkFlag);
    }

    /**
     * Проверка ошибка именно у конкретного поля
     *
     * @param element веб элемент (поле какое-то) которое не заполнили
     * @param errorMessage - ожидаемая ошибка под полем которое мы не заполнили
     */
    private void checkErrorMessageAtField(WebElement element, String errorMessage) {
        element = element.findElement(By.xpath("./..//span"));
        Assert.assertEquals("Проверка ошибки у поля не была пройдена",
                errorMessage, element.getText());
    }

    private void chooseAnyOption(By control) {
        driver.findElement(control).click();

        WebElement option = wait.until(
                ExpectedConditions.elementToBeClickable(
                        By.cssSelector("[role='option']")
                )
        );
        option.click();
    }

    private void selectOption(By control, String optionText) {
        driver.findElement(control).click();

        By option = By.xpath(
                "//*[@role='option' and normalize-space()='" + optionText + "']"
        );

        wait.until(ExpectedConditions.elementToBeClickable(option)).click();
    }

}
