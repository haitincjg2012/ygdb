package com.apec.framework.common.excel;

import java.text.DecimalFormat;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 * @author xx
 */
public class ReportUtil {

	/**
	 * 获取主标题的字体样式
	 * since v1.0
	 * date 2013-9-24 下午1:59:47
	 * @param workBook workBook
	 * @return HSSFCellStyle
	 */
	public static HSSFCellStyle getTitleStyle(HSSFWorkbook workBook) {
		HSSFFont fonthead = workBook.createFont();
		// 加粗
		fonthead.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		// 字体大小
		fonthead.setFontHeightInPoints((short) 12);
		fonthead.setFontName("宋体");
		// 表名样式
		HSSFCellStyle cellStyle = workBook.createCellStyle();
		cellStyle.setFont(fonthead);
		cellStyle.setWrapText(true);
		cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		cellStyle.setBorderTop(HSSFCellStyle.BORDER_DOTTED);
		cellStyle.setBorderBottom(HSSFCellStyle.BORDER_DOTTED);
		cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
		return cellStyle;
	}

	/**
	 * 获取表头样式
	 * since v1.0
	 * date 2013-9-24 下午2:01:00
	 * @param workBook workBook
	 * @return HSSFCellStyle
	 */
	public static HSSFCellStyle getHeaderStyle(HSSFWorkbook workBook) {
		HSSFFont font = workBook.createFont();
		font.setColor(HSSFFont.COLOR_NORMAL);
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		font.setFontHeightInPoints((short) 12);
		font.setFontName("宋体");
		// 创建格式
		HSSFCellStyle cellStyle = workBook.createCellStyle();
		cellStyle.setFont(font);
		//自动换行   强制换行
		cellStyle.setWrapText(true);
		cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		cellStyle.setBorderTop(HSSFCellStyle.BORDER_DOTTED);
		cellStyle.setBorderBottom(HSSFCellStyle.BORDER_DOTTED);
		cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
		return cellStyle;
	}

	/**
	 * 获取字体
	 * since v1.0
	 * date 2013-9-24 下午2:01:00
	 * @param workBook workBook
	 * @return  HSSFCellStyle
	 */
	public static HSSFCellStyle getStyle(HSSFWorkbook workBook) {
		HSSFFont font = workBook.createFont();
		font.setColor(HSSFFont.COLOR_NORMAL);
		font.setFontHeightInPoints((short) 12);
		font.setFontName("宋体");
		// 创建格式
		HSSFCellStyle cellStyle = workBook.createCellStyle();
		cellStyle.setFont(font);
		//自动换行   强制换行
		cellStyle.setWrapText(true);
		cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		//设置边框样式
		cellStyle.setBorderTop(HSSFCellStyle.BORDER_DOTTED);
		cellStyle.setBorderBottom(HSSFCellStyle.BORDER_DOTTED);
		cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
		return cellStyle;
	}

	/**
	 * 获取主要金额的字体样式
	 * since v1.0
	 * date 2013-9-24 下午1:59:47
	 * @param workBook workBook
	 * @return HSSFCellStyle
	 */
	public static HSSFCellStyle getAmountStyle(HSSFWorkbook workBook) {
		HSSFFont fonthead = workBook.createFont();
		// 加粗
		fonthead.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		// 字体大小
		fonthead.setFontHeightInPoints((short) 12);
		fonthead.setFontName("宋体");
		// 表名样式
		HSSFCellStyle cellStyle = workBook.createCellStyle();
		cellStyle.setFont(fonthead);
		cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		cellStyle.setVerticalAlignment(HSSFCellStyle.ALIGN_CENTER);
		return cellStyle;
	}

	/**
	 * 格式化金额
	 * since v1.0
	 * date 2013-9-24 下午2:39:31
	 * @param amount amount
	 * @return String
	 */
	public static String formatAmount(double amount) {
		DecimalFormat df = new DecimalFormat("￥0.00");
		return df.format(amount);
	}

}
