package app.pocketpulse.service;

import app.pocketpulse.dto.request.LoginRequestDTO;
import app.pocketpulse.dto.response.LoginResponseDTO;

public interface AuthService {
    LoginResponseDTO login(LoginRequestDTO request);
}