package app.pocketpulse.mapper;

import app.pocketpulse.dto.request.RegisterRequestDTO;
import app.pocketpulse.dto.response.ProfileResponseDTO;
import app.pocketpulse.entity.ProfileEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProfileMapper {

    ProfileEntity toEntity(RegisterRequestDTO dto);

    ProfileResponseDTO toDTO(ProfileEntity entity);
}