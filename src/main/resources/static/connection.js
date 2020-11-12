var stompTopicClient = null; // for general info, like "player plays card X"
var stompQueueClient = null; // for user-specific info, like cards in hand

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);

    $("#generalInfo").html("");
}

function connectToLobby() {
    $("#lobby").show();
    $("#table").hide();

    var socket = new SockJS('/gs-guide-websocket');
    stompTopicClient = Stomp.over(socket);
    stompTopicClient.connect({}, function (frame) {
        setConnected(true);
        console.log('Connected: ' + frame);
        stompTopicClient.subscribe('/topic/lobby', handleLobbyMessage);

        stompTopicClient.send("/app/lobby/hello", {},  JSON.stringify({}));
    });
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

$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });

     connectToLobby();
     connectToGameInfo();

    $( "#startGame" ).click(function() { startGame(); });
    $( "#leaveGame" ).click(function() { leaveGame(); });

    $("#lobby").show();
    $("#table").hide();
});