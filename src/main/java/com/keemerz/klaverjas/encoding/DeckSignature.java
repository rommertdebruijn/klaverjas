package com.keemerz.klaverjas.encoding;

import com.keemerz.klaverjas.domain.*;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

public class DeckSignature {

    public static final String SECRET = "NotReallyASecret";

    public static String encrypt(List<Card> cards) {
        String cardsSignature = cards.stream()
                .map(DeckSignature::toSignatureFragment)
                .reduce("", (result, fragment) -> result + fragment);

        return encrypt(cardsSignature);
    }

    public static List<Card> decrypt(String base64AndEncryptedSignature) {
        try {
            // first unwrap the BASE64
            byte[] encryptedSignature = Base64.getDecoder().decode(base64AndEncryptedSignature);

            Key aesKey = new SecretKeySpec(SECRET.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES");

            // decrypt the text
            cipher.init(Cipher.DECRYPT_MODE, aesKey);
            String decryptedSignature = new String(cipher.doFinal(encryptedSignature));
            return convertToCards(decryptedSignature);
        } catch (NoSuchAlgorithmException | InvalidKeyException | NoSuchPaddingException | BadPaddingException | IllegalBlockSizeException e) {
            throw new RuntimeException(e);
        }
    }

    private static String encrypt(String cardsSignature) {
        try {
            // Create key and cipher
            Key aesKey = new SecretKeySpec(SECRET.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            // encrypt the text
            cipher.init(Cipher.ENCRYPT_MODE, aesKey);
            byte[] encrypted = cipher.doFinal(cardsSignature.getBytes());
            byte[] base64encrypted = Base64.getEncoder().encode(encrypted);
            return new String(base64encrypted, StandardCharsets.UTF_8);
        } catch (NoSuchAlgorithmException | InvalidKeyException | NoSuchPaddingException | BadPaddingException | IllegalBlockSizeException e) {
            throw new RuntimeException(e);
        }
    }

    private static List<Card> convertToCards(String decryptedSignature) {
        List<String> fragments = new ArrayList<>();
        for (int position = 0; position < decryptedSignature.length(); position +=2) {
            fragments.add(decryptedSignature.substring(position, position+2));
        }
        return fragments.stream()
                .map(DeckSignature::toCard)
                .collect(Collectors.toList());
    }

    private static Card toCard(String cardFragment) {
        Suit suit = Suit.fromAbbreviation(cardFragment.substring(0, 1));
        Rank rank = Rank.fromAbbreviation(cardFragment.substring(1, 2));
        return Card.of(suit, rank);
    }

    private static String toSignatureFragment(Card card) {
        return card.getSuit().getAbbreviation() + card.getRank().getAbbreviation();
    }
}

