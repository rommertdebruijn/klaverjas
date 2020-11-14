package com.keemerz.klaverjas.repository;

import com.keemerz.klaverjas.domain.Credentials;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.List;

public class CredentialsRepository {

    private static final CredentialsRepository INSTANCE = new CredentialsRepository();
    private static final List<Credentials> CREDENTIALS = new ArrayList<>();
    static {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        CREDENTIALS.add(new Credentials("user1", encoder.encode("pass1")));
        CREDENTIALS.add(new Credentials("user2", encoder.encode("pass2")));
        CREDENTIALS.add(new Credentials("user3", encoder.encode("pass3")));
        CREDENTIALS.add(new Credentials("user4", encoder.encode("pass4")));
        CREDENTIALS.add(new Credentials("user5", encoder.encode("pass5")));
    }

    private CredentialsRepository() {}

    public static CredentialsRepository getInstance() {
        return INSTANCE;
    }

    public Credentials getCredentials(String userId) {
        return CREDENTIALS.stream()
                .filter(credentials -> credentials.getUserId().equals(userId))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
