package app.pocketpulse.controller;

import app.pocketpulse.dto.request.CreateTransactionRequestDTO;
import app.pocketpulse.dto.response.TransactionResponseDTO;
import app.pocketpulse.entity.enums.CategoryType;
import app.pocketpulse.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import java.io.ByteArrayInputStream;

import java.time.LocalDate;
import java.util.List;
@RestController
@RequestMapping("/transactions")
@RequiredArgsConstructor

public class TransactionController {

    private final TransactionService transactionService;

    //  CREATE
    @PostMapping
    public ResponseEntity<TransactionResponseDTO> createTransaction(
            @Valid @RequestBody CreateTransactionRequestDTO request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(transactionService.createTransaction(request, null));
    }

    //  FILTERED LIST
    @GetMapping
    public ResponseEntity<List<TransactionResponseDTO>> getTransactions(
            @RequestParam(required = false) CategoryType type,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate startDate,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate endDate
    ) {
        return ResponseEntity.ok(
                transactionService.getTransactions(type, startDate, endDate)
        );
    }

    //  RECENT (TOP N)
    @GetMapping("/recent")
    public ResponseEntity<List<TransactionResponseDTO>> getRecentTransactions(
            @RequestParam CategoryType type,
            @RequestParam(defaultValue = "5") int limit
    ) {
        return ResponseEntity.ok(
                transactionService.getRecentTransactions(type, limit)
        );
    }

    // CURRENT MONTH
    @GetMapping("/monthly")
    public ResponseEntity<List<TransactionResponseDTO>> getCurrentMonthTransactions(
            @RequestParam CategoryType type
    ) {
        return ResponseEntity.ok(
                transactionService.getCurrentMonthTransactionsByType(type)
        );
    }

    //  KEYWORD SEARCH + SORT
    @GetMapping("/search")
    public ResponseEntity<List<TransactionResponseDTO>> searchTransactions(

            @RequestParam(required = false) CategoryType type,

            @RequestParam(required = false) String keyword,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate startDate,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate endDate,

            @RequestParam(defaultValue = "date,desc") String[] sort
    ) {
        return ResponseEntity.ok(
                transactionService.searchTransactions(type, keyword, startDate, endDate, sort)
        );
    }

    //  UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<TransactionResponseDTO> updateTransaction(
            @PathVariable Long id,
            @Valid @RequestBody CreateTransactionRequestDTO request
    ) {
        return ResponseEntity.ok(
                transactionService.updateTransaction(id, request)
        );
    }

    //  DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTransaction(@PathVariable Long id) {
        transactionService.deleteTransaction(id);
        return ResponseEntity.ok("Transaction deleted successfully");
    }


    //  EXPORT TRANSACTION TO EXCEL FILE

    @GetMapping("/export")
    public ResponseEntity<InputStreamResource> exportTransactions(

            @RequestParam(required = false) CategoryType type,

            @RequestParam(required = false) String keyword,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate startDate,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate endDate,

            @RequestParam(defaultValue = "date,desc") String[] sort
    ) {

        ByteArrayInputStream file = transactionService.exportTransactions(
                type, keyword, startDate, endDate, sort
        );

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=transactions.xlsx")
                .contentType(MediaType.parseMediaType(
                        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
                ))
                .body(new InputStreamResource(file));
    }
}