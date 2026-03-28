package app.pocketpulse.mapper;

import app.pocketpulse.dto.request.RegisterRequestDTO;
import app.pocketpulse.dto.response.ProfileResponseDTO;
import app.pocketpulse.entity.ProfileEntity;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-03-28T00:51:28+0530",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.45.0.v20260224-0835, environment: Java 21.0.10 (Eclipse Adoptium)"
)
@Component
public class ProfileMapperImpl implements ProfileMapper {

    @Override
    public ProfileEntity toEntity(RegisterRequestDTO dto) {
        if ( dto == null ) {
            return null;
        }

        ProfileEntity.ProfileEntityBuilder profileEntity = ProfileEntity.builder();

        profileEntity.email( dto.getEmail() );
        profileEntity.fullName( dto.getFullName() );
        profileEntity.password( dto.getPassword() );

        return profileEntity.build();
    }

    @Override
    public ProfileResponseDTO toDTO(ProfileEntity entity) {
        if ( entity == null ) {
            return null;
        }

        ProfileResponseDTO profileResponseDTO = new ProfileResponseDTO();

        profileResponseDTO.setCreatedAt( entity.getCreatedAt() );
        profileResponseDTO.setEmail( entity.getEmail() );
        profileResponseDTO.setFullName( entity.getFullName() );
        profileResponseDTO.setId( entity.getId() );
        profileResponseDTO.setIsActive( entity.getIsActive() );
        profileResponseDTO.setProfileImageUrl( entity.getProfileImageUrl() );
        profileResponseDTO.setUpdatedAt( entity.getUpdatedAt() );

        return profileResponseDTO;
    }
}
