package com.keemerz.klaverjas.domain;

import java.util.*;

import static com.keemerz.klaverjas.domain.Seat.*;
import static com.keemerz.klaverjas.domain.Seat.EAST;
import static com.keemerz.klaverjas.domain.Suit.CLUBS;

public class Bidding {

    private Suit proposedTrump;
    private Map<Seat, Bid> bids = new HashMap<>();

    public static Bidding createFirstGameBidding() {
        return createBidding(CLUBS, new HashMap<>());
    }

    public static Bidding createBidding() {
        List<Suit> randomList = Arrays.asList(Suit.values());
        Collections.shuffle(randomList);
        Suit randomTrump = randomList.get(0);
        return createBidding(randomTrump, new HashMap<>());
    }

    private static Bidding createBidding(Suit proposedTrump, HashMap<Seat, Bid> bids) {
        return new Bidding(proposedTrump, bids);
    }

    Bidding(Suit proposedTrump, Map<Seat, Bid> bids) {
        this.proposedTrump = proposedTrump;
        this.bids = bids;
    }

    public Suit getProposedTrump() {
        return proposedTrump;
    }

    public Bidding setProposedTrump(Suit proposedTrump) {
        this.proposedTrump = proposedTrump;
        return this;
    }

    public Map<Seat, Bid> getBids() {
        return bids;
    }

    public Bidding setBids(Map<Seat, Bid> bids) {
        this.bids = bids;
        return this;
    }

    public Bidding rotateForSeat(Seat currentPlayerSeat) {
        Map<Seat, Bid> rotatedBids = new HashMap<>();
        rotatedBids.put(SOUTH, bids.get(currentPlayerSeat));
        rotatedBids.put(WEST, bids.get(currentPlayerSeat.getLeftHandPlayer()));
        rotatedBids.put(NORTH, bids.get(currentPlayerSeat.getPartner()));
        rotatedBids.put(EAST, bids.get(currentPlayerSeat.getRightHandPlayer()));

        return new Bidding(this.proposedTrump, rotatedBids); // startingPlayer is only useful when calculating winner. No need to pass it to PlayerGameState
    }
}
