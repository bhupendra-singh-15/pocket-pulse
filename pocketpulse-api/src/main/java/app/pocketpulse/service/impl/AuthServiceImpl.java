package app.pocketpulse.service.impl;

import app.pocketpulse.dto.request.LoginRequestDTO;
import app.pocketpulse.dto.response.LoginResponseDTO;
import app.pocketpulse.entity.ProfileEntity;
import app.pocketpulse.mapper.ProfileMapper;
import app.pocketpulse.repository.ProfileRepository;
import app.pocketpulse.util.JwtUtil;
import app.pocketpulse.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final ProfileRepository profileRepository;
    private final ProfileMapper profileMapper;

    @Override
    public LoginResponseDTO login(LoginRequestDTO request) {

        //  Check user exists
        ProfileEntity user = profileRepository.findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new UsernameNotFoundException("Invalid email or password")
                );

        // Check account activation BEFORE authentication
        if (!Boolean.TRUE.equals(user.getIsActive())) {
            throw new IllegalStateException("Account not activated. Please activate via email before login.");
        }

        // Authenticate (password check)
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        // Generate JWT
        String token = jwtUtil.generateToken(user.getEmail());

        return new LoginResponseDTO(token,profileMapper.toDTO(user));
    }
}