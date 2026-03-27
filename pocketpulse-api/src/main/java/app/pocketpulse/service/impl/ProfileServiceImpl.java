package app.pocketpulse.service.impl;

import app.pocketpulse.dto.request.RegisterRequestDTO;
import app.pocketpulse.dto.response.ProfileResponseDTO;
import app.pocketpulse.entity.ProfileEntity;
import app.pocketpulse.mapper.ProfileMapper;
import app.pocketpulse.repository.ProfileRepository;
import app.pocketpulse.service.EmailService;
import app.pocketpulse.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {

    private final ProfileRepository profileRepository;
    private final ProfileMapper profileMapper;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.base-url}")
    private String baseUrl;

    @Override
    @Transactional
    public ProfileResponseDTO register(RegisterRequestDTO request) {

        if (profileRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new IllegalStateException("Email already registered. Please login instead.");
        }


        ProfileEntity entity = profileMapper.toEntity(request);


        entity.setPassword(passwordEncoder.encode(entity.getPassword()));

        ProfileEntity saved = profileRepository.save(entity);


        sendActivationEmail(saved);

        return profileMapper.toDTO(saved);
    }

    //  ACTIVATE ACCOUNT
    @Override
    @Transactional
    public boolean activateProfile(String activationToken) {

        return profileRepository.findByActivationToken(activationToken)
                .map(profile -> {
                    profile.setIsActive(true);
                    profile.setActivationToken(null);
                    profileRepository.save(profile);
                    return true;
                })
                .orElse(false);
    }



    //  CHECK ACCOUNT STATUS
    @Override
    public boolean isAccountActive(String email) {

        return profileRepository.findByEmail(email)
                .map(ProfileEntity::getIsActive)
                .orElse(false);
    }

    //  GET CURRENT LOGGED-IN USER
    @Override
    public ProfileEntity getCurrentUser() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return profileRepository.findByEmail(authentication.getName())
                .orElseThrow(() ->
                        new UsernameNotFoundException(
                                "Profile not found with email: " + authentication.getName()
                        )
                );
    }

    //  GET PUBLIC PROFILE
    @Override
    public ProfileResponseDTO getPublicProfile(String email) {

        ProfileEntity profile;

        if (email == null || email.isBlank()) {
            profile = getCurrentUser();
        } else {
            profile = profileRepository.findByEmail(email)
                    .orElseThrow(() ->
                            new UsernameNotFoundException(
                                    "Profile not found with email: " + email
                            )
                    );
        }

        return profileMapper.toDTO(profile);
    }

    //  PRIVATE HELPER METHOD (Clean Code)
    private void sendActivationEmail(ProfileEntity user) {

        String activationLink = baseUrl + "/profile/activate?token=" + user.getActivationToken();

        String subject = "Activate your account";
        String body = "Click the link below to activate your account:\n\n" + activationLink;

        emailService.sendSimpleEmail(user.getEmail(), subject, body);
    }
}