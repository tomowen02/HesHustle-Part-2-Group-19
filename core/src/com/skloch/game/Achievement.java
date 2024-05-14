package com.skloch.game;

import java.util.HashMap;
import java.util.function.Predicate;


/**
 * Tracks an Event-based achievement based on a set of predicates.
 * Each predicate must evaluate to true for the achievement to be considered achieved.
 * Once achieved, an achievement cannot be 'un-achieved'.
 */
public class Achievement {
    private final String name;
    private final String description;
    private boolean achieved;
    private final HashMap<Event, Predicate<Event>> predicates;
    public final int score;

    public Achievement(String name, String description, int score) {
        this.name = name;
        this.description = description;
        achieved = false;
        predicates = new HashMap<>();
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public boolean isAchieved() {
        return achieved;
    }

    /**
     * Evaluates all predicates and updates the 'achieved' status of the achievement.
     * @return True if ALL predicates are true.
     */
    public boolean validate() {
        if (achieved) return true;

        achieved = true;
        for (Event event : predicates.keySet()) {
            achieved = achieved && predicates.get(event).test(event);
        }
        return achieved;
    }

    /**
     * @param event A game Event. Only one predicate can be added per unique Event object. If multiple are provided,
     *              the most recent is used.
     * @param predicate This condition must be met in order for the achievement to be achieved.
     */
    public void addPredicate(Event event, Predicate<Event> predicate) {
        predicates.put(event, predicate);
    }

    /**
     * Convenience method for predicates that do not depend on an Event.
     * @param predicate This condition must be met in order for the achievement to be achieved.
     */
    public void addPredicate(Predicate<Event> predicate) {
        predicates.put(new Event("", "", 0), predicate);
    }
}
