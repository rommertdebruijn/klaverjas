package com.keemerz.klaverjas.domain;

import com.keemerz.klaverjas.comparator.TrumpOrderComparator;

import java.util.*;
import java.util.stream.Collectors;

import static com.keemerz.klaverjas.domain.Seat.*;

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
                        turn = currentTrick.isTrickFinished() // trick ended
                                ? currentTrick.determineHighestCardSeat()
                                : turn.getLeftHandPlayer();
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
        if (bidding.getBids().size() == 4 &&
            !bidding.getAvailableSuits().isEmpty() &&
            bidding.getAvailableSuits().contains(forcedTrump)
        ) {
            bidding.setFinalTrump(forcedTrump);
            bidding.setFinalBidBy(seat);
            bidding.setAvailableSuits(new ArrayList<>());
            setTurn(getDealer().getLeftHandPlayer());
        }
    }

    public List<Card> determinePlayableCards(Seat player) {
        if (getTurn() != player) {
            return new ArrayList<>();
        }
        return determinePlayableCards(currentTrick, hands.get(player));
    }

    List<Card> determinePlayableCards(Trick currentTrick, List<Card> hand) {
        if (currentTrick == null || currentTrick.determineOpeningCard() == null || currentTrick.isTrickFinished()) {
            return hand; // if no card on the table, then anything goes
        }

        Suit openingSuit = currentTrick.determineOpeningCard().getSuit();
        if (handContainsSuit(hand, openingSuit)) {
            return followSuit(hand, currentTrick);
        } else if (partnerLeadsTrick(currentTrick)) {
            return hand; // maatslag
        } else {
            return playTrumpIfAllowed(hand,currentTrick);
        }
    }

    private boolean partnerLeadsTrick(Trick currentTrick) {
        Seat seatForHighestCard = currentTrick.determineHighestCardSeat();

        Seat currentPlayer = getTurn();
        Seat partnerSeat =
               currentPlayer == NORTH ? SOUTH :
               currentPlayer == EAST ? WEST :
               currentPlayer == SOUTH ? NORTH :
               EAST;
        return partnerSeat == seatForHighestCard;
    }

    private List<Card> followSuit(List<Card> hand, Trick currentTrick) {
        Suit trump = currentTrick.getTrump();
        Suit openingSuit = currentTrick.determineOpeningCard().getSuit();

        if (openingSuit == trump) {
            if (handContainsHigherTrump(hand, trump, currentTrick.determineHighestCard())) {
                return higherTrumpCards(hand, currentTrick);
            } else {
                return allCardsOfSuit(hand, trump);
            }
        } else {
            return allCardsOfSuit(hand, openingSuit);
        }
    }

    private List<Card> playTrumpIfAllowed(List<Card> hand, Trick currentTrick) {
        Suit trump = currentTrick.getTrump();

        if (currentTrick.determineHighestCard().getSuit() == trump) {
            if (handContainsHigherTrump(hand, trump, currentTrick.determineHighestCard())) {
                return higherTrumpCards(hand, currentTrick);
            } else {
                return allButTrumpCards(hand, trump);
            }
        } else if (handContainsSuit(hand, trump)) {
            return allCardsOfSuit(hand, trump);
        } else {
            return hand;
        }
    }

    private List<Card> higherTrumpCards(List<Card> hand, Trick currentTrick) {
        return allCardsOfSuit(hand, currentTrick.getTrump()).stream()
                .filter(card -> isHigherTrump(card, currentTrick.determineHighestCard()))
                .collect(Collectors.toList());
    }

    private boolean isHigherTrump(Card cardInHand, Card cardOnTable) {
        return new TrumpOrderComparator().compare(cardInHand, cardOnTable) > 0;
    }

    private boolean handContainsHigherTrump(List<Card> hand, Suit trump, Card highestCardOnTable) {
        List<Card> allTrumpsInHandPlusHighestCardOnTable = new ArrayList<>(allCardsOfSuit(hand, trump));
        allTrumpsInHandPlusHighestCardOnTable.add(highestCardOnTable);

        Card highestTrump = allTrumpsInHandPlusHighestCardOnTable.stream()
                .max(new TrumpOrderComparator())
                .orElseThrow(IllegalArgumentException::new);
        return highestTrump != highestCardOnTable;
    }

    private List<Card> allCardsOfSuit(List<Card> hand, Suit suit) {
        return hand.stream().filter(card -> card.getSuit() == suit).collect(Collectors.toList());
    }

    private boolean handContainsSuit(List<Card> hand, Suit suit) {
        return hand.stream().anyMatch(card -> card.getSuit() == suit);
    }

    private List<Card> allButTrumpCards(List<Card> hand, Suit trump) {
        List<Card> allButTrump = new ArrayList<>(hand);
        allButTrump.removeAll(allCardsOfSuit(hand, trump));
        return allButTrump; // "ondertroeven" not allowed
    }

}