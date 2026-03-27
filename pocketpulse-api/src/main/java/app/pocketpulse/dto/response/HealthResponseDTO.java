package app.pocketpulse.dto.response;

import java.time.LocalDateTime;

public class HealthResponseDTO {

    private String status;
    private String service;
    private String version;
    private LocalDateTime timestamp;

    public HealthResponseDTO(String status, String service) {
        this.status = status;
        this.service = service;
        this.version = "1.0.0";
        this.timestamp = LocalDateTime.now();
    }

    // Getters and setters
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getService() { return service; }
    public void setService(String service) { this.service = service; }

    public String getVersion() { return version; }
    public void setVersion(String version) { this.version = version; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}
