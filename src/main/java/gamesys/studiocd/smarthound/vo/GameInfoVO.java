package gamesys.studiocd.smarthound.vo;

import java.io.Serializable;
import lombok.Data;

/**
 *
 * @author iulian.dafinoiu
 */
@Data
public class GameInfoVO implements Serializable {
    private static final long serialVersionUID = 1784561237894561231L;
    
    private String dma;
    private String gameProductSkinName;
    private Double actives;
    private Double theoHW;
    private String gameProductTypeName;
    private String localeName;
}
