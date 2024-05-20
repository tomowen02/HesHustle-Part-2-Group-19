import com.skloch.game.DialogueBox;
import com.skloch.game.EventManager;
import com.skloch.game.HustleGame;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import screens.GameScreen;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

@RunWith(GdxTestRunner.class)
public class EventManagerTests {

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
    public void testNoEnergy() {
        GameScreen game = new GameScreen(mock(HustleGame.class),1, true);
        game.dialogueBox = mock(DialogueBox.class);
        EventManager eventManager = new EventManager(game);
        game.passTime(12f*60, false); // Set the time to midday

        // Ensure that activities can be done when we have enough energy
        assertTrue("An activity was not allowed to occur but the player has enough energy",
                eventManager.ducksEvent(new String[] {}));

        // Ensure that activities cannot be done when we do not have enough energy
        game.setEnergy(0);
        assertFalse("An activity was allowed to when the player did not have enough energy",
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
}
