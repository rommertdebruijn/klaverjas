var gameState = null;

function startGame() {
    stompQueueClient.send("/app/game/start", {}, JSON.stringify({}));
}

function joinGame(gameId) {
    stompQueueClient.send("/app/game/join", {}, JSON.stringify({'gameId': gameId }));
}

function leaveGame() {
    stompQueueClient.send("/app/game/leave", {}, JSON.stringify({'gameId': gameState.gameId }));
}

function handleGameState(gameStateMessage) { // *player* game state!
    gameState = JSON.parse(gameStateMessage.body);
    if (gameState.playerStillPlaying) {
        $("#connections").hide();
        $("#lobby").hide();
        $("#table").show();
        showGameState(gameState);
    } else {
        $("#connections").show();
        $("#lobby").show();
        $("#table").hide();
        gameState = null;
    }
}

function getSuitCharacter(suit) {
    if (suit === "SPADES") {
        return "&spades;";
    }
    if (suit === "HEARTS") {
        return "&hearts;";
    }
    if (suit === "DIAMONDS") {
        return "&diams;";
    }
    if (suit === "CLUBS") {
        return "&clubs;";
    }
}

function playCard(gameId, cardId) {
    stompQueueClient.send("/app/game/playcard", {}, JSON.stringify({ 'gameId' : gameId, 'cardId' : cardId }));
}

function renderCardInHand(card) {
    var cardId = card.cardId;
    var cardHtml = "" +
        "<div id=\"card-" + cardId + "\" class=\"card-in-hand " + card.suit + "\">" +
        "  <div class=\"suit\">" +
            getSuitCharacter(card.suit) +
        "  </div>" +
        "  <div class=\"rank\">" +
            card.rank +
        "  </div>" +
        "</div>";
    $("#cards-south").append(cardHtml);

    if (isPlayerTurn() && cardId) {
        $("#card-" + cardId).click(function() {
            playCard(gameState.gameId, cardId);
        });
    }
}

function renderCardOnTable(containerId, card) {
    if (card) {
        var cardHtml ="" +
            "<div class=\"card-on-table " + card.suit + "\">" +
            "  <div class=\"suit\">" +
            getSuitCharacter(card.suit) +
            "  </div>" +
            "  <div class=\"rank\">" +
            card.rank +
            "  </div>" +
            "</div>";
        $(containerId).append(cardHtml);
    }
}

function renderCurrentTrick(currentTrick) {
    renderCardOnTable("#tableNorth", currentTrick.cardsPlayed['NORTH']);
    renderCardOnTable("#tableEast", currentTrick.cardsPlayed['EAST']);
    renderCardOnTable("#tableSouth", currentTrick.cardsPlayed['SOUTH']);
    renderCardOnTable("#tableWest", currentTrick.cardsPlayed['WEST']);
}

function renderPlayerBidOnTable(elementId, bid) {
    if (!!bid) {
        $(elementId).append("<div>" + bid + "</div>");
    }
}

function renderProposedTrump(proposedTrump) {
    $("#bidding").append("<div>Spelen op deze troef?</div><div class=\"trump\">" + getSuitCharacter(proposedTrump) + "</div>");
}

function renderContract(bidding) {
    $("#bidding").append("<div>" + gameState.players[bidding.finalBidBy] + " speelt op</div><div class=\"trump\">" + getSuitCharacter(bidding.finalTrump) + "</div>");
}

function renderBidding(bidding) {
    if (!bidding.finalTrump) {
        renderProposedTrump(bidding.proposedTrump);
        renderPlayerBidOnTable("#tableNorth", bidding.bids["NORTH"]);
        renderPlayerBidOnTable("#tableEast", bidding.bids["EAST"]);
        renderPlayerBidOnTable("#tableSouth", bidding.bids["SOUTH"]);
        renderPlayerBidOnTable("#tableWest", bidding.bids["WEST"]);
    } else {
        renderContract(bidding);
    }
}

function renderCurrentPlayerHand(hand) {
    $("#cards-south").empty();
    if (!!hand && hand.length > 0) {
        for (var i = 0; i < hand.length; i++) {
            renderCardInHand(hand[i]);
        }
    }
}

function renderPlayPassOptions() {
    var $bidding = $("#bidding-box");
    $bidding.append("<div id=\"bidPlay\" class=\"action\">[SPEEL]</div>");
    $("#bidPlay").click(function() {
       makeBid("PLAY");
    });

    $bidding.append("<div id=\"bidPass\" class=\"action\">[PAS]</div>");
    $("#bidPass").click(function() {
        makeBid("PASS");
    });
}

function makeBid(bid) {
    stompQueueClient.send("/app/game/makebid", {}, JSON.stringify({ 'gameId' : gameState.gameId, 'bid' : bid }));
}

function renderForcedPickOptions(availableSuits) {
    alert("forced play for you!");
    for (var i;i<availableSuits.length;i++) {
        $("#bidding-box").append("<div>[PLAY " + getSuitCharacter(availableSuits[i]) + "]</div>");
    }
}

function renderBiddingBox(bidding) {
    $("#bidding-box").empty();
    if (!bidding.finalTrump && isPlayerTurn()) {
        if (bidding.bids["SOUTH"] == null) { // we haven't placed a bid yet
            renderPlayPassOptions();
        } else {
            renderForcedPickOptions(bidding.availableSuits); // south is on the curb ;)
        }
    }
}

function isPlayerTurn() {
    return !!gameState && !!gameState.turn && gameState.turn === "SOUTH";
}

function cleanTable() {
    $("#tableNorth").empty();
    $("#tableEast").empty();
    $("#tableSouth").empty();
    $("#tableWest").empty();
    $("#bidding").empty();
}

function showGameState(state) {
    $("#player-south").text(state.players['SOUTH']);
    $("#player-west").text(state.players['WEST']);
    $("#player-north").text(state.players['NORTH']);
    $("#player-east").text(state.players['EAST']);

    cleanTable();
    if (!bidding.finalTrump) {
        renderBidding(state.bidding);
        renderBiddingBox(state.bidding);
    }
    if (state.currentTrick) {
        renderCurrentTrick(state.currentTrick);
    }
    renderCurrentPlayerHand(state.hand);
}