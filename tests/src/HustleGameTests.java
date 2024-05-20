import com.skloch.game.HustleGame;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertFalse;

@RunWith(GdxTestRunner.class)
public class HustleGameTests {
    @Test
    public void TestReadTextFile() {
        String text = HustleGame.readTextFile("TestTextFile.txt");
        assertFalse("Text file read incorrectly!",
                text.contains("Couldn't load"));
    }
}
