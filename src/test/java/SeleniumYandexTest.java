import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

public class SeleniumYandexTest {
    private WebDriver webDriver;


    private static final String baseURL = "https://yandex.ru";

    //локаторы
    private static final String YANDEX_LOGO = "//div[@class='home-logo__default']";
    private static final String MARKET = "//div [@class='services-new__item-title'][.='Маркет']";
    private static final String FIND_FIELD = "//input [@type='text'][@name='text'][@placeholder='Искать товары']";
    private static final String FIND_BUTTON = "//span [.='Найти'] /parent::button";

    //локаторы на фильтр
    private static final String PRICE_FROM_LOC = "//div [@data-auto='filter-range-glprice']//span [@data-auto='filter-range-min']";
    private static final String PRICE_TO_LOC = "//div [@data-auto='filter-range-glprice']//span [@data-auto='filter-range-max']";
    private static final String PRICE_FROM_ID = "glpricefrom";
    private static final String PRICE_TO_ID = "glpriceto";
    private static final String SHOW_ALL_LABEL_MAN = "//legend [text()='Производитель']/following-sibling::footer/button [text()='Показать всё']";
    private static final String MANUFACTURE_FIND_FIELD = "//input [@name='Поле поиска']";
    private static final String MANUFACTURE_CHECK = "//input [@name = 'Производитель Кошачье Счастье']/parent::label";
    private static final String DELIVERY_METHOD = "//span [text()='Доставка курьером']";

    //локаторы на товар
    private static final String RESULT_FIRST = "//div [@data-zone-data='{\"viewType\":\"grid\"}']/article[1]/div/h3/a";
    private static final String ADD_TO_COMPARE = "//div [@data-node-name='comparison']";



    //предусловие для каждого тестового класса
    @BeforeEach
    public void SetUp() {
        //указываем путь к драйверу браузера для управления браузером при тестировании
        System.setProperty("webdriver.chrome.driver","src\\test\\resources\\chromedriver.exe");
        //экземпляр драйвера
        webDriver = new ChromeDriver();
        //запуск браузера (драйвера)
        webDriver.get(baseURL);
        webDriver.manage().window().maximize();
        webDriver.manage().timeouts().implicitlyWait(15000, TimeUnit.MILLISECONDS);

    }

    @Test
    @DisplayName("Поиск корма с фильтрами")
    public void CatFeedTest() {
        //проверка загрузки стартовой страницы по лого
        Assertions.assertTrue(webDriver.findElement(By.xpath(YANDEX_LOGO)).isDisplayed());
        //переход в маркет (в новую вкладку)
        WebElement webElement = webDriver.findElement(By.xpath(MARKET));
        webElement.click();
        //переходим на крайнюю правую вкладку, т.к. она в ней открылся маркет
        for (String windowHandle : webDriver.getWindowHandles()) {
            webDriver.switchTo().window(windowHandle);
        }

        //поиск по ключевым словам
        webElement = webDriver.findElement(By.xpath(FIND_FIELD));
        webElement.sendKeys("Корм для кошек");
        webElement = webDriver.findElement(By.xpath(FIND_BUTTON));
        webElement.click();
        //настройка фильтра
        //цена
       // ((JavascriptExecutor) webDriver).executeScript("arguments[0].scrollIntoView(true);",
        //        webDriver.findElement(By.xpath(PRICE_FROM_LOC)));
        //webDriver.findElement(By.xpath(PRICE_FROM_LOC)).sendKeys("100");
        //webDriver.findElement(By.xpath(PRICE_TO_LOC)).sendKeys("1500");
        webDriver.findElement(By.id(PRICE_FROM_ID)).sendKeys("100");
        webDriver.findElement(By.id(PRICE_TO_ID)).sendKeys("1500");
        //производитель
        webDriver.findElement(By.xpath(SHOW_ALL_LABEL_MAN)).click();
        //WebElement manuFind = WebDriverWait(webDriver, Duration.ofSeconds(10)).
        //        until(ExpectedConditions.elementToBeClickable(By.xpath(MANUFACTURE_FIND_FIELD)));

        webDriver.findElement(By.xpath(MANUFACTURE_FIND_FIELD)).sendKeys("Кошачье счастье");
        webDriver.findElement(By.xpath(MANUFACTURE_CHECK)).click();
        //доставка
        webDriver.findElement(By.xpath(DELIVERY_METHOD)).click();

        //по результатам поиска выбираем первый товар и нажимаем сравнить
        //поднимаемся на верх страницы и выбираем первый товар
        ((JavascriptExecutor) webDriver)
                .executeScript("window.scrollTo(0, -document.body.scrollHeight)");
         ((JavascriptExecutor) webDriver).executeScript("arguments[0].scrollIntoView(true);",
                webDriver.findElement(By.xpath(RESULT_FIRST)));
        webDriver.findElement(By.xpath(RESULT_FIRST)).click();
        //переходим на крайнюю правую вкладку, т.к. она в ней открылся товар
        for (String windowHandle : webDriver.getWindowHandles()) {
            webDriver.switchTo().window(windowHandle);
        }
        webDriver.findElement(By.xpath(ADD_TO_COMPARE)).click();

    }

    //пост-действия по окончании каждого теста
    @AfterEach
    public void CloseBrowser(){
        //закрываем браузер (останавливаем драйвер)
        webDriver.quit();
    }
}
