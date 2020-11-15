package com.keemerz.klaverjas.repository;

import com.keemerz.klaverjas.domain.Credentials;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.List;

public class CredentialsRepository {

/*
martin
alex
ronald
Eelco
robert
rommert
Toon
Jasper
yoran
paul
joris
Frank
Janthijs
Anton
Edzo
Marc
Sander
Michell
Jonathan
Niek


 */


    private static final CredentialsRepository INSTANCE = new CredentialsRepository();
    private static final List<Credentials> CREDENTIALS = new ArrayList<>();
    static {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        CREDENTIALS.add(new Credentials("user1", encoder.encode("pass1")));
        CREDENTIALS.add(new Credentials("user2", encoder.encode("pass2")));
        CREDENTIALS.add(new Credentials("user3", encoder.encode("pass3")));
        CREDENTIALS.add(new Credentials("user4", encoder.encode("pass4")));
        CREDENTIALS.add(new Credentials("martin", encoder.encode("Martin")));
        CREDENTIALS.add(new Credentials("alex", encoder.encode("Alex")));
        CREDENTIALS.add(new Credentials("ronald", encoder.encode("Ronald")));
        CREDENTIALS.add(new Credentials("eelco", encoder.encode("Eelco")));
        CREDENTIALS.add(new Credentials("robert", encoder.encode("Robert")));
        CREDENTIALS.add(new Credentials("rommert", encoder.encode("Rommert")));
        CREDENTIALS.add(new Credentials("toon", encoder.encode("Toon")));
        CREDENTIALS.add(new Credentials("jasper", encoder.encode("Jasper")));
        CREDENTIALS.add(new Credentials("yoran", encoder.encode("Yoran")));
        CREDENTIALS.add(new Credentials("paul", encoder.encode("Paul")));
        CREDENTIALS.add(new Credentials("joris", encoder.encode("Joris")));
        CREDENTIALS.add(new Credentials("frank", encoder.encode("Frank")));
        CREDENTIALS.add(new Credentials("janthijs", encoder.encode("Janthijs")));
        CREDENTIALS.add(new Credentials("anton", encoder.encode("Anton")));
        CREDENTIALS.add(new Credentials("edzo", encoder.encode("Edzo")));
        CREDENTIALS.add(new Credentials("marc", encoder.encode("Marc")));
        CREDENTIALS.add(new Credentials("sander", encoder.encode("Sander")));
        CREDENTIALS.add(new Credentials("michell", encoder.encode("Michell")));
        CREDENTIALS.add(new Credentials("jonathan", encoder.encode("Jonathan")));
        CREDENTIALS.add(new Credentials("niek", encoder.encode("Niek")));
        CREDENTIALS.add(new Credentials("stefan", encoder.encode("Stefan")));

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
