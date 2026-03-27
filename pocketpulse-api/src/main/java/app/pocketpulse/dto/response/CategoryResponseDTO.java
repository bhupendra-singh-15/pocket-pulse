package app.pocketpulse.dto.response;

import app.pocketpulse.entity.enums.CategoryType;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CategoryResponseDTO {

    private Long id;
    private String name;
    private CategoryType type;
    private String icon;
    private LocalDateTime createdAt;
}