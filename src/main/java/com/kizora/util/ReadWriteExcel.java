package com.kizora.util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Date;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;

public class ReadWriteExcel {
	public static XSSFSheet ExcelWSheet;
	public static XSSFWorkbook ExcelWBook;
	private static Cell Cell;
	private static Row Row;

	public static String sheetName;

	public static void setExcelFile(final String Filename, final int SheetNumber) throws Exception {
		System.out.println("Excel Data");
		System.out.println(System.getProperty("user.dir"));

		try {
			// String path1="\\src\\main\\testdata\\RCSTestdata.xlsx";
			final FileInputStream ExcelFile = new FileInputStream(
					String.valueOf(System.getProperty("user.dir")) + "/src/main/java/testdata/" + Filename);
			ReadWriteExcel.ExcelWBook = new XSSFWorkbook();
			ReadWriteExcel.ExcelWSheet = ReadWriteExcel.ExcelWBook.getSheetAt(SheetNumber);
			sheetName = ReadWriteExcel.ExcelWSheet.getSheetName();
//            ReadWriteExcel.ExcelWSheet=ReadWriteExcel.ExcelWBook.getSheet("");

		} catch (Exception e) {
			throw e;
		}
	}

	public static String getCellData(final int RowNum, final int ColNum) throws Exception {
		final DataFormatter formatter = new DataFormatter();
		try {
			ReadWriteExcel.Cell = (Cell) ReadWriteExcel.ExcelWSheet.getRow(RowNum).getCell(ColNum);
			final String CellData = formatter.formatCellValue(ReadWriteExcel.Cell);
			return CellData;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	public static void setCellData(final String Result, final int RowNum, final int ColNum, final String Filename)
			throws Exception {
		try {
			ReadWriteExcel.Row = (Row) ReadWriteExcel.ExcelWSheet.getRow(RowNum);
			ReadWriteExcel.Cell = ReadWriteExcel.Row.getCell(ColNum);
			if (ReadWriteExcel.Cell == null) {
				(ReadWriteExcel.Cell = ReadWriteExcel.Row.createCell(ColNum)).setCellValue(Result);
			} else {
				ReadWriteExcel.Cell.setCellValue(Result);
			}
			final FileOutputStream fileOut = new FileOutputStream(
					String.valueOf(System.getProperty("user.dir")) + "/src/main/java/testdata/" + Filename);
			ReadWriteExcel.ExcelWBook.write((OutputStream) fileOut);
			fileOut.flush();
			fileOut.close();
		} catch (Exception e) {
			throw e;
		}
	}

	public static String getvalue(final int i, final int j) {
		final DataFormatter formatter = new DataFormatter();
		ReadWriteExcel.Cell = ReadWriteExcel.ExcelWSheet.getRow(i).getCell(j);
		final String CellData = ReadWriteExcel.Cell.getStringCellValue();// formatter.formatCellValue(ReadWriteExcel.Cell,

		return CellData;
	}

	public static String getDate(int i, int j) {
		ReadWriteExcel.Cell = ReadWriteExcel.ExcelWSheet.getRow(i).getCell(j);
		final Date Celldate = ReadWriteExcel.Cell.getDateCellValue();
		return Celldate.toString();
	}

	// Get the row count and Row count

	public static int getRowCount(String sheetName) {
		int rowCnt = ReadWriteExcel.ExcelWBook.getSheet(sheetName).getLastRowNum();
		return rowCnt + 1;

	}

	// Get the column count and Row count

	public static int getColumnCount(String sheetName) {
		int columnCnt = ReadWriteExcel.ExcelWBook.getSheet(sheetName).getRow(0).getLastCellNum();
		return columnCnt + 1;

	}

	public boolean searchDataInExcel(String fileName, String SearchInSheetName, String verifyData) throws Exception {
		FileOutputStream fileOut = new FileOutputStream(
				String.valueOf(System.getProperty("user.dir")) + "/src/main/java/testdata/" + fileName);
		int rowCnt = getRowCount(SearchInSheetName);
		int colCnt = getColumnCount(SearchInSheetName);
		int i, j;
		for (i = 0; i < rowCnt; i++) {
			for (j = 0; j < colCnt; j++) {
				String cellData = getCellData(i, j);
				if (cellData.equals(verifyData) == true) {
					System.out.println("Seached Value Exist!!");
					return true;
				}
			}

		}

		return false;

	}

	// Get the Data from sheet
	public static Object[][] getData(String fileName, String sheetName) throws Exception {
//        FileOutputStream fileOut = new FileOutputStream(String.valueOf(System.getProperty("user.dir"))+"/src/main/java/com/kizora/testdata/" + fileName);
		int rowCnt = getRowCount(sheetName);

		int colCnt = getColumnCount(sheetName);
		System.out.println("TotalNo. of rows: " + rowCnt + "   " + "Total no.of Column:   " + colCnt);
		Object[][] data = new Object[rowCnt][colCnt];
		for (int row = 1; row <= rowCnt - 1; row++) {
//            System.out.println("RowNo: "+row);
			for (int col = 0; col < colCnt; col++) {
				data[row][col] = getCellData(row, col);
				System.out.println(data[row][col].toString());
			}
		}
		return data;

	}

}
