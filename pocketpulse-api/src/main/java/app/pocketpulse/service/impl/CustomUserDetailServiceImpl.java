package app.pocketpulse.service.impl;

import app.pocketpulse.entity.ProfileEntity;
import app.pocketpulse.repository.ProfileRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailServiceImpl implements UserDetailsService {

    private final ProfileRepository profileRepository;

    public CustomUserDetailServiceImpl(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        ProfileEntity profile = profileRepository.findByEmail(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException("Profile not found with email: " + username)
                );

        // Block inactive users
        if (!Boolean.TRUE.equals(profile.getIsActive())) {
            throw new UsernameNotFoundException("Account not activated");
        }

        return org.springframework.security.core.userdetails.User
                .withUsername(profile.getEmail())
                .password(profile.getPassword())
                .authorities("USER") // or profile.getRole()
                .build();
    }
}