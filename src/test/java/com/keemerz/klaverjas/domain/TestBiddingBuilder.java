package com.keemerz.klaverjas.domain;

import java.util.HashMap;
import java.util.Map;

import static com.keemerz.klaverjas.domain.Suit.CLUBS;

public class TestBiddingBuilder {

    private Suit proposedTrump = CLUBS;
    private Map<Seat, Bid> bids = new HashMap<>();

    public Bidding build() {
        return new Bidding(proposedTrump, bids);
    }

    public TestBiddingBuilder withProposedTrump(Suit trump) {
        this.proposedTrump = trump;
        return this;
    }

    public TestBiddingBuilder withBids(Map<Seat, Bid> bids) {
        this.bids = bids;
        return this;
    }

    public TestBiddingBuilder withBid(Seat seat, Bid bid) {
        this.bids.put(seat, bid);
        return this;
    }
}
