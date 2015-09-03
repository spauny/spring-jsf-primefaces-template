package gamesys.studiocd.smarthound.util;

import gamesys.studiocd.smarthound.vo.DeviceInfoVO;
import java.util.Comparator;

/**
 *
 * @author iulian.dafinoiu
 */
public class DeviceComparator implements Comparator<DeviceInfoVO> {

    @Override
    public int compare(DeviceInfoVO o1, DeviceInfoVO o2) {
        return o1.getTheoHWDiffFromLastMonth().compareTo(o2.getTheoHWDiffFromLastMonth());
    }

}
