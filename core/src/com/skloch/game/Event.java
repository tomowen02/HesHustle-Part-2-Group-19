package com.skloch.game;

// This class is new in part 2 of the assessment

/**
 * An in-game event that can be performed by the player. Tracks statistics such as the total number of times performed,
 * the total times performed today, and the number of consecutive days performed. Defines the energy cost of the event.
 * Contains methods which must be invoked when the event takes placed, and when day is advanced.
 */
public class Event {
    private final String name;
    private final String text;
    private final int energyCost;
    private int streak;
    private int maxStreak;
    private int timesPerformedToday;
    private int timesPerformedTotal;

    /**
     * @param name Event's name.
     * @param text Description or prompt text.
     * @param energyCost Cost in energy.
     */
    public Event(String name, String text, int energyCost) {
        this.name = name;
        this.text = text;
        this.energyCost = energyCost;
    }


    public String getName() {
        return name;
    }

    /**
     * @return The provided description or prompt text.
     */
    public String getText() {
        return text;
    }

    public int getEnergyCost() {
        return energyCost;
    }

    public int getCurrentStreak() {
        return streak;
    }

    /**
     * Call this method when the player performs this event.
     */
    public void perform() {
        if (timesPerformedToday < 1) {
            streak++;
            if (streak > maxStreak) {
                maxStreak = streak;
            }
        }
        timesPerformedToday++;
        timesPerformedTotal++;
    }

    public void resetStreak() {
        streak = 0;
    }

    public int getMaxStreak() {
        return maxStreak;
    }

    public int getTimesPerformedTotal() {
        return timesPerformedTotal;
    }

    public int getTimesPerformedToday() {
        return timesPerformedToday;
    }

    /**
     * Call this method at the end of each day.
     */
    public void dayAdvanced() {
        if (timesPerformedToday == 0) {
            resetStreak();
        }
        timesPerformedToday = 0;
    }

    public Event copy() {
        Event copy;
        copy = new Event(name, text, energyCost);
        copy.streak = streak;
        copy.maxStreak = maxStreak;
        copy.timesPerformedTotal = timesPerformedTotal;
        copy.timesPerformedToday = timesPerformedToday;
        return copy;
    }
}
