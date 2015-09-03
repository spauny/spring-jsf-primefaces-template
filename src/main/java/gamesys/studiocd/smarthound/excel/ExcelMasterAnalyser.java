package gamesys.studiocd.smarthound.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

/**
 *
 * @author iulian.dafinoiu
 */
@Component
@Slf4j
public class ExcelMasterAnalyser {

    private Iterator<Row> getExcelRows(InputStream excelInputStream, int sheetNumber) {
        try {
            XSSFWorkbook workbook = new XSSFWorkbook(excelInputStream);
            XSSFSheet gamesSheet = workbook.getSheetAt(sheetNumber);
            return gamesSheet.iterator();
        } catch (IOException ex) {
            log.error("getExcelRows: ", ex);
        }
        return null;
    }

    public <T> List<T> readExcelFile(File excelFile, int sheetNumber, Class<T> excelStructureClass) {
        List<T> listOfExcelStructureObjects = new ArrayList<>();
        try {
            //        ClassLoader classLoader = getClass().getClassLoader();
//        classLoader.getResourceAsStream(filePathAndName)
            Iterator<Row> gameIterator = getExcelRows(new FileInputStream(excelFile), sheetNumber);
            
            
            
            List<String> headers = new ArrayList<>();
            int rowCounter = 0;
            while (gameIterator.hasNext()) {
                try {
                    Row row = gameIterator.next();
                    Iterator<Cell> cellIterator = row.cellIterator();
                    int columnCounter = 0;
                    T excelStructureObject = excelStructureClass.newInstance();
                    while (cellIterator.hasNext()) {
                        Cell cell = cellIterator.next();
                        if (Cell.CELL_TYPE_STRING == cell.getCellType() && StringUtils.isNotBlank(cell.getStringCellValue())) {
                            String cellValue = cell.getStringCellValue();
                            if (rowCounter == 0) {
                                headers.add(cellValue);
                            } else {
                                callSetMethodForColumn(headers.get(columnCounter), cellValue, excelStructureObject);
                            }
                        } else if (Cell.CELL_TYPE_NUMERIC == cell.getCellType()) {
                            double cellValue = cell.getNumericCellValue();
                            if (rowCounter > 0) {
                                callSetMethodForColumn(headers.get(columnCounter), cellValue, excelStructureObject);
                            }
                        }
                        columnCounter++;
                    }
                    if (rowCounter > 0) {
                        listOfExcelStructureObjects.add(excelStructureObject);
                    }
                    rowCounter++;
                } catch (InstantiationException | IllegalAccessException ex) {
                    log.error("readExcelFile: ", ex);
                }
            }
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ExcelMasterAnalyser.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listOfExcelStructureObjects;
    }

    private <T> void callSetMethodForColumn(String columnHeader, Object value, T excelStructureObject) {
        try {
            Method[] methods = excelStructureObject.getClass().getDeclaredMethods();
            String simpleHeaderName;
            if (columnHeader.indexOf('_') != -1) {
                simpleHeaderName = columnHeader.replaceAll("_", "");
            } else if (columnHeader.indexOf('-') != -1) {
                simpleHeaderName = columnHeader.replaceAll("-", "");
            } else {
                simpleHeaderName = columnHeader.replaceAll(" ", "");
            }
            for (Method method : methods) {
                if (method.getName().equalsIgnoreCase("set" + simpleHeaderName)) {
                    Class<?>[] paramTypes = method.getParameterTypes();
                    if (value instanceof String) {
                        if (paramTypes[0].isInstance(value)) {
                            method.invoke(excelStructureObject, (String) value);
                        } else {
                            method.invoke(excelStructureObject, 0D);
                        }
                    } else {
                        if (paramTypes[0].isInstance(value)) {
                            method.invoke(excelStructureObject, (Double) value);
                        } else {
                            method.invoke(excelStructureObject, ((Double) value).toString());
                        }
                    }
                }
            }
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            log.error("header: " + columnHeader + " value: " + value, ex);
        }
    }

}
