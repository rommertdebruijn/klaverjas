package com.keemerz.klaverjas.encoding;

import com.keemerz.klaverjas.domain.Card;
import com.keemerz.klaverjas.domain.Deck;
import com.keemerz.klaverjas.domain.ShuffledDeck;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

class DeckSignatureTest {

    @Test
    void smokeTestEncryptDecrypt() {
        Deck deck = new ShuffledDeck();
        List<Card> expected = deck.getCards();

        String encryptedCards = DeckSignature.encrypt(expected);
        List<Card> actual = DeckSignature.decrypt(encryptedCards);

        assertThat(actual, is(expected));
    }
}