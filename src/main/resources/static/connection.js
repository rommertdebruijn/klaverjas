var stompTopicClient = null; // for general info, like 'player joined lobby'
var stompQueueClient = null; // for user-specific info, like (per-player) gameState

function cleanGeneralInfo() {
    $('#generalInfo').html('');
}

function connectToLobby() {
    $('#lobby').show();
    $('#table').hide();

    var socket = new SockJS('/klaverjas-websocket');
    stompTopicClient = Stomp.over(socket);
    stompTopicClient.connect({}, function (frame) {
        cleanGeneralInfo();
        console.log('Connected: ' + frame);
        stompTopicClient.subscribe('/topic/lobby', handleLobbyMessage);

        stompTopicClient.send('/app/lobby/hello', {},  JSON.stringify({}));
    },
    clearStaleConnectionsOnError());
}

function connectToGameInfo() {
    var socket = new SockJS('/klaverjas-websocket');
    stompQueueClient = Stomp.over(socket);
    stompQueueClient.connect({}, function (frame) {
        cleanGeneralInfo();
        console.log('Connected: ' + frame);
        stompQueueClient.subscribe('/user/topic/game', handleGameState);

        if (gameState) {
            requestState();
        }
    },
    clearStaleConnectionsOnError());
}

function clearStaleConnectionsOnError() {
    return function () {
        stompTopicClient = null;
        stompQueueClient = null;
    };
}

function requestState() {
    stompTopicClient = Stomp.over(new SockJS('/klaverjas-websocket'));
    stompTopicClient.connect({}, function (frame) {
        stompTopicClient.subscribe('/topic/lobby', handleLobbyMessage);
    },
    clearStaleConnectionsOnError());

    stompQueueClient = Stomp.over(new SockJS('/klaverjas-websocket'));
    stompQueueClient.connect({}, function (frame) {
        stompQueueClient.subscribe('/user/topic/game', handleGameState);
        if (!!gameState) {
            stompQueueClient.send('/app/game/requestState', {}, JSON.stringify({'gameId': gameState.gameId}));
        }
    },
    clearStaleConnectionsOnError());
}

$(function () {
    $('form').on('submit', function (e) {
        e.preventDefault();
    });

     connectToLobby();
     connectToGameInfo();

    $( '#leaveLobby' ).click(function() { leaveLobby();  });
    $( '#startGame' ).click(function() { startGame(); });
    $( '#leaveGame' ).click(function() { leaveGame(); });

    $('#lobby').show();
    $('#table').hide();

    setInterval(function() {
        if (!stompTopicClient || !stompQueueClient) {
            console.log('Auto-reconnecting...');
            requestState();
            console.log('connected');
        }
    }, 3000);
});