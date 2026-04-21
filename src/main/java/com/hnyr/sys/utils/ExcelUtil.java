package com.hnyr.sys.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFFont;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ExcelUtil {

    /**
     * 合并各党员个，并给单元格赋值
     * 无格式
     *
     * @param wb
     * @param value    单元格赋值
     * @param row      合并的第几行
     * @param startRow 合并开始行
     * @param endRow   合并结束行
     * @param startCol 合并开始列
     * @param endCol   合并结束列
     * @return
     */
    private SXSSFWorkbook writeMergeRow(SXSSFWorkbook wb, String sheetName, String value, int row, int startRow, int endRow, int startCol, int endCol) {
        Row rowValue = wb.getSheet(sheetName).createRow(row);
        //标题
        CellRangeAddress address = new CellRangeAddress(startRow, endRow, startCol, endCol);
        wb.getSheetAt(0).addMergedRegion(address);
        Cell cell = rowValue.createCell(0);
        cell.setCellValue(value);
        return wb;
    }

    /**
     * 合并各党员个，并给单元格赋值
     * 无格式
     *
     * @param wb
     * @param value    单元格赋值
     * @param rowNum      合并的第几行
     * @param cellNum      合并的第几列
     * @param startRow 合并开始行
     * @param endRow   合并结束行
     * @param startCol 合并开始列
     * @param endCol   合并结束列
     * @return
     */
    public static SXSSFWorkbook writeMergeCell(SXSSFWorkbook wb, String sheetName, String value, int rowNum,int cellNum, int startRow, int endRow, int startCol, int endCol) {
        Sheet sh = wb.getSheet(sheetName);
        Row rowValue = sh.getRow(rowNum);
        if(rowValue==null){
            rowValue=sh.createRow(rowNum);
        }
        //标题
        CellRangeAddress address = new CellRangeAddress(startRow, endRow, startCol, endCol);
        sh.addMergedRegion(address);

        CellStyle style = getMergeStyle(wb);
        Cell cell = rowValue.createCell(cellNum);
        cell.setCellValue(value);
        cell.setCellStyle(style);
        return wb;
    }

    /**
     * @param wb
     * @param isMerge  是否合并行列
     * @param startRow 合并开始行
     * @param endRow   合并结束行
     * @param startCol 合并开始列
     * @param endCol   合并结束列
     * @return
     */
    public static SXSSFWorkbook writeTitle(SXSSFWorkbook wb, String sheetName, String titleName, Boolean isMerge, Boolean isStyle, int startRow, int endRow, int startCol, int endCol) {
        Sheet sh = null;
        if (wb == null) {
            wb = new SXSSFWorkbook(100);
            sh = wb.createSheet(sheetName); // 建立新的sheet对象
        } else {
            sh = wb.getSheet(sheetName);
            if(sh == null){
                sh = wb.createSheet(sheetName);
            }
        }
        Row row = sh.createRow(0);
        //标题
        if (isMerge) {
            CellRangeAddress address = new CellRangeAddress(startRow, endRow, startCol, endCol);
            sh.addMergedRegion(address);
        }
        CellStyle style = getTopStyle(wb);
        Cell cell = row.createCell(0);
        cell.setCellStyle(style);
        if (isStyle) {
            cell.setCellStyle(style);
        }
        cell.setCellValue(titleName);
        return wb;
    }

    /**
     * 写表头
     *
     * @param wb
     * @param row       第几行
     * @param titleName 表头列表
     * @return
     */
    public static SXSSFWorkbook writeHead(SXSSFWorkbook wb, String sheetName, int row, List<String> titleName, Boolean isStyle) {
        Sheet sheet = null;
        if (wb == null) {
            wb = new SXSSFWorkbook(100);
            sheet = wb.createSheet(sheetName); // 建立新的sheet对象
        } else {
            sheet = wb.getSheet(sheetName);
            if(sheet == null){
                sheet = wb.createSheet(sheetName);
            }
        }

        Row rowValue = sheet.createRow(row);
        //循环生成表头
        CellStyle bodyStyle = getHeadStyle(wb);
        Cell cell = null;
        for (int i = 0; i < titleName.size(); i++) {
            cell = rowValue.createCell(i);
            if (isStyle) {
                cell.setCellStyle(bodyStyle);
            }
            cell.setCellValue(titleName.get(i));
            //sheet.autoSizeColumn((short) i,true);
        }
        return wb;
    }

    /**
     * 写内容
     *
     * @param wb
     * @param sheetName
     * @param row       第几行开始
     * @param bodyList  内容列表
     * @return
     */
    public static SXSSFWorkbook writeBody(SXSSFWorkbook wb, String sheetName, int row, List<Map<String, Object>> bodyList, Boolean isStyle) {
        Sheet sheet = wb.getSheet(sheetName);

        Row rowValue = null;
        Cell cell = null;
        Iterator<String> keys = null;
        String key = null;
        String value = null;
        CellStyle bodyStyle = getBodyStyle(wb);
        for (Map<String, Object> map : bodyList) {
            rowValue = sheet.createRow(row);
            keys = map.keySet().iterator();
            int i = 0;
            while (keys.hasNext()) {
                cell = rowValue.createCell(i);
                key = keys.next();
                value = map.get(key) == null ? "" : String.valueOf(map.get(key));
                if (isStyle) {
                    cell.setCellStyle(bodyStyle);
                }
                cell.setCellValue(StringUtils.isNotBlank(value) ? value : "");
                i++;
//                sheet.autoSizeColumn((short) i,true);
            }
            row++;

        }
        return wb;
    }

    /**
     * 写内容
     *
     * @param wb
     * @param sheetName
     * @param row       第几行开始
     * @param bodyList  内容列表
     * @return
     */
    public static SXSSFWorkbook writeBody(SXSSFWorkbook wb, String sheetName, int row, List<String> headList, List<Map<String, Object>> bodyList, Boolean isStyle) {
        Sheet sheet = wb.getSheet(sheetName);
        CellStyle bodyStyle = getBodyStyle(wb);
        for (Map<String, Object> map : bodyList) {
            Row rowValue = sheet.createRow(row);
            int i = 0;
            for (String key : headList) {
                Cell cell = rowValue.createCell(i);
                String value = map.get(key) == null ? "" : String.valueOf(map.get(key));
                if (isStyle) {
                    cell.setCellStyle(bodyStyle);
                }
                cell.setCellValue(StringUtils.isNotBlank(value) ? value : "");
                i++;
            }
            row++;
        }
        return wb;
    }


    /**
     * @param wb
     * @param sheetName
     * @param footValue
     * @param isMerge
     * @param isStyle
     * @param row
     * @param startRow
     * @param endRow
     * @param startCol
     * @param endCol
     * @return
     */
    public static SXSSFWorkbook writeFoot(SXSSFWorkbook wb, String sheetName, String footValue, Boolean isMerge, Boolean isStyle, int row, int startRow, int endRow, int startCol, int endCol) {
        Sheet sh = wb.getSheet(sheetName);
        Row rowValue = sh.createRow(row);
        //标题
        if (isMerge) {
            CellRangeAddress address = new CellRangeAddress(startRow, endRow, startCol, endCol);
            sh.addMergedRegion(address);
        }
        CellStyle style = getTopStyle(wb);
        Cell cell = rowValue.createCell(0);
        if (isStyle) {
            cell.setCellStyle(style);
        }
        cell.setCellValue(footValue);
        return wb;
    }

    private static CellStyle getTopStyle(SXSSFWorkbook workbook) {
        // 设置字体
        Font font = workbook.createFont();
        // 设置字体大小
        font.setFontHeightInPoints((short) 18);
        // 字体加粗
//        font.setBold(HSSFFont.BOLDWEIGHT_BOLD);
        // 设置字体名字
        font.setFontName("Courier New");
        // 设置样式;
        CellStyle style = workbook.createCellStyle();
        // 设置底边框;
        //style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        // 设置底边框颜色;
        //style.setBottomBorderColor(HSSFColor.BLACK.index);
        // 设置左边框;
        //style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        // 设置左边框颜色;
        //style.setLeftBorderColor(HSSFColor.BLACK.index);
        // 设置右边框;
        //style.setBorderRight(HSSFCellStyle.BORDER_THIN);
        // 设置右边框颜色;
        //style.setRightBorderColor(HSSFColor.BLACK.index);
        // 设置顶边框;
        //style.setBorderTop(HSSFCellStyle.BORDER_THIN);
        // 设置顶边框颜色;
        //style.setTopBorderColor(HSSFColor.BLACK.index);
        // 在样式用应用设置的字体;
        style.setFont(font);
        // 设置自动换行;
        style.setWrapText(false);
        // 设置水平对齐的样式为居中对齐;
        style.setAlignment(HorizontalAlignment.CENTER);
        // 设置垂直对齐的样式为居中对齐;
        style.setVerticalAlignment(VerticalAlignment.CENTER);

        return style;
    }

    private static CellStyle getMergeStyle(SXSSFWorkbook workbook) {
        // 设置字体
        Font font = workbook.createFont();
        // 设置字体大小
//        font.setFontHeightInPoints((short) 18);
        // 字体加粗
        font.setBold(Boolean.TRUE);
        // 设置字体名字
//        font.setFontName("Courier New");
        // 设置样式;
        CellStyle style = workbook.createCellStyle();
        // 设置底边框;
        //style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        // 设置底边框颜色;
        //style.setBottomBorderColor(HSSFColor.BLACK.index);
        // 设置左边框;
        //style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        // 设置左边框颜色;
        //style.setLeftBorderColor(HSSFColor.BLACK.index);
        // 设置右边框;
        //style.setBorderRight(HSSFCellStyle.BORDER_THIN);
        // 设置右边框颜色;
        //style.setRightBorderColor(HSSFColor.BLACK.index);
        // 设置顶边框;
        //style.setBorderTop(HSSFCellStyle.BORDER_THIN);
        // 设置顶边框颜色;
        //style.setTopBorderColor(HSSFColor.BLACK.index);
        // 在样式用应用设置的字体;
        style.setFont(font);
        // 设置自动换行;
        style.setWrapText(false);
        // 设置水平对齐的样式为居中对齐;
        style.setAlignment(HorizontalAlignment.CENTER);
        // 设置垂直对齐的样式为居中对齐;
        style.setVerticalAlignment(VerticalAlignment.CENTER);

        return style;
    }
    // 设置表头的单元格样式
    public static CellStyle getHeadStyle(SXSSFWorkbook workbook) {
        // 创建单元格样式
        CellStyle cellStyle = workbook.createCellStyle();

        // 设置单元格的背景颜色为淡蓝色
        //cellStyle.setFillForegroundColor(HSSFColor.PALE_BLUE.index);
        // 设置填充字体的样式
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        // 设置单元格居中对齐
        cellStyle.setAlignment(HorizontalAlignment.CENTER);

        // 设置单元格垂直居中对齐
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        // cellStyle.setShrinkToFit(true);
        // 创建单元格内容显示不下时自动换行
        //cellStyle.setWrapText(true);

        // 设置单元格字体样式
        XSSFFont font = (XSSFFont) workbook.createFont();
        //  font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);// 这是字体加粗
        font.setFontName("宋体");// 设置字体的样式
        font.setFontHeight(12);// 设置字体的大小
        cellStyle.setFont(font);// 将字体填充到表格中去

        // 设置单元格边框为细线条（上下左右）
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setBorderTop(BorderStyle.THIN);

        return cellStyle;

    }

    // 设置表体的单元格样式
    public static CellStyle getBodyStyle(SXSSFWorkbook workbook) {
        // 创建单元格样式
        CellStyle cellStyle = workbook.createCellStyle();
        // 设置单元格居中对齐
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        // 设置单元格居中对齐
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        // 创建单元格内容不显示自动换行
        //cellStyle.setWrapText(true);
        // 设置单元格字体样式
        XSSFFont font = (XSSFFont) workbook.createFont();
        //   font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);// 这是字体加粗
        font.setFontName("宋体");// 设置字体
        font.setFontHeight(10);// 设置字体的大小
        cellStyle.setFont(font);// 将字体添加到表格中去

        // 设置单元格边框为细线条
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setBorderTop(BorderStyle.THIN);

        return cellStyle;

    }

    public static SXSSFWorkbook writeTitleNew(SXSSFWorkbook wb, String sheetName, String titleName, Boolean isMerge, Boolean isStyle, int startRow, int endRow, int startCol, int endCol) {
        Sheet sh = null;
        sh = wb.createSheet(sheetName); // 建立新的sheet对象

        Row row = sh.createRow(0);
        //标题
        if (isMerge) {
            CellRangeAddress address = new CellRangeAddress(startRow, endRow, startCol, endCol);
            sh.addMergedRegion(address);
        }
        CellStyle style = getTopStyle(wb);
        Cell cell = row.createCell(0);
        cell.setCellStyle(style);
        if (isStyle) {
            cell.setCellStyle(style);
        }
        cell.setCellValue(titleName);
        return wb;
    }


    /**
     * 写内容
     *
     * @param wb
     * @param sheetName
     * @param row       第几行开始
     * @param bodyList  内容列表
     * @return
     */
    public static SXSSFWorkbook writeBodyWrap(SXSSFWorkbook wb, String sheetName, int row, List<String> headList, List<Map<String, Object>> bodyList, Boolean isStyle) {
        Sheet sheet = wb.getSheet(sheetName);
        CellStyle bodyStyle = getBodyStyle(wb);
        for (Map<String, Object> map : bodyList) {
            Row rowValue = sheet.createRow(row);
            int i = 0;
            for (String key : headList) {
                Cell cell = rowValue.createCell(i);

                String value = map.get(key) == null ? "" : String.valueOf(map.get(key));
                if (isStyle ) {
                    bodyStyle.setWrapText(true);  //关键
                    cell.setCellStyle(bodyStyle);
                }
                cell.setCellValue(StringUtils.isNotBlank(value) ? value : "");
                i++;
            }
            row++;
        }
        return wb;
    }

}
