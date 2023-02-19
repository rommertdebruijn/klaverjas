package com.keemerz.klaverjas.repository;

import com.keemerz.klaverjas.domain.Credentials;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

@Component
public class CredentialsRepository {

    private static final List<Credentials> CREDENTIALS = new ArrayList<>();

    public static final String DEFAULT_PASSWORD = "bier";

    static {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        // for now, actual security is of no importance.
        // What matters is that the logged in user has a Principal, which is required for individual web sockets to work :)

        Stream.of(
            "user1",
            "user2",
            "user3",
            "user4",
            "martin",
            "alex",
            "ronald",
            "eelco",
            "robert",
            "rommert",
            "toon",
            "jasper",
            "yoran",
            "paul",
            "joris",
            "frank",
            "janthijs",
            "anton",
            "edzo",
            "marc",
            "sander",
            "mitchell",
            "jonathan",
            "niek",
            "stefan",
            "ties",
            "hans",
            "mark",
            "matthijs",
            "sidney", // maatjes van Ties
            "rogier" // maatjes van Ties
        )
        .map(u -> new Credentials(u, encoder.encode(DEFAULT_PASSWORD)))
        .forEach(CREDENTIALS::add);
    }

    public Credentials getCredentials(String userId) {
        return CREDENTIALS.stream()
                .filter(credentials -> credentials.getUserId().equals(userId))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
