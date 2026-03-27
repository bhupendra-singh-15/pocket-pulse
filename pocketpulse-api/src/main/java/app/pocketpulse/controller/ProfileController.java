package app.pocketpulse.controller;

import app.pocketpulse.dto.request.RegisterRequestDTO;
import app.pocketpulse.dto.response.ProfileResponseDTO;
import app.pocketpulse.service.ProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;


    @PostMapping("/register")
    public ResponseEntity<ProfileResponseDTO> register(
            @Valid @RequestBody RegisterRequestDTO request) {
            ProfileResponseDTO registeredProfile = profileService.register(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(registeredProfile);
    }

    @GetMapping("/me")
    public ResponseEntity<ProfileResponseDTO> getCurrentUser() {
        return ResponseEntity.ok(profileService.getPublicProfile(null));
    }

    @GetMapping("/activate")
    public ResponseEntity<String> activateProfile (@RequestParam String token){
        boolean isActivated = profileService.activateProfile(token);
        if (isActivated){
            return ResponseEntity.ok("Profile activated successfully");
        }else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Activation token not found or already used");
        }
    }

}