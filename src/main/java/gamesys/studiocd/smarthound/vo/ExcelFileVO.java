package gamesys.studiocd.smarthound.vo;

import java.io.InputStream;
import java.io.Serializable;
import lombok.Data;

/**
 *
 * @author iulian.dafinoiu
 */
@Data
public class ExcelFileVO implements Serializable {
    private static final long serialVersionUID = 174185296345789123L;
    
    private InputStream excelInputStream;
    private String excelFileName;
}
