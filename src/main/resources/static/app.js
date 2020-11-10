var stompTopicClient = null; // for general info, like "player plays card X"
var stompQueueClient = null; // for user-specific info, like cards in hand
var gameState = null;

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);

    $("#generalInfo").html("");
}

function connectToLobby() {
    showIf(true, "#lobby");
    showIf(false, "#table");

    var socket = new SockJS('/gs-guide-websocket');
    stompTopicClient = Stomp.over(socket);
    stompTopicClient.connect({}, function (frame) {
        setConnected(true);
        console.log('Connected: ' + frame);
        stompTopicClient.subscribe('/topic/lobby', handleLobbyMessage);

        stompTopicClient.send("/app/lobby/hello", {},  JSON.stringify({}));
    });
}

function handleLobbyMessage(lobbyMessage) {
    var msg = JSON.parse(lobbyMessage.body);
    if (msg.messageType === 'WELCOME') {
        showWelcomeMessage(msg.content);
        updateLoggedInPlayers(msg.loggedInPlayers);
        updateActiveGames(msg.activeGames);
    }
    if (msg.messageType === 'GOODBYE') {
        showGoodbyeMessage(msg.content);
        updateLoggedInPlayers(msg.loggedInPlayers);
        updateActiveGames(msg.activeGames);
    }
    if (msg.messageType === 'ACTIVE_GAMES') {
        updateActiveGames(msg.activeGames);
    }
}

function connectToGameInfo() {
    var socket = new SockJS('/gs-guide-websocket');
    stompQueueClient = Stomp.over(socket);
    stompQueueClient.connect({}, function (frame) {
        setConnected(true);
        console.log('Connected: ' + frame);
        stompQueueClient.subscribe('/user/topic/game', handleGameState);
    });
}

function disconnect() {
    stompQueueClient.send("/app/lobby/goodbye", {},  JSON.stringify({}));

    if (stompTopicClient !== null) {
        stompTopicClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

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
    var gameInProgress = gameState.playerStillPlaying;

    showIf(!gameInProgress, "#connections");
    showIf(!gameInProgress, "#lobby");
    showIf(gameInProgress, "#table");

    if (gameInProgress) {
        showGameState(gameState);
    } else {
        gameState = null;
    }
}

function showWelcomeMessage(message) {
    $("#generalInfo").append("<tr><td>" + message + "</td></tr>");
}

function showGoodbyeMessage(message) {
    $("#generalInfo").append("<tr><td>" + message + "</td></tr>");
}

function updateActiveGames(activeGames) {
    $("#gamesInfo").empty();
    for (var j=0;j<activeGames.length;j++) {
        showActiveGame(activeGames[j]);
    }
}

function updateLoggedInPlayers(loggedInPlayers) {
    $("#playersInfo").empty();
    for (var i=0;i<loggedInPlayers.length;i++) {
        showPlayer(loggedInPlayers[i]);
    }
}

function showActiveGame(activeGame) {
    var playerString = "";
    for (var i=0;i<activeGame.playerNames.length;i++) {
        playerString = playerString + activeGame.playerNames[i] + " "
    }

    var elementId = 'join-' + activeGame.gameId;
    $("#gamesInfo").append("" +
        "<tr>" +
        "<td>" + playerString + "</td>" +
        "<td><button id=\"" + elementId + "\" class=\"btn btn-default\" type=\"submit\">Join</button></td>" +
        "</tr>");
    $('#' + elementId).click(function() {
      joinGame(activeGame.gameId);
    });
}

function showPlayer(playerName) {
    $("#playersInfo").append("<tr><td>" + playerName + "</td></tr>");
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

    if (cardId) {
        $("#card-" + cardId).click(function() {
            playCard(gameState.gameId, cardId);
        });
    }
}

function renderCardOnTable(card, containerId) {
    $(containerId).empty();
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

function renderCurrentTrick(gameState) {
    renderCardOnTable(gameState.currentTrick.cardsPlayed['NORTH'], "#trickCardNorth");
    renderCardOnTable(gameState.currentTrick.cardsPlayed['EAST'], "#trickCardEast");
    renderCardOnTable(gameState.currentTrick.cardsPlayed['SOUTH'], "#trickCardSouth");
    renderCardOnTable(gameState.currentTrick.cardsPlayed['WEST'], "#trickCardWest");
}

function renderBidding(bidding) {
    $("#bidding").empty();
    $("#bidding").append("<div>Spelen op</div><div class=\"trump\">" + getSuitCharacter(bidding.proposedTrump) + "</div>");
}

function showGameState(state) {
    $("#player-south").text(state.players['SOUTH']);
    $("#player-west").text(state.players['WEST']);
    $("#player-north").text(state.players['NORTH']);
    $("#player-east").text(state.players['EAST']);

    if (state.bidding) {
        alert("bidding has started!");
        renderBidding(state.bidding);
    } else {
        renderCurrentTrick(state);
    }

    if (state.hand.length > 0) {
        $("#cards-south").empty();
        for (var i=0;i<state.hand.length;i++) {
            renderCardInHand(state.hand[i]);
        }
    }
}

$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $( "#connect" ).click(function() {
        connectToLobby();
        connectToGameInfo();
    });
    $( "#disconnect" ).click(function() { disconnect(); });
    $( "#startGame" ).click(function() { startGame(); });
    $( "#leaveGame" ).click(function() { leaveGame(); });

    showIf(true, "#connections");
    showIf(false, "#lobby");
    showIf(false, "#table");
});

function showIf(condition, element) {
    if (condition) {
        $(element).show();
    } else {
        $(element).hide();
    }
}