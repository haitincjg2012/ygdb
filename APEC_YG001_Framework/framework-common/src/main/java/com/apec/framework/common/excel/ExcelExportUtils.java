package com.apec.framework.common.excel;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.Region;

public class ExcelExportUtils {

	public static String getSystemResourceMapped(HttpServletRequest request) {
		return request.getSession().getServletContext().getRealPath("/");
	}

	public static ByteArrayOutputStream exportExcel(String[] excelheader, List<?> excellist, boolean ishk) {
		return exportExcel(excelheader, excellist, ishk, new HashSet<Object>(), new ArrayList<Object>());
	}

	public static void exportExcel(String templetPath, int startRow, OutputStream output, List<?> excellist,
			boolean ishk) {
		exportExcel(templetPath, startRow, output, excellist, ishk, new HashSet<Object>(), new ArrayList<Object>());
	}

	public static void exportExcel(String templetPath, int startRow, int startCol, OutputStream output,
			List<?> excellist, boolean ishk) {
		exportExcel(templetPath, startRow, startCol, output, excellist, ishk, new HashSet<Object>(),
				new ArrayList<Object>());
	}

	// javax.swing.plaf.synth
	@SuppressWarnings("deprecation")
	public static ByteArrayOutputStream exportExcel(String[] excelheader, List<?> excellist, boolean ishk,
			Set<?> redcells, List<?> mergedcells) {

		HSSFWorkbook book;

		HSSFSheet sheet;

		HSSFRow row;

		HSSFCell cell;
		ByteArrayOutputStream os = null;
		try {
			os = new ByteArrayOutputStream();
			book = new HSSFWorkbook();
			sheet = book.createSheet();
			book.setSheetName(0, "sheet1");

			sheet.setDefaultRowHeightInPoints(20);
			if(excelheader != null){
				for (int i = 0; i < excelheader.length; i++) {
					sheet.setColumnWidth((short) i, (short) 3840);
				}
			}

			HSSFCellStyle styleLeft = ExcelStyle.getLineCellStyleLeft(book);

			HSSFCellStyle styleLeftRed = ExcelStyle.getLineCellStyleLeft(book);

			HSSFCellStyle styleLong = ExcelStyle.getLineCellStyleLong(book);

			HSSFCellStyle styleLongRed = ExcelStyle.getLineCellStyleLongRed(book);

			HSSFCellStyle styleNumber = ExcelStyle.getLineCellStyleNumber(book);

			HSSFCellStyle styleNumberRed = ExcelStyle.getLineCellStyleNumberRed(book);

			HSSFCellStyle styleDate = ExcelStyle.getLineCellStyleDate(book);

			HSSFCellStyle styleDateRed = ExcelStyle.getLineCellStyleDate(book);

			row = sheet.createRow(0);

			if (excelheader != null) {
				for (int i = 0; i < excelheader.length; i++) {

					cell = row.createCell((short) i);
					cell.setCellStyle(getHeaderStyle(book));// 单元格添加样式
					// cell.setEncoding(HSSFCell.ENCODING_UTF_16);

					if (ishk) {
						cell.setCellValue(
								EncodingTranslate.convertString(excelheader[i], Encoding.GB2312, Encoding.BIG5));
					} else {
						cell.setCellValue(excelheader[i]);
					}

				}
			}
			if (excellist != null && excellist.size() != 0) {
				int n = excellist.size();
				if (n > 10000) {
					n = 10000;
				}
				for (int i = 0; i < n; i++) {

					Object[] excelrow = (Object[]) excellist.get(i);

					row = sheet.createRow(i + 1);

					for (int j = 0; j < excelrow.length; j++) {
						cell = row.createCell((short) j);
						if (excelrow[j] instanceof Long) {

							if (redcells.contains(new ExcelCell(i, j)))
								cell.setCellStyle(styleLongRed);
							else
								cell.setCellStyle(styleLong);

							cell.setCellValue(((Long) excelrow[j]).doubleValue());

						} else if (excelrow[j] instanceof BigDecimal) {

							if (((BigDecimal) excelrow[j]).scale() == 0) {
								if (redcells.contains(new ExcelCell(i, j)))
									cell.setCellStyle(styleLongRed);
								else
									cell.setCellStyle(styleLong);
							} else {
								if (redcells.contains(new ExcelCell(i, j)))
									cell.setCellStyle(styleNumberRed);
								else
									cell.setCellStyle(styleNumber);
							}

							cell.setCellValue(((Number) excelrow[j]).doubleValue());

						} else if (excelrow[j] instanceof Number) {

							if (redcells.contains(new ExcelCell(i, j)))
								cell.setCellStyle(styleNumberRed);
							else
								cell.setCellStyle(styleNumber);

							cell.setCellValue(((Number) excelrow[j]).doubleValue());

						} else if (excelrow[j] instanceof Date) {
							if (redcells.contains(new ExcelCell(i, j)))
								cell.setCellStyle(styleDateRed);
							else
								cell.setCellStyle(styleDate);

							cell.setCellValue((Date) excelrow[j]);

						} else {
							if (redcells.contains(new ExcelCell(i, j)))
								cell.setCellStyle(styleLeftRed);
							else
								cell.setCellStyle(styleLeft);

							// cell.setEncoding(HSSFCell.ENCODING_UTF_16);

							if (ishk) {
								cell.setCellValue(EncodingTranslate.convertString(
										ExcelExportUtils.formatString(excelrow[j]), Encoding.GB2312, Encoding.BIG5));
							} else {
								cell.setCellValue(ExcelExportUtils.formatString(excelrow[j]));
							}
						}
					}
				}
			}

			for (int i = 0; i < mergedcells.size(); i++) {
				sheet.addMergedRegion((Region) mergedcells.get(i));
			}

			book.write(os);

		} catch (Exception ex) {

			ex.printStackTrace();

		} finally {
			cell = null;
			row = null;
			sheet = null;
			book = null;

		}
		return os;
	}

	// javax.swing.plaf.synth
	@SuppressWarnings("deprecation")
	public static void exportExcel(String path, int startRow, OutputStream output, List<?> excellist,
			boolean ishk, Set<?> redcells, List<?> mergedcells) {

		HSSFWorkbook book;

		HSSFSheet sheet;

		HSSFRow row;

		HSSFCell cell;
		InputStream is = null;
		try {
			 is = new FileInputStream(path);

			book = new HSSFWorkbook(is);
			sheet = book.getSheet("Sheet1");

			sheet.setDefaultRowHeightInPoints(10);

			HSSFCellStyle styleLeft = ExcelStyle.getLineCellStyleLeft(book);

			HSSFCellStyle styleLeftRed = ExcelStyle.getLineCellStyleLeft(book);

			HSSFCellStyle styleLong = ExcelStyle.getLineCellStyleLong(book);

			HSSFCellStyle styleLongRed = ExcelStyle.getLineCellStyleLongRed(book);

			HSSFCellStyle styleNumber = ExcelStyle.getLineCellStyleNumber(book);

			HSSFCellStyle styleNumberRed = ExcelStyle.getLineCellStyleNumberRed(book);

			HSSFCellStyle styleDate = ExcelStyle.getLineCellStyleDate(book);

			HSSFCellStyle styleDateRed = ExcelStyle.getLineCellStyleDate(book);

			row = sheet.createRow(startRow);
			int len = startRow;
			for (int i = 0; i < excellist.size(); i++) {

				Object[] excelrow = (Object[]) excellist.get(i);

				row = sheet.createRow(len);

				for (int j = 0; j < excelrow.length; j++) {
					cell = row.createCell((short) j);
					if (excelrow[j] instanceof Long) {

						if (redcells.contains(new ExcelCell(i, j)))
							cell.setCellStyle(styleLongRed);
						else
							cell.setCellStyle(styleLong);

						cell.setCellValue(((Long) excelrow[j]).doubleValue());

					} else if (excelrow[j] instanceof BigDecimal) {

						if (((BigDecimal) excelrow[j]).scale() == 0) {
							if (redcells.contains(new ExcelCell(i, j)))
								cell.setCellStyle(styleLongRed);
							else
								cell.setCellStyle(styleLong);
						} else {
							if (redcells.contains(new ExcelCell(i, j)))
								cell.setCellStyle(styleNumberRed);
							else
								cell.setCellStyle(styleNumber);
						}

						cell.setCellValue(((Number) excelrow[j]).doubleValue());

					} else if (excelrow[j] instanceof Number) {

						if (redcells.contains(new ExcelCell(i, j)))
							cell.setCellStyle(styleNumberRed);
						else
							cell.setCellStyle(styleNumber);

						cell.setCellValue(((Number) excelrow[j]).doubleValue());

					} else if (excelrow[j] instanceof Date) {
						if (redcells.contains(new ExcelCell(i, j)))
							cell.setCellStyle(styleDateRed);
						else
							cell.setCellStyle(styleDate);

						cell.setCellValue((Date) excelrow[j]);

					} else {
						if (redcells.contains(new ExcelCell(i, j)))
							cell.setCellStyle(styleLeftRed);
						else
							cell.setCellStyle(styleLeft);

						// cell.setEncoding(HSSFCell.ENCODING_UTF_16);

						if (ishk) {
							cell.setCellValue(EncodingTranslate.convertString(
									ExcelExportUtils.formatString(excelrow[j]), Encoding.GB2312, Encoding.BIG5));
						} else {
							cell.setCellValue(ExcelExportUtils.formatString(excelrow[j]));
						}

					}

				}
				len++;

			}

			for (int i = 0; i < mergedcells.size(); i++) {
				sheet.addMergedRegion((Region) mergedcells.get(i));
			}

			book.write(output);

			is.close();
			output.close();

		} catch (Exception ex) {

			ex.printStackTrace();

		} finally {
			if(is != null) {
				try {
					is.close();
					output.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			cell = null;
			row = null;
			sheet = null;
			book = null;

		}
	}

	// javax.swing.plaf.synth
	@SuppressWarnings("deprecation")
	public static void exportExcel(String path, int startRow, int startCol, OutputStream output,
			List<?> excellist, boolean ishk, Set<?> redcells, List<?> mergedcells) {

		HSSFWorkbook book;

		HSSFSheet sheet;

		HSSFRow row;

		HSSFCell cell;

		try {
			InputStream is = new FileInputStream(path);

			book = new HSSFWorkbook(is);
			sheet = book.getSheet("Sheet1");

			sheet.setDefaultRowHeightInPoints(10);

			HSSFCellStyle styleLeft = ExcelStyle.getLineCellStyleLeft(book);

			HSSFCellStyle styleLeftRed = ExcelStyle.getLineCellStyleLeft(book);

			HSSFCellStyle styleLong = ExcelStyle.getLineCellStyleLong(book);

			HSSFCellStyle styleLongRed = ExcelStyle.getLineCellStyleLongRed(book);

			HSSFCellStyle styleNumber = ExcelStyle.getLineCellStyleNumber(book);

			HSSFCellStyle styleNumberRed = ExcelStyle.getLineCellStyleNumberRed(book);

			HSSFCellStyle styleDate = ExcelStyle.getLineCellStyleDate(book);

			HSSFCellStyle styleDateRed = ExcelStyle.getLineCellStyleDate(book);

			row = sheet.createRow(startRow);
			int len = startRow;
			for (int i = 0; i < excellist.size(); i++) {

				Object[] excelrow = (Object[]) excellist.get(i);

				row = sheet.createRow(len);

				for (int j = 0; j < excelrow.length; j++) {
					cell = row.createCell((short) startCol);
					if (excelrow[j] instanceof Long) {

						if (redcells.contains(new ExcelCell(i, j)))
							cell.setCellStyle(styleLongRed);
						else
							cell.setCellStyle(styleLong);

						cell.setCellValue(((Long) excelrow[j]).doubleValue());

					} else if (excelrow[j] instanceof BigDecimal) {

						if (((BigDecimal) excelrow[j]).scale() == 0) {
							if (redcells.contains(new ExcelCell(i, j)))
								cell.setCellStyle(styleLongRed);
							else
								cell.setCellStyle(styleLong);
						} else {
							if (redcells.contains(new ExcelCell(i, j)))
								cell.setCellStyle(styleNumberRed);
							else
								cell.setCellStyle(styleNumber);
						}

						cell.setCellValue(((Number) excelrow[j]).doubleValue());

					} else if (excelrow[j] instanceof Number) {

						if (redcells.contains(new ExcelCell(i, j)))
							cell.setCellStyle(styleNumberRed);
						else
							cell.setCellStyle(styleNumber);

						cell.setCellValue(((Number) excelrow[j]).doubleValue());

					} else if (excelrow[j] instanceof Date) {
						if (redcells.contains(new ExcelCell(i, j)))
							cell.setCellStyle(styleDateRed);
						else
							cell.setCellStyle(styleDate);

						cell.setCellValue((Date) excelrow[j]);

					} else {
						if (redcells.contains(new ExcelCell(i, j)))
							cell.setCellStyle(styleLeftRed);
						else
							cell.setCellStyle(styleLeft);

						// cell.setEncoding(HSSFCell.ENCODING_UTF_16);

						if (ishk) {
							cell.setCellValue(EncodingTranslate.convertString(
									ExcelExportUtils.formatString(excelrow[j]), Encoding.GB2312, Encoding.BIG5));
						} else {
							cell.setCellValue(ExcelExportUtils.formatString(excelrow[j]));
						}

					}

				}
				len++;

			}

			for (int i = 0; i < mergedcells.size(); i++) {
				sheet.addMergedRegion((Region) mergedcells.get(i));
			}

			book.write(output);

			is.close();
			output.close();

		} catch (Exception ex) {

			ex.printStackTrace();

		}
	}

	/**
	 * 传参文件名构造
	 * 
	 * @param fileName
	 * @return
	 */
	private static String getFileName(String fileName) {
		StringBuffer str = new StringBuffer(fileName);
		str.append("xls");
		return str.toString();
	}

	public static final String EMPTY = "";

	public static String formatString(Object val) {
		if (val == null)
			return EMPTY;

		if (val instanceof Date)
			return formatDate((Date) val);

		return StringUtils.stripToEmpty(val.toString());

	}

	public static String formatDate(Date val) {

		if (val == null)
			return "";

		return DateFormatUtils.ISO_DATE_FORMAT.format(val);

	}

	public static HSSFCellStyle getHeaderStyle(HSSFWorkbook workBook) {
		HSSFFont font = workBook.createFont();
		font.setColor(HSSFFont.COLOR_NORMAL);
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		font.setFontHeightInPoints((short) 10);
		font.setFontName("宋体");
		HSSFCellStyle cellStyle = workBook.createCellStyle();// 创建格式
		cellStyle.setFont(font);
		cellStyle.setWrapText(true);// 自动换行 强制换行 cell.setCellValue(new
									// HSSFRichTextString("hello/r/n world!"));
		cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		cellStyle.setBorderTop(HSSFCellStyle.BORDER_DOTTED);
		cellStyle.setBorderBottom(HSSFCellStyle.BORDER_DOTTED);
		cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
		return cellStyle;
	}
}
