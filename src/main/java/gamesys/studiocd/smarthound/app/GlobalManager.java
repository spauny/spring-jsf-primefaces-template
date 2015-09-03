package gamesys.studiocd.smarthound.app;

import gamesys.studiocd.smarthound.vo.BrowserInfoVO;
import gamesys.studiocd.smarthound.vo.DeviceInfoVO;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;


@Component
@Scope("singleton")
@Slf4j
public class GlobalManager implements Serializable {
    private static final long serialVersionUID = 1010234324505869476L;
    
    @Getter
    private final List<DeviceInfoVO> entireDeviceList = new ArrayList<>(200);
    
    @Getter
    private final List<DeviceInfoVO> distinctDeviceList = new ArrayList<>(200);
    
    @Getter
    private final List<DeviceInfoVO> topEmergingDevices = new ArrayList<>(200);
    
    @Getter
    private final Map<String, List<DeviceInfoVO>> compactDeviceMap = new HashMap<>(100);
    
    @Getter
    private final Map<String, DeviceInfoVO> lastMonthDevices = new HashMap<>(200);
    
    @Getter
    private final List<BrowserInfoVO> androidBrowsersInfo = new ArrayList<>();
    
    @Getter
    private final List<BrowserInfoVO> iosBrowsersInfo = new ArrayList<>();
    
    
    // NEW STUFF --------------------
    
    @Getter
    private final Map<String, List<DeviceInfoVO>> deviceListByMonth = new HashMap<>();
}
