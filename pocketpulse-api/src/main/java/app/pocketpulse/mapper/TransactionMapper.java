package app.pocketpulse.mapper;

import app.pocketpulse.dto.request.CreateTransactionRequestDTO;
import app.pocketpulse.dto.response.TransactionResponseDTO;
import app.pocketpulse.entity.TransactionEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TransactionMapper {

    // Request → Entity
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "profile", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "type", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    TransactionEntity toEntity(CreateTransactionRequestDTO dto);

    // Entity → Response
    @Mapping(source = "category.id", target = "categoryId")
    @Mapping(source = "category.name", target = "categoryName")
    TransactionResponseDTO toDTO(TransactionEntity entity);
}