var stompTopicClient = null; // for general info, like 'player plays card X'
var stompQueueClient = null; // for user-specific info, like cards in hand

function setConnected(connected) {
    $('#generalInfo').html('');
}

function connectToLobby() {
    $('#lobby').show();
    $('#table').hide();

    var socket = new SockJS('/klaverjas-websocket');
    stompTopicClient = Stomp.over(socket);
    stompTopicClient.connect({}, function (frame) {
        setConnected(true);
        console.log('Connected: ' + frame);
        stompTopicClient.subscribe('/topic/lobby', handleLobbyMessage);

        stompTopicClient.send('/app/lobby/hello', {},  JSON.stringify({}));
    },
    function() {
        stompTopicClient = null;
        stompQueueClient = null;
    });
}

function connectToGameInfo() {
    var socket = new SockJS('/klaverjas-websocket');
    stompQueueClient = Stomp.over(socket);
    stompQueueClient.connect({}, function (frame) {
        setConnected(true);
        console.log('Connected: ' + frame);
        stompQueueClient.subscribe('/user/topic/game', handleGameState);

        if (gameState) {
            requestState();
        }
    },
    function() {
        stompTopicClient = null;
        stompQueueClient = null;
    });
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
    $( '#requestState' ).click(function() { requestState(); });

    $('#lobby').show();
    $('#table').hide();

    setInterval(function() {
        if (!stompTopicClient || !stompQueueClient) {
            console.log('Auto-reconnecting...');
            requestState();
            console.log('connected');
        }
    }, 10000);
});