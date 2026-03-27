package app.pocketpulse.controller;


import app.pocketpulse.dto.response.HealthResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

    @GetMapping({"/health", "/status"})
    public ResponseEntity<HealthResponseDTO> healthCheck() {
        HealthResponseDTO response = new HealthResponseDTO("UP", "PocketPulse API");
        return ResponseEntity.ok(response);
    }
}
