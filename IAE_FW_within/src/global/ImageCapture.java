package global;


import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import org.apache.commons.io.*;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.TakesScreenshot;


public class ImageCapture {
	
	public static String takeFullScreenshot(String imgName){
		String path = Global.curImagePath + imgName;
		try{
			File scrFile = ((TakesScreenshot)Global.drvr).getScreenshotAs(OutputType.FILE);
			FileUtils.copyFile(scrFile, new File((path)));
			//System.out.println("Screensoht is taken and saved at location: " + path);
		} catch(Exception e){
			System.out.println("Failed to take Screenshot. " + e);
		}
		//System.out.println("Before Replace page image Path: " + path);
		path = path.replace("\\\\", "/");
		//System.out.println("After replace, page Image path: " + path);
		path = "<a href=\"javascript:openWin('" + path + "')\"> See Image.</a>";
		
		return path;
	}
	
	public static String takeElementImage(String imgName){
		String iPath = Global.curImagePath + imgName;
		try{
			//Get entire page screenshot
			File screenshot = ((TakesScreenshot)Global.drvr).getScreenshotAs(OutputType.FILE);
			BufferedImage  fullImg = ImageIO.read(screenshot);
			//Get the location of element on the page
			Point point = Global.curElement.getLocation();
			Dimension winArea = Global.drvr.findElement(By.xpath("/html/body")).getSize();
			int winWidth = winArea.getWidth();
			int winHeight = winArea.getHeight();
			
			System.out.println("Page hieght: " + winHeight);
			System.out.println("Page width: " + winWidth);
			//Crop the entire page screenshot to get only element screenshot
			int objXcoord = point.getX();
			int objYcoord = point.getY();
			System.out.println("Object's X coordinate = " + objXcoord);
			System.out.println("Object's Y coordinate = " + objYcoord);
			
			int elemWidth, elemHeight;
			elemWidth = Global.curElement.getSize().getWidth();
			elemHeight = Global.curElement.getSize().getHeight();
			System.out.println("Object Actual Width: " + elemWidth);
			System.out.println("Object Actual Height: " + elemHeight);
			
			if ((winWidth - (objXcoord + elemWidth))>= 200){
				elemWidth = elemWidth + 200;
			}else{
				elemWidth = elemWidth + (winWidth - (objXcoord + elemWidth));
			}
			/**
			if ((winHeight - (objYcoord + elemHeight))> 100){
				elemHeight = elemHeight + 100;
			}else{
				elemHeight = elemHeight + (winHeight - (objYcoord + elemHeight));
			}
			*/
			
			elemHeight = elemHeight + 100;
			
			System.out.println("Enhanced Width: " + elemWidth);
			System.out.println("After Height: " + elemHeight);
			
			if (objXcoord > 100){
				objXcoord = objXcoord - 100;
			} else{
				objXcoord = 0;
			}
			System.out.println("After Enhancement, obj X coord = " + objXcoord);
			if (objYcoord >= 50){
				objYcoord = objYcoord - 50;
			} else {
				objYcoord = 0;
			}
			System.out.println("After Enhancement, obj y coord = " + objYcoord);
			
			BufferedImage eleScreenshot= fullImg.getSubimage(objXcoord, objYcoord, elemWidth, elemHeight);
			ImageIO.write(eleScreenshot, "png", screenshot);
			//Copy the element screenshot to disk
			FileUtils.copyFile(screenshot, new File(iPath));
		} catch(Exception ex){
			System.out.println("Failed to take image: Error:" + ex);
		}
		//System.out.println("Before Replace, element image Path: " + iPath);
		iPath = iPath.replace("\\\\", "/");
		iPath = "<a href=\"javascript:openWin('" + iPath + "')\"> See Image.</a>";
		//System.out.println("After replace, element image path: " + iPath);
		return iPath;
		
	}
	
	public static String getImageName(){
		String iNam;
		
		iNam = Global.curTesName + "_" + Integer.toString(Global.curStep)+ "_" + Global.getStamp("HH:mm:ss:a").replace(":", "_") + ".png";
		
		return iNam;
	}

}
