package demo.wrappers;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.asserts.SoftAssert;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class Wrappers {
    /*
     * Write your selenium wrappers here
     */
    ChromeDriver driver;
    WebDriverWait wait;

    public Wrappers(ChromeDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public boolean navigateToUrl(String text) {
        driver.get(text);
        Boolean status = wait.until(ExpectedConditions.urlContains("youtube"));
        return status;

    }

    public void click(WebElement ele) {
        JavascriptExecutor js = driver;
        js.executeScript("arguments[0].click()", ele);
    }

    public static void pause(long n) throws InterruptedException {
        Thread.sleep(n);
    }
    public void printText(WebElement ele){
        String text = ele.getText();
                System.out.println(text);
    }

    public void printAbout(List<WebElement> list){
        for (WebElement ele : list) {
                        String text = ele.getText();
                        System.out.println(text);
                }

    }

    public List<String> getMovieTypeAndAge(List<WebElement> list)
            throws InterruptedException {
        List<String> movieList = new ArrayList<>();
        WebElement lastMovie = list.get(list.size() - 1);
        pause(3000);

        WebElement movieType = lastMovie.findElement(By.xpath(".//span[contains(@class,'renderer-metadata')]"));
        WebElement ageType = lastMovie.findElement(By.xpath(".//ytd-badge-supported-renderer[contains(@class,'badges style')]//div[2]//div")); // ===> child 2

        String typeText1 = movieType.getText().trim();
        String typeText = typeText1.split(" ")[0];
        String ageText = ageType.getText().trim();

        System.out.println(list.size());
        System.out.println(typeText + "====>" + ageText);

        movieList.add(typeText);
        movieList.add(ageText);

        return movieList;
    }

    public double getNewsHeadingAndSum(List<WebElement> list){
        double num =0;
        for(WebElement parentEle : list){
                        String author = parentEle.findElement(By.xpath(".//div[@id='author']")).getText().trim();
                        List<WebElement> elems = parentEle.findElements(By.xpath(".//div[@id='post-text']//yt-formatted-string[@id='home-content-text']//span[1]"));
                        String body = elems.isEmpty() ? "" : elems.get(0).getText().trim();
                        //String body = parentEle.findElement(By.xpath(".//div[@id='post-text']//yt-formatted-string[@id='home-content-text']//span[1]")).getText().trim();
                        String likeCount= parentEle.findElement(By.xpath(".//span[@id='vote-count-middle']")).getText().trim();
                        System.out.println(author +"====>" + body + "====>" + likeCount);
                        num = num + numericalValue(likeCount);
                        
    }
    return num;

}

    public double numericalValue(String likeCount){

         if(!likeCount.isEmpty()){
                                likeCount = likeCount.toUpperCase();
                                char lastLetter = likeCount.charAt(likeCount.length()-1);
                                int multiplier = 1;
                                switch(lastLetter){
                                        case 'K':
                                                multiplier = 1*1000;
                                                break;
                                        case 'M':
                                                multiplier = 1*1000000;
                                                break;
                                        case 'B':
                                                multiplier = 1*100000000;
                                                break;
                                        default :
                                                if(Character.isDigit(lastLetter)){
                                                        return Double.parseDouble(likeCount);
                                                }
                                }
                                String text = likeCount.substring(0, likeCount.length()-1); // 100K --> 100
                                double num= Double.parseDouble(text);
                                return num = num*multiplier;
                        }
        return 0;


    }
}
