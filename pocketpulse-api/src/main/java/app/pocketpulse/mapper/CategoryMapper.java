package app.pocketpulse.mapper;

import app.pocketpulse.dto.request.CreateCategoryRequestDTO;
import app.pocketpulse.dto.response.CategoryResponseDTO;
import app.pocketpulse.entity.CategoryEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    // Request → Entity
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "profile", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    CategoryEntity toEntity(CreateCategoryRequestDTO dto);

    // Entity → Response
    CategoryResponseDTO toDTO(CategoryEntity entity);
}