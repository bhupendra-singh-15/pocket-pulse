package app.pocketpulse.mapper;

import app.pocketpulse.dto.request.CreateTransactionRequestDTO;
import app.pocketpulse.dto.response.TransactionResponseDTO;
import app.pocketpulse.entity.CategoryEntity;
import app.pocketpulse.entity.TransactionEntity;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-03-28T00:51:28+0530",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.45.0.v20260224-0835, environment: Java 21.0.10 (Eclipse Adoptium)"
)
@Component
public class TransactionMapperImpl implements TransactionMapper {

    @Override
    public TransactionEntity toEntity(CreateTransactionRequestDTO dto) {
        if ( dto == null ) {
            return null;
        }

        TransactionEntity.TransactionEntityBuilder transactionEntity = TransactionEntity.builder();

        transactionEntity.amount( dto.getAmount() );
        transactionEntity.date( dto.getDate() );
        transactionEntity.description( dto.getDescription() );
        transactionEntity.name( dto.getName() );

        return transactionEntity.build();
    }

    @Override
    public TransactionResponseDTO toDTO(TransactionEntity entity) {
        if ( entity == null ) {
            return null;
        }

        TransactionResponseDTO transactionResponseDTO = new TransactionResponseDTO();

        transactionResponseDTO.setCategoryId( entityCategoryId( entity ) );
        transactionResponseDTO.setCategoryName( entityCategoryName( entity ) );
        transactionResponseDTO.setAmount( entity.getAmount() );
        transactionResponseDTO.setCreatedAt( entity.getCreatedAt() );
        transactionResponseDTO.setDate( entity.getDate() );
        transactionResponseDTO.setDescription( entity.getDescription() );
        transactionResponseDTO.setId( entity.getId() );
        transactionResponseDTO.setName( entity.getName() );
        transactionResponseDTO.setType( entity.getType() );
        transactionResponseDTO.setUpdatedAt( entity.getUpdatedAt() );

        return transactionResponseDTO;
    }

    private Long entityCategoryId(TransactionEntity transactionEntity) {
        if ( transactionEntity == null ) {
            return null;
        }
        CategoryEntity category = transactionEntity.getCategory();
        if ( category == null ) {
            return null;
        }
        Long id = category.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String entityCategoryName(TransactionEntity transactionEntity) {
        if ( transactionEntity == null ) {
            return null;
        }
        CategoryEntity category = transactionEntity.getCategory();
        if ( category == null ) {
            return null;
        }
        String name = category.getName();
        if ( name == null ) {
            return null;
        }
        return name;
    }
}
