package com.keemerz.klaverjas.repository;

import com.keemerz.klaverjas.domain.Credentials;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CredentialsRepository {

    private static final List<Credentials> CREDENTIALS = new ArrayList<>();

    public static final String DEFAULT_PASSWORD = "bier";

    static {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        // for now, actual security is of no importance.
        // What matters is that the logged in user has a Principal, which is required for individual web sockets to work :)
        CREDENTIALS.add(new Credentials("user1", encoder.encode(DEFAULT_PASSWORD)));
        CREDENTIALS.add(new Credentials("user2", encoder.encode(DEFAULT_PASSWORD)));
        CREDENTIALS.add(new Credentials("user3", encoder.encode(DEFAULT_PASSWORD)));
        CREDENTIALS.add(new Credentials("user4", encoder.encode(DEFAULT_PASSWORD)));
        CREDENTIALS.add(new Credentials("martin", encoder.encode(DEFAULT_PASSWORD)));
        CREDENTIALS.add(new Credentials("alex", encoder.encode(DEFAULT_PASSWORD)));
        CREDENTIALS.add(new Credentials("ronald", encoder.encode(DEFAULT_PASSWORD)));
        CREDENTIALS.add(new Credentials("eelco", encoder.encode(DEFAULT_PASSWORD)));
        CREDENTIALS.add(new Credentials("robert", encoder.encode(DEFAULT_PASSWORD)));
        CREDENTIALS.add(new Credentials("rommert", encoder.encode(DEFAULT_PASSWORD)));
        CREDENTIALS.add(new Credentials("toon", encoder.encode(DEFAULT_PASSWORD)));
        CREDENTIALS.add(new Credentials("jasper", encoder.encode(DEFAULT_PASSWORD)));
        CREDENTIALS.add(new Credentials("yoran", encoder.encode(DEFAULT_PASSWORD)));
        CREDENTIALS.add(new Credentials("paul", encoder.encode(DEFAULT_PASSWORD)));
        CREDENTIALS.add(new Credentials("joris", encoder.encode(DEFAULT_PASSWORD)));
        CREDENTIALS.add(new Credentials("frank", encoder.encode(DEFAULT_PASSWORD)));
        CREDENTIALS.add(new Credentials("janthijs", encoder.encode(DEFAULT_PASSWORD)));
        CREDENTIALS.add(new Credentials("anton", encoder.encode(DEFAULT_PASSWORD)));
        CREDENTIALS.add(new Credentials("edzo", encoder.encode(DEFAULT_PASSWORD)));
        CREDENTIALS.add(new Credentials("marc", encoder.encode(DEFAULT_PASSWORD)));
        CREDENTIALS.add(new Credentials("sander", encoder.encode(DEFAULT_PASSWORD)));
        CREDENTIALS.add(new Credentials("mitchell", encoder.encode(DEFAULT_PASSWORD)));
        CREDENTIALS.add(new Credentials("jonathan", encoder.encode(DEFAULT_PASSWORD)));
        CREDENTIALS.add(new Credentials("niek", encoder.encode(DEFAULT_PASSWORD)));
        CREDENTIALS.add(new Credentials("stefan", encoder.encode(DEFAULT_PASSWORD)));
        CREDENTIALS.add(new Credentials("ties", encoder.encode(DEFAULT_PASSWORD)));
        CREDENTIALS.add(new Credentials("hans", encoder.encode(DEFAULT_PASSWORD)));
        CREDENTIALS.add(new Credentials("mark", encoder.encode(DEFAULT_PASSWORD)));
        CREDENTIALS.add(new Credentials("matthijs", encoder.encode(DEFAULT_PASSWORD)));
        CREDENTIALS.add(new Credentials("sidney", encoder.encode(DEFAULT_PASSWORD))); // maatjes van Ties
        CREDENTIALS.add(new Credentials("rogier", encoder.encode(DEFAULT_PASSWORD))); // maatjes van Ties

    }

    public Credentials getCredentials(String userId) {
        return CREDENTIALS.stream()
                .filter(credentials -> credentials.getUserId().equals(userId))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
