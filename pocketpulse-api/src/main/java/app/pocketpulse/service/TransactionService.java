package app.pocketpulse.service;

import app.pocketpulse.dto.request.CreateTransactionRequestDTO;
import app.pocketpulse.dto.response.DashboardResponseDTO;
import app.pocketpulse.dto.response.TransactionResponseDTO;
import app.pocketpulse.entity.enums.CategoryType;

import java.io.ByteArrayInputStream;
import java.time.LocalDate;
import java.util.List;

public interface TransactionService {

    // CREATE
    TransactionResponseDTO createTransaction(
            CreateTransactionRequestDTO request,
            CategoryType expectedType
    );

    // READ
    List<TransactionResponseDTO> getTransactions(
            CategoryType type,
            LocalDate startDate,
            LocalDate endDate
    );

    // GET TOP RECENT TRANSACTIONS
    List<TransactionResponseDTO> getRecentTransactions(CategoryType type, int limit);


    // GET MONTHLY TRANSACTIONS
    List<TransactionResponseDTO> getCurrentMonthTransactionsByType(CategoryType type);

    List<TransactionResponseDTO> searchTransactions(
            CategoryType type,
            String keyword,
            LocalDate startDate,
            LocalDate endDate,
            String[] sort
    );

    //  UPDATE
    TransactionResponseDTO updateTransaction(
            Long transactionId,
            CreateTransactionRequestDTO request
    );

    // DELETE
    void deleteTransaction(Long transactionId);

    // DASHBOARD
    DashboardResponseDTO getDashboard();

    // EXPORT TRANSACTION INTO EXCEL FILE
    ByteArrayInputStream exportTransactions(
            CategoryType type,
            String keyword,
            LocalDate startDate,
            LocalDate endDate,
            String[] sort
    );
}