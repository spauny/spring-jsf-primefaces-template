package gamesys.studiocd.smarthound.util;

import java.io.File;
import java.util.Collection;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;

/**
 *
 * @author iulian.dafinoiu
 */
public class GroovyListUtil {
    public static final String EXCELS_ROOT_PATH = "/Users/iulian.dafinoiu/Documents/excels";
    
    public static Collection<File> listFilesFromFolder(String relativePath, String prefix) {
        String dir = EXCELS_ROOT_PATH + relativePath;
        return FileUtils.listFiles(new File(dir), FileFilterUtils.prefixFileFilter(prefix), null);
    }
}
