package app.pocketpulse.mapper;

import app.pocketpulse.dto.request.CreateCategoryRequestDTO;
import app.pocketpulse.dto.response.CategoryResponseDTO;
import app.pocketpulse.entity.CategoryEntity;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-03-28T00:51:28+0530",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.45.0.v20260224-0835, environment: Java 21.0.10 (Eclipse Adoptium)"
)
@Component
public class CategoryMapperImpl implements CategoryMapper {

    @Override
    public CategoryEntity toEntity(CreateCategoryRequestDTO dto) {
        if ( dto == null ) {
            return null;
        }

        CategoryEntity.CategoryEntityBuilder categoryEntity = CategoryEntity.builder();

        categoryEntity.icon( dto.getIcon() );
        categoryEntity.name( dto.getName() );
        categoryEntity.type( dto.getType() );

        return categoryEntity.build();
    }

    @Override
    public CategoryResponseDTO toDTO(CategoryEntity entity) {
        if ( entity == null ) {
            return null;
        }

        CategoryResponseDTO categoryResponseDTO = new CategoryResponseDTO();

        categoryResponseDTO.setCreatedAt( entity.getCreatedAt() );
        categoryResponseDTO.setIcon( entity.getIcon() );
        categoryResponseDTO.setId( entity.getId() );
        categoryResponseDTO.setName( entity.getName() );
        categoryResponseDTO.setType( entity.getType() );

        return categoryResponseDTO;
    }
}
