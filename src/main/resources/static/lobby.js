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

function connectToLobby() {
    $('#lobby').show();
    $('#table').hide();

    var socket = new SockJS('/gs-guide-websocket');
    stompTopicClient = Stomp.over(socket);
    stompTopicClient.connect({}, function (frame) {
        setConnected(true);
        console.log('Connected: ' + frame);
        stompTopicClient.subscribe('/topic/lobby', handleLobbyMessage);

        stompTopicClient.send('/app/lobby/hello', {},  JSON.stringify({}));
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

function showWelcomeMessage(message) {
    $('#generalInfo').append('<tr><td>' + message + '</td></tr>');
}

function showGoodbyeMessage(message) {
    $('#generalInfo').append('<tr><td>' + message + '</td></tr>');
}

function updateActiveGames(activeGames) {
    $('#gamesInfo').empty();
    for (var j=0;j<activeGames.length;j++) {
        showActiveGame(activeGames[j]);
    }
}

function updateLoggedInPlayers(loggedInPlayers) {
    $('#playersInfo').empty();
    for (var i=0;i<loggedInPlayers.length;i++) {
        showPlayer(loggedInPlayers[i]);
    }
}

function showActiveGame(activeGame) {
    var playerString = '';
    for (var i=0;i<activeGame.playerNames.length;i++) {
        playerString = playerString + activeGame.playerNames[i] + ' '
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