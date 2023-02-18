package com.keemerz.klaverjas.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.keemerz.klaverjas.domain.Seat.*;

public class Bidding {

    private Suit proposedTrump;
    private List<Suit> availableSuits = new ArrayList<>();
    private Map<Seat, Bid> bids = new HashMap<>();
    private Suit finalTrump;
    private Seat finalBidBy;

    Bidding(Suit proposedTrump, List<Suit> availableSuits, Map<Seat, Bid> rotatedBids, Suit finalTrump, Seat finalBidBy) {
        this.proposedTrump = proposedTrump;
        this.availableSuits = availableSuits;
        bids = rotatedBids;
        this.finalTrump = finalTrump;
        this.finalBidBy = finalBidBy;
    }



    Bidding(Suit proposedTrump) {
        this.proposedTrump = proposedTrump;
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

    public List<Suit> getAvailableSuits() {
        return availableSuits;
    }

    public Suit getFinalTrump() {
        return finalTrump;
    }

    public void setFinalTrump(Suit finalTrump) {
        this.finalTrump = finalTrump;
    }

    public Seat getFinalBidBy() {
        return finalBidBy;
    }

    public void setFinalBidBy(Seat finalBidBy) {
        this.finalBidBy = finalBidBy;
    }

    public void setAvailableSuits(List<Suit> availableSuits) {
        this.availableSuits = availableSuits;
    }

    public Bidding rotateForSeat(Seat currentPlayerSeat) {
        Map<Seat, Bid> rotatedBids = new HashMap<>();
        rotatedBids.put(SOUTH, bids.get(currentPlayerSeat));
        rotatedBids.put(WEST, bids.get(currentPlayerSeat.getLeftHandPlayer()));
        rotatedBids.put(NORTH, bids.get(currentPlayerSeat.getPartner()));
        rotatedBids.put(EAST, bids.get(currentPlayerSeat.getRightHandPlayer()));

        Seat rotatedFinalBidBy = finalBidBy != null ? finalBidBy.rotateForSeat(currentPlayerSeat) : null;

        return new Bidding(this.proposedTrump, this.availableSuits, rotatedBids, finalTrump, rotatedFinalBidBy); // startingPlayer is only useful when calculating winner. No need to pass it to PlayerGameState
    }

    public void addBid(Seat seat, Bid bid) {
        bids.put(seat, bid);
    }
}
