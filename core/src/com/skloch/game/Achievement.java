package com.skloch.game;

import java.util.HashMap;
import java.util.function.Predicate;


public class Achievement {
    private final String name;
    private final String description;
    private boolean achieved;
    private final HashMap<Event, Predicate<Event>> predicates;

    public Achievement(String name, String description) {
        this.name = name;
        this.description = description;
        achieved = false;
        predicates = new HashMap<>();
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

    public boolean validate() {
        if (achieved) return true;

        achieved = true;
        for (Event event : predicates.keySet()) {
            achieved = achieved && predicates.get(event).test(event);
        }
        return achieved;
    }

    public void addPredicate(Event event, Predicate<Event> predicate) {
        predicates.put(event, predicate);
    }

    public void addPredicate(Predicate<Event> predicate) {
        predicates.put(new Event("", "", 0), predicate);
    }
}
