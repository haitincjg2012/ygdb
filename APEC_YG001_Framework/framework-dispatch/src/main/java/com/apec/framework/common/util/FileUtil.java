package com.apec.framework.common.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * 文件处理
 * @author PC
 *
 */
public class FileUtil {

	private final static Log log = LogFactory.getLog(FileUtil.class);
	/**
	 * 下载文件
	 * @param res
	 * @param fileName
	 * @param fileUrl
	 * @throws FileNotFoundException
	 */
	public static void downloadFile(HttpServletResponse res, String fileName, String fileUrl) throws FileNotFoundException {
		InputStream inputStream = new FileInputStream(fileUrl);// 文件的存放路径
		// 设置输出的格式
		res.reset();
		res.setContentType("bin");
		res.addHeader("Content-disposition", "attachment;filename=" + fileName);
		// 循环取出流中的数据
		byte[] b = new byte[10240];
		int len;
		try {
			while ((len = inputStream.read(b)) > 0) {
				res.getOutputStream().write(b, 0, len);
			}
			inputStream.close();
		} catch (IOException e) {
			log.error("It occured error in downloadFile class FileUtil ,Cause:" + e.getMessage());
		}

	}
}
