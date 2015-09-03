package gamesys.studiocd.smarthound;

import com.google.common.collect.Lists;
import gamesys.studiocd.smarthound.app.GlobalManager;
import gamesys.studiocd.smarthound.vo.DeviceInfoVO;
import gamesys.studiocd.smarthound.vo.ExcelFileVO;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
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
public class ExcelAnalyzer implements Serializable {

    private static final long serialVersionUID = 1879456123897456123L;

    @Autowired
    private GlobalManager globalManager;
    
    @Autowired
    private AndroidBrowserExcelAnalyzer androidBrowserExcelAnalyzer;
    
    @Autowired
    private IosBrowserExcelAnalyzer iosBrowserExcelAnalyzer;

    @Getter
    @Setter
    private List<DeviceInfoVO> filteredDeviceList = new ArrayList<>();

    @Getter
    @Setter
    private long totalActives;

    @Getter
    @Setter
    private double totalTheoHWPercent;

    @Getter
    @Setter
    private int theoHWPercent = 100;

    @Getter
    @Setter
    private boolean lastMonth;
    
    @Getter
    @Setter
    private boolean showIOS = true;

    @Getter
    private final Map<String, Integer> osVersionCounter = new HashMap<>();

    private List<Iterator<Row>> getExcelRows(ExcelFileVO excelFIleVO) {
        try {
            String excelFileName = excelFIleVO.getExcelFileName();
            InputStream excelInputStream = excelFIleVO.getExcelInputStream();

            if (excelInputStream != null && excelFileName.contains(".xlsx")) {
                XSSFWorkbook workbook = new XSSFWorkbook(excelInputStream);
                XSSFSheet sheet = workbook.getSheetAt(0);
                if (this.lastMonth) {
                    return Lists.newArrayList(sheet.iterator(), null);
                }
                XSSFSheet secSheet = workbook.getSheetAt(1);
                XSSFSheet thirdSheet = workbook.getSheetAt(2);
                return Lists.newArrayList(sheet.iterator(), secSheet.iterator(), thirdSheet.iterator());
            } else if (excelInputStream != null && excelFileName.contains(".xls")) {
                HSSFWorkbook hSSFWorkbook = new HSSFWorkbook(excelInputStream);
//                HSSFSheet hSSFSheet = hSSFWorkbook.getSheetAt(0);
                HSSFSheet sheet = hSSFWorkbook.getSheetAt(0);
                if (this.lastMonth) {
                    return Lists.newArrayList(sheet.iterator(), null);
                }
                HSSFSheet secSheet = hSSFWorkbook.getSheetAt(1);
                HSSFSheet thirdSheet = hSSFWorkbook.getSheetAt(2);
                return Lists.newArrayList(sheet.iterator(), secSheet.iterator(), thirdSheet.iterator());
            }
        } catch (IOException ex) {
            Logger.getLogger(ExcelAnalyzer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public void analyzeExcel(ExcelFileVO excelFIleVO) {
        if (this.lastMonth) {
            this.globalManager.getLastMonthDevices().clear();
        } else {
            this.globalManager.getEntireDeviceList().clear();
            this.globalManager.getCompactDeviceMap().clear();
        }
        this.globalManager.getDistinctDeviceList().clear();
        this.globalManager.getTopEmergingDevices().clear();
        List<Iterator<Row>> its = getExcelRows(excelFIleVO);
        Iterator<Row> rowIterator = its.get(0);
        
        Iterator<Row> androidRowIterator = null;
        Iterator<Row> iosRowIterator = null;
        if (!this.lastMonth) {
            androidRowIterator = its.get(1);
            iosRowIterator = its.get(2);
        }

        String excelFileName = excelFIleVO.getExcelFileName();

        List<String> headers = new ArrayList<>();
        int rowCounter = 0;
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            Iterator<Cell> cellIterator = row.cellIterator();
            int columnCounter = 0;
            DeviceInfoVO deviceInfoVO;
            if (this.lastMonth) {
                deviceInfoVO = new DeviceInfoVO();
            } else {
                if (this.globalManager.getEntireDeviceList().size() > rowCounter) {
                    deviceInfoVO = this.globalManager.getEntireDeviceList().get(rowCounter);
                } else {
                    deviceInfoVO = new DeviceInfoVO();
                    this.globalManager.getEntireDeviceList().add(deviceInfoVO);
                }
            }
            while (cellIterator.hasNext()) {
                Cell cell = cellIterator.next();
                if (Cell.CELL_TYPE_STRING == cell.getCellType() && StringUtils.isNotBlank(cell.getStringCellValue())) {
                    String cellValue = cell.getStringCellValue();
                    if (rowCounter == 0) {
                        headers.add(cellValue);
                    } else {
                        callSetMethodForColumn(headers.get(columnCounter), cellValue, deviceInfoVO);
                    }
                } else if (Cell.CELL_TYPE_NUMERIC == cell.getCellType()) {
                    double cellValue = cell.getNumericCellValue();
                    if (rowCounter > 0) {
                        callSetMethodForColumn(headers.get(columnCounter), cellValue, deviceInfoVO);
                    }
                }
                columnCounter++;
            }
            if (this.lastMonth) {
                this.globalManager.getLastMonthDevices().put(deviceInfoVO.getMarketingName() + deviceInfoVO.getOsName() + deviceInfoVO.getOsVersion(), deviceInfoVO);
            } else if (StringUtils.isNotBlank(deviceInfoVO.getTerritory()) && deviceInfoVO.getTerritory().equalsIgnoreCase("UK")) {
                DeviceInfoVO lastMonthDeviceInfo = this.globalManager.getLastMonthDevices().get(deviceInfoVO.getMarketingName() + deviceInfoVO.getOsName() + deviceInfoVO.getOsVersion());
                if (lastMonthDeviceInfo != null) {
                    deviceInfoVO.setRankStatus(deviceInfoVO.getTheoreticalHW().compareTo(lastMonthDeviceInfo.getTheoreticalHW()));
                    deviceInfoVO.setTheoHWDiffFromLastMonth(deviceInfoVO.getTheoreticalHW() - lastMonthDeviceInfo.getTheoreticalHW());
                    deviceInfoVO.getPreviousMonths().add(lastMonthDeviceInfo);
                    deviceInfoVO.setFirstDevice(deviceInfoVO);
                }
            }
            rowCounter++;
        }
        if (!this.lastMonth) {
            collapseDeviceList();
            this.filteredDeviceList = new ArrayList<>(this.globalManager.getEntireDeviceList());
            this.androidBrowserExcelAnalyzer.analyzeExcel(excelFIleVO, androidRowIterator);
            this.iosBrowserExcelAnalyzer.analyzeExcel(excelFIleVO, iosRowIterator);
        }
        
    }

    private void callSetMethodForColumn(String columnHeader, Object value, DeviceInfoVO deviceInfoVO) {
        try {
            Method[] methods = deviceInfoVO.getClass().getDeclaredMethods();
            String simpleHeaderName = columnHeader.replaceAll(" ", "");
            for (Method method : methods) {
                if (method.getName().equalsIgnoreCase("set" + simpleHeaderName)) {
                    Class<?>[] paramTypes = method.getParameterTypes();
                    if (value instanceof String) {
                        if (paramTypes[0].isInstance(value)) {
                            method.invoke(deviceInfoVO, (String) value);
                        } else {
                            method.invoke(deviceInfoVO, 0D);
                        }
                    } else {
                        if (paramTypes[0].isInstance(value)) {
                            method.invoke(deviceInfoVO, (Double) value);
                        } else {
                            method.invoke(deviceInfoVO, ((Double) value).toString());
                        }
                    }
                }
            }
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            Logger.getLogger(ExcelAnalyzer.class.getName()).log(Level.SEVERE, "header: " + columnHeader + " value: " + value, ex);
        }
    }

    public void collapseDeviceList() {
        this.globalManager.getEntireDeviceList().stream()
                .filter(device -> StringUtils.isNotBlank(device.getTerritory()) && device.getTerritory().equalsIgnoreCase("UK") && !device.getMarketingName().equalsIgnoreCase("UNKNOWN"))
                .forEach(device -> {
                    if (this.globalManager.getCompactDeviceMap().containsKey(device.getMarketingName())) {
                        this.globalManager.getCompactDeviceMap().get(device.getMarketingName()).add(device);
                    } else {
                        this.globalManager.getDistinctDeviceList().add(device);
                        this.globalManager.getCompactDeviceMap().put(device.getMarketingName(), Lists.newArrayList(device));
                    }
                });
    }

}
