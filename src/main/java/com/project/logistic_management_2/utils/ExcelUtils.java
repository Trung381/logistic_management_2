package com.project.logistic_management_2.utils;

import com.project.logistic_management_2.dto.report.ReportDetailSalaryDTO;
import com.project.logistic_management_2.dto.salary.SalaryDTO;
import com.project.logistic_management_2.dto.schedule.ScheduleSalaryDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.project.logistic_management_2.utils.FileFactory.PATH_TEMPLATE;

@Slf4j
@Component
public class ExcelUtils {

    //export config
    public static ByteArrayInputStream export(List<?> list, String fileName, ExportConfig exportConfig) throws Exception {

        XSSFWorkbook xssfWorkbook = new XSSFWorkbook();

        File file;
        FileInputStream fileInputStream;
        try {
            file = ResourceUtils.getFile(PATH_TEMPLATE + fileName);
            fileInputStream = new FileInputStream(file);
        } catch (Exception e) {
            log.info("FILE NOT FOUND");
            file = FileFactory.createFile(fileName, xssfWorkbook);
            fileInputStream = new FileInputStream(file);
        }

        processInsertData(xssfWorkbook, list, exportConfig);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        xssfWorkbook.write(outputStream);

        outputStream.close();
        fileInputStream.close();

        log.info("done");
        return new ByteArrayInputStream(outputStream.toByteArray());
    }

    private static void processInsertData(XSSFWorkbook workbook, Object data, ExportConfig exportConfig) throws Exception {
        if (data instanceof List<?> list && !list.isEmpty()) {
            Object firstElement = list.get(0);

            if (firstElement instanceof ReportDetailSalaryDTO) {
                // Xử lý đặc biệt cho ReportDetailSalaryDTO
                processReportDetailSalary(workbook, (ReportDetailSalaryDTO) firstElement);
            } else {
                // Xử lý chung cho danh sách các đối tượng khác
                processGenericList(workbook, list, exportConfig);
            }
        } else {
            throw new IllegalArgumentException("Unsupported data type for export: " + data.getClass());
        }
    }

    private static void processReportDetailSalary(XSSFWorkbook workbook, ReportDetailSalaryDTO reportDetail) throws Exception {
        ExportConfig salaryConfig = ExportConfig.createExportConfig(SalaryDTO.class, 0, 1);
        ExportConfig schedulesConfig = ExportConfig.createExportConfig(ScheduleSalaryDTO.class, 1, 1);

        // Xuất dữ liệu Salary vào Sheet 1
        Sheet salarySheet = workbook.createSheet("Salary");
        insertFieldNameAsTitleToWorkbook(salaryConfig.getCellExportConfigList(), salarySheet, createTitleCellStyle(workbook));
        insertDataToWorkbook(workbook, salaryConfig, List.of(reportDetail.getSalary()), createDataCellStyle(workbook));

        // Xuất danh sách Schedule vào Sheet 2
        Sheet schedulesSheet = workbook.createSheet("Schedules");
        insertFieldNameAsTitleToWorkbook(schedulesConfig.getCellExportConfigList(), schedulesSheet, createTitleCellStyle(workbook));
        insertDataToWorkbook(workbook, schedulesConfig, reportDetail.getSchedules(), createDataCellStyle(workbook));
    }

    private static void processGenericList(XSSFWorkbook workbook, List<?> list, ExportConfig exportConfig) {
        Sheet sheet = workbook.createSheet("sheet1");
        sheet.createFreezePane(4, 2, 4, 2);

        XSSFCellStyle titleCellStyle = createTitleCellStyle(workbook);
        XSSFCellStyle dataCellStyle = createDataCellStyle(workbook);

        insertFieldNameAsTitleToWorkbook(exportConfig.getCellExportConfigList(), sheet, titleCellStyle);
        insertDataToWorkbook(workbook, exportConfig, list, dataCellStyle);
    }

    private static <T> void insertDataToWorkbook(Workbook workbook, ExportConfig exportConfig, List<T> datas,
                                                 XSSFCellStyle dataCellStyle) {
        int startRowIndex = exportConfig.getStartRow();//2

        int sheetIndex = exportConfig.getSheetIndex();//1

        Class clazz = exportConfig.getDataClazz();

        List<CellConfig> cellConfigs = exportConfig.getCellExportConfigList();

        Sheet sheet = workbook.getSheetAt(sheetIndex);

        int currentRowIndex = startRowIndex;

        for (T data : datas) {
            Row currentRow = sheet.getRow(currentRowIndex);
            if (ObjectUtils.isEmpty(currentRow)) {
                currentRow = sheet.createRow(currentRowIndex);
            }
            //insert data to row
            insertDataToCell(data, currentRow, cellConfigs, clazz, sheet, dataCellStyle);
            currentRowIndex++;
        }
    }

    private static <T> void insertFieldNameAsTitleToWorkbook(List<CellConfig> cellConfigs,
                                                             Sheet sheet,
                                                             XSSFCellStyle titleCellStyle) {

        //title -> first row of excel -> get top row
        int currentRow = sheet.getTopRow();
        Row row = sheet.createRow(currentRow);
        int i = 0;

        //resize fix text in each cell
        sheet.autoSizeColumn(currentRow);

        //insert field name to cell
        for (CellConfig cellConfig : cellConfigs) {
            Cell currentCell = row.createCell(i);
            String headerName = cellConfig.getHeaderName();
            currentCell.setCellValue(headerName);
            currentCell.setCellStyle(titleCellStyle);
            sheet.autoSizeColumn(i);
            i++;
        }

    }

    private static <T> void insertDataToCell(T data, Row currentRow, List<CellConfig> cellConfigs,
                                             Class clazz, Sheet sheet, XSSFCellStyle dataStyle) {

        for (CellConfig cellConfig : cellConfigs) {
            Cell currentCell = currentRow.getCell(cellConfig.getColumnIndex());
            if (ObjectUtils.isEmpty(currentCell)) {
                currentCell = currentRow.createCell(cellConfig.getColumnIndex());
            }

            String cellValue = getCellValue(data, cellConfig, clazz);

            currentCell.setCellValue(cellValue);
            sheet.autoSizeColumn(cellConfig.getColumnIndex());
            currentCell.setCellStyle(dataStyle);
        }

    }

    private static <T> String getCellValue(T data, CellConfig cellConfig, Class clazz) {
        String fieldName = cellConfig.getFieldName();
        try {
            Field field = getDeclaredField(clazz, fieldName);
            if (!ObjectUtils.isEmpty(field)) {
                field.setAccessible(true);
                Object value = field.get(data);

                // Nếu là Float hoặc Double, định dạng thành chuỗi
                if (value instanceof Float || value instanceof Double) {
                    DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");
                    return decimalFormat.format(value);
                }
                return value != null ? value.toString() : "";
            }
            return "";
        } catch (Exception e) {
            log.error("Error retrieving value for field: " + fieldName, e);
            return "";
        }
    }


    private static Field getDeclaredField(Class clazz, String fieldName) {
        if (ObjectUtils.isEmpty(clazz) || ObjectUtils.isEmpty(fieldName)) {
            return null;
        }
        do {
            try {
                Field field = clazz.getDeclaredField(fieldName);
                field.setAccessible(true);
                return field;
            } catch (Exception e) {
                log.info("" + e);
            }
        } while ((clazz = clazz.getSuperclass()) != null);

        return null;
    }

    public static <T> List<T> getImportData(Workbook workbook, ImportConfig importConfig) {
        List<T> list = new ArrayList<>();

        List<CellConfig> cellConfigs = importConfig.getCellImportConfigs();

        int countSheet = 0;

        for (Sheet sheet : workbook) {
            if (countSheet != importConfig.getSheetIndex()) {
                countSheet++;
                continue;
            }

            int countRow = 0;
            for (Row row : sheet) {
                if (countRow < importConfig.getStartRow()) {
                    countRow++;
                    continue;
                }
                T rowData = getRowData(row, cellConfigs, importConfig.getDataClazz());
                list.add(rowData);
                countRow++;
            }
            countSheet++;
        }
        return list;
    }

    private static <T> T getRowData(Row row, List<CellConfig> cellConfigs, Class dataClazz) {
        T instance = null;
        try {
            instance = (T) dataClazz.getDeclaredConstructor().newInstance();

            for (int i = 0; i < cellConfigs.size(); i++) {
                CellConfig currentCell = cellConfigs.get(i);
                try {
                    Field field = getDeclaredField(dataClazz, currentCell.getFieldName());

                    Cell cell = row.getCell(currentCell.getColumnIndex());
                    if (!ObjectUtils.isEmpty(cell)) {
                        cell.setCellType(CellType.STRING);

                        Object cellValue = cell.getStringCellValue();

                        setFieldValue(instance, field, cellValue);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return instance;

    }

    private static <T> void setFieldValue(Object instance, Field field, Object cellValue) {
        if (ObjectUtils.isEmpty(instance) || ObjectUtils.isEmpty(field)) {
            return;
        }

        Class clazz = field.getType();

        Object valueConverted = parseValueByClass(clazz, cellValue);

        field.setAccessible(true);

        try {
            field.set(instance, valueConverted);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Object parseValueByClass(Class clazz, Object cellValue) {
        if (ObjectUtils.isEmpty(cellValue) || ObjectUtils.isEmpty(clazz)) {
            return null;
        }
        String clazzName = clazz.getSimpleName();

        switch (clazzName) {
            case "char" -> cellValue = parseChar(cellValue);
            case "String" -> cellValue = cellValue.toString().trim();
            case "boolean", "Boolean" -> cellValue = parseBoolean(cellValue);
            case "byte", "Byte" -> cellValue = parseByte(cellValue);
            case "short", "Short" -> cellValue = parseShort(cellValue);
            case "int", "Integer" -> cellValue = parseInt(cellValue);
            case "long", "Long" -> cellValue = parseLong(cellValue);
            case "float", "Float" -> cellValue = parseFloat(cellValue);
            case "double", "Double" -> cellValue = parseDouble(cellValue);
            case "Date" -> cellValue = parseDate(cellValue);
            case "Instant" -> cellValue = parseInstant(cellValue);
            case "Enum" -> cellValue = parseEnum(cellValue, clazz);
            case "Map" -> cellValue = parseMap(cellValue);
            case "BigDecimal" -> cellValue = parseBigDecimal(cellValue);
        }
        return cellValue;
    }

    private static Object parseChar(Object value) {
        return ObjectUtils.isEmpty(value) ? null : (char) value;
    }

    private static Object parseBoolean(Object value) {
        return ObjectUtils.isEmpty(value) ? null : (Boolean) value;
    }

    private static Object parseMap(Object value) {
        if (ObjectUtils.isEmpty(value)) {
            return null;
        }
        return value;
    }

    private static Object parseEnum(Object value, Class clazz) {
        if (ObjectUtils.isEmpty(value)) {
            return null;
        }

        String valueStr = value.toString().trim();
        return Enum.valueOf(clazz, valueStr);
    }

    private static Date parseDate(Object value) {
        String[] formatsDate = {"yyyy-MM-dd HH:mm:ss", "dd/MM/yyyy", "dd/MM/yyyy h:mm:ss a", "yyyy-MM-dd"};

        if (ObjectUtils.isEmpty(value)) {
            return null;
        }

        String dateStr = value.toString().trim();
        if (dateStr.matches("\\d+(\\.\\d+)?")) {
            try {
                double excelDate = Double.parseDouble(dateStr);
                return convertExcelDateToJavaDate(excelDate);
            } catch (NumberFormatException e) {
                e.printStackTrace();
                return null;
            }
        }

        // Thử parse theo các định dạng ngày tháng đã chỉ định
        for (String format : formatsDate) {
            try {
                DateFormat dateFormat = new SimpleDateFormat(format);
                dateFormat.setLenient(false); // Bắt buộc định dạng chính xác
                return dateFormat.parse(dateStr);
            } catch (Exception e) {
                // Bỏ qua lỗi, thử định dạng tiếp theo
            }
        }
        return null;
    }

    private static Object parseInstant(Object value) {
        return ObjectUtils.isEmpty(value) ? null : parseDate(value).toInstant();
    }

    private static Double parseDouble(Object value) {
        if (ObjectUtils.isEmpty(value)) {
            return null;
        }

        try {
            return Double.parseDouble(value.toString());
        } catch (Exception e) {
            return null;
        }
    }

    private static Object parseFloat(Object value) {
        if (ObjectUtils.isEmpty(value)) {
            return null;
        }

        Double doubleValue = parseDouble(value);
        return ObjectUtils.isEmpty(doubleValue) ? null : doubleValue.floatValue();
    }


    private static Object parseLong(Object value) {
        if (ObjectUtils.isEmpty(value)) {
            return null;
        }

        Double longDoubleValue = parseDouble(value);
        return ObjectUtils.isEmpty(longDoubleValue) ? null : longDoubleValue.longValue();
    }

    private static Object parseShort(Object value) {
        if (ObjectUtils.isEmpty(value)) {
            return null;
        }

        Double shortDoubleValue = parseDouble(value);
        return ObjectUtils.isEmpty(shortDoubleValue) ? null : shortDoubleValue.shortValue();
    }

    private static Object parseInt(Object value) {
        if (ObjectUtils.isEmpty(value)) {
            return null;
        }

        Double intDoubleValue = parseDouble(value);
        return ObjectUtils.isEmpty(intDoubleValue) ? null : intDoubleValue.intValue();
    }

    private static Object parseBigDecimal(Object value) {
        if (ObjectUtils.isEmpty(value)) {
            return null;
        }

        try {
            return BigDecimal.valueOf(Double.valueOf(value.toString()));
        } catch (Exception e) {
            return null;
        }
    }

    private static Object parseByte(Object value) {
        if (ObjectUtils.isEmpty(value)) {
            return null;
        }

        Double byteDoubleValue = parseDouble(value);
        return ObjectUtils.isEmpty(byteDoubleValue) ? null : byteDoubleValue.byteValue();
    }

    private static XSSFCellStyle createTitleCellStyle(XSSFWorkbook xssfWorkbook) {
        // Tạo font tiêu đề
        XSSFFont titleFont = xssfWorkbook.createFont();
        titleFont.setFontName("Arial");
        titleFont.setBold(true);
        titleFont.setFontHeightInPoints((short) 14);

        // Tạo style và áp dụng font cho tiêu đề
        XSSFCellStyle titleCellStyle = xssfWorkbook.createCellStyle();
        titleCellStyle.setAlignment(HorizontalAlignment.CENTER);
        titleCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        titleCellStyle.setFillForegroundColor(IndexedColors.SKY_BLUE.index);
        titleCellStyle.setBorderBottom(BorderStyle.MEDIUM);
        titleCellStyle.setBorderLeft(BorderStyle.MEDIUM);
        titleCellStyle.setBorderRight(BorderStyle.MEDIUM);
        titleCellStyle.setBorderTop(BorderStyle.MEDIUM);
        titleCellStyle.setFont(titleFont);
        titleCellStyle.setWrapText(true);

        return titleCellStyle;
    }

    private static XSSFCellStyle createDataCellStyle(XSSFWorkbook xssfWorkbook) {
        // Tạo font cho dữ liệu
        XSSFFont dataFont = xssfWorkbook.createFont();
        dataFont.setFontName("Arial");
        dataFont.setBold(false);
        dataFont.setFontHeightInPoints((short) 10);

        // Tạo style và áp dụng font cho dữ liệu
        XSSFCellStyle dataCellStyle = xssfWorkbook.createCellStyle();
        dataCellStyle.setAlignment(HorizontalAlignment.CENTER);
        dataCellStyle.setBorderBottom(BorderStyle.THIN);
        dataCellStyle.setBorderLeft(BorderStyle.THIN);
        dataCellStyle.setBorderRight(BorderStyle.THIN);
        dataCellStyle.setBorderTop(BorderStyle.THIN);
        dataCellStyle.setFont(dataFont);
        dataCellStyle.setWrapText(true);

        DataFormat format = xssfWorkbook.createDataFormat();
        dataCellStyle.setDataFormat(format.getFormat("@"));

        return dataCellStyle;
    }

    public static Date convertExcelDateToJavaDate(double excelDate) {
        // Ngày bắt đầu từ 1/1/1900
        Calendar calendar = Calendar.getInstance();
        calendar.set(1900, Calendar.JANUARY, 1);
        calendar.add(Calendar.DATE, (int) excelDate - 2); // Trừ 2 vì Excel bắt đầu từ 1/1/1900
        return calendar.getTime();
    }
}
