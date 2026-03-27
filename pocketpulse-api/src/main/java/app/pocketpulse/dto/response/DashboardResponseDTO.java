package app.pocketpulse.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
public class DashboardResponseDTO {

    private BigDecimal totalIncome;
    private BigDecimal totalExpense;
    private BigDecimal balance;

    private List<TransactionResponseDTO> recentIncome;
    private List<TransactionResponseDTO> recentExpense;
    private List<TransactionResponseDTO> recentTransactions;
}