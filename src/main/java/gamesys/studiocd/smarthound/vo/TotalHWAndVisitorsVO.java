package gamesys.studiocd.smarthound.vo;

import java.io.Serializable;
import lombok.Data;

/**
 *
 * @author iulian.dafinoiu
 */
@Data
public class TotalHWAndVisitorsVO implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private double totalTheoHW;
    private double totalUniqueVis;
    
}
