package app.pocketpulse.dto.request;

import app.pocketpulse.entity.enums.CategoryType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateCategoryRequestDTO {

    @NotBlank
    private String name;

    @NotNull
    private CategoryType type;

    private String icon;
}