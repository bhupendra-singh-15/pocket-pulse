package app.pocketpulse.service;

import app.pocketpulse.dto.request.RegisterRequestDTO;
import app.pocketpulse.dto.response.ProfileResponseDTO;
import app.pocketpulse.entity.ProfileEntity;

public interface ProfileService {

    ProfileResponseDTO register(RegisterRequestDTO request);

    boolean activateProfile(String activationToken);

    boolean isAccountActive(String email);

    ProfileEntity getCurrentUser();

    ProfileResponseDTO getPublicProfile(String email);
}