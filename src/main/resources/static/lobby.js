function leaveLobby() {
    stompTopicClient.send('/app/lobby/goodbye', {},  JSON.stringify({}));

    if (stompTopicClient !== null) {
        stompTopicClient.disconnect();
    }
    if (stompQueueClient !== null) {
        stompQueueClient.disconnect();
    }
    console.log("Disconnected");
    window.location.replace("logout.html")
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

function showWelcomeMessage(message) {
    $('#generalInfo').append('<tr><td>' + message + '</td></tr>');
}

function showGoodbyeMessage(message) {
    $('#generalInfo').append('<tr><td>' + message + '</td></tr>');
}

function updateActiveGames(activeGames) {
    $('#gamesInfo').empty();
    for (const activeGame of activeGames) {
        showActiveGame(activeGame);
    }
}

function updateLoggedInPlayers(loggedInPlayers) {
    $('#playersInfo').empty();
    for (const loggedInPlayer of loggedInPlayers) {
        showPlayer(loggedInPlayer);
    }
}

function showActiveGame(activeGame) {
    var playerString = '';
    for (const playerName of activeGame.playerNames) {
        playerString = playerString + playerName + ' '
    }
    var elementId = 'join-' + activeGame.gameId;

    var joinButtonHtml = '<div id="' + elementId + '" class="buttonLight">Meedoen</div>';
    var activeGameHtml = '' +
        '<tr>' +
        '  <td>' + playerString + '</td>' +
        '  <td>' + joinButtonHtml + '</td>' +
        '</tr>';
    $('#gamesInfo').append(activeGameHtml);


    $('#' + elementId).click(function () {
        joinGame(activeGame.gameId);
    });
}

function showPlayer(playerName) {
    $('#playersInfo').append('<tr><td>' + playerName + '</td></tr>');
}