package com.apec.framework.jpa.common.tools;

import org.apache.commons.lang3.StringUtils;

/**
 * 
 * 类 编 号：
 * 类 名 称：SqlFilter
 * 内容摘要：
 * 完成日期：
 * 编码作者：
 */
public abstract class SqlFilter {

	public static final char DEFALT_ESCAPE_CHAR = '/';
	
	private static final String DB_LIKE = "%";
	private static final char[] ESCAPE_CHAR = {'%', '_', '/'};
	private static final String[] ESCAPE = {DB_LIKE, "_", "/"};
	private static final String[] REPLACEMENT = {"/%", "/_", "//"};
	
	private static final String SPLIT = ",";

	public static String filterForLike(String param) {
		if (StringUtils.containsAny(param, ESCAPE_CHAR)) {
			return DB_LIKE + StringUtils.replaceEach(param, ESCAPE, REPLACEMENT) + DB_LIKE;
		}
		return DB_LIKE + param + DB_LIKE;
	}

	/**
	 * @param busType
	 * @return
	 */
	public static String filterForMatches(String busType) {
		return "(" + SPLIT + busType.replaceAll(SPLIT, ",)|(,") + SPLIT + ")";
	}
}
