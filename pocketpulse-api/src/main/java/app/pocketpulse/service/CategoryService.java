package app.pocketpulse.service;

import app.pocketpulse.dto.request.CreateCategoryRequestDTO;
import app.pocketpulse.dto.request.UpdateCategoryRequestDTO;
import app.pocketpulse.dto.response.CategoryResponseDTO;
import app.pocketpulse.entity.enums.CategoryType;

import java.util.List;

public interface CategoryService {

    CategoryResponseDTO createCategory(CreateCategoryRequestDTO request);

    List<CategoryResponseDTO> getUserCategories();

    List<CategoryResponseDTO> getCategoriesByType(CategoryType type);

    CategoryResponseDTO updateCategory(Long categoryId, UpdateCategoryRequestDTO request);
    void deleteCategory(Long categoryId);
}