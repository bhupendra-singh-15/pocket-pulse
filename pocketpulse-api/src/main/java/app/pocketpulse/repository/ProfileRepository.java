package app.pocketpulse.repository;



import app.pocketpulse.entity.ProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProfileRepository extends JpaRepository<ProfileEntity, Long> {

    // Find a profile by email
    Optional<ProfileEntity> findByEmail(String email);

    // Check if email already exists
    boolean existsByEmail(String email);

    // Find a profile by activation token
    Optional<ProfileEntity> findByActivationToken(String activationToken);
}
