import com.skloch.game.Achievement;
import com.skloch.game.Event;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class AchievementTests {
    @Test
    public void testNoPredicates() {
        Achievement achievement = new Achievement("name", "description", 0);
        achievement.validate();
        assertTrue("The achievement has no predicates but is not achieved.", achievement.isAchieved());
    }

    @Test
    public void testSimplePredicate() {
        Achievement achievement = new Achievement("name", "description", 0);
        achievement.addPredicate(p -> true);
        achievement.validate();
        assertTrue("The achievement's predicate returns true but the achievement is not achieved.", achievement.isAchieved());

        achievement = new Achievement("name", "description", 0);
        achievement.addPredicate(p -> false);
        achievement.validate();
        assertFalse("The achievement's predicate returns false but the achievement is achieved.", achievement.isAchieved());
    }

    @Test
    public void testCompoundPredicate() {
        Achievement achievement = new Achievement("name", "description", 0);
        achievement.addPredicate(p -> true);
        achievement.addPredicate(p -> true);
        achievement.addPredicate(p -> true);
        achievement.validate();
        assertTrue("The achievement's predicates are true but the achievement is not achieved.", achievement.isAchieved());

        achievement = new Achievement("name", "description", 0);
        achievement.addPredicate(p -> false);
        achievement.addPredicate(p -> false);
        achievement.addPredicate(p -> false);
        achievement.validate();
        assertFalse("The achievement's predicates are false but the achievement is achieved.", achievement.isAchieved());

        achievement = new Achievement("name", "description", 0);
        achievement.addPredicate(p -> true);
        achievement.addPredicate(p -> false);
        achievement.addPredicate(p -> true);
        achievement.validate();
        assertFalse("One of the achievement's predicates is false but the achievement is achieved.", achievement.isAchieved());
    }

    @Test
    public void testEventPredicate() {
        Event event = new Event("eventName", "eventText", 0);
        Achievement achievement = new Achievement("name", "description", 0);
        achievement.addPredicate(event, p -> p.getTimesPerformedTotal() == 0);
        achievement.validate();
        assertTrue("The achievement's predicates are true but the achievement is not achieved.", achievement.isAchieved());

        event = new Event("eventName", "eventText", 0);
        achievement = new Achievement("name", "description", 0);
        event.perform();
        achievement.addPredicate(event, p -> p.getTimesPerformedTotal() == 1);
        achievement.validate();
        assertTrue("The achievement's predicates are true but the achievement is not achieved.", achievement.isAchieved());

        event = new Event("eventName", "eventText", 0);
        achievement = new Achievement("name", "description", 0);
        achievement.addPredicate(event, p -> p.getTimesPerformedTotal() == 1);
        achievement.validate();
        assertFalse("The achievement's predicates are false but the achievement is achieved.", achievement.isAchieved());
    }
}
