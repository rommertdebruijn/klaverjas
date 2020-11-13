package com.keemerz.klaverjas.domain;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.keemerz.klaverjas.domain.Seat.*;
import static com.keemerz.klaverjas.domain.Suit.CLUBS;

public class GameState {

    private String gameId;
    private Bidding bidding;
    private Map<Seat, List<Card>> hands = new HashMap<>();
    private Map<Seat, Player> players = new HashMap<>();
    private Seat dealer = WEST;
    private List<Trick> previousTricks = new ArrayList<>();
    private Seat turn = NORTH;
    private Trick currentTrick;

    public GameState(String gameId) {
        this.gameId = gameId;
    }

    public static GameState createNewGame() {
        return new GameState(UUID.randomUUID().toString());
    }

    public String getGameId() {
        return gameId;
    }

    public Bidding getBidding() {
        return bidding;
    }

    public GameState setBidding(Bidding bidding) {
        this.bidding = bidding;
        return this;
    }

    public Map<Seat, List<Card>> getHands() {
        return hands;
    }

    public void setHands(Map<Seat, List<Card>> hands) {
        this.hands = hands;
    }

    public Map<Seat, Player> getPlayers() {
        return players;
    }

    public void setPlayers(Map<Seat, Player> players) {
        this.players = players;
    }

    public Seat getDealer() {
        return dealer;
    }

    public void setDealer(Seat dealer) {
        this.dealer = dealer;
    }

    public List<Trick> getPreviousTricks() {
        return previousTricks;
    }

    public void setPreviousTricks(List<Trick> previousTricks) {
        this.previousTricks = previousTricks;
    }

    public Seat getTurn() {
        return turn;
    }

    public void setTurn(Seat turn) {
        this.turn = turn;
    }

    public Trick getCurrentTrick() {
        return currentTrick;
    }

    public void setCurrentTrick(Trick currentTrick) {
        this.currentTrick = currentTrick;
    }

    public void dealHands() {
        ShuffledDeck deck = new ShuffledDeck();
        List<Card> cards = deck.getCards();

        // OK, it's not 3-2-3, but hey
        for (int card = 0; card< cards.size();) {
            for (Seat seat : Seat.values()) {
                List<Card> hand = hands.getOrDefault(seat, new ArrayList<>());
                hand.add(cards.get(card));
                hands.put(seat, hand);
                card++;
            }
        }
    }

    public void fillSeat(Player player) {
        Arrays.stream(Seat.values())
        .filter(seat -> players.get(seat) == null)
        .findFirst()
        .ifPresent(seat -> players.put(seat, player));
    }

    public void freeSeat(Player player) {
        Seat availableSeat = players.entrySet().stream()
                .filter(entry -> entry.getValue().getPlayerId().equals(player.getPlayerId()))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new).getKey();
        players.remove(availableSeat);
    }

    public List<String> determinePlayerIds() {
        List<String> playerIds = players.values().stream()
                .map(Player::getPlayerId)
                .collect(Collectors.toList());
        return Collections.unmodifiableList(playerIds);
    }

    public Seat getAbsoluteSeatForPlayer(String playerId) {
        return getPlayers().entrySet().stream().
                filter(entry -> entry.getValue().getPlayerId().equals(playerId))
                .findFirst()
                .map(Map.Entry::getKey)
                .orElseThrow(IllegalArgumentException::new);
    }

    public void joinGame(Player player) {
        if (!determinePlayerIds().contains(player.getPlayerId())) {
            fillSeat(player);
            if (getHands().isEmpty() && getPlayers().size() > 3) { // if 4th player joins, deal first hand
                dealHands();
                setBidding(Bidding.createFirstGameBidding()); // first game always clubs
            }
        }
    }

    public void playCard(String cardId) {
        if (getBidding().getFinalTrump() != null) {
            Seat seat = getTurn(); // only player whose turn it is can play cards
            List<Card> cards = getHands().get(seat);
            cards.stream()
                    .filter(card -> card.getCardId().equals(cardId))
                    .findFirst()
                    .ifPresent(card -> {
                        if (currentTrick == null || currentTrick.getCardsPlayed().size() == 4) {
                            // copy current trick to previousTricks
                            currentTrick = new Trick(bidding.getFinalTrump(), seat, new HashMap<>());
                        }

                        getHands().get(seat).remove(card);
                        currentTrick.getCardsPlayed().put(seat, card);
                        turn = turn.getLeftHandPlayer(); // todo if trick ends, determine winner
                    });
        }
    }

    public void makeBid(Bid bid) {
        Seat seat = getTurn();
        Bidding bidding = getBidding();
        if (bidding.getBids().size() < 4 && bidding.getBids().get(seat) == null) {
            bidding.addBid(seat, bid);

            if (bid == Bid.PLAY) {
                bidding.setFinalTrump(bidding.getProposedTrump());
                bidding.setFinalBidBy(seat);
                setTurn(getDealer().getLeftHandPlayer()); // bidding ended, let's play
            } else {
                if (bidding.getBids().size() == 4) { // Starting player is forced to play from the remaining 3 suits
                    List<Suit> availableSuits = new ArrayList<>(Arrays.asList(Suit.values()));
                    availableSuits.remove(bidding.getProposedTrump());
                    bidding.setAvailableSuits(availableSuits);
                }
                setTurn(seat.getLeftHandPlayer()); // next bidder please
            }
        }
    }

    public void makeForcedBid(Suit forcedTrump) {
        Seat seat = getTurn();
        Bidding bidding = getBidding();
        if (bidding.getBids().size() == 4 && !bidding.getAvailableSuits().isEmpty()) {
            bidding.setFinalTrump(forcedTrump);
            bidding.setFinalBidBy(seat);
            setTurn(getDealer().getLeftHandPlayer());
        }
    }
}
