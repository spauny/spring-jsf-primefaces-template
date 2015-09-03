var funFactPollCounter = 0;

function checkFunFactPoll() {
    if (funFactPollCounter === 6) {
        PF('funFactPollWar').stop();
    }
    funFactPollCounter++;
}