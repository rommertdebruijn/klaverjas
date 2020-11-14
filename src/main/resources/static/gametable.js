var gameState = null;

function handleGameState(gameStateMessage) { // *player* game state!
    gameState = JSON.parse(gameStateMessage.body);
    if (gameState.playerStillPlaying) {
        $('#lobby').hide();
        $('#table').show();
        showGameState(gameState);
    } else {
        $('#lobby').show();
        $('#table').hide();
        gameState = null;
    }
}

function isPlayerTurn() {
    return !!gameState && !!gameState.turn && gameState.turn === 'SOUTH';
}

function cleanTable() {
    $('#tableNorth').empty();
    $('#tableEast').empty();
    $('#tableSouth').empty();
    $('#tableWest').empty();
    $('#bidding').empty();
}

function getSuitCharacter(suit) {
    if (suit === 'SPADES') {
        return '<span class="black-suit">&spades;</span>';
    }
    if (suit === 'HEARTS') {
        return '<span class="red-suit">&hearts;</span>';
    }
    if (suit === 'DIAMONDS') {
        return '<span class="red-suit">&diams;</span>';
    }
    if (suit === 'CLUBS') {
        return '<span class="black-suit">&clubs;</span>';
    }
}

function renderCardInHand(card) {
    var cardId = card.cardId;
    var cardHtml = '' +
        '<div id="card-' + cardId + '" class="card-in-hand ' + card.suit.toLowerCase() + '">' +
        '  <div class="suit">' +
            getSuitCharacter(card.suit) +
        '  </div>' +
        '  <div class="rank">' +
            card.rank +
        '  </div>' +
        '</div>';
    $('#cards-south').append(cardHtml);

    if (isPlayerTurn() && cardId) {
        $('#card-' + cardId).click(function() {
            playCard(cardId);
        });
    }
}

function renderCardOnTable(containerId, card) {
    if (card) {
        var cardHtml ='' +
            '<div class="card-on-table ' + card.suit.toLowerCase() + '">' +
            '  <div class="suit">' +
            getSuitCharacter(card.suit) +
            '  </div>' +
            '  <div class=\'rank\'>' +
            card.rank +
            '  </div>' +
            '</div>';
        $(containerId).append(cardHtml);
    }
}

function renderCurrentTrick(currentTrick) {
    renderCardOnTable('#tableNorth', currentTrick.cardsPlayed['NORTH']);
    renderCardOnTable('#tableEast', currentTrick.cardsPlayed['EAST']);
    renderCardOnTable('#tableSouth', currentTrick.cardsPlayed['SOUTH']);
    renderCardOnTable('#tableWest', currentTrick.cardsPlayed['WEST']);
}

function renderPlayerBidOnTable(elementId, bid) {
    if (!!bid) {
        $(elementId).append('<div class="bidOnTable">' + bid + '</div>');
    }
}

function renderProposedTrump(proposedTrump) {
    $("#bidding").append('<div>Spelen op deze troef?</div><div class="trump">' + getSuitCharacter(proposedTrump) + '</div>');
}

function renderContract(bidding) {
    $('#bidding').append('<div>' + gameState.players[bidding.finalBidBy] + ' speelt op</div><div class="trump">' + getSuitCharacter(bidding.finalTrump) + '</div>');
}

function renderBidding(bidding) {
    if (!bidding.finalTrump) {
        if (bidding.availableSuits.length > 0) {
            var playerName = gameState.players[gameState.turn];
            $('#bidding').append('<div>' + playerName + ' kiest troef...</div>');
        } else {
            renderProposedTrump(bidding.proposedTrump);
            renderPlayerBidOnTable('#tableNorth', bidding.bids['NORTH']);
            renderPlayerBidOnTable('#tableEast', bidding.bids['EAST']);
            renderPlayerBidOnTable('#tableSouth', bidding.bids['SOUTH']);
            renderPlayerBidOnTable('#tableWest', bidding.bids['WEST']);
        }
    } else {
        renderContract(bidding);
    }
}

function renderCurrentPlayerHand(hand) {
    $('#cards-south').empty();
    if (!!hand && hand.length > 0) {
        for (var i = 0; i < hand.length; i++) {
            renderCardInHand(hand[i]);
        }
    }
}

function renderPlayPassOptions() {
    var $bidding = $('#bidding-box');
    $bidding.append('<div id="bidPlay" class="action">SPEEL</div>');
    $('#bidPlay').click(function() {
       makeBid('PLAY');
    });

    $bidding.append('<div id="bidPass" class="action">PAS</div>');
    $('#bidPass').click(function() {
        makeBid('PASS');
    });
}

function renderForcedBidButton(availableSuit) {
    var $bidding = $('#bidding-box');
    var forcedBidButtonId = 'forced-' + availableSuit.toLowerCase();
    $bidding.append('<div id="' + forcedBidButtonId + '" class="action">SPEEL OP ' + getSuitCharacter(availableSuit) + '</div>');
    $('#' + forcedBidButtonId).click(function () {
        makeForcedBid(availableSuit);
    });
}

function renderForcedBidOptions(availableSuits) {
    for (var i=0;i<availableSuits.length;i++) {
        renderForcedBidButton(availableSuits[i]);
    }
}

function renderBiddingBox(bidding) {
    $('#bidding-box').empty();
    if (!bidding.finalTrump && isPlayerTurn()) {
        if (bidding.bids['SOUTH'] == null) { // we haven't placed a bid yet
            renderPlayPassOptions();
        } else {
            renderForcedBidOptions(bidding.availableSuits); // south is on the curb ;)
        }
    }
}

function renderOtherPlayerCards(state, seat, $element) {
    var nrOfCardsInHand = state.nrOfCardsInHand[seat];
    var handHtml = '' +
        '<div class="row">' +
        '  <div class="other-player-cards">';

    for (var i = 0; i < nrOfCardsInHand - 1; i++) {
        handHtml += '<div class="half-card" />';
    }
    if (nrOfCardsInHand > 0) {
        handHtml += '<div class="full-card" />'
    }

    handHtml += '' +
        '  </div>' +
        '</div>';

    $element.append(handHtml);
}

function renderPlayer(state, seat) {
    var $player = $('#player-' + seat.toLowerCase());
    $player.empty();

    var nameClass = 'player-name';
    if (state.turn === seat) {
        nameClass = 'player-name-highlight';
    }

    var nameHtml = '<div class="' + nameClass + '">' + state.players[seat] + '</div>';
    $player.append(nameHtml);

    if ('SOUTH' !== seat) {
        renderOtherPlayerCards(state, seat, $player);
    }
}

function renderDealerButton(state) {
    var playerAction = $('#playerAction');

    if (isPlayerTurn() && state.dealer === 'SOUTH' && (!state.hand || state.hand.length === 0)) {
        playerAction.append('<div id="dealer-button" class="action">DELEN</div>');
        $('#dealer-button').click(function() {
            dealHand();
        });
    }
}

function renderClaimComboButton(state) {
    var playerAction = $('#playerAction');

    if (isPlayerTurn() && state.currentTrick && state.currentTrick.trickWinner === 'SOUTH' && !state.currentTrick.comboClaimed) {
        playerAction.append('<div id="claim-button" class="action">ROEM</div>');
        $('#claim-button').click(function() {
            claimCombo();
        });
    }
}

function renderPlayerActionBox(state) {
    $('#playerAction').empty();
    renderDealerButton(state);
    renderClaimComboButton(state);
}

function renderComboScore(state) {
    var $comboScore = $('#comboScore');

    $comboScore.empty();
    if (state.comboPoints.comboPointsNS > 0 ||
        state.comboPoints.comboPointsEW > 0) {
        var comboScoreHtml = '' +
            '<div class="row">' +
            '  <div class="col-md-12">ROEM:</div>' +
            '</div>' +
            '<div class="row">' +
            '  <div class="col-md-6">Wij</div><div class="col-md-6">Zij</div>' +
            '</div>' +
            '<div class="row">' +
            '  <div class="col-md-6">' + state.comboPoints.comboPointsNS + '</div><div class="col-md-6">' + state.comboPoints.comboPointsEW + '</div>' +
            '</div>';
        $comboScore.append(comboScoreHtml);

    }
}

function showGameState(state) {
    renderPlayer(state, 'NORTH');
    renderPlayer(state, 'EAST');
    renderPlayer(state, 'SOUTH');
    renderPlayer(state, 'WEST');

    cleanTable();
    if (!!state.bidding) {
        renderBidding(state.bidding);
        renderBiddingBox(state.bidding);
    }
    if (state.currentTrick) {
        renderCurrentTrick(state.currentTrick);
    }
    renderComboScore(state);
    renderPlayerActionBox(state);
    renderCurrentPlayerHand(state.hand);
}

function startGame() {
    stompQueueClient.send('/app/game/start', {}, JSON.stringify({}));
}

function joinGame(gameId) {
    stompQueueClient.send('/app/game/join', {}, JSON.stringify({'gameId': gameId }));
}

function leaveGame() {
    stompQueueClient.send('/app/game/leave', {}, JSON.stringify({'gameId': gameState.gameId }));
}

function dealHand() {
    stompQueueClient.send('/app/game/deal', {}, JSON.stringify({'gameId': gameState.gameId }));
}

function makeBid(bid) {
    stompQueueClient.send('/app/game/makebid', {}, JSON.stringify({ 'gameId' : gameState.gameId, 'bid' : bid }));
}

function makeForcedBid(forcedTrump) {
    stompQueueClient.send('/app/game/makeforcedbid', {}, JSON.stringify({ 'gameId' : gameState.gameId, 'forcedTrump' : forcedTrump }));
}

function playCard(cardId) {
    stompQueueClient.send('/app/game/playcard', {}, JSON.stringify({ 'gameId' : gameState.gameId, 'cardId' : cardId }));
}

function claimCombo() {
    stompQueueClient.send('/app/game/claimCombo', {}, JSON.stringify({ 'gameId' :  gameState.gameId }));
}