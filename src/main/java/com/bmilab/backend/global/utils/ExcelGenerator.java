package com.bmilab.backend.global.utils;

import lombok.val;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

@Component
public class ExcelGenerator {
    private static final int HEADER_ROW = 0;
    public static final String EXCEL_MEDIA_TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

    public ByteArrayInputStream generateBy(String[] headerTitles, List<ExcelRow> rows) throws IOException {
        SXSSFWorkbook workbook = generateWorkbook(headerTitles, rows);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.close();

        return new ByteArrayInputStream(outputStream.toByteArray());
    }

    public File generateExcelFile(String[] headerTitles, List<ExcelRow> rows, String fileName) throws IOException {
        SXSSFWorkbook workbook = generateWorkbook(headerTitles, rows);
        File tempFile = new File(System.getProperty("java.io.tmpdir"), fileName);

        try (FileOutputStream outputStream = new FileOutputStream(tempFile)) {
            workbook.write(outputStream);
        }

        workbook.close();

        return tempFile;
    }

    private SXSSFWorkbook generateWorkbook(String[] headerTitles, List<ExcelRow> rows) {
        SXSSFWorkbook workbook = new SXSSFWorkbook();
        SXSSFSheet sheet = workbook.createSheet();

        resizeSheetWidth(sheet);
        styleHeaders(workbook, sheet, headerTitles);
        fillData(sheet, rows);

        return workbook;
    }

    private void styleHeaders(SXSSFWorkbook workbook, SXSSFSheet sheet, String[] headerTitles) {
        Font headerFont = workbook.createFont();
        headerFont.setColor(IndexedColors.WHITE.getIndex());
        headerFont.setBold(true);

        CellStyle headerCellStyle = workbook.createCellStyle();
        headerCellStyle.setFont(headerFont);
        headerCellStyle.setFillBackgroundColor(IndexedColors.DARK_BLUE.getIndex());

        SXSSFRow headerRow = sheet.createRow(HEADER_ROW);

        for (int i = 0; i < headerTitles.length; i++) {
            SXSSFCell cell = headerRow.createCell(i);

            cell.setCellValue(headerTitles[i]);
            cell.setCellStyle(headerCellStyle);
        }
    }

    private void resizeSheetWidth(SXSSFSheet sheet) {
        sheet.setColumnWidth(0, 256 * 20);
        sheet.setColumnWidth(1, 256 * 20);
        sheet.setColumnWidth(2, 256 * 30);
        sheet.setColumnWidth(3, 256 * 50);
        sheet.setColumnWidth(4, 256 * 100);
        sheet.setColumnWidth(5, 256 * 200);
    }

    private void fillData(SXSSFSheet sheet, List<ExcelRow> rows) {
        for (int i = 0; i < rows.size(); i++) {
            SXSSFRow row = sheet.createRow(i + 1);
            List<String> properties = rows.get(i).data();

            for (int j = 0; j < properties.size(); j++) {
                row.createCell(j).setCellValue(properties.get(j));
            }
        }
    }
}
