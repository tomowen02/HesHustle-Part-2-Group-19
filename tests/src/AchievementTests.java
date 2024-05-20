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

    @Test
    public void testExternalPredicate() {
        class TestClass {
            public int value = 1;
        }

        TestClass testClass = new TestClass();
        Achievement achievement = new Achievement("name", "description", 0);
        achievement.addPredicate(p -> testClass.value == 1);
        achievement.validate();
        assertTrue("Achievement does not correctly track captured value", achievement.isAchieved());

        TestClass testClass1 = new TestClass();
        Achievement achievement1 = new Achievement("name", "description", 0);
        achievement1.addPredicate(p -> testClass1.value == 1);
        testClass1.value = 2;
        achievement1.validate();
        assertFalse("Achievement does not correctly track captured value", achievement1.isAchieved());

        TestClass testClass2 = new TestClass();
        Achievement achievement2 = new Achievement("name", "description", 0);
        achievement2.addPredicate(p -> testClass2.value == 2);
        testClass2.value = 2;
        achievement2.validate();
        assertTrue("Achievement does not correctly track captured value", achievement2.isAchieved());
    }
}
