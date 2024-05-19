import com.skloch.game.Event;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class EventTests {
    @Test
    public void testPerform() {
        Event event = new Event("name", "text", 1);
        event.perform();
        assertEquals("Perform method does not correctly increment value for total times performed.", event.getTimesPerformedTotal(), 1);
        assertEquals("Perform method does not correctly increment value for daily times performed.", event.getTimesPerformedToday(), 1);
        event.perform();
        assertEquals("Perform method does not correctly increment value for total times performed.", event.getTimesPerformedTotal(), 2);
        assertEquals("Perform method does not correctly increment value for daily times performed.", event.getTimesPerformedToday(), 2);
    }

    @Test
    public void testPerformStreak() {
        Event event = new Event("name", "text", 1);
        event.perform();
        assertEquals("Perform method does not correctly increment value for streak.", event.getCurrentStreak(), 1);
        assertEquals("Perform method does not correctly increment value for max streak.", event.getMaxStreak(), 1);
        event.perform();
        assertEquals("Perform method incorrectly increments value for streak.", event.getCurrentStreak(), 1);
        assertEquals("Perform method incorrectly increments value for max streak.", event.getMaxStreak(), 1);
    }

    @Test
    public void testDayAdvanced() {
        Event event = new Event("name", "text", 1);
        event.perform();
        event.dayAdvanced();
        assertEquals("Advancing the day does not correctly reset the daily times performed stat.", event.getTimesPerformedToday(), 0);
        assertEquals("Advancing the day incorrectly modifies the total times performed stat.", event.getTimesPerformedTotal(), 1);
    }

    @Test
    public void testDayAdvancedStreak() {
        Event event = new Event("name", "text", 1);
        event.perform();
        event.dayAdvanced();
        event.perform();
        assertEquals("Advancing the day does not correctly update the streak", event.getCurrentStreak(), 2);
        assertEquals("Advancing the day does not correctly update the max streak", event.getMaxStreak(), 2);
        event.dayAdvanced();
        event.dayAdvanced();
        assertEquals("Advancing the day does not correctly update the streak", event.getCurrentStreak(), 0);
        assertEquals("Advancing the day does not correctly update the max streak", event.getMaxStreak(), 2);
    }

    @Test
    public void testCopy() {
        Event event = new Event("name", "text", 44);
        Event event2 = event.copy();

        assertEquals("Copied Event instance name differs.", event.getName(), event2.getName());
        assertEquals("Copied Event instance text differs.", event.getText(), event2.getText());
        assertEquals("Copied Event instance energy cost differs", event.getEnergyCost(), event2.getEnergyCost());
        assertEquals("Copied Event instance streak differs", event.getCurrentStreak(), event2.getCurrentStreak());
        assertEquals("Copied Event instance max streak differs", event.getMaxStreak(), event2.getMaxStreak());
        assertEquals("Copied Event instance daily times performed differs", event.getTimesPerformedToday(), event2.getTimesPerformedToday());
        assertEquals("Copied Event instance total times performed differs", event.getTimesPerformedTotal(), event2.getTimesPerformedTotal());
    }
}
