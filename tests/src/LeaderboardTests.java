import com.skloch.game.Leaderboard;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(GdxTestRunner.class)
public class LeaderboardTests {
    @Test
    public void addNewTopScore() {
        Leaderboard leaderboard = new Leaderboard(true);
        int leaderboardLength = leaderboard.GetLeaderboardLength();
        if (leaderboardLength > 0) {
            int topScore = leaderboard.GetScore(0);
            leaderboard.AddScore("Test", topScore + 1);
            assertTrue("New score was not added to the leaderboard",
                    leaderboard.GetScore(0) == topScore + 1);
        }
    }

    @Test
    public void fillLeaderboardAndAddBottomScore() {
        Leaderboard leaderboard = new Leaderboard(true);
        // Fill the leaderboard with scores
        for (int i = 0; i < leaderboard.GetMaxEntries(); i++) {
            int newScore = i + 2;
            leaderboard.AddScore("Test", newScore);
        }
        // Now if we add a score that is lower than the lowest score in the leaderboard, it should not be added
        leaderboard.AddScore("Test", 1);
        assertTrue("New score was added to the leaderboard when it shouldn't have been",
                leaderboard.GetScore(leaderboard.GetLeaderboardLength() - 1) > 1);
    }

    @Test
    public void setAndGetEntryInfo() {
        Leaderboard leaderboard = new Leaderboard(true);

        // Get the formatted leaderboard when no scores have been added
        String emptyLeaderboardText = leaderboard.GetLeaderboardText();
        assertTrue("The leaderboard text was not generated correctly when no scores were added",
                emptyLeaderboardText.contains("empty"));

        leaderboard.AddScore("Test", 3);

        // Get the name of player
        assertEquals("The name of the requested player in the leaderboard was not returned correctly",
                "Test", leaderboard.GetName(0));

        // Get the score
        assertEquals("The score of the requested player in the leaderboard was not returned correctly",
                3, leaderboard.GetScore(0));

        // Get the formatted leaderboard string
        String text = leaderboard.GetLeaderboardText();
        Boolean containsPlayerName = text.contains("Test");
        Boolean containsScore = text.contains("3");
        assertTrue("The leaderboard text was not generated correctly",
                containsPlayerName && containsScore);
    }
}
