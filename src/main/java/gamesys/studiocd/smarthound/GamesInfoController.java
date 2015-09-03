package gamesys.studiocd.smarthound;

import gamesys.studiocd.smarthound.vo.GameInfoVO;
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
import javax.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

/**
 *
 * @author iulian.dafinoiu
 */
@Component
public class GamesInfoController implements Serializable {

    private static final long serialVersionUID = 17894564185200025L;

    @Getter
    private final List<GameInfoVO> gameInfoVOs = new ArrayList<>(600);
    
    @Getter
    @Setter
    private List<GameInfoVO> filteredGameInfoVOs = new ArrayList<>(600);

    public static void main(String[] args) {
        GamesInfoController gamesInfoController = new GamesInfoController();
        gamesInfoController.readGames();
    }

    @PostConstruct
    private void init() {
        readGames();
    }

    private Iterator<Row> getExcelRows(InputStream excelInputStream) {
        try {
            XSSFWorkbook workbook = new XSSFWorkbook(excelInputStream);
            XSSFSheet gamesSheet = workbook.getSheetAt(0);
            return gamesSheet.iterator();
        } catch (IOException ex) {
            Logger.getLogger(ExcelAnalyzer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    private void readGames() {
        ClassLoader classLoader = getClass().getClassLoader();
        Iterator<Row> gameIterator = getExcelRows(classLoader.getResourceAsStream("excels/Games_june_byeverything.xlsx"));

        gameInfoVOs.clear();
        filteredGameInfoVOs.clear();

        List<String> headers = new ArrayList<>();
        int rowCounter = 0;
        while (gameIterator.hasNext()) {
            Row row = gameIterator.next();
            Iterator<Cell> cellIterator = row.cellIterator();
            int columnCounter = 0;
            GameInfoVO gameInfoVO = new GameInfoVO();
            while (cellIterator.hasNext()) {
                Cell cell = cellIterator.next();
                if (Cell.CELL_TYPE_STRING == cell.getCellType() && StringUtils.isNotBlank(cell.getStringCellValue())) {
                    String cellValue = cell.getStringCellValue();
                    if (rowCounter == 0) {
                        headers.add(cellValue);
                    } else {
                        callSetMethodForColumn(headers.get(columnCounter), cellValue, gameInfoVO);
                    }
                } else if (Cell.CELL_TYPE_NUMERIC == cell.getCellType()) {
                    double cellValue = cell.getNumericCellValue();
                    if (rowCounter > 0) {
                        callSetMethodForColumn(headers.get(columnCounter), cellValue, gameInfoVO);
                    }
                }
                columnCounter++;
            }
            if (rowCounter > 0) {
                gameInfoVOs.add(gameInfoVO);
            }
            rowCounter++;
        }
        this.filteredGameInfoVOs = new ArrayList<>(gameInfoVOs);
    }

    private void callSetMethodForColumn(String columnHeader, Object value, GameInfoVO gameInfoVO) {
        try {
            Method[] methods = gameInfoVO.getClass().getDeclaredMethods();
            String simpleHeaderName = columnHeader.replaceAll(" ", "");
            for (Method method : methods) {
                if (method.getName().equalsIgnoreCase("set" + simpleHeaderName)) {
                    Class<?>[] paramTypes = method.getParameterTypes();
                    if (value instanceof String) {
                        if (paramTypes[0].isInstance(value)) {
                            method.invoke(gameInfoVO, (String) value);
                        } else {
                            method.invoke(gameInfoVO, 0D);
                        }
                    } else {
                        if (paramTypes[0].isInstance(value)) {
                            method.invoke(gameInfoVO, (Double) value);
                        } else {
                            method.invoke(gameInfoVO, ((Double) value).toString());
                        }
                    }
                }
            }
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            Logger.getLogger(ExcelAnalyzer.class.getName()).log(Level.SEVERE, "header: " + columnHeader + " value: " + value, ex);
        }
    }

}
