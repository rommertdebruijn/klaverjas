package com.keemerz.klaverjas.security;

import com.keemerz.klaverjas.domain.Credentials;
import com.keemerz.klaverjas.repository.CredentialsRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class KlaverjasUserDetailsService implements UserDetailsService {

    private CredentialsRepository credentialsRepository = CredentialsRepository.getInstance();

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        Credentials credentials = credentialsRepository.getCredentials(userId);
        return User.builder()
                .username(credentials.getUserId())
                .password(credentials.getEncryptedPassword())
                .roles("USER")
                .build();
    }

}
