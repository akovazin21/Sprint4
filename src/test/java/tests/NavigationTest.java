package tests;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import pages.MainPage;
import pages.StatusPage;
import utils.WebDriverFactory;
import java.util.ArrayList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class NavigationTest {
    private WebDriver driver;

    @Before
    public void setUp() {
        driver = WebDriverFactory.create("chrome");
        driver.get("https://qa-scooter.praktikum-services.ru/");
    }

    @Test
    public void testScooterLogoRedirect() {
        MainPage mainPage = new MainPage(driver);
        mainPage.clickScooterLogo();

        assertEquals("Неверный URL после клика на логотип Самоката",
                "https://qa-scooter.praktikum-services.ru/",
                driver.getCurrentUrl());
    }

    @Test
    public void testYandexLogoRedirect() {
        MainPage mainPage = new MainPage(driver);
        mainPage.clickYandexLogo();

        // Переключаемся на новую вкладку
        ArrayList<String> tabs = new ArrayList<>(driver.getWindowHandles());
        driver.switchTo().window(tabs.get(1));

        assertTrue("Не открылась главная страница Яндекса",
                driver.getCurrentUrl().contains("yandex.ru"));

        // Закрываем вкладку и возвращаемся обратно
        driver.close();
        driver.switchTo().window(tabs.get(0));
    }

    @Test
    public void testInvalidOrderStatus() {
        MainPage mainPage = new MainPage(driver);
        StatusPage statusPage = new StatusPage(driver);

        mainPage.clickOrderStatusButton();
        mainPage.enterOrderNumber("000000");
        mainPage.clickGoButton();

        assertTrue("Не отображается сообщение о несуществующем заказе",
                statusPage.isNotFoundImageDisplayed());
    }

    @After
    public void tearDown() {
        driver.quit();
    }
}