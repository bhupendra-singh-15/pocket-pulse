package app.pocketpulse.service;

import app.pocketpulse.entity.TransactionEntity;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;

@Service
public class ExcelExportService {

    public ByteArrayInputStream exportTransactions(List<TransactionEntity> transactions) {

        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.createSheet("Transactions");

            // Header
            Row header = sheet.createRow(0);
            String[] columns = {
                    "ID", "Name", "Amount", "Type", "Category", "Date", "Description"
            };

            for (int i = 0; i < columns.length; i++) {
                Cell cell = header.createCell(i);
                cell.setCellValue(columns[i]);
            }

            // Data
            int rowIdx = 1;
            for (TransactionEntity t : transactions) {
                Row row = sheet.createRow(rowIdx++);

                row.createCell(0).setCellValue(t.getId());
                row.createCell(1).setCellValue(t.getName());
                row.createCell(2).setCellValue(t.getAmount().doubleValue());
                row.createCell(3).setCellValue(t.getType().name());
                row.createCell(4).setCellValue(t.getCategory().getName());
                row.createCell(5).setCellValue(t.getDate().toString());
                row.createCell(6).setCellValue(
                        t.getDescription() != null ? t.getDescription() : ""
                );
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());

        } catch (Exception e) {
            throw new IllegalStateException("Failed to export Excel");
        }
    }
}