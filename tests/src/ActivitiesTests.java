import com.badlogic.gdx.Game;
import com.skloch.game.EventManager;
import com.skloch.game.HustleGame;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import screens.GameScreen;

import static org.mockito.Mockito.mock;

@RunWith(GdxTestRunner.class)
public class ActivitiesTests {

    private GameScreen game;
    private EventManager eventManager;

    @Before
    public void setUp() {
        game = new GameScreen(mock(HustleGame.class),1, true);
        eventManager = new EventManager(game);
    }

    @Test
    public void testNightActivities() {
        // Ensure that activities cannot be done at nighttime
        // TODO
    }


}
