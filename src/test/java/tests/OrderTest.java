package tests;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.openqa.selenium.WebDriver;
import pages.MainPage;
import pages.OrderPage;
import utils.WebDriverFactory;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertTrue;

@RunWith(Parameterized.class)
public class OrderTest {
    public static final String URL = "https://qa-scooter.praktikum-services.ru/";
    public WebDriver driver;
    private final String name;
    private final String surname;
    private final String address;
    private final String metroStation;
    private final String phone;
    private final String date;
    private final String period;
    private final String color;
    private final String comment;
    private final boolean useUpperButton;

    public OrderTest(String name, String surname, String address, String metroStation,
                     String phone, String date, String period, String color,
                     String comment, boolean useUpperButton) {
        this.name = name;
        this.surname = surname;
        this.address = address;
        this.metroStation = metroStation;
        this.phone = phone;
        this.date = date;
        this.period = period;
        this.color = color;
        this.comment = comment;
        this.useUpperButton = useUpperButton;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                {"Иван", "Иванов", "ул. Ленина, 1", "Черкизовская",
                        "89991112233", "01.01.2023", "сутки", "black",
                        "Позвонить за час", true},
                {"Анна", "Петрова", "пр. Мира, 10", "Сокольники",
                        "89994445566", "15.01.2023", "двое суток", "grey",
                        "Не звонить", false}
        });
    }

    @Before
    public void setUp() {
        driver = WebDriverFactory.create("firefox");
        driver.get(URL);
    }

    @Test
    public void testOrderCreation() {
        MainPage mainPage = new MainPage(driver);
        OrderPage orderPage = new OrderPage(driver);

        // Нажимаем кнопку заказа (верхнюю или нижнюю)
        if (useUpperButton) {
            mainPage.clickUpperOrderButton();
        } else {
            mainPage.clickLowerOrderButton();
        }

        // Заполняем первую страницу заказа
        orderPage.fillFirstPage(name, surname, address, metroStation, phone);

        // Заполняем вторую страницу заказа
        orderPage.fillSecondPage(date, period, color, comment);

        // Подтверждаем заказ
        orderPage.confirmOrder();

        // Проверяем, что появилось окно успешного заказа
        assertTrue("Окно успешного заказа не отображается",
                orderPage.isOrderSuccessModalDisplayed());

        // Добавляем проверку текста сообщения
        String expectedMessage = "Заказ оформлен";
        String actualMessage = orderPage.getOrderSuccessMessage();
        assertTrue("Сообщение об успешном заказе не соответствует ожидаемому",
                actualMessage.contains(expectedMessage));
    }

    @After
    public void tearDown() {
        driver.quit();
    }
}