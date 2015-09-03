package gamesys.studiocd.smarthound.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

/**
 *
 * @author iulian.dafinoiu
 */
@Data
public class DeviceInfoVO implements Serializable {
    private static final long serialVersionUID = 1784561237894561231L;
    
    private String territory;
    private Double activityYearMonth;
    private String marketingName;
    private String osName;
    private String osVersion;
    private Double totalActives = 0D;
    private Double vips = 0D;
    private Double theoreticalHW = 0D;
    private Double theoRank = 0D;
    private Double percentageTheo = 0D;
    private Double houseWin = 0D;
    private Double hwRank = 0D;
    private Double percentageHW = 0D;
    
    private Double theoHWDiffFromLastMonth = 0D;
    
    private List<String> osVersions = new ArrayList<>();
    
    private DeviceInfoVO firstDevice;
    
    private List<DeviceInfoVO> previousMonths = new ArrayList<>();
    
    private int rankStatus;
}
