var gameState = null;
var cardsMarked = [];

function handleGameState(gameStateMessage) { // *player* game state!
    gameState = JSON.parse(gameStateMessage.body);
    if (gameState.playerStillPlaying) {
        $('#lobby').hide();
        $('#table').show();
        renderGameState(gameState);
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
    $('#combo').empty();

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

function getCardName(card) {
    return 'card-' + card.suit.toLowerCase() + '-' + card.rank.toLowerCase();
}

function cardMarkingAvailable(state) {
    return !!state.bidding && !!state.bidding.finalTrump;
}

function renderCardInHand(card, state) {
    var cardId = card.cardId;
    var cardElementId = cardId ? 'card-' + cardId : '';
    var cardplayable = isPlayerTurn() && !cardId ? 'notPlayable' : 'playable';
    var cardName = getCardName(card);
    var marked = cardIsMarked(cardName) ? 'marked' : '';
    var cardHtml = '' +
        ' <div id="' + cardElementId + '" class="card-in-hand ' + card.suit.toLowerCase() + ' ' + cardplayable + ' ' + cardName + '">' +
        '  <div class="suit">' +
            getSuitCharacter(card.suit) +
        '  </div>' +
        '  <div class="rank ' + marked + '">' +
            getRankName(card) +
        '  </div>' +
        ' </div>';
    $('#cards-south').append(cardHtml);

    if (isPlayerTurn() && cardId) {
        $('#card-' + cardId).click(function() {
            playCard(cardId);
        });
    }

    if (!isPlayerTurn() && cardMarkingAvailable(state)) {
        $('.' + cardName).click(function() {
            markCard(card);
        });
    }
}

function cardIsMarked(cardName) {
    return cardsMarked.indexOf(cardName) !== -1;
}

function markCard(card) {
    var cardName = getCardName(card);
    if (cardIsMarked(cardName)) {
        cardsMarked = cardsMarked.filter(function(value, index, arr){
            return value !== cardName;
        });
    } else {
        cardsMarked.push(cardName);
    }
    renderCurrentPlayerHand(gameState);
}

function getImageNameSuitPart(card) {
    if (card.suit === 'HEARTS') {
        return 'H';
    } else if (card.suit === 'SPADES') {
        return 'S';
    } else if (card.suit === 'DIAMONDS') {
        return 'D';
    } else if (card.suit === 'CLUBS') {
        return 'C';
    }
}

function getRankName(card) {
    if (card.rank === 'SEVEN') {
        return '7';
    } else if (card.rank === 'EIGHT') {
        return '8';
    } else if (card.rank === 'NINE') {
        return '9';
    } else if (card.rank === 'TEN') {
        return '10';
    } else if (card.rank === 'JACK') {
        return 'J';
    } else if (card.rank === 'QUEEN') {
        return 'Q';
    } else if (card.rank === 'KING') {
        return 'K';
    } else if (card.rank === 'ACE') {
        return 'A';
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
                getRankName(card) +
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
        // if this is rendered, then bid will, in fact, always be PASS
        $(elementId).append('' +
            '<div class="bidOnTable">' +
            ' <div class="bid">PAS</div>' +
            '</div>');
    }
}

function renderProposedTrump(proposedTrump) {
    $("#bidding").append('<div class="trump">' + getSuitCharacter(proposedTrump) + '</div>');
}

function renderContract(bidding) {
    $('#bidding').append('<div class="trump">' + getSuitCharacter(bidding.finalTrump) + '</div>');
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

function renderCurrentPlayerHand(state) {
    var hand = state.hand;
    $('#cards-south').empty();
    if (!!hand && hand.length > 0) {
        for (const card of hand) {
            renderCardInHand(card, state);
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
    for (const availableSuit of availableSuits) {
        renderForcedBidButton(availableSuit);
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

    var name = state.players[seat] ? state.players[seat] : '[wachten op speler]';

    if (!!state.bidding && state.bidding.finalBidBy === seat) {
        name += ' (speelt)';
    }
    var playerHtml = '' +
        '<div class="playerDetails">' +
        ' <div class="' + nameClass + '">' + name + '</div>';

    if (state.nrOfTricks && state.nrOfTricks[seat] && state.nrOfTricks[seat] > 0) {
        playerHtml += '' +
        ' <div class="nrOfTricks">Slagen: ' + state.nrOfTricks[seat] + '</div>';
    }
    playerHtml += '</div>';
    $player.append(playerHtml);

    if ('SOUTH' !== seat) {
        renderOtherPlayerCards(state, seat, $player);
    }
}

function allSeatsTaken(state) {
    return state.players['NORTH'] &&
        state.players['EAST'] &&
        state.players['SOUTH'] &&
        state.players['WEST'];
}

function renderDealerButton(state) {
    var playerAction = $('#playerAction');

    if (allSeatsTaken(state)
        && isPlayerTurn()
        && state.dealer === 'SOUTH'
        && state.dealerButtonAvailable
        && (!state.hand || state.hand.length === 0)
        && state.gameScores.length < 16) { // we play 16 games at most
        playerAction.append('<div id="dealer-button" class="action">DELEN</div>');
        $('#dealer-button').click(function() {
            dealHand();
        });
    }
}


function allCardsHaveBeenPlayed(state) {
    return state.nrOfCardsInHand['NORTH'] === 0 &&
           state.nrOfCardsInHand['EAST'] === 0 &&
           state.nrOfCardsInHand['SOUTH'] === 0 &&
           state.nrOfCardsInHand['WEST'] === 0;
}

function renderCalculateScoreButton(state) {
    var playerAction = $('#playerAction');

    // game is finished
    if (isPlayerTurn() && !!state.currentTrick && state.currentTrick.trickWinner === 'SOUTH' && allCardsHaveBeenPlayed(state)) {
        playerAction.append('<div id="calculate-score-button" class="action">TEL PUNTEN</div>');
        $('#calculate-score-button').click(function() {
            calculateScore();
        });
    }
}

function renderClaimComboButton(state) {
    var combo = $('#combo');

    if (isPlayerTurn() && state.currentTrick && state.currentTrick.trickWinner === 'SOUTH' && !state.currentTrick.comboClaimed) {
        combo.append('<div id="claim-button">ROEM</div>');
        $('#claim-button').click(function() {
            claimCombo();
        });
    }
}

function renderPlayerActionBox(state) {
    $('#playerAction').empty();
    renderDealerButton(state);
    renderCalculateScoreButton(state);
}

function renderComboScore(state) {
    var $comboScore = $('#comboScore');

    $comboScore.empty();
    if (state.comboPoints.comboPoints['NS'] > 0 ||
        state.comboPoints.comboPoints['EW'] > 0) {
        var comboScoreHtml = '' +
           '<div class="comboScores">' +
            '<div class="row">' +
            '  <div class="col-md-12">ROEM:</div>' +
            '</div>' +
            '<div class="row">' +
            '  <div class="col-md-6"><div class="team">Wij</div></div><div class="col-md-6"><div class="team">Zij</div></div>' +
            '</div>' +
            '<div class="row">' +
            '  <div class="col-md-6">' + state.comboPoints.comboPoints['NS'] + '</div><div class="col-md-6">' + state.comboPoints.comboPoints['EW'] + '</div>' +
            '</div>' +
           '</div>';
        $comboScore.append(comboScoreHtml);
    }
}

function addScoreToggleHandler(scoreIndex) {
    var container = $('#score-' + scoreIndex);
    var remarksLine = $('#score-' + scoreIndex + '-remarks');
    var rawScoresLine = $('#score-' + scoreIndex + '-scores');

    rawScoresLine.hide();

    container.click(function() {
        if (remarksLine.is(':visible')) {
            remarksLine.hide();
            rawScoresLine.show();
        } else {
            remarksLine.show();
            rawScoresLine.hide();
        }
    });
}

function addScoreToggleHandlers(nrOfScores) {

}

function renderScore(state) {
    var $score = $('#score');

    $score.empty();
    if (state.totalScore) {
        var totalScoreHtml = '' +
            '<div class="totalScore">' +
            '<div class="row">' +
            '  <div class="col-md-12">Totaalscore:</div>' +
            '</div>' +
            '<div class="row">' +
            '  <div class="col-md-6"><div class="team">Wij</div></div><div class="col-md-6"><div class="team">Zij</div></div>' +
            '</div>' +
            '<div class="row">' +
            '  <div class="col-md-6">' + state.totalScore.scores['NS'] + '</div><div class="col-md-6">' + state.totalScore.scores['EW'] + '</div>' +
            '</div>' +
            '</div>';
        $score.append(totalScoreHtml);
    }

    if (state.gameScores.length > 0) {
        var gameScoresHtml = '' +
            '<div class="allScores">' +
            '<div class="row">' +
            '  <div class="col-md-12">Alle scores:</div>' +
            '</div>' +
            '<div class="row">' +
            '  <div class="col-md-2"></div><div class="col-md-4"><div class="team">Wij</div></div><div class="col-md-6"><div class="team">Zij</div></div>' +
            '</div>';
        for (var i=0;i<state.gameScores.length;i++) {
            var score = state.gameScores[i];
            var scoreIndex = (i + 1);

            var rawScoreNS = score.tableScores['NS'] + '+(' + score.comboScores['NS'] + ')';
            var rawScoreEW = score.tableScores['EW'] + '+(' + score.comboScores['EW'] + ')';

            var remarkNS = score.scores['NS'];
            if (score.remarks['NS']) {
                remarkNS = score.remarks['NS'];
            }

            var remarkEW = score.scores['EW'];
            if (score.remarks['EW']) {
                remarkEW = score.remarks['EW'];
            }

            gameScoresHtml += '' +
                '<div id="score-' + scoreIndex + '" class="row scoreRow">' +
                '  <div id="score-' + scoreIndex + '-remarks">' +
                '    <div class="col-md-2">' + scoreIndex +'.</div><div class="col-md-4">' + remarkNS + '</div><div class="col-md-6">' + remarkEW + '</div>' +
                '  </div>' +
                '  <div id="score-' + scoreIndex + '-scores">' +
                '    <div class="col-md-2">' + scoreIndex +'.</div><div class="col-md-4">' + rawScoreNS + '</div><div class="col-md-6">' + rawScoreEW + '</div>' +
                '  </div>' +
                '</div>';
        }
        gameScoresHtml += '</div>';
        $score.append(gameScoresHtml);

        for (var scoreRow=1;scoreRow<=state.gameScores.length;scoreRow++) {
            addScoreToggleHandler(scoreRow);
        }
    }
}

function cleanCardsMarked(state) {
    if (!cardMarkingAvailable(state)) {
       cardsMarked = []; // we don't know the trump, so marking makes no sense
    }
}

function renderGameState(state) {
    renderPlayer(state, 'NORTH');
    renderPlayer(state, 'EAST');
    renderPlayer(state, 'SOUTH');
    renderPlayer(state, 'WEST');

    cleanTable();
    cleanCardsMarked(state);
    if (!!state.bidding) {
        renderBidding(state.bidding);
        renderBiddingBox(state.bidding);
    }
    if (state.currentTrick) {
        renderCurrentTrick(state.currentTrick);
    }
    renderComboScore(state);
    renderScore(state);
    renderClaimComboButton(state);
    renderPlayerActionBox(state);
    renderCurrentPlayerHand(state);
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

function calculateScore() {
    stompQueueClient.send('/app/game/calculateScore', {}, JSON.stringify({'gameId': gameState.gameId }));
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