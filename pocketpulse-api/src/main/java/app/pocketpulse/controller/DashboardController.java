package app.pocketpulse.controller;

import app.pocketpulse.dto.response.DashboardResponseDTO;
import app.pocketpulse.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final TransactionService transactionService;

    @GetMapping
    public ResponseEntity<DashboardResponseDTO> getDashboard() {
        return ResponseEntity.ok(transactionService.getDashboard());
    }
}