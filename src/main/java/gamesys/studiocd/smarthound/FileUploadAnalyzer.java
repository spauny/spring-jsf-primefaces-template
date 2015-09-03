package gamesys.studiocd.smarthound;

import gamesys.studiocd.smarthound.vo.ExcelFileVO;
import java.io.IOException;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;


@Component
@Scope("request")
public class FileUploadAnalyzer implements Serializable {
    private static final long serialVersionUID = 17410852096345678L;
    
    @Autowired
    private ExcelAnalyzer excelAnalyzer;
    
    private boolean analyseFinished;
    
    public void handleFileUpload(FileUploadEvent event) {  
//        FacesMessage msg = new FacesMessage(event.getFile().getFileName() + " uploaded successfully.");  
//        FacesContext.getCurrentInstance().addMessage(null, msg);  
        try {
            ExcelFileVO excelFIleVO = new ExcelFileVO();
            excelFIleVO.setExcelFileName(event.getFile().getFileName());
            excelFIleVO.setExcelInputStream(event.getFile().getInputstream());
            
            this.excelAnalyzer.analyzeExcel(excelFIleVO);
            RequestContext requestContext = RequestContext.getCurrentInstance();
            this.analyseFinished = true;
            requestContext.update("analysingDialogForm");
            //requestContext.execute("PF('analyzingDialogVar').hide(); PF('fileContentDialogVar').show();");
        } catch (IOException ex) {
            Logger.getLogger(FileUploadAnalyzer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }  

    public boolean isAnalyseFinished() {
        return analyseFinished;
    }

}
