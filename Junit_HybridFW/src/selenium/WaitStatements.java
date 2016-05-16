package selenium;

import global.Global;

import java.util.concurrent.TimeUnit;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class WaitStatements {
	
	public static void Wait(int tm) throws InterruptedException {
		
		Global.drvr.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		// Add result string code here . . . //
		
	}
	
	public static void WaitProperty(String propText, int tOut) throws InterruptedException {
		WebDriverWait wt = new WebDriverWait(Global.drvr, tOut);
		
		String prArr[] = propText.split("=");
		
		String pName =  prArr[0].trim();
		
		String pVal = prArr[1].trim();
		
		switch(pName){
			case "id":
				//
				wt.until(ExpectedConditions.visibilityOfElementLocated(By.id(pVal)));				
				break;
			case "name":
				//
				wt.until(ExpectedConditions.visibilityOfElementLocated(By.name(pVal)));
				break;
			case "xpath":
				//
				wt.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(pVal)));
				break;
			default:
				//
				
		}		
		
	}
	
}
