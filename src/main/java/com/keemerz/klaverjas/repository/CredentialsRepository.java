package com.keemerz.klaverjas.repository;

import com.keemerz.klaverjas.domain.Credentials;
import com.keemerz.klaverjas.domain.Player;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CredentialsRepository {

    private static final CredentialsRepository INSTANCE = new CredentialsRepository();
    private static final List<Credentials> CREDENTIALS = new ArrayList<>();
    static {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        // for now, actual security is of no importance.
        // What matters is that the logged in user has a Principal, which is required for individual web sockets to work :)
        CREDENTIALS.add(new Credentials("user1", encoder.encode("bier")));
        CREDENTIALS.add(new Credentials("user2", encoder.encode("bier")));
        CREDENTIALS.add(new Credentials("user3", encoder.encode("bier")));
        CREDENTIALS.add(new Credentials("user4", encoder.encode("bier")));
        CREDENTIALS.add(new Credentials("martin", encoder.encode("bier")));
        CREDENTIALS.add(new Credentials("alex", encoder.encode("bier")));
        CREDENTIALS.add(new Credentials("ronald", encoder.encode("bier")));
        CREDENTIALS.add(new Credentials("eelco", encoder.encode("bier")));
        CREDENTIALS.add(new Credentials("robert", encoder.encode("bier")));
        CREDENTIALS.add(new Credentials("rommert", encoder.encode("bier")));
        CREDENTIALS.add(new Credentials("toon", encoder.encode("bier")));
        CREDENTIALS.add(new Credentials("jasper", encoder.encode("bier")));
        CREDENTIALS.add(new Credentials("yoran", encoder.encode("bier")));
        CREDENTIALS.add(new Credentials("paul", encoder.encode("bier")));
        CREDENTIALS.add(new Credentials("joris", encoder.encode("bier")));
        CREDENTIALS.add(new Credentials("frank", encoder.encode("bier")));
        CREDENTIALS.add(new Credentials("janthijs", encoder.encode("bier")));
        CREDENTIALS.add(new Credentials("anton", encoder.encode("bier")));
        CREDENTIALS.add(new Credentials("edzo", encoder.encode("bier")));
        CREDENTIALS.add(new Credentials("marc", encoder.encode("bier")));
        CREDENTIALS.add(new Credentials("sander", encoder.encode("bier")));
        CREDENTIALS.add(new Credentials("mitchell", encoder.encode("bier")));
        CREDENTIALS.add(new Credentials("jonathan", encoder.encode("bier")));
        CREDENTIALS.add(new Credentials("niek", encoder.encode("bier")));
        CREDENTIALS.add(new Credentials("stefan", encoder.encode("bier")));
        CREDENTIALS.add(new Credentials("ties", encoder.encode("bier")));
        CREDENTIALS.add(new Credentials("hans", encoder.encode("bier")));
        CREDENTIALS.add(new Credentials("mark", encoder.encode("bier")));
        CREDENTIALS.add(new Credentials("matthijs", encoder.encode("bier")));
        CREDENTIALS.add(new Credentials("sidney", encoder.encode("bier"))); // maatjes van Ties
        CREDENTIALS.add(new Credentials("rogier", encoder.encode("bier"))); // maatjes van Ties

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
