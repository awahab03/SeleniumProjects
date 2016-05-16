Option Explicit
on error resume next
err.clear
Rem ----------Varaible declaration--------------------------------------------------

Rem ___Text File Variables______________________

Dim obj_FSO
Dim my_objFile
DIm text_FilePath
Dim pathRoot

Rem ___Excel File Variables______________________

Dim obj_Excel
Dim Excel_FileName
Dim Excel_sheetName
Dim Excel_Row
Dim Excel_Col
Dim sExcelData

Dim projName

Dim bFlag,sCol1_Page,sCol2_Element,sCol3_Action,sCol4_Data,sTempPage 

Rem----------Varaiable Initializations--------------------------------------------------
'projName= Inputbox("Enter your project name:", "What is your project name?")

pathRoot = "C:\IAE_testWorkSpace"	' root path variable for your framework.
'C:\testDev\AutFW\MT\OR

'text_FilePath = pathRoot & projName & "\OR\OR.properties"
text_FilePath = pathRoot & "\OR\OR.properties"

'Excel_FileName = pathRoot & projName & "\OR\OR.xls"
Excel_FileName = pathRoot & "\OR\OR.xls"

'msgbox "excel path " & Excel_FileName
Set obj_FSO = CreateObject("Scripting.FileSystemObject")

set my_objFile  = obj_FSO.CreateTextFile (text_FilePath, True)
my_objFile.Close


Const ForReading = 1, ForWriting = 2, ForAppending = 8

Set my_objFile = obj_FSO.opentextfile(text_FilePath,ForAppending, True) 

Excel_sheetName = "OR"
Excel_Row = 2
bFlag = True

Set obj_Excel = CreateObject ("Excel.Application")
obj_Excel.Workbooks.open Excel_FileName 


Do Until obj_Excel.ActiveWorkBook.WorkSheets(Excel_sheetName).Cells(Excel_Row,2) = ""

	sTempPage = Trim(obj_Excel.ActiveWorkBook.WorkSheets(Excel_sheetName).Cells(Excel_Row,1))
	
	IF Excel_Row = 2 then
		sCol1_Page = sTempPage
	Else
		IF sTempPage <> "" Then
			sCol1_Page = sTempPage
		End If 
	End If 

	
	sCol2_Element = Trim(obj_Excel.ActiveWorkBook.WorkSheets(Excel_sheetName).Cells(Excel_Row,2))
	sCol3_Action = Trim(obj_Excel.ActiveWorkBook.WorkSheets(Excel_sheetName).Cells(Excel_Row,3))
	sCol4_Data = Trim(obj_Excel.ActiveWorkBook.WorkSheets(Excel_sheetName).Cells(Excel_Row,4))


	Rem -----------------

		'sCol1_Page = ReplaceSpaceWithUnderscore ( sCol1_Page )
		'sCol2_Element = ReplaceSpaceWithUnderscore ( sCol2_Element )
		'sCol3_Action = ReplaceSpaceWithUnderscore ( sCol3_Action )
		'sCol4_Data = ReplaceSpaceWithUnderscore ( sCol4_Data )

	Rem ----------------

		
	IF Excel_Row = 2 and  sCol1_Page = ""  Then
		
		bFlag = False
		
	End If
	
	IF sCol4_Data ="" Then
		bFlag = False
	End If 

	IF bFlag = True Then


		sExcelData = sCol1_Page & "<>"& sCol2_Element & "=" & sCol3_Action &"<>"&sCol4_Data 
	
		my_objFile.WriteLine sExcelData 
	
	
	End If  

	
	
	
	Excel_Row = Excel_Row + 1

Loop
if err.number <> 0 then
	msgbox "Error occured: " & err.number & vbTab & err.description
else
	msgbox "OR parse complete!"
end if
'obj_Excel.ActiveWorkBook.Save
obj_Excel.ActiveWorkBook.Close
obj_Excel.WorkBooks.Close
obj_Excel.Application.Quit
Set Obj_Excel = Nothing

my_objFile.Close


Function ReplaceSpaceWithUnderscore (sTemp)

	sTemp = Replace(sTemp," ", "_")

	ReplaceSpaceWithUnderscore = sTemp 
End Function 