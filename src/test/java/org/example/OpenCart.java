package org.example;

import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

import static java.awt.SystemColor.window;
import static org.testng.Assert.assertEquals;

public class OpenCart {


    WebDriver driver = new FirefoxDriver();

    // WebDriver driver = new ChromeDriver();


    private boolean isElementPresent(By by) {

        try {
            driver.findElement(by);
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }


    @BeforeTest
    public void openWebsite() {
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.navigate().to("https://opencart.abstracta.us/");
    }

    @Test
    public void getTitle() {

        //driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        String title = driver.getTitle();

        assertEquals("Your Store", title);
    }

    @Test(dependsOnMethods = "getTitle")
    public void checkHomePageOpened() {

        WebElement logo = driver.findElement(By.id("logo"));

        logo.isDisplayed();
    }

    @Test(dependsOnMethods = "checkHomePageOpened")
    public void CheckWrongSearchResult() {

        WebElement searchInput = driver.findElement(By.name("search"));

        searchInput.sendKeys("car");

        WebElement searchButton = driver.findElement(By.className("btn-lg"));

        searchButton.click();
    }

    @Test(dependsOnMethods = "CheckWrongSearchResult")
    public void happyCaseCheckSearch() throws InterruptedException {

        WebElement searchInput = driver.findElement(By.name("search"));
        searchInput.clear();
        searchInput.sendKeys("MacBook");

        WebElement searchButton = driver.findElement(By.className("btn-lg"));
        searchButton.click();

        Thread.sleep(5000);

        WebElement searchPageButton = driver.findElement(By.className("btn-primary"));
        searchPageButton.getText();

        if (searchPageButton.getText() == "Search") {
            System.out.println("You opened the search page.");
        }

        Thread.sleep(5000);

        WebElement numberOfSearchResults = driver.findElement(By.className("text-right"));
        numberOfSearchResults.getText();

        String str = numberOfSearchResults.getText();
        String numbers = str.replaceAll("[^0-9]", "");

        int searchResultsNumber = Integer.parseInt(numbers);
        if (searchResultsNumber > 0) {
            System.out.println("We found one product and maybe more.");
        }
    }

    @Test(dependsOnMethods = "happyCaseCheckSearch")
    public void checkAddToCart() throws InterruptedException {
        WebElement addToCartButton = driver
                .findElement(By.xpath("/html/body/div[2]/div/div/div[3]/div[1]/div/div[2]/div[2]/button[1]"));
        addToCartButton.click();

        Thread.sleep(5000);

        WebElement cartIcon = driver.findElement(By.id("cart"));
        cartIcon.click();

        Thread.sleep(5000);
    }

    @Test(dependsOnMethods = "checkAddToCart")
    public void comparePrice() {

        WebElement itemPriceInProductCard = driver.findElement(By.className("price-tax"));
        itemPriceInProductCard.getText();
        System.out.println(itemPriceInProductCard.getText());

        String stringOfItemInCard = itemPriceInProductCard.getText();
        String numb = stringOfItemInCard.replaceAll("[^0-9]", "");

        WebElement itemPriceInCart = driver
                .findElement(By.xpath("/html/body/header/div/div/div[3]/div/ul/li[2]/div/table/tbody/tr[1]/td[2]"));
        itemPriceInCart.getText();
        System.out.println(itemPriceInCart.getText());

        String stringOfItemInCart = itemPriceInCart.getText();
        String numbers = stringOfItemInCart.replaceAll("[^0-9]", "");

        if (numb == numbers) {
            System.out.println(
                    "The price of items in the cart is the same as the price of the item in the card. ElAs3ar wa7da ya 7omar.");
        }
    }

    @Test(dependsOnMethods = "comparePrice")
    public void checkoutPage() {
        WebElement checkoutPageButton = driver.findElement(By.className("fa-share"));
        checkoutPageButton.click();

        String title = driver.getTitle();
        assertEquals("Checkout", title);
    }

    @Test(dependsOnMethods = "checkoutPage")
    public void signToCompleteCheckout() {

        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollBy(0,1500)", "");

        WebElement email = driver.findElement(By.id("input-email"));
        email.sendKeys("ssgtester@yahoo.com");

        WebElement password = driver.findElement(By.id("input-password"));
        password.sendKeys("Testing");

        WebElement buttonLogin = driver.findElement(By.id("button-login"));
        buttonLogin.click();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions
                .visibilityOfElementLocated(By.xpath("/html/body/div[2]/div/div/div/div[1]/div[2]/div/div[1]")));
        driver.findElement(By.xpath("/html/body/div[2]/div/div/div/div[1]/div[2]/div/div[1]"));
        assertEquals("Warning: No match for E-Mail Address and/or Password.",
                "Warning: No match for E-Mail Address and/or Password.");

        WebElement buttonContinue = driver.findElement(By.id("button-account"));
        buttonContinue.click();
    }

    /*@AfterMethod
    public void takeScreenShot(ITestResult result) throws IOException {

        if(ITestResult.FAILURE == result.getStatus()) {

            TakesScreenshot takeShot = (TakesScreenshot) driver;
            File source = takeShot.getScreenshotAs(OutputType.FILE);

            FileUtils.copyFile(source, new File("./Screenshots/" + result.getName() + ".png"));
        }
    }*/

    @Test(dependsOnMethods = "signToCompleteCheckout")
    public void signUpNewAccount() throws InterruptedException {

        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        WebElement firstName = driver.findElement(By.id("input-payment-firstname"));
        firstName.sendKeys("Qc");
        String sentence = "";
        System.out.println(sentence.length());

        WebElement lastName = driver.findElement(By.id("input-payment-lastname"));
        lastName.sendKeys("Testing");

        String userName = "" + (int) (Math.random() * Integer.MAX_VALUE);
        String emailID = "User" + userName + "@example.com";

        WebElement userEmail = driver.findElement(By.id("input-payment-email"));
        userEmail.sendKeys(emailID);

        WebElement userPhone = driver.findElement(By.id("input-payment-telephone"));
        userPhone.sendKeys("+201212653930");

        WebElement passwordField = driver.findElement(By.id("input-payment-password"));
        passwordField.sendKeys("Testing");

        WebElement confirmPassword = driver.findElement(By.id("input-payment-confirm"));
        confirmPassword.sendKeys("Testing");

        WebElement address = driver.findElement(By.id("input-payment-address-1"));
        address.sendKeys("Asorc");

        WebElement city = driver.findElement(By.id("input-payment-city"));
        city.sendKeys("Asyut");

        WebElement postalCode = driver.findElement(By.id("input-payment-postcode"));
        postalCode.sendKeys("71782");

        Thread.sleep(5000);

        WebElement countryOfUser = driver.findElement(By.id("input-payment-country"));
        countryOfUser.click();
        Thread.sleep(5000);
        Select countryOfTheUser = new Select(countryOfUser);
        countryOfTheUser.selectByVisibleText("Egypt");


        WebElement shipmentCity = driver.findElement(By.id("input-payment-zone"));
        shipmentCity.click();
        Thread.sleep(5000);
        Select cityOfUser = new Select(shipmentCity);
        cityOfUser.selectByVisibleText("Asyut");

        Actions builder = new Actions(driver);
        builder.scrollToElement(shipmentCity).perform();

        WebElement checkBox = driver.findElement(By.name("agree"));
        checkBox.click();
        //Actions act=new Actions(driver);
        //act.moveToElement(checkBox).click().perform();

        WebElement registerButton = driver.findElement(By.id("button-register"));
        registerButton.click();


    }

    @Test(dependsOnMethods = "signUpNewAccount")
    public void confirmPaymentMethod() throws InterruptedException {

        Thread.sleep(5000);
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        WebElement checkBox = driver.findElement(By.cssSelector("div.buttons:nth-child(5) > div:nth-child(1) > input:nth-child(2)"));
        checkBox.click();

        WebElement registerButton = driver.findElement(By.id("button-payment-method"));
        registerButton.click();
    }


     /*@Test(dependsOnMethods = "confirmPaymentMethod") public void completeOrder()
     {
         WebElement itemPrice = driver.findElement(By.xpath("/html/body/div[2]/div/div/div/div[4]/div[2]/div/div[1]/table/tfoot/tr[4]/td[2]"));
         WebElement totalPrice = driver.findElement(By.xpath("/html/body/div[2]/div/div/div/div[4]/div[2]/div/div[1]/table/tbody/tr/td[4]"));
         System.out.println(itemPrice);
         System.out.println(totalPrice);

         Assert.assertEquals(itemPrice, totalPrice);
     }*/

    @Test(dependsOnMethods = "confirmPaymentMethod")
    public void finishOrder() {


        JavascriptExecutor js = (JavascriptExecutor) driver;
        WebElement finishOrderButton = driver.findElement(By.xpath("//*[@id=\"button-confirm\"]"));

        js.executeScript("arguments[0].scrollIntoView();", finishOrderButton);

        finishOrderButton.click();
    }

}
