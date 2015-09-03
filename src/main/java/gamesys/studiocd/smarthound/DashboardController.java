package gamesys.studiocd.smarthound;

import gamesys.studiocd.smarthound.app.GlobalManager;
import gamesys.studiocd.smarthound.util.DeviceComparator;
import gamesys.studiocd.smarthound.vo.DeviceInfoVO;
import gamesys.studiocd.smarthound.vo.TotalHWAndVisitorsVO;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.primefaces.event.ItemSelectEvent;
import org.primefaces.model.DashboardColumn;
import org.primefaces.model.DashboardModel;
import org.primefaces.model.DefaultDashboardColumn;
import org.primefaces.model.DefaultDashboardModel;
import org.primefaces.model.chart.PieChartModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("view")
public class DashboardController implements Serializable {

    private static final long serialVersionUID = 145678009989897844L;

    @Autowired
    private GlobalManager globalManager;

    @Getter
    @Setter
    private DashboardModel model;

    @Getter
    private List<DeviceInfoVO> top30Devices;

    @Getter
    private List<DeviceInfoVO> top30EmergingDevices;

    @Getter
    private List<DeviceInfoVO> top30FadingDevices;

    @Getter
    @Setter
    private boolean viewEmerging = true;

    @Getter
    @Setter
    private PieChartModel androidPieModel;
    
    @Getter
    @Setter
    private PieChartModel androidPopPieModel;

    @Getter
    @Setter
    private PieChartModel iosPieModel;
    
    @Getter
    @Setter
    private PieChartModel iosPopPieModel;

    @Getter
    @Setter
    private int screenWidth;
    
    private DecimalFormat df = new DecimalFormat("#,###.##"); 

//    @PostConstruct
    public void init() {
        model = new DefaultDashboardModel();
        DashboardColumn column1 = new DefaultDashboardColumn();
        DashboardColumn column2 = new DefaultDashboardColumn();
        DashboardColumn column3 = new DefaultDashboardColumn();

        column1.addWidget("top30DevicesPanel");
        column2.addWidget("top30EmergingDevicesPanel");
        
        column1.addWidget("androidbrowserChartsPanel");
        column2.addWidget("iosbrowserChartsPanel");

        if (this.screenWidth > 1600) {
            column3.addWidget("androidPopBrowserChartsPanel");
            column3.addWidget("iosPopBrowserChartsPanel");
        } else {
            column1.addWidget("androidPopBrowserChartsPanel");
            column2.addWidget("iosPopBrowserChartsPanel");
        }

        this.top30Devices = this.globalManager.getEntireDeviceList().stream()
                .filter(device -> device != null && StringUtils.isNotBlank(device.getMarketingName()) && !device.getMarketingName().equalsIgnoreCase("UNKNOWN") && device.getTerritory().equalsIgnoreCase("UK"))
                .limit(30).collect(Collectors.toList());
        List<DeviceInfoVO> trends = this.globalManager.getEntireDeviceList().stream()
                .filter(device -> device != null && StringUtils.isNotBlank(device.getMarketingName()) && !device.getMarketingName().equalsIgnoreCase("UNKNOWN") && device.getTerritory().equalsIgnoreCase("UK"))
                .sorted(new DeviceComparator()).collect(Collectors.toList());
        if (!trends.isEmpty()) {
            this.top30EmergingDevices = trends.stream().skip(trends.size() - 30).collect(Collectors.toList());
            Collections.reverse(this.top30EmergingDevices);
            this.top30FadingDevices = trends.stream().limit(30).collect(Collectors.toList());
        }
        model.addColumn(column1);
        model.addColumn(column2);
        model.addColumn(column3);

        createPieModels();
    }

    private void createPieModels() {
        if (this.globalManager.getAndroidBrowsersInfo() != null && !this.globalManager.getAndroidBrowsersInfo().isEmpty()) {
            createAndroidPieModel();
            createIOSPieModel();
        }
    }

    private void createAndroidPieModel() {
        androidPieModel = new PieChartModel();
        androidPopPieModel = new PieChartModel();
        df.setGroupingUsed(true);
        df.setGroupingSize(3);
        TotalHWAndVisitorsVO totalHWAndVisitors = new TotalHWAndVisitorsVO();

        this.globalManager.getAndroidBrowsersInfo().stream().filter(browser -> browser != null && browser.getTheoreticalHW() != null && browser.getTheoreticalHW() > 100000).forEach(browser -> {
            androidPieModel.set(browser.getBrowserName() + "|" + browser.getBrowserVersion(), browser.getTheoreticalHW());
            totalHWAndVisitors.setTotalTheoHW(totalHWAndVisitors.getTotalTheoHW() + browser.getTheoreticalHW());
        });
        
        this.globalManager.getAndroidBrowsersInfo().stream().filter(browser -> browser != null && browser.getUniqueMembers() != null && browser.getUniqueMembers() > 2000).forEach(browser -> {
            androidPopPieModel.set(browser.getBrowserName() + "|" + browser.getBrowserVersion(), browser.getUniqueMembers());
            totalHWAndVisitors.setTotalUniqueVis(totalHWAndVisitors.getTotalUniqueVis() + browser.getUniqueMembers());
        });

        androidPieModel.setTitle("Theoretical House Win (total of £" + df.format(totalHWAndVisitors.getTotalTheoHW()) + ")");
        androidPieModel.setDataLabelFormatString(null);
        androidPieModel.setShowDataLabels(true);
        androidPieModel.setLegendPosition("w");
        
        androidPopPieModel.setTitle("Unique Members (total of " + df.format(totalHWAndVisitors.getTotalUniqueVis()) + ")");
        androidPopPieModel.setDataLabelFormatString(null);
        androidPopPieModel.setShowDataLabels(true);
        androidPopPieModel.setLegendPosition("w");
    }

    private void createIOSPieModel() {
        iosPieModel = new PieChartModel();
        iosPopPieModel = new PieChartModel();
        df.setGroupingUsed(true);
        df.setGroupingSize(3);
        TotalHWAndVisitorsVO totalHWAndVisitors = new TotalHWAndVisitorsVO();

        this.globalManager.getIosBrowsersInfo().stream().filter(browser -> browser != null && browser.getTheoreticalHW() != null && browser.getTheoreticalHW() > 65000).forEach(browser -> {
            iosPieModel.set(browser.getBrowserName() + "|" + browser.getBrowserVersion(), browser.getTheoreticalHW());
            totalHWAndVisitors.setTotalTheoHW(totalHWAndVisitors.getTotalTheoHW() + browser.getTheoreticalHW());
        });
        
        this.globalManager.getIosBrowsersInfo().stream().filter(browser -> browser != null && browser.getUniqueMembers() != null && browser.getUniqueMembers() > 1000).forEach(browser -> {
            iosPopPieModel.set(browser.getBrowserName() + "|" + browser.getBrowserVersion(), browser.getUniqueMembers());
            totalHWAndVisitors.setTotalUniqueVis(totalHWAndVisitors.getTotalUniqueVis() + browser.getUniqueMembers());
        });
        
        iosPieModel.setTitle("Theoretical House Win (total of £" + df.format(totalHWAndVisitors.getTotalTheoHW()) + ")");
        iosPieModel.setShowDataLabels(true);
        iosPieModel.setMouseoverHighlight(true);
        iosPieModel.setLegendPosition("w");
        
        iosPopPieModel.setTitle("Unique Members (total of " + df.format(totalHWAndVisitors.getTotalUniqueVis()) + ")");
        iosPopPieModel.setDataLabelFormatString(null);
        iosPopPieModel.setShowDataLabels(true);
        iosPopPieModel.setLegendPosition("w");
    }

    public void itemSelect(ItemSelectEvent event) {
        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Item Index: " + event.getItemIndex() + ", Series Index:" + event.getSeriesIndex(),
                "");

        FacesContext.getCurrentInstance().addMessage(null, msg);
    }
}
