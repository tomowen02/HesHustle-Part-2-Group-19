import com.skloch.game.HustleGame;
import org.junit.Before;
import org.junit.runner.RunWith;
import screens.CreditScreen;
import screens.MenuScreen;

import static org.mockito.Mockito.mock;

@RunWith(GdxTestRunner.class)
public class MenuTests {
    private HustleGame game;
    private MenuScreen menuScreen;
    private CreditScreen creditScreen;

    @Before
    public void setUp() {
        game = mock(HustleGame.class);
        menuScreen = mock(MenuScreen.class);
        creditScreen = mock(CreditScreen.class);
    }


}
