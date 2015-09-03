var funFactPollCounter = 0;

function scrollPage(location) {
    window.location.hash = location;
}

function checkFunFactPoll() {
    if (funFactPollCounter === 3) {
        PF('funFactPollWar').stop();
    }
    funFactPollCounter++;
}

function changeGridsDisplayStatus(averagePanelId, actualPanelId) {
    var averageGrid = document.getElementById(averagePanelId);
    var actualGrid = document.getElementById(actualPanelId);
    if (averageGrid.style.display === 'none') {
        averageGrid.style.display = ''
    } else {
        averageGrid.style.display = 'none'
    }
    if (actualGrid.style.display === 'none') {
        actualGrid.style.display = ''
    } else {
        actualGrid.style.display = 'none'
    }
}
