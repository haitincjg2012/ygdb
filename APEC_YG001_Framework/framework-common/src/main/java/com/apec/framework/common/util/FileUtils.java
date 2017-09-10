package com.apec.framework.common.util;

import java.io.File;
import java.util.Calendar;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 文件处理
 * @author PC
 *
 */
public class FileUtils {
	private final static Log log = LogFactory.getLog(FileUtils.class);
	
    private static final String SLASH = "/";
    
	public static void mkdirs(String path) {
		try {
			File file = new File(path);
			if (!file.exists()) {
				file.mkdirs();
			}
		} catch (Exception e) {
			log.error("It occured error in mkdirs Class FileUtils,Cause:" + e.getMessage());
		}

	}
	
    /**
     * 获取图片的相对路径
     * @param fileType 文件类型
     * @return 图片的相对路径
     */
    public static String getFileRelativePath(String fileType)
    {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get( Calendar.YEAR );
        int month = calendar.get( Calendar.MONTH ) + 1;
        int date = calendar.get( Calendar.DATE );
        return  fileType + SLASH + year + SLASH + month + SLASH + date + SLASH;
    }
    
}
