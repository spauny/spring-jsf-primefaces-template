package gamesys.studiocd.smarthound;

import gamesys.studiocd.smarthound.app.GlobalManager;
import gamesys.studiocd.smarthound.vo.BrowserInfoVO;
import gamesys.studiocd.smarthound.vo.ExcelFileVO;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 *
 * @author iuli
 */
@Component
@Scope("session")
public class IosBrowserExcelAnalyzer implements Serializable {

    private static final long serialVersionUID = 1879456179305456123L;

    @Autowired
    private GlobalManager globalManager;

    @Getter
    @Setter
    private List<BrowserInfoVO> filteredAndroidBrowserList = new ArrayList<>();

    private Iterator<Row> getExcelRows(ExcelFileVO excelFIleVO, int sheetNumber) {
        try {
            String excelFileName = excelFIleVO.getExcelFileName();
            InputStream excelInputStream = excelFIleVO.getExcelInputStream();

            if (excelInputStream != null && excelFileName.contains(".xlsx")) {
                XSSFWorkbook workbook = new XSSFWorkbook(excelInputStream);
                XSSFSheet sheet = workbook.getSheetAt(sheetNumber);
                return sheet.iterator();
            } else if (excelInputStream != null && excelFileName.contains(".xls")) {
                HSSFWorkbook hSSFWorkbook = new HSSFWorkbook(excelInputStream);
                HSSFSheet hSSFSheet = hSSFWorkbook.getSheetAt(sheetNumber);
                return hSSFSheet.iterator();
            }
        } catch (IOException ex) {
            Logger.getLogger(IosBrowserExcelAnalyzer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public void analyzeExcel(ExcelFileVO excelFIleVO, Iterator<Row> rowIterator) {
        this.globalManager.getIosBrowsersInfo().clear();
//        this.globalManager.getIosBrowsersInfo().clear();

//        Iterator<Row> rowIterator = getExcelRows(excelFIleVO, sheetNumber);

        String excelFileName = excelFIleVO.getExcelFileName();

        List<String> headers = new ArrayList<>();
        int rowCounter = 0;
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            Iterator<Cell> cellIterator = row.cellIterator();
            int columnCounter = 0;
            BrowserInfoVO browserInfoVO;
            if (this.globalManager.getIosBrowsersInfo().size() > rowCounter) {
                browserInfoVO = this.globalManager.getIosBrowsersInfo().get(rowCounter);
            } else {
                browserInfoVO = new BrowserInfoVO();
                this.globalManager.getIosBrowsersInfo().add(browserInfoVO);
            }
            while (cellIterator.hasNext()) {
                Cell cell = cellIterator.next();
                if (Cell.CELL_TYPE_STRING == cell.getCellType() && StringUtils.isNotBlank(cell.getStringCellValue())) {
                    String cellValue = cell.getStringCellValue();
                    if (rowCounter == 0) {
                        headers.add(cellValue);
                    } else {
                        callSetMethodForColumn(headers.get(columnCounter), cellValue, browserInfoVO);
                    }
                } else if (Cell.CELL_TYPE_NUMERIC == cell.getCellType()) {
                    double cellValue = cell.getNumericCellValue();
                    if (rowCounter > 0) {
                        callSetMethodForColumn(headers.get(columnCounter), cellValue, browserInfoVO);
                    }
                }
                columnCounter++;
            }
            rowCounter++;
        }
        this.filteredAndroidBrowserList = new ArrayList<>(this.globalManager.getIosBrowsersInfo());
    }

    private void callSetMethodForColumn(String columnHeader, Object value, BrowserInfoVO browserInfoVO) {
        try {
            Method[] methods = browserInfoVO.getClass().getDeclaredMethods();
            String simpleHeaderName = columnHeader.replaceAll("_", "");
            for (Method method : methods) {
                if (method.getName().equalsIgnoreCase("set" + simpleHeaderName)) {
                    Class<?>[] paramTypes = method.getParameterTypes();
                    if (value instanceof String) {
                        if (paramTypes[0].isInstance(value)) {
                            method.invoke(browserInfoVO, (String) value);
                        } else {
                            method.invoke(browserInfoVO, 0D);
                        }
                    } else {
                        if (paramTypes[0].isInstance(value)) {
                            method.invoke(browserInfoVO, (Double) value);
                        } else {
                            method.invoke(browserInfoVO, ((Double) value).toString());
                        }
                    }
                }
            }
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            Logger.getLogger(IosBrowserExcelAnalyzer.class.getName()).log(Level.SEVERE, "header: " + columnHeader + " value: " + value, ex);
        }
    }

}
