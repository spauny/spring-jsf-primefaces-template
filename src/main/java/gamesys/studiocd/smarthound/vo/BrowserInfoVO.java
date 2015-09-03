package gamesys.studiocd.smarthound.vo;

import java.io.Serializable;
import lombok.Data;

/**
 *
 * @author iulian.dafinoiu
 */
@Data
public class BrowserInfoVO implements Serializable {
    private static final long serialVersionUID = 17418520410410520L;
    
    private String territory;
    private String activityYearMonth;
    private String osName;
    private String browserName;
    private String browserVersion;
    private Double theoreticalHW;
    private Double uniqueMembers;
    
}
