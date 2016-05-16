package global;

//import jxl.read.biff.File;
//import java.io.*;
//import java.io.File.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


public abstract class FileChecker {
	
	public static Boolean fileCheck(String FtoChk) {
		Path p = Paths.get(FtoChk);
		
		if(Files.exists(p)){
			return true;
		} else{
			return false;
		}
		
	}

}
