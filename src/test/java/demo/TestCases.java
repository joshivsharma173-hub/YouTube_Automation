package demo;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import static demo.wrappers.Wrappers.*;

import java.time.Duration;
import java.util.List;
import java.util.logging.Level;

import demo.utils.ExcelDataProvider;
// import io.github.bonigarcia.wdm.WebDriverManager;
import demo.wrappers.Wrappers;

public class TestCases extends ExcelDataProvider { // Lets us read the data
        ChromeDriver driver;

        /*
         * TODO: Write your tests here with testng @Test annotation.
         * Follow `testCase01` `testCase02`... format or what is provided in
         * instructions
         */

        /*
         * Do not change the provided methods unless necessary, they will help in
         * automation and assessment
         */
        @BeforeTest
        public void startBrowser() {
                System.setProperty("java.util.logging.config.file", "logging.properties");

                // NOT NEEDED FOR SELENIUM MANAGER
                // WebDriverManager.chromedriver().timeout(30).setup();

                ChromeOptions options = new ChromeOptions();
                LoggingPreferences logs = new LoggingPreferences();

                logs.enable(LogType.BROWSER, Level.ALL);
                logs.enable(LogType.DRIVER, Level.ALL);
                options.setCapability("goog:loggingPrefs", logs);
                options.addArguments("--remote-allow-origins=*");

                System.setProperty(ChromeDriverService.CHROME_DRIVER_LOG_PROPERTY, "build/chromedriver.log");

                driver = new ChromeDriver(options);

                driver.manage().window().maximize();
        }

        @AfterTest
        public void endTest() {
                driver.close();
                driver.quit();

        }

        @BeforeMethod
        public void navigateToYouTube() {
                Wrappers wrappers = new Wrappers(driver);
                wrappers.navigateToUrl("https://www.youtube.com/");

        }

        @Test
        public void testCase01() throws InterruptedException {
                System.out.println("=============TestCase01 Starts=============");
                Wrappers wrapper = new Wrappers(driver);
                Boolean status = driver.getCurrentUrl().contains("youtube");
                Assert.assertTrue(status, "URL does not contains youtube ");
                WebElement sideButton = driver.findElement(By.xpath("//button[@aria-label='Guide']"));
                WebElement about = driver.findElement(By.xpath("//a[text()='About']"));
                pause(3000);
                if (!about.isDisplayed()) {
                        wrapper.click(sideButton);

                }
                wrapper.click(about);

                WebElement aboutText = driver.findElement(By.tagName("h1"));
                wrapper.printText(aboutText);
                // //a[text()='About']
                List<WebElement> list = driver.findElements(By.tagName("p"));
                wrapper.printAbout(list);
                System.out.println("=============TestCase01 Ends===============");

        }

        @Test
        public void testCase02() throws InterruptedException {
                System.out.println("=============TestCase02 Starts=============");
                Wrappers wrapper = new Wrappers(driver);
                WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

                WebElement sideButton = driver.findElement(By.xpath("//button[@aria-label='Guide']"));
                List<WebElement> moviesList = driver.findElements(By.xpath("//a[@title='Movies']"));
                pause(3000);
                if (moviesList.isEmpty()) {
                        wrapper.click(sideButton);
                }
                WebElement movies = wait
                                .until(ExpectedConditions.elementToBeClickable(By.xpath("//a[@title='Movies']")));
                wrapper.click(movies);

                wait.until(ExpectedConditions.urlContains("storefront"));

                // //button[@aria-label='Next']
                WebElement next = wait.until(
                                ExpectedConditions.elementToBeClickable(By.xpath("//span[contains(.,'Top selling')]//ancestor::ytd-shelf-renderer//ytd-button-renderer//button[@aria-label='Next']")));

                while (next.isDisplayed()) {
                        wrapper.click(next);

                }

                List<WebElement> list = driver.findElements(By.xpath("//div[@id='items']//ytd-grid-movie-renderer"));

                List<String> movieList = wrapper.getMovieTypeAndAge(list);
                String actualMovieType = movieList.get(0);
                String actualAge = movieList.get(1);

                SoftAssert softAssert = new SoftAssert();
                softAssert.assertTrue(actualMovieType.equals("Comedy") || actualMovieType.equals("Drama")
                                || actualMovieType.equals("Animation"), "Movie Type is not matching");

                softAssert.assertTrue(actualAge.equals("A") || actualAge.equals("U/A")
                                || actualAge.equals("U"), "Movie Type is not matching");

                softAssert.assertAll();
                // //div[@id='items']//ytd-grid-movie-renderer//span[@id='video-title'] ====>
                // Video title
                // //div[@id='items']//ytd-grid-movie-renderer//span[contains(@class,'renderer-metadata')]
                // ===> Type of movie
                // //div[@id='items']//ytd-grid-movie-renderer//*[contains(@class,'badges
                // style')]//div[2]//div ====> U or U/A

                System.out.println("=============TestCase02 Ends===============");

        }

        @Test
        public void testCase03() throws InterruptedException {
                System.out.println("=============TestCase03 Starts=============");
                Wrappers wrapper = new Wrappers(driver);
                WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
                WebElement sideButton = driver.findElement(By.xpath("//button[@aria-label='Guide']"));
                pause(3000);
                List<WebElement> musicList = driver.findElements(By.xpath("//a[@title='Music']"));
                if (musicList.isEmpty()) {
                        wrapper.click(sideButton);
                }
                WebElement music = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[@title='Music']")));
                wrapper.click(music);

                wait.until(ExpectedConditions.urlContains("channel"));

                WebElement parentEle = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(
                                "(//ytd-rich-section-renderer[1]//ytd-rich-item-renderer[not(@hidden)][last()]")));
                WebElement childSongNum = parentEle.findElement(By.xpath(".//div[@class='yt-badge-shape__text']"));

                WebElement childSongTitle = parentEle.findElement(By.xpath(".//h3//span"));
                String numText = childSongNum.getText().trim().split(" ")[0];
                String titleText = childSongTitle.getText();
                int num = Integer.parseInt(numText);
                System.out.println(num + "====>" + titleText);

                SoftAssert softAssert = new SoftAssert();
                softAssert.assertTrue(num <= 60, "Songs number are not less than or equals 50");
                softAssert.assertAll();

                // (//ytd-rich-shelf-renderer)[1]//ytd-rich-item-renderer[not(@hidden)][last()]
                // =====> last right most non-hidden
                // (//ytd-rich-shelf-renderer)[1]//ytd-rich-item-renderer[not(@hidden)][last()]//div[@class='yt-badge-shape__text']
                // =====> Title
                // (//ytd-rich-shelf-renderer)[1]//ytd-rich-item-renderer[not(@hidden)][last()]//h3//span
                System.out.println("=============TestCase03 Ends===============");
        }

        @Test
        public void testCase04() throws InterruptedException {
                System.out.println("=============TestCase04 Starts=============");
                Wrappers wrapper = new Wrappers(driver);
                WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
                WebElement sideButton = driver.findElement(By.xpath("//button[@aria-label='Guide']"));

                // //a[@title='Movies']
                List<WebElement> newsList = driver.findElements(By.xpath("//a[@title='News']"));
                By showMoreBy = By.xpath("//yt-formatted-string[text()='Show more']");
                List<WebElement> showMoreList = driver.findElements(showMoreBy);
                System.out.println(newsList.size());
                System.out.println(newsList);
                pause(3000);
                if (newsList.isEmpty() && showMoreList.isEmpty()) {
                        wrapper.click(sideButton);

                }   

                if (!showMoreList.isEmpty()) {
                        WebElement showMore = wait.until(ExpectedConditions.elementToBeClickable(showMoreBy));
                        showMore.click();
                }
                System.out.println(showMoreList);
                // if (driver.findElement(By.xpath("//yt-formatted-string[text()='Show
                // more']")).isDisplayed()) {
                // driver.findElement(By.xpath("//yt-formatted-string[text()='Show
                // more']")).click();

                // }

                WebElement news = wait.until(ExpectedConditions.elementToBeClickable(
                                By.xpath("//a[@title='News']")));
                wrapper.click(news);

                wait.until(ExpectedConditions.urlContains("channel"));

                List<WebElement> list = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath(
                                "//div[@id='rich-shelf-header-container' and contains(.,'Latest news posts')]//ancestor::div[1]//div[@id='contents']//ytd-rich-item-renderer[not(@hidden)]")));

                pause(3000);

                double sum = wrapper.getNewsHeadingAndSum(list);
                System.out.println("Total sum of Likes Count is " + "====>" + sum);

                System.out.println(list.size());

                // //div[@id='rich-shelf-header-container' and contains(.,'Latest news
                // posts')]//ancestor::div[1]//div[@id='contents']//ytd-rich-item-renderer[not(@hidden)]
                // ====> List of News
                // .//div[@id='author'] ======> author
                // .//div[@id='post-text']//yt-formatted-string[@id='home-content-text']//span[1]
                // ======> body
                // .//span[@id='vote-count-middle'] =====> like count

                System.out.println("=============TestCase04 Ends===============");

        }
}
