package mg.cnaps.gestion.ccl.framework.excel.service;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import mg.cnaps.gestion.ccl.framework.excel.annotation.ExcelColumn;
import mg.cnaps.gestion.ccl.framework.excel.annotation.ExcelColorField;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;

@Service
public class GenericExcelService {

    public <T> ByteArrayInputStream exportToExcel(List<T> dataList, Class<T> dtoClass) throws IOException {
        if (dataList == null || dataList.isEmpty()) {
            throw new IllegalArgumentException("La liste est vide !");
        }

        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.createSheet(dtoClass.getSimpleName());

            // Style d’en-tête
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            CellStyle headerStyle = workbook.createCellStyle();
            headerStyle.setFont(headerFont);
            headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            Field[] fields = dtoClass.getDeclaredFields();

            // Trouver le champ servant à la couleur
            Field colorField = null;
            for (Field f : fields) {
                if (f.isAnnotationPresent(ExcelColorField.class)) {
                    colorField = f;
                    break;
                }
            }

            // Extraire la Map de couleurs depuis l’annotation @ExcelColumn
            Map<String, IndexedColors> annotationColorMap = new HashMap<>();
            if (colorField != null && colorField.isAnnotationPresent(ExcelColumn.class)) {
                ExcelColumn excelColumn = colorField.getAnnotation(ExcelColumn.class);
                for (String entry : excelColumn.colors()) {
                    String[] parts = entry.split(":");
                    if (parts.length == 2) {
                        annotationColorMap.put(parts[0].trim(), colorNameToColor(parts[1].trim()));
                    }
                }
            }

            //  En-tête
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < fields.length; i++) {
                fields[i].setAccessible(true);
                String header = fields[i].isAnnotationPresent(ExcelColumn.class)
                        ? fields[i].getAnnotation(ExcelColumn.class).value()
                        : fields[i].getName();

                Cell cell = headerRow.createCell(i);
                cell.setCellValue(header);
                cell.setCellStyle(headerStyle);
            }

            //  Lignes de données
            int rowIdx = 1;
            for (T item : dataList) {
                Row row = sheet.createRow(rowIdx++);

                // Déterminer la couleur selon la valeur du champ coloré
                IndexedColors color = IndexedColors.WHITE;
                if (colorField != null) {
                    colorField.setAccessible(true);
                    Object val = colorField.get(item);
                    if (val != null) {
                        color = annotationColorMap.getOrDefault(val.toString(), IndexedColors.WHITE);
                    }
                }

                // Style pour cette ligne
                CellStyle rowStyle = workbook.createCellStyle();
                rowStyle.setFillForegroundColor(color.getIndex());
                rowStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

                for (int i = 0; i < fields.length; i++) {
                    Cell cell = row.createCell(i);
                    Object value = fields[i].get(item);
                    cell.setCellValue(value != null ? value.toString() : "");
                    cell.setCellStyle(rowStyle);
                }
            }

            for (int i = 0; i < fields.length; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Erreur d’accès aux champs du DTO", e);
        }
    }

    private IndexedColors colorNameToColor(String colorName) {
        String upper = colorName.toUpperCase();
        switch (upper) {
            case "ROUGE":
            case "RED":
                return IndexedColors.RED;
            case "BLEU":
            case "BLUE":
                return IndexedColors.LIGHT_BLUE;
            case "VERT":
            case "GREEN":
                return IndexedColors.LIGHT_GREEN;
            case "JAUNE":
            case "YELLOW":
                return IndexedColors.LIGHT_YELLOW;
            case "GRIS":
            case "GRAY":
            case "GREY":
                return IndexedColors.GREY_25_PERCENT;
            default:
                return IndexedColors.WHITE;
        }
    }
}
