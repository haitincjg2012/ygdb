package com.apec.framework.common.excel;

import java.text.DecimalFormat;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class ReportUtil {

	/**
	 * 获取主标题的字体样式
	 * 
	 * @author 
	 * @since v1.0
	 * @date 2013-9-24 下午1:59:47
	 * @param workBook
	 * @return
	 */
	public static HSSFCellStyle getTitleStyle(HSSFWorkbook workBook) {
		HSSFFont fonthead = workBook.createFont();
		fonthead.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);// 加粗
		fonthead.setFontHeightInPoints((short) 12); // 字体大小
		fonthead.setFontName("宋体");
		HSSFCellStyle cellStyle = workBook.createCellStyle();// 表名样式
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
	 * 
	 * @author 
	 * @since v1.0
	 * @date 2013-9-24 下午2:01:00
	 * @param workBook
	 * @return
	 */
	public static HSSFCellStyle getHeaderStyle(HSSFWorkbook workBook) {
		HSSFFont font = workBook.createFont();
		font.setColor(HSSFFont.COLOR_NORMAL);
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		font.setFontHeightInPoints((short) 12);
		font.setFontName("宋体");
		HSSFCellStyle cellStyle = workBook.createCellStyle();// 创建格式
		cellStyle.setFont(font);
		cellStyle.setWrapText(true);//自动换行   强制换行 cell.setCellValue(new HSSFRichTextString("hello/r/n world!"));
		cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		cellStyle.setBorderTop(HSSFCellStyle.BORDER_DOTTED);
		cellStyle.setBorderBottom(HSSFCellStyle.BORDER_DOTTED);
		cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
		return cellStyle;
	}

	/**
	 * 获取字体
	 * 
	 * @author 
	 * @since v1.0
	 * @date 2013-9-24 下午2:01:00
	 * @param workBook
	 * @return
	 */
	public static HSSFCellStyle getStyle(HSSFWorkbook workBook) {
		HSSFFont font = workBook.createFont();
		font.setColor(HSSFFont.COLOR_NORMAL);
		font.setFontHeightInPoints((short) 12);
		font.setFontName("宋体");
		HSSFCellStyle cellStyle = workBook.createCellStyle();// 创建格式
		cellStyle.setFont(font);
		cellStyle.setWrapText(true);//自动换行   强制换行 cell.setCellValue(new HSSFRichTextString("hello/r/n world!"));
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
	 * 
	 * @author 
	 * @since v1.0
	 * @date 2013-9-24 下午1:59:47
	 * @param workBook
	 * @return
	 */
	public static HSSFCellStyle getAmountStyle(HSSFWorkbook workBook) {
		HSSFFont fonthead = workBook.createFont();
		fonthead.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);// 加粗
		fonthead.setFontHeightInPoints((short) 12); // 字体大小
		fonthead.setFontName("宋体");
		HSSFCellStyle cellStyle = workBook.createCellStyle();// 表名样式
		cellStyle.setFont(fonthead);
		cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		cellStyle.setVerticalAlignment(HSSFCellStyle.ALIGN_CENTER);
		return cellStyle;
	}

	/**
	 * 格式化金额
	 * 
	 * @author 
	 * @since v1.0
	 * @date 2013-9-24 下午2:39:31
	 * @param amount
	 * @return
	 */
	public static String formatAmount(double amount) {
		DecimalFormat df = new DecimalFormat("￥0.00");
		return df.format(amount);
	}

	public static void main(String[] args) {
		double d1 = 234.3451;
		DecimalFormat df = new DecimalFormat("￥0.00");
		System.out.println(df.format(d1));
	}
}
