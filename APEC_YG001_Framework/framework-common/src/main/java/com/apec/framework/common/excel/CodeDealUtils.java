/**
 * 
 */
package com.apec.framework.common.excel;

import java.net.URLEncoder;

import javax.mail.internet.MimeUtility;
import javax.servlet.http.HttpServletRequest;

import org.springframework.util.StringUtils;

/**
 * @ CharacterDealUtils.java 作用 注意事项
 * @author xx
 * 注意： 本内容仅限于中农网公司内部使用，禁止转发 VERSION DATE BY CHANGE/COMMENT 1.0 2015-4-25 czw
 * create
 */
public class CodeDealUtils {

	private final static String MSIE = "MSIE";

	private final static String CODE = "UTF-8";

	private final static String MOZILLA = "Mozilla";

	private final static int NEW_FILE_LENGTH = 150;

	/**
	 * 设置下载文件中文件的名称
	 * 
	 * @param filename filename
	 * @param request request
	 * @return String
	 */
	public static String encodeFilename(String filename,
			HttpServletRequest request) {

		/*
		 * 获取客户端浏览器和操作系统信息 在IE浏览器中得到的是：User-Agent=Mozilla/4.0 (compatible; MSIE
		 * 6.0; Windows NT 5.1; SV1; Maxthon; Alexa Toolbar)
		 * 在Firefox中得到的是：User-Agent=Mozilla/5.0 (Windows; U; Windows NT 5.1;
		 * zh-CN; rv:1.7.10) Gecko/20050717 Firefox/1.0.6
		 */
		String agent = request.getHeader("USER-AGENT");
		try {
			if ((agent != null) && agent.contains(MSIE)) {
				String newFileName = URLEncoder.encode(filename, CODE);
				newFileName = StringUtils.replace(newFileName, "+", "%20");
				if (newFileName.length() > NEW_FILE_LENGTH) {
					newFileName = new String(filename.getBytes("GB2312"),
							"ISO8859-1");
					newFileName = StringUtils.replace(newFileName, " ", "%20");
				}
				return newFileName;
			}
			if ((agent != null) && agent.contains(MOZILLA) ){
				return MimeUtility.encodeText(filename, CODE, "B");
			}

			return filename;
		} catch (Exception ex) {
			return filename;
		}
	}
}
