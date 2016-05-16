package excel;


import java.io.*;

import global.Global;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WritableCell;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

public class WriteToExcel {
	
	public static void addRunStatusToScriptList(int row, String str) throws IOException, WriteException, RowsExceededException, BiffException {
		File fileToRead = new File(Global.scriptListPath);
		String tempLocation = "Z:\\FQT\\AutFramework\\Config\\tempList.xls";
		Workbook wb = Workbook.getWorkbook(fileToRead);
		
		WritableWorkbook newWB  = Workbook.createWorkbook(new File(tempLocation), wb);
		
		WritableSheet sht = newWB.getSheet("List");
		WritableCell cell = sht.getWritableCell(3, row);
		Label lb = (Label) cell;
		lb.setString(str);
		sht.addCell(lb);
		
		wb.close();
		newWB.write();
		newWB.close();

	}
	
	public void addDateToScriptList(int row, String str) throws IOException, WriteException, RowsExceededException {
		File fileToRead = new File(Global.scriptListPath);
		WritableWorkbook wb;
		
		wb = Workbook.createWorkbook(fileToRead);
		
		WritableSheet sht = wb.getSheet("List");
		
		Label lbl = new Label(4, row, str);
		sht.addCell(lbl);
		wb.write();
		wb.close();
	}
	
}
