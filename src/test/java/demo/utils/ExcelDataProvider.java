package demo.utils;

import org.testng.annotations.DataProvider;

public class ExcelDataProvider {

    @DataProvider(name = "excelData")
    public static Object[][] excelData() {

        return ExcelReaderUtil.readExcelData("DataSetForYoutube.xlsx", "YoutubeTestData");
    }
}