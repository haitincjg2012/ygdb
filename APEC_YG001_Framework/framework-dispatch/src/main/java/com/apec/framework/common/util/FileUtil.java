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
	 * @param res res
	 * @param fileName 文件名
	 * @param fileUrl 文件路径
	 * @throws FileNotFoundException 文件找不到
	 */
	public static void downloadFile(HttpServletResponse res, String fileName, String fileUrl) throws FileNotFoundException {
		// 文件的存放路径
		InputStream inputStream = new FileInputStream(fileUrl);
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
