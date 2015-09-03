package gamesys.studiocd.smarthound.strategies;

import gamesys.studiocd.smarthound.app.GlobalManager;
import gamesys.studiocd.smarthound.excel.ExcelMasterAnalyser;
import gamesys.studiocd.smarthound.util.GroovyListUtil;
import gamesys.studiocd.smarthound.vo.DeviceInfoVO;
import java.io.File;
import java.util.Collection;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author iulian.dafinoiu
 */
public class DeviceListStrategy implements GroovyStrategy {

    @Autowired
    private ExcelMasterAnalyser excelMasterAnalyser;
    
    @Autowired
    private GlobalManager globalManager;

    private static final String DEVICE_FILE_PATH = "/devices/";
    private static final String DEVICE_FILE_PREFIX = "device-list-";
    private static final int DEVICE_DATA_SHEET = 0;

    @Override
    public void compileList() {
        Collection<File> deviceFileNames = GroovyListUtil.listFilesFromFolder(DEVICE_FILE_PATH, DEVICE_FILE_PREFIX);
        deviceFileNames.stream().forEach(deviceFile -> {
            List<DeviceInfoVO> devices = this.excelMasterAnalyser.readExcelFile(deviceFile, DEVICE_DATA_SHEET, DeviceInfoVO.class);
            this.globalManager.getDeviceListByMonth().put(getYeahMonthFromFileName(deviceFile.getName()), devices);
        });
    }
    
    private String getYeahMonthFromFileName(String fileName) {
        return fileName.substring(fileName.lastIndexOf('-') + 1, fileName.indexOf('.'));
    }
}
