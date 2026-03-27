package app.pocketpulse.service.impl;

import app.pocketpulse.dto.request.CreateTransactionRequestDTO;
import app.pocketpulse.dto.response.DashboardResponseDTO;
import app.pocketpulse.dto.response.TransactionResponseDTO;
import app.pocketpulse.entity.CategoryEntity;
import app.pocketpulse.entity.ProfileEntity;
import app.pocketpulse.entity.TransactionEntity;
import app.pocketpulse.entity.enums.CategoryType;
import app.pocketpulse.mapper.TransactionMapper;
import app.pocketpulse.repository.CategoryRepository;
import app.pocketpulse.repository.TransactionRepository;
import app.pocketpulse.service.ExcelExportService;
import app.pocketpulse.service.ProfileService;
import app.pocketpulse.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.domain.Sort;


@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final CategoryRepository categoryRepository;
    private final ProfileService profileService;
    private final TransactionMapper transactionMapper;
    private final ExcelExportService excelExportService;

    //  CREATE
    @Override
    public TransactionResponseDTO createTransaction(
            CreateTransactionRequestDTO request,
            CategoryType expectedType
    ) {

        ProfileEntity currentUser = profileService.getCurrentUser();

        CategoryEntity category = categoryRepository
                .findByIdAndProfile(request.getCategoryId(), currentUser)
                .orElseThrow(() -> new IllegalStateException("Category not found"));

        if (expectedType != null && category.getType() != expectedType) {
            throw new IllegalStateException("Invalid category type. Expected: " + expectedType);
        }

        if (request.getDate().isAfter(LocalDate.now())) {
            throw new IllegalStateException("Date cannot be in the future");
        }

        TransactionEntity transaction = transactionMapper.toEntity(request);
        transaction.setProfile(currentUser);
        transaction.setCategory(category);
        transaction.setType(category.getType());

        return transactionMapper.toDTO(transactionRepository.save(transaction));
    }

    //  UNIFIED READ
    @Override
    public List<TransactionResponseDTO> getTransactions(
            CategoryType type,
            LocalDate startDate,
            LocalDate endDate
    ) {

        ProfileEntity currentUser = profileService.getCurrentUser();

        if (startDate != null && endDate == null) endDate = LocalDate.now();
        if (startDate == null && endDate != null) startDate = LocalDate.of(1970, 1, 1);

        List<TransactionEntity> transactions;

        if (type != null && startDate != null && endDate != null) {
            transactions = transactionRepository
                    .findByProfileAndTypeAndDateBetweenOrderByDateDescCreatedAtDesc(
                            currentUser, type, startDate, endDate);

        } else if (type != null) {
            transactions = transactionRepository
                    .findByProfileAndTypeOrderByDateDescCreatedAtDesc(currentUser, type);

        } else if (startDate != null) {
            transactions = transactionRepository
                    .findByProfileAndDateBetweenOrderByDateDescCreatedAtDesc(
                            currentUser, startDate, endDate);

        } else {
            transactions = transactionRepository
                    .findByProfileOrderByDateDescCreatedAtDesc(currentUser);
        }

        return transactions.stream()
                .map(transactionMapper::toDTO)
                .toList();
    }

    // RECENT TRANSACTIONS (GENERIC + LIMIT)
    @Override
    public List<TransactionResponseDTO> getRecentTransactions(CategoryType type, int limit) {

        ProfileEntity currentUser = profileService.getCurrentUser();

        // ⚠️ fallback (since repo doesn't support dynamic limit yet)
        List<TransactionEntity> transactions = transactionRepository
                .findByProfileAndTypeOrderByDateDescCreatedAtDesc(currentUser, type);

        return transactions.stream()
                .limit(limit)
                .map(transactionMapper::toDTO)
                .toList();
    }

    //  CURRENT MONTH TRANSACTIONS
    @Override
    public List<TransactionResponseDTO> getCurrentMonthTransactionsByType(CategoryType type) {

        ProfileEntity currentUser = profileService.getCurrentUser();

        LocalDate start = LocalDate.now().withDayOfMonth(1);
        LocalDate end = LocalDate.now();

        List<TransactionEntity> transactions = transactionRepository
                .findByProfileAndTypeAndDateBetweenOrderByDateDescCreatedAtDesc(
                        currentUser, type, start, end
                );

        return transactions.stream()
                .map(transactionMapper::toDTO)
                .toList();
    }

    //  SEARCH TRANSACTIONS
    @Override
    public List<TransactionResponseDTO> searchTransactions(
            CategoryType type,
            String keyword,
            LocalDate startDate,
            LocalDate endDate,
            String[] sort
    ) {

        ProfileEntity currentUser = profileService.getCurrentUser();

        //  DEFAULTS
        if (keyword != null && keyword.isBlank()) keyword = null;

        if (startDate != null && endDate == null) endDate = LocalDate.now();
        if (startDate == null && endDate != null) startDate = LocalDate.of(1970, 1, 1);

        // SORT LOGIC
        String sortField = (sort != null && sort.length > 0) ? sort[0] : "date";
        String sortDirection = (sort != null && sort.length > 1) ? sort[1] : "desc";

        //  VALIDATE SORT FIELD
        List<String> allowedFields = List.of("date", "amount", "name", "createdAt");

        if (!allowedFields.contains(sortField)) {
            sortField = "date";
        }

        Sort.Direction direction = sortDirection.equalsIgnoreCase("asc")
                ? Sort.Direction.ASC
                : Sort.Direction.DESC;

        Sort sorting = Sort.by(direction, sortField);

        List<TransactionEntity> transactions = transactionRepository.searchTransactions(
                currentUser,
                type,
                startDate,
                endDate,
                keyword,
                sorting
        );

        return transactions.stream()
                .map(transactionMapper::toDTO)
                .toList();
    }
    // UPDATE
    @Override
    public TransactionResponseDTO updateTransaction(
            Long transactionId,
            CreateTransactionRequestDTO request
    ) {

        ProfileEntity currentUser = profileService.getCurrentUser();

        TransactionEntity transaction = transactionRepository
                .findByIdAndProfile(transactionId, currentUser)
                .orElseThrow(() -> new IllegalStateException("Transaction not found"));

        CategoryEntity category = categoryRepository
                .findByIdAndProfile(request.getCategoryId(), currentUser)
                .orElseThrow(() -> new IllegalStateException("Category not found"));

        if (request.getDate().isAfter(LocalDate.now())) {
            throw new IllegalStateException("Date cannot be in the future");
        }

        transaction.setAmount(request.getAmount());
        transaction.setName(request.getName());
        transaction.setDescription(request.getDescription());
        transaction.setDate(request.getDate());
        transaction.setCategory(category);
        transaction.setType(category.getType());

        return transactionMapper.toDTO(transactionRepository.save(transaction));
    }

    //  DELETE
    @Override
    public void deleteTransaction(Long transactionId) {

        ProfileEntity currentUser = profileService.getCurrentUser();

        TransactionEntity transaction = transactionRepository
                .findByIdAndProfile(transactionId, currentUser)
                .orElseThrow(() -> new IllegalStateException("Transaction not found"));

        transactionRepository.delete(transaction);
    }

    //  DASHBOARD (FULLY UPDATED)
    @Override
    public DashboardResponseDTO getDashboard() {

        ProfileEntity currentUser = profileService.getCurrentUser();

        BigDecimal income = transactionRepository
                .sumAmountByType(currentUser, CategoryType.INCOME);

        BigDecimal expense = transactionRepository
                .sumAmountByType(currentUser, CategoryType.EXPENSE);

        BigDecimal balance = income.subtract(expense);

        //  USE NEW GENERIC METHOD
        List<TransactionResponseDTO> recentIncome = getRecentTransactions(CategoryType.INCOME, 5);
        List<TransactionResponseDTO> recentExpense = getRecentTransactions(CategoryType.EXPENSE, 5);

        LocalDate oneMonthAgo = LocalDate.now().minusMonths(1);

        List<TransactionResponseDTO> recentTransactions = transactionRepository
                .findTop10ByProfileAndDateBetweenOrderByDateDescCreatedAtDesc(
                        currentUser,
                        oneMonthAgo,
                        LocalDate.now()
                )
                .stream()
                .map(transactionMapper::toDTO)
                .toList();

        return new DashboardResponseDTO(
                income,
                expense,
                balance,
                recentIncome,
                recentExpense,
                recentTransactions
        );

    }

    // EXPORT TRANSACTION TO EXCEL

    @Override
    public ByteArrayInputStream exportTransactions(
            CategoryType type,
            String keyword,
            LocalDate startDate,
            LocalDate endDate,
            String[] sort
    ) {

        ProfileEntity currentUser = profileService.getCurrentUser();

        //  DEFAULTS
        if (keyword != null && keyword.isBlank()) keyword = null;

        if (startDate != null && endDate == null) endDate = LocalDate.now();
        if (startDate == null && endDate != null) startDate = LocalDate.of(1970, 1, 1);

        //  SORT LOGIC
        String sortField = (sort != null && sort.length > 0) ? sort[0] : "date";
        String sortDirection = (sort != null && sort.length > 1) ? sort[1] : "desc";

        // VALIDATE SORT FIELD
        List<String> allowedFields = List.of("date", "amount", "name", "createdAt");

        if (!allowedFields.contains(sortField)) {
            sortField = "date";
        }

        Sort.Direction direction = sortDirection.equalsIgnoreCase("asc")
                ? Sort.Direction.ASC
                : Sort.Direction.DESC;

        Sort sorting = Sort.by(direction, sortField);

        List<TransactionEntity> transactions = transactionRepository.searchTransactions(
                currentUser,
                type,
                startDate,
                endDate,
                keyword,
                sorting
        );

        return excelExportService.exportTransactions(transactions);
    }
}