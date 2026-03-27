package app.pocketpulse.controller;

import app.pocketpulse.dto.request.CreateCategoryRequestDTO;
import app.pocketpulse.dto.request.UpdateCategoryRequestDTO;
import app.pocketpulse.dto.response.CategoryResponseDTO;
import app.pocketpulse.entity.enums.CategoryType;
import app.pocketpulse.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    //  CREATE CATEGORY
    @PostMapping
    public ResponseEntity<CategoryResponseDTO> createCategory(
            @Valid @RequestBody CreateCategoryRequestDTO request
    ) {
        CategoryResponseDTO response = categoryService.createCategory(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    //  GET ALL USER CATEGORIES
    @GetMapping
    public ResponseEntity<List<CategoryResponseDTO>> getUserCategories() {
        return ResponseEntity.ok(categoryService.getUserCategories());
    }

    //  GET CATEGORIES BY TYPE (INCOME / EXPENSE)
    @GetMapping("/type")
    public ResponseEntity<List<CategoryResponseDTO>> getCategoriesByType(
            @RequestParam CategoryType type
    ) {
        return ResponseEntity.ok(categoryService.getCategoriesByType(type));
    }

    //  UPDATE CATEGORY
    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponseDTO> updateCategory(
            @PathVariable Long id,
            @Valid @RequestBody UpdateCategoryRequestDTO request
    ) {
        return ResponseEntity.ok(categoryService.updateCategory(id, request));
    }

    //  DELETE CATEGORY
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.ok("Category deleted successfully");
    }
}