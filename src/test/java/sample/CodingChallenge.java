package sample;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.asserts.SoftAssert;

public class CodingChallenge {

	public static void main(String[] args) throws InterruptedException {
		WebDriver driver = new ChromeDriver();
		driver.get("https://petdiseasealerts.org/forecast-map/#/");
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(20));
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
		
		Actions actions = new Actions(driver);
		SoftAssert softAssert = new SoftAssert();
		
		String[] states = { "california", "new-york", "delaware", "maryland" };
		//instead of Floorida taken Delaware 

		for (int a = 0; a < states.length; a++) {
			
			WebElement mapFrame = driver.findElement(By.xpath("//*[contains(@id,'map-instance-')]"));
			wait.until(ExpectedConditions.visibilityOf(mapFrame));
			driver.switchTo().frame(mapFrame);
			
			try {
				WebElement statesPoint = driver.findElement(By.id(states[a]));
				actions.moveToElement(statesPoint).build().perform();
				statesPoint.click();
				Thread.sleep(2000);   //intentionally added to check on screen
			} 
			catch (org.openqa.selenium.StaleElementReferenceException ex) {
				WebElement statesPoint = driver.findElement(By.id(states[a]));
				actions.moveToElement(statesPoint).build().perform();
				statesPoint.click();
				Thread.sleep(2000);   //intentionally added to check on screen
			}
			
			WebElement getStateFromScreen = driver.findElement(By.xpath("//*[@id='map-component']//li[2]/span"));
			String actualStateName = wait.until(ExpectedConditions.visibilityOf(getStateFromScreen)).getText();
			softAssert.assertEquals(states[a].toLowerCase().replace("-", " "), actualStateName.toLowerCase());
			
			WebElement backToMap = driver.findElement(By.xpath("//*[@id='map-component']//li[1]/a"));
			wait.until(ExpectedConditions.elementToBeClickable(backToMap));
			actions.moveToElement(backToMap).click();
			backToMap.click();
			
			Thread.sleep(2000);
			
			driver.switchTo().defaultContent();
		}
		softAssert.assertAll();
		driver.close();
	}

}
