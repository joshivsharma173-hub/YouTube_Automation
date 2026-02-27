package demo.utils;


import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ExcelReaderUtil {

   public static Object[][] readExcelData(String fileName, String sheetName) {

    try {
        System.out.println("=== START Excel Reading ===");

        InputStream file = Thread.currentThread()
                .getContextClassLoader()
                .getResourceAsStream(fileName);

        if (file == null) {
            throw new RuntimeException(" Excel file NOT found in classpath: " + fileName);
        }

        Workbook workbook = new XSSFWorkbook(file);

        Sheet sheet = workbook.getSheet(sheetName);

        if (sheet == null) {
            throw new RuntimeException(" Sheet NOT found: " + sheetName);
        }

        int totalRows = sheet.getPhysicalNumberOfRows();
        int totalCols = sheet.getRow(0).getPhysicalNumberOfCells();

        System.out.println("Rows Found: " + totalRows);
        System.out.println("Cols Found: " + totalCols);

        if (totalRows <= 1) {
            throw new RuntimeException(" No data rows present in Excel!");
        }

        Object[][] data = new Object[totalRows - 1][totalCols];

        DataFormatter formatter = new DataFormatter();

        for (int i = 1; i < totalRows; i++) {
            Row row = sheet.getRow(i);

            for (int j = 0; j < totalCols; j++) {
                Cell cell = row.getCell(j);
                data[i - 1][j] = formatter.formatCellValue(cell);

                System.out.println("Read Data: " + data[i - 1][j]);
            }
        }

        workbook.close();

        System.out.println("=== Excel Reading Completed ===");

        return data;

    } catch (Exception e) {
        e.printStackTrace();
        throw new RuntimeException("Excel Read Failed");
    }
}
    // Find the last non-blank row in a sheet
    public static int findLastNonBlankRow(Sheet sheet) {
        int lastNonBlankRowNum = -1;
        if (sheet != null) {
            for (int i = sheet.getFirstRowNum(); i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row != null && !isRowBlank(row)) {
                    lastNonBlankRowNum = i;
                }
            }
        }
        return lastNonBlankRowNum;
    }

    // Find the last non-blank column in a given row
    public static int findLastNonBlankColumn(Row row) {
        int lastNonBlankColumnNum = -1;
        if (row != null) {
            for (int j = row.getFirstCellNum(); j < row.getLastCellNum(); j++) {
                Cell cell = row.getCell(j, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                if (cell != null && cell.getCellType() != CellType.BLANK) {
                    lastNonBlankColumnNum = j;
                }
            }
        }
        return lastNonBlankColumnNum;
    }

    // Helper method to determine if a row is blank
    private static boolean isRowBlank(Row row) {
        for (int cellNum = row.getFirstCellNum(); cellNum < row.getLastCellNum(); cellNum++) {
            Cell cell = row.getCell(cellNum, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
            if (cell != null && cell.getCellType() != CellType.BLANK) {
                return false;
            }
        }
        return true;
    }
    
    private static Object getCellValue(Cell cell) {
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                return cell.getNumericCellValue();
            case BOOLEAN:
                return cell.getBooleanCellValue();
            default:
                return null;
        }
    }
}
