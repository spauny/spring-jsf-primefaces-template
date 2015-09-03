package gamesys.studiocd.smarthound.util;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import org.apache.commons.lang3.StringUtils;


public final class FacesMessageUtil {
    
    public static void addFacesInfoMessage(String message) {
        addFacesInfoMessage(null, message);
    }
    
    public static void addFacesInfoMessage(String clientId, String message) {
        addFacesMessage(clientId, message, FacesMessage.SEVERITY_INFO);
    }
    
    public static void addFacesWarnMessage(String message) {
        addFacesWarnMessage(null, message);
    }
    
    public static void addFacesWarnMessage(String clientId, String message) {
        addFacesMessage(clientId, message, FacesMessage.SEVERITY_WARN);
    }
    
    public static void addFacesErrorMessage(String message) {
        addFacesErrorMessage(null, message);
    }
    
    public static void addFacesErrorMessage(String clientId, String message) {
        addFacesMessage(clientId, message, FacesMessage.SEVERITY_ERROR);
    }
    
    public static void addFacesMessage(String clientId, String message, FacesMessage.Severity severity) {
        if (FacesContext.getCurrentInstance() != null) {
            FacesContext.getCurrentInstance().addMessage(clientId, new FacesMessage(severity, message, StringUtils.EMPTY));
        }
    }
}
