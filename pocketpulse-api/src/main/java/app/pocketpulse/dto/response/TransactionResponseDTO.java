package app.pocketpulse.dto.response;

import app.pocketpulse.entity.enums.CategoryType;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class TransactionResponseDTO {

    private Long id;

    private BigDecimal amount;

    private String name;

    private String description;

    private LocalDate date;

    private Long categoryId;

    private String categoryName;

    private CategoryType type;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}