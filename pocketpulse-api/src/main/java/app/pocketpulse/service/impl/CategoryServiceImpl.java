package app.pocketpulse.service.impl;

import app.pocketpulse.dto.request.CreateCategoryRequestDTO;
import app.pocketpulse.dto.request.UpdateCategoryRequestDTO;
import app.pocketpulse.dto.response.CategoryResponseDTO;
import app.pocketpulse.entity.CategoryEntity;
import app.pocketpulse.entity.ProfileEntity;
import app.pocketpulse.entity.enums.CategoryType;
import app.pocketpulse.mapper.CategoryMapper;
import app.pocketpulse.repository.CategoryRepository;
import app.pocketpulse.service.CategoryService;
import app.pocketpulse.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final ProfileService profileService;
    private final CategoryMapper categoryMapper;

    @Override
    public CategoryResponseDTO createCategory(CreateCategoryRequestDTO request) {

        ProfileEntity currentUser = profileService.getCurrentUser();

        //  Case-insensitive duplicate check
        if (categoryRepository.existsByNameIgnoreCaseAndProfile(request.getName(), currentUser)) {
            throw new IllegalStateException("Category already exists");
        }

        //  MapStruct mapping
        CategoryEntity category = categoryMapper.toEntity(request);

        if (category.getIcon() == null || category.getIcon().isBlank()) {
            category.setIcon("📁");
        }

        //  Set logged-in user
        category.setProfile(currentUser);

        CategoryEntity saved = categoryRepository.save(category);

        return categoryMapper.toDTO(saved);
    }

    @Override
    public List<CategoryResponseDTO> getUserCategories() {

        ProfileEntity currentUser = profileService.getCurrentUser();

        return categoryRepository.findByProfileOrderByCreatedAtDesc(currentUser)
                .stream()
                .map(categoryMapper::toDTO)
                .toList();
    }

    @Override
    public List<CategoryResponseDTO> getCategoriesByType(CategoryType type) {

        ProfileEntity currentUser = profileService.getCurrentUser();

        return categoryRepository.findByTypeAndProfile(type, currentUser)
                .stream()
                .map(categoryMapper::toDTO)
                .toList();
    }

    @Override
    public CategoryResponseDTO updateCategory(Long categoryId, UpdateCategoryRequestDTO request) {

        ProfileEntity currentUser = profileService.getCurrentUser();

        CategoryEntity category = categoryRepository
                .findByIdAndProfile(categoryId, currentUser)
                .orElseThrow(() -> new IllegalStateException("Category not found"));

        // Check duplicate name (ignore case, exclude same category)
        if (!category.getName().equalsIgnoreCase(request.getName()) &&
                categoryRepository.existsByNameIgnoreCaseAndProfile(request.getName(), currentUser)) {
            throw new IllegalStateException("Category already exists");
        }

        // Update fields
        category.setName(request.getName());
        category.setType(request.getType());

        // Handle optional icon
        if (request.getIcon() == null || request.getIcon().isBlank()) {
            category.setIcon("📁");
        } else {
            category.setIcon(request.getIcon());
        }

        CategoryEntity updated = categoryRepository.save(category);

        return categoryMapper.toDTO(updated);
    }

    @Override
    public void deleteCategory(Long categoryId) {

        ProfileEntity currentUser = profileService.getCurrentUser();

        CategoryEntity category = categoryRepository
                .findByIdAndProfile(categoryId, currentUser)
                .orElseThrow(() -> new IllegalStateException("Category not found"));

        categoryRepository.delete(category);
    }
}