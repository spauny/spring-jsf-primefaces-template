package gamesys.studiocd.smarthound.fun;

import gamesys.studiocd.smarthound.util.FacesMessageUtil;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 *
 * @author iulian.dafinoiu
 */
@Component
@Scope("singleton")
public class FunFacts implements Serializable {
    private static final long serialVersionUID = 176845493674583492L;
    
    private final List<String> funFacts = new ArrayList<>();
    
    {
        funFacts.add("Instead of thinking about what you're missing, try thinking about what you have that everyone else is missing");
        funFacts.add("Ever tried MadFox? It's amazing!");
        funFacts.add("Believe in your dreams and they may come true; believe in yourself and they will come true");
        funFacts.add("I didn’t attend the funeral, but I sent a nice letter saying I approved of it");
        funFacts.add("To hear many religious people talk, one would think God created the torso, head, legs and arms, but the devil slapped on the genitals");
        funFacts.add("Keep your friends close, but your enemies closer");
        funFacts.add("If your actions inspire others to dream more, learn more, do more and become more, you are a leader");
        funFacts.add("You don't have to see the whole staircase, just take the first step");
        funFacts.add("Even if you are on the right track, you will get run over if you just sit there");
        funFacts.add("No one can whistle a symphony. It takes a whole orchestra");
        funFacts.add("We will either find a way, or make one");
        funFacts.add("The P in PM is as much about ‘people management’ as it is about ‘project management’");
        funFacts.add("A task is not done until it is done");
        funFacts.add("If your actions inspire others to dream more, learn more, do more and become more, you are a leader");
        funFacts.add("An ounce of action is worth a ton of theory");
        funFacts.add("If everything seems under control, you’re not going fast enough");
        funFacts.add("Plans are only good intentions unless they immediately degenerate into hard work");
        funFacts.add("Some things are better done than described");
        funFacts.add("Working ten hour days allows you to fall behind twice as fast as you could working five hour days");
        funFacts.add("Change is not made without inconvenience, even from worse to better");
        funFacts.add("Story in a game is like a story in a porn movie. It's expected to be there, but it's not that important");
        funFacts.add("Just play. Have fun. Enjoy the game");
        funFacts.add("Time is a game played beautifully by children");
        funFacts.add("Life is more fun if you play games");
        funFacts.add("If Pac-Man had affected us as kids, we'd all be running around in dark rooms, munching pills and listening to repetitive electronic music");
        funFacts.add("Always trust computer games");
        funFacts.add("Mix a little mystery with everything, for mystery arouses veneration");
        funFacts.add("If your actions inspire others to dream more, learn more, do more and become more, you are a leader");
        funFacts.add("One day your life will flash before your eyes. Make sure it's worth watching");
        funFacts.add("If an IT project works the first time, it was a very small and simple project");
        funFacts.add("It is better to know some of the questions than all of the answers");
        funFacts.add("It’s not enough that we do our best; sometimes we have to do what’s required");
        funFacts.add("Skills can be learned while experience must be earned");
        funFacts.add("Plans are worthless, but planning is invaluable");
        funFacts.add("Success consists of going from failure to failure without loss of enthusiasm");
        funFacts.add("He who asks is a fool for five minutes, but he who does not ask remains a fool forever");
        funFacts.add("Losers visualize the penalties of failure. Winners visualize the rewards of success");
        funFacts.add("You miss 100% of the shots you don’t take");
        funFacts.add("If you don’t like how things are, change it. You’re not a tree");
        funFacts.add("Success is the sum of small efforts, repeated day in and day out");
        funFacts.add("I will not lose, for even in defeat there’s a valuable lesson learned");
//        funFacts.add("I am thankful to all of those who said no, it’s because of them I did it myself");
        funFacts.add("Either you run the day or the day runs you");
        funFacts.add("There are two types of software: bad software and the next release");
        funFacts.add("When debugging, novices insert corrective code; experts remove defective code");
        funFacts.add("Good leaders do not take on all the work themselves; neither do they take all the credit");
        funFacts.add("Assumption is the mother of all screw-ups");
        funFacts.add("If everyone is thinking alike, someone isn’t thinking");
        funFacts.add("Vision is the art of seeing what is invisible to others");
        funFacts.add("People buy into the leader before they buy into the vision"); 
        funFacts.add("Any fool can know. The point is to understand.");
    }
    
    public void tellAStory() {
        FacesMessageUtil.addFacesInfoMessage(this.funFacts.get(new Random().nextInt(this.funFacts.size() - 1)));
    }
    
}
