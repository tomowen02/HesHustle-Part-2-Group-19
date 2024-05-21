import com.badlogic.gdx.Gdx;
import com.skloch.game.DialogueBox;
import com.skloch.game.EventManager;
import com.skloch.game.HustleGame;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import screens.GameScreen;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

@RunWith(GdxTestRunner.class)
public class EventManagerTests {

    // Testing how activities and time interact
    @Test
    public void testDayNightActivities() {
        GameScreen game = new GameScreen(mock(HustleGame.class),1, true);
        game.dialogueBox = mock(DialogueBox.class);
        EventManager eventManager = new EventManager(game);

        // Ensure that activities can be done during the day
        float currentTime = game.getSeconds();
        game.passTime(12f*60 - currentTime, false); // Set the time to midday
        assertTrue("An activity was not allowed to occur when it is day time",
                eventManager.ducksEvent(new String[] {}));

        // Ensure that activities cannot be done at nighttime
        currentTime = game.getSeconds();
        game.passTime(22f*60 - currentTime, false); // Set the time to 10:30pm
        assertFalse("An activity was allowed to occur at night time",
                eventManager.ducksEvent(new String[] {}));
    }
    @Test
    public void testIncreaseTime() {
        GameScreen game = new GameScreen(mock(HustleGame.class),1, true);
        game.dialogueBox = mock(DialogueBox.class);
        EventManager eventManager = new EventManager(game);
        game.passTime(12f*60, false); // Set the time to midday

        // Ensure that doing the activity passes time in the day
        float originalTime = game.getSeconds();
        eventManager.ducksEvent(new String[] {});
        assertTrue("An activity did not increase the time",
                originalTime < game.getSeconds());
    }
    @Test
    public void testSleepIncrementsDay() {
        GameScreen game = new GameScreen(mock(HustleGame.class), 1, true);
        game.dialogueBox = mock(DialogueBox.class);
        EventManager eventManager = new EventManager(game);
        game.passTime(12f * 60, false); // Set the time to midday

        eventManager.accomEvent(new String[] {});
        assertEquals("The day did not increment after sleep.",
                2, game.getDay());
    }
    @Test
    public void testSevenDays() {
        GameScreen game = new GameScreen(mock(HustleGame.class), 1, true);
        game.dialogueBox = mock(DialogueBox.class);
        EventManager eventManager = new EventManager(game);

        // The week starts on day 1. Sleep 6 times to get to the seventh day
        for (int i = 0; i < 6; i++) {
            game.passTime(18f * 60, false); // Set the time to 6pm
            eventManager.accomEvent(new String[] {});
        }

        assertFalse("Game-over activated early.",
                game.gameover);

        // Sleep one more time to reach game-over
        game.passTime(18f * 60, false); // Set the time to 6pm
        eventManager.accomEvent(new String[] {});

        assertTrue("Game-over did not activate.",
                game.gameover);
    }

    // Testing how activities and energy interact
    @Test
    public void testDecrementEnergy() {
        GameScreen game = new GameScreen(mock(HustleGame.class),1, true);
        game.dialogueBox = mock(DialogueBox.class);
        EventManager eventManager = new EventManager(game);
        game.passTime(12f*60, false); // Set the time to midday

        float originalEnergy = game.getEnergy();
        eventManager.ducksEvent(new String[] {});
        assertTrue("The energy did not decrement",
                game.getEnergy() < originalEnergy);
    }
    @Test
    public void testNotEnoughEnergy() {
        GameScreen game = new GameScreen(mock(HustleGame.class),1, true);
        game.dialogueBox = mock(DialogueBox.class);
        EventManager eventManager = new EventManager(game);
        game.passTime(12f*60, false); // Set the time to midday

        // Ensure that activities can be done when we have enough energy
        assertTrue("An activity was not allowed to occur but the player has enough energy",
                eventManager.ducksEvent(new String[] {}));

        // Ensure that activities cannot be done when we do not have enough energy
        game.setEnergy(5);
        assertFalse("An activity was allowed to when the player did not have enough energy",
                eventManager.ducksEvent(new String[] {}));
    }
    @Test
    public void testSleepResetsEnergy() {
        GameScreen game = new GameScreen(mock(HustleGame.class), 1, true);
        game.dialogueBox = mock(DialogueBox.class);
        EventManager eventManager = new EventManager(game);
        game.passTime(12f * 60, false); // Set the time to midday
        game.setEnergy(50);

        eventManager.accomEvent(new String[] {});
        assertEquals("The energy did reset after sleep.",
                100, game.getEnergy());
    }

    @Test
    public void countActivityType() {
        GameScreen game = new GameScreen(mock(HustleGame.class), 1, true);
        game.dialogueBox = mock(DialogueBox.class);
        EventManager eventManager = new EventManager(game);
        game.passTime(12f * 60, false); // Set the time to midday

        eventManager.basketballEvent(new String[] {});
        eventManager.compSciEvent(new String[] {"2"});
        eventManager.accomEvent(new String[] {});
        assertEquals("Hours studied is not what was expected",
                game.hoursStudied, 2);
        assertEquals("Recreational hours are not as expected",
                game.hoursRecreational, 1);
    }

    @Test
    public void testTooMuchStudy() {
        GameScreen game = new GameScreen(mock(HustleGame.class), 1, true);
        game.dialogueBox = mock(DialogueBox.class);
        EventManager eventManager = new EventManager(game);
        game.passTime(12f * 60, false); // Set the time to midday

        // Ensure that we can study once on a day
        assertTrue("The player was not allowed to study when they should have been allowed to",
                eventManager.event("comp_sci", "1"));

        eventManager.event("accomodation", "");

        eventManager.event("comp_sci", "1");
        assertTrue("The player was not allowed to study for a second time when they should have been allowed to",
                eventManager.event("comp_sci", "1"));

        eventManager.event("accomodation", "1");

        eventManager.event("comp_sci", "1");
        assertFalse("The player was allowed to study for a second time in a day when they had already used up their extra study allowance",
                eventManager.event("comp_sci", "1"));

    }
}
