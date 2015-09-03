package gamesys.studiocd.smarthound;

import gamesys.studiocd.smarthound.app.GlobalManager;
import gamesys.studiocd.smarthound.vo.DeviceInfoVO;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.CategoryAxis;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.LineChartModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("view")
@Slf4j
public class CompactListController implements Serializable {

    private static final long serialVersionUID = 178978878931206758L;
    
    @Autowired
    private GlobalManager globalManager;

    @Getter
    private TreeNode root;
    
    @Getter
    @Setter
    private TreeNode selectedNode;
    
    @Getter
    @Setter
    private DeviceInfoVO selectedDeviceInfo;
    
    @Getter
    @Setter
    private int theoHWPercent = 100;
    
    @Getter
    @Setter
    private LineChartModel theoHWLineModel = new LineChartModel();
    
    @Getter
    @Setter
    private LineChartModel theoRankLineModel = new LineChartModel();
    
    @Getter
    @Setter
    private LineChartModel totalActivesLineModel = new LineChartModel();
    
    @Getter
    @Setter
    private int showChart = 1;
    
    @Getter
    @Setter
    private String searchBy;
    
    @PostConstruct
    private void init() {
        refreshDevices();
    }

    public void refreshDevices() {
        constructTreeTable();
    }
    
    public void createLineModels() {
        theoHWLineModel = initCategoryModel();
        theoHWLineModel.setTitle("Theo HW Chart");
        theoHWLineModel.setLegendPosition("e");
        theoHWLineModel.setShowPointLabels(true);
        theoHWLineModel.getAxes().put(AxisType.X, new CategoryAxis("Month"));
        Axis yAxis = theoHWLineModel.getAxis(AxisType.Y);
        yAxis.setLabel("Theo HW");
        yAxis.setMin(0);
        
        theoRankLineModel = initTheoRankModel();
        theoRankLineModel.setTitle("Theo HW Rank Chart");
        theoRankLineModel.setLegendPosition("e");
        theoRankLineModel.setShowPointLabels(true);
        theoRankLineModel.getAxes().put(AxisType.X, new CategoryAxis("Month"));
        yAxis = theoRankLineModel.getAxis(AxisType.Y);
        yAxis.setLabel("Rank");
        yAxis.setMin(0);
        
        totalActivesLineModel = initTotalActivesModel();
        totalActivesLineModel.setTitle("Total Actives Chart");
        totalActivesLineModel.setLegendPosition("e");
        totalActivesLineModel.setShowPointLabels(true);
        totalActivesLineModel.getAxes().put(AxisType.X, new CategoryAxis("Month"));
        yAxis = totalActivesLineModel.getAxis(AxisType.Y);
        yAxis.setLabel("Actives");
        yAxis.setMin(0);
//        yAxis.setMax(200);
    }
    
    private LineChartModel initCategoryModel() {
        LineChartModel model = new LineChartModel();
 
        ChartSeries deviceChartSerie = new ChartSeries();
        deviceChartSerie.setLabel(this.selectedDeviceInfo.getMarketingName() + " | " + this.selectedDeviceInfo.getFirstDevice().getOsVersion());
        if (this.selectedDeviceInfo.getPreviousMonths().size() > 0 && this.selectedDeviceInfo.getPreviousMonths().get(0) != null) {
            deviceChartSerie.set("June", this.selectedDeviceInfo.getPreviousMonths().get(0).getTheoreticalHW());
        }
        deviceChartSerie.set("July", this.selectedDeviceInfo.getFirstDevice().getTheoreticalHW());
 
        model.addSeries(deviceChartSerie);
         
        return model;
    }
    
    private LineChartModel initTheoRankModel() {
        LineChartModel model = new LineChartModel();
 
        ChartSeries deviceChartSerie = new ChartSeries();
        deviceChartSerie.setLabel(this.selectedDeviceInfo.getMarketingName() + " | " + this.selectedDeviceInfo.getFirstDevice().getOsVersion());
        if (this.selectedDeviceInfo.getPreviousMonths().size() > 0 && this.selectedDeviceInfo.getPreviousMonths().get(0) != null) {
            deviceChartSerie.set("June", this.selectedDeviceInfo.getPreviousMonths().get(0).getTheoRank());
        }
        deviceChartSerie.set("July", this.selectedDeviceInfo.getFirstDevice().getTheoRank());
 
        model.addSeries(deviceChartSerie);
         
        return model;
    }
    
    private LineChartModel initTotalActivesModel() {
        LineChartModel model = new LineChartModel();
 
        ChartSeries deviceChartSerie = new ChartSeries();
        deviceChartSerie.setLabel(this.selectedDeviceInfo.getMarketingName() + " | " + this.selectedDeviceInfo.getFirstDevice().getOsVersion());
        if (this.selectedDeviceInfo.getPreviousMonths().size() > 0 && this.selectedDeviceInfo.getPreviousMonths().get(0) != null) {
            deviceChartSerie.set("June", this.selectedDeviceInfo.getPreviousMonths().get(0).getTotalActives());
        }
        deviceChartSerie.set("July", this.selectedDeviceInfo.getFirstDevice().getTotalActives());
 
        model.addSeries(deviceChartSerie);
         
        return model;
    }
    
    public void constructTreeTable() {
        root = new DefaultTreeNode(new DeviceInfoVO(), null);
        double percent = 0;
        for (DeviceInfoVO device : this.globalManager.getDistinctDeviceList()) {
            
            if (StringUtils.isBlank(device.getMarketingName())) {
                continue;
            }
            
            if (StringUtils.isNotBlank(searchBy) 
                    && !(device.getMarketingName().toLowerCase().contains(searchBy.toLowerCase()) 
                    || device.getOsName().toLowerCase().contains(searchBy.toLowerCase()) 
                    || device.getOsVersion().toLowerCase().contains(searchBy.toLowerCase())) ) {
                continue;
            }
            
            DeviceInfoVO deviceInfoVO = new DeviceInfoVO();
            deviceInfoVO.setMarketingName(device.getMarketingName());
            deviceInfoVO.setOsName(device.getOsName());
            deviceInfoVO.setTheoRank(device.getTheoRank());
            deviceInfoVO.setPreviousMonths(device.getPreviousMonths());
            
            deviceInfoVO.setFirstDevice(device);
            deviceInfoVO.setRankStatus(showChart);
            
            TreeNode parentDeviceNode = new DefaultTreeNode(deviceInfoVO, root);
            if (this.globalManager.getCompactDeviceMap().containsKey(device.getMarketingName())) {
                List<DeviceInfoVO> childDevices = this.globalManager.getCompactDeviceMap().get(device.getMarketingName());
                deviceInfoVO.setRankStatus(childDevices.get(0).getRankStatus());
                childDevices.stream().forEach(childDevice -> {
                    TreeNode subDeviceNode = new DefaultTreeNode(childDevice, parentDeviceNode);
                    deviceInfoVO.getOsVersions().add(childDevice.getOsVersion());
                    deviceInfoVO.setTotalActives(deviceInfoVO.getTotalActives() + childDevice.getTotalActives());
                    deviceInfoVO.setVips(deviceInfoVO.getVips() + childDevice.getVips());
                    deviceInfoVO.setTheoreticalHW(deviceInfoVO.getTheoreticalHW() + childDevice.getTheoreticalHW());
                    deviceInfoVO.setPercentageTheo(deviceInfoVO.getPercentageTheo() + childDevice.getPercentageTheo());
                    childDevice.setFirstDevice(childDevice);
                });
            } else {
                log.info("Marking name doesn't exist in the compact list: {}", device.getMarketingName());
            }
            
            deviceInfoVO.setOsVersion(deviceInfoVO.getOsVersions().size() + " distinct versions");
            percent += deviceInfoVO.getPercentageTheo();
            if (percent * 100 > theoHWPercent) {
                break;
            }
        }
    }

}
