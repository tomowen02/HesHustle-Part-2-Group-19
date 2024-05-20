package com.skloch.game;

import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.utils.Array;
import screens.GameScreen;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;

/**
 * A class that maps Object's event strings to actual Java functions.
 */
public class EventManager {
    private final GameScreen gameScreen;
    private final HashMap<String, Event> events;
    private final ArrayList<Achievement> achievements;
    private final Array<String> talkTopics;
    private final float FADE_DURATION = 0.5f;

    /**
     * A class that maps Object's event strings to actual Java functions.
     * To run a function call event(eventString), to add arguments add dashes.
     * E.g. a call to the Piazza function with an arg of 1 would be: "piazza-1"
     * Which the function interprets as "study at the piazza for 1 hour".
     * Object's event strings can be set in the Tiled map editor with a property called "event"
     *
     * @param gameScreen An instance of the GameScreen containing a player and dialogue box
     */
    public EventManager (GameScreen gameScreen) {
        this.gameScreen = gameScreen;

        // Define what to say when interacting with an object whose text won't change
        events = new HashMap<String, Event>();
        events.put("chest", new Event("chest", "Open the chest?", 0));
        events.put("comp_sci", new Event("comp_sci", "Would you like to study?", 20));
        events.put("chat", new Event("chat", "Would you like to chat to your friends?", 10));
        events.put("basketball", new Event("basketball", "Would you like to play basketball?", 20));
        events.put("eat", new Event("eat", "Would you like to eat?", 10));
        events.put("accomodation", new Event("accomodation", "Go to sleep for the night?\nYour alarm is set for 8am.", 0));
        events.put("rch", new Event("rch", "Would you like to study in the Ron Cook Hub?", 10)); // Changes, dynamically returned in getObjectInteraction
        events.put("tree", new Event("tree", "Speak to the tree?", 5));
        events.put("teleport", new Event("teleport", "Would you like to move location?", 0));
        events.put("ducks", new Event("ducks", "Would you like to feed the ducks?", 10));
        events.put("cook", new Event("cook", "Would you like to cook?", 30));

        // Some random topics that can be chatted about
        String[] topics = {"Dogs", "Cats", "Exams", "Celebrities", "Flatmates", "Video games", "Sports", "Food", "Fashion"};
        talkTopics = new Array<String>(topics);
        String[] meals = {"Pesto pasta", "Red pesto pasta", "Plain pasta", "Plain pesto", "Butter pasta", "Salt + Pepper pasta", "Pasta on toast", "Plain toast"};

        achievements = new ArrayList<>();

        Achievement achievement = new Achievement("Yappa-Yapper", "Talk to your friends 3 days in a row.", 3000);
        achievement.addPredicate(events.get("chat"), p -> p.getMaxStreak() >= 3);
        achievements.add(achievement);

        achievement = new Achievement("Academic Weapon", "Study every day.", 10000);
        achievement.addPredicate(events.get("comp_sci"), p -> p.getMaxStreak() == 7);
        achievements.add(achievement);

        achievement = new Achievement("Eco Warrior", "Feed the ducks and speak to the tree on the same day.", 3000);
        achievement.addPredicate(events.get("ducks"), p -> p.getTimesPerformedToday() == 1);
        achievement.addPredicate(events.get("tree"), p -> p.getTimesPerformedToday() == 1);
        achievements.add(achievement);

        achievement = new Achievement("Living on the edge", "Don't study a single time all week.", 1000);
        achievement.addPredicate(events.get("comp_sci"), p -> p.getTimesPerformedTotal() == 0);
        achievement.addPredicate(p -> gameScreen.getDay() == gameScreen.FINAL_DAY);
        achievements.add(achievement);

        achievement = new Achievement("Air Jordan", "Play basketball 4 days in a row.", 5000);
        achievement.addPredicate(events.get("basketball"), p -> p.getMaxStreak() == 4);
        achievements.add(achievement);

        achievement = new Achievement("Roses Champion", "Play basketball 5 times in one day.", 6500);
        achievement.addPredicate(events.get("basketball"), p -> p.getTimesPerformedToday() == 5);
        achievements.add(achievement);

        achievement = new Achievement("Hunger Strike", "Go a whole day without eating anything.", 1000);
        achievement.addPredicate(events.get("eat"), p -> p.getTimesPerformedToday() == 0);
        achievement.addPredicate(events.get("cook"), p -> p.getTimesPerformedToday() == 0);
        achievements.add(achievement);

        achievement = new Achievement("Swot", "Study every day, but don't talk to your friends a single time. Your only friend is a tree.", 10000);
        achievement.addPredicate(events.get("comp_sci"), p -> p.getMaxStreak() == 7);
        achievement.addPredicate(events.get("chat"), p -> p.getTimesPerformedTotal() == 0);
        achievement.addPredicate(events.get("tree"), p -> p.getTimesPerformedTotal() >= 1);
        achievements.add(achievement);
    }

    public void event (String eventKey, String params) {
        String[] args;
        if (!params.isEmpty()) {
            args = params.split(";");
        } else {
            args = new String[0];
        }
        // Important functions, most likely called after displaying text
        switch (eventKey) {
            case "fadefromblack":
                fadeFromBlack();
                break;
            case "fadetoblack":
                fadeToBlack();
                break;
            case "gameover":
                gameScreen.GameOver();
                break;
        }

        // Events related to objects
        boolean eventPerformed = false;
        switch (eventKey) {
            case "tree":
                eventPerformed = treeEvent();
                break;
            case "chest":
                eventPerformed = chestEvent();
                break;
            case "chat":
                eventPerformed = chatEvent(args);
                break;
            case "eat":
                eventPerformed = eatEvent(args);
                break;
            case "comp_sci":
                eventPerformed = compSciEvent(args);
                break;
            case "rch":
                compSciEvent(args);
                break;
            case "accomodation":
                eventPerformed = accomEvent(args);
                break;
            case "teleport":
                eventPerformed = teleportEvent(args);
                break;
            case "basketball":
                eventPerformed = basketballEvent(args);
                break;
            case "ducks":
                eventPerformed = ducksEvent(args);
                break;
            case "cook":
                eventPerformed = cookEvent(args);
                break;
            case "exit":
                // Should do nothing and just close the dialogue menu
                gameScreen.dialogueBox.hide();
                break;
            default:
                objectEvent(eventKey);
                break;
        }
        if (eventPerformed && events.containsKey(eventKey)) {
            events.get(eventKey).perform();
        }
    }

    public void advanceDay() {
        for (Achievement achievement : achievements)
        {
            if (!achievement.isAchieved()) {
                if (achievement.validate())
                    System.out.println(achievement.getName());
            }
        }
        for (Event event : events.values()) {
            event.dayAdvanced();
        }
    }

    /**
     * Gets the interaction text associated with each object via a key
     * @param key
     * @return The object interaction text
     */
    public String getObjectInteraction(String key) {
        if (key.contains("teleport")) {
            return "teleport";
        } else {
            return events.get(key).getText();
        }
    }

    /**
     * @return True if the object has some custom text to display that isn't just "This is an x!"
     */
    public boolean hasCustomObjectInteraction(String key) {
        return events.containsKey(key);
    }

    /**
     * Sets the text when talking to a tree
     */
    public boolean treeEvent() {
        gameScreen.dialogueBox.hideSelectBox();
        gameScreen.dialogueBox.setText("The tree doesn't say anything back.");
        return true;
    }


    public boolean chestEvent() {
        gameScreen.dialogueBox.hideSelectBox();
        gameScreen.dialogueBox.setText("Wow! This chest is full of so many magical items! I wonder how they will help you out on your journey! Boy, this is an awfully long piece of text, I wonder if someone is testing something?\n...\n...\n...\nHow cool!");
        return true;
    }

    /**
     * Sets the text when talking to an object without a dedicated function
     */
    public void objectEvent(String object) {
        gameScreen.dialogueBox.hideSelectBox();
        gameScreen.dialogueBox.setText("This is a " +  object + "!");
    }

    /**
     * Lets the player study at the piazza for x num of hours, decreases the player's energy and increments the
     * game time.
     *
     * @param args Arguments to be passed, should contain the hours the player wants to study. E.g. ["piazza", "1"]
     */
    public boolean chatEvent(String[] args) {
        if (isDay()) {
            int energyCost = events.get("chat").getEnergyCost();
            // If the player is too tired to meet friends
            if (gameScreen.getEnergy() < energyCost) {
                gameScreen.dialogueBox.setText("You are too tired to meet your friends right now!");

            } else if (args.length == 0) {
                // Ask the player to chat about something (makes no difference)
                String[] topics = randomTopics(3);
                gameScreen.dialogueBox.setText("What do you want to chat about?");
                String[] events = new String[] {"chat", "chat", "chat"};
                gameScreen.dialogueBox.getSelectBox().setOptions(topics, events, topics);
            } else {
                // Say that the player chatted about this topic for 1-3 hours
                // RNG factor adds a slight difficulty (may consume too much energy to study)
                int hours = ThreadLocalRandom.current().nextInt(1, 4);
                gameScreen.dialogueBox.setText(String.format("You talked about %s for %d hours!", args[0].toLowerCase(), hours));
                gameScreen.decreaseEnergy(energyCost * hours);
                gameScreen.passTime(hours * 60, false); // in seconds
                gameScreen.addRecreationalHours(hours);
                return true;
            }
        } else {
            gameScreen.dialogueBox.setText("It's too late to meet your friends, go to bed!");
        }

        return false;
    }

    /**
     * @param amount The amount of topics to return
     * @return An array of x random topics the player can chat about
     */
    private String[] randomTopics(int amount) {
        // Returns an array of 3 random topics
        Array<String> topics = new Array<>(amount);

        for (int i = 0;i<amount;i++) {
            String choice = talkTopics.random();
            // If statement to ensure topic hasn't already been selected
            if (!topics.contains(choice, false)) {
                topics.add(choice);
            } else {
                i -= 1;
            }
        }

        return topics.toArray(String.class);
    }

    /**
     * The event to be run when interacting with the computer science building
     * Gives the player the option to study for 2, 3 or 4 hours
     * @param args
     * @return true if the event is successfully performed, false otherwise.
     */
    public boolean compSciEvent(String[] args) {
        if (isDay()) {
            // If the player has already studied today and has used their 'catchup' session
            Event event = events.get("comp_sci");
            if (event.getTimesPerformedToday() > 0 && event.getTimesPerformedTotal() > gameScreen.getDay())
            {
                gameScreen.dialogueBox.setText("You've already studied enough for one day...");
                return false;
            }

            int energyCost = events.get("comp_sci").getEnergyCost();
            // If the player is too tired for any studying:
            if (gameScreen.getEnergy() < energyCost) {
                gameScreen.dialogueBox.hideSelectBox();
                gameScreen.dialogueBox.setText("You are too tired to study right now!");
            } else if (args.length == 0) {
                // If the player has not yet chosen how many hours, ask
                gameScreen.dialogueBox.setText("Study for how long?");
                String[] options = new String[] {"2 Hours (20)", "3 Hours (30)", "4 Hours (40)"};
                String[] events = new String[] {"comp_sci", "comp_sci", "comp_sci"};
                String[] params = new String[] {"2", "3", "4"};
                gameScreen.dialogueBox.getSelectBox().setOptions(options, events, params);
            } else {
                int hours = Integer.parseInt(args[0]);
                // If the player does not have enough energy for the selected hours
                if (gameScreen.getEnergy() < hours*energyCost) {
                    gameScreen.dialogueBox.setText("You don't have the energy to study for this long!");
                } else {
                    // If they do have the energy to study
                    gameScreen.dialogueBox.setText(String.format("You studied for %s hours!\nYou lost %d energy", args[0], hours*energyCost));
                    gameScreen.decreaseEnergy(energyCost * hours);
                    gameScreen.addStudyHours(hours);
                    gameScreen.passTime(hours * 60, false); // in seconds
                    return true;
                }
            }
        } else {
            gameScreen.dialogueBox.setText("It's too late to study, go to bed!");
        }

        return false;
    }


    /**
     * The event to be run when the player interacts with the ron cooke hub
     * Gives the player the choice to eat breakfast, lunch or dinner depending on the time of day
     * @param args
     * @return true if the event is successfully performed, false otherwise.
     */
    public boolean eatEvent(String[] args) {
        String eventKey = "eat";
        if (args.length >= 1) {
            eventKey = args[0];
        }
        if (isDay()) {
            int energyCost = events.get(eventKey).getEnergyCost();
            if (gameScreen.getEnergy() < energyCost) {
                gameScreen.dialogueBox.setText("You are too tired to eat right now!");
            } else {
                gameScreen.dialogueBox.setText(String.format("You took an hour to eat %s!\nYou lost %d energy!", gameScreen.getMeal(), energyCost));
                gameScreen.decreaseEnergy(energyCost);
                gameScreen.passTime(60, false); // in seconds
                return true;
            }
        } else {
            gameScreen.dialogueBox.setText("It's too late to eat food, go to bed!");
        }

        return false;
    }

    /**
     * Lets the player go to sleep, fades the screen to black then shows a dialogue about the amount of sleep
     * the player gets
     * Then queues up fadeFromBlack to be called when this dialogue closes
     * @see GameScreen fadeToBlack function
     * @param args Unused currently
     * @return true if the event is successfully performed, false otherwise.
     */
    public boolean accomEvent(String[] args) {
        gameScreen.dialogueBox.hide();

        // Calculate the hours slept to the nearest hour
        // Wakes the player up at 8am
        float secondsSlept;
        if (isVeryEarly()) {
            gameScreen.setSleeping(true);
            secondsSlept = (60*8 - gameScreen.getSeconds());
        } else {
            // Account for the wakeup time being in the next day
            gameScreen.setSleeping(true);
            secondsSlept = (((60*8) + 1440) - gameScreen.getSeconds());
        }
        int hoursSlept = Math.round(secondsSlept / 60f);

        RunnableAction setTextAction = new RunnableAction();
        setTextAction.setRunnable(() -> {
            if (gameScreen.getSleeping()) {
                gameScreen.dialogueBox.show();
                gameScreen.dialogueBox.setText(String.format("You slept for %d hours!\nYou recovered %d energy!", hoursSlept, Math.min(100, hoursSlept*13)), "fadefromblack");
                // Restore energy and pass time
                gameScreen.setEnergy(hoursSlept*13);
                gameScreen.passTime(secondsSlept, true);
                gameScreen.addSleptHours(hoursSlept);
            }
        });
        if (!gameScreen.isTest) {
            fadeToBlack(setTextAction);
        }
        return true;
    }

    public boolean teleportEvent(String[] args) {
        try {
//            gameScreen.dialogueBox.hide();
            String mapPath = args[0];
            if (args.length == 1) {
                gameScreen.mapManager.loadMap(mapPath);
            } else {
                String spawn = args[1]; // in the form "x,y"
                String[] spawnCoords = spawn.split(",");
                float x = Float.parseFloat(spawnCoords[0]);
                float y = Float.parseFloat(spawnCoords[1]);
                gameScreen.mapManager.loadMap(mapPath);
                gameScreen.player.setPos(x, y);
            }
            gameScreen.dialogueBox.hide();
            return true;
        } catch (Exception e) {
            gameScreen.dialogueBox.setText("Teleport failed!");
        }

        return false;
    }

    public boolean ducksEvent(String[] args) {
        if (isDay()) {
            int energyCost = events.get("ducks").getEnergyCost();
            if (gameScreen.getEnergy() < energyCost) {
                gameScreen.dialogueBox.setText("You are too tired to feed the ducks right now!");
            } else {
                gameScreen.dialogueBox.setText("You fed the ducks for an hour!\nYou lost "+energyCost+" energy!");
                gameScreen.decreaseEnergy(energyCost);
                gameScreen.passTime(60, false);
                gameScreen.addRecreationalHours(1);
                return true;
            }
        } else {
            gameScreen.dialogueBox.setText("It's too late to feed the ducks, the ducks are asleep!");
        }

        return false;
    }

    public boolean basketballEvent(String[] args) {
        if (isDay()) {
            int energyCost = events.get("basketball").getEnergyCost();
            if (gameScreen.getEnergy() < energyCost) {
                gameScreen.dialogueBox.setText("You are too tired to play basketball right now!");
            } else {
                gameScreen.dialogueBox.setText("You played for an hour!\nYou lost "+energyCost+" energy!");
                gameScreen.decreaseEnergy(energyCost);
                gameScreen.passTime(60, false);
                gameScreen.addRecreationalHours(1);
                return true;
            }
        } else {
            gameScreen.dialogueBox.setText("It's too late to play basketball!");
        }

        return false;
    }

    public boolean cookEvent(String[] args) {
        if (isDay()) {
            int energyCost = events.get("cook").getEnergyCost();
            if (gameScreen.getEnergy() < energyCost) {
                gameScreen.dialogueBox.setText("You are too tired to cook right now. You might burn the house down!");
            } else {
                gameScreen.dialogueBox.setText(String.format("You took an hour to cook %s.\nYou lost %d energy!", gameScreen.getMeal(), energyCost));
                gameScreen.decreaseEnergy(energyCost);
                gameScreen.passTime(60, false); // in seconds
                return true;
            }
        } else {
            gameScreen.dialogueBox.setText("It's too late to cook a meal - you might fall asleep!");
        }

        return false;
    }

    /**
     * Fades the screen to black
     */
    public void fadeToBlack() {
        gameScreen.blackScreen.addAction(Actions.fadeIn(FADE_DURATION));
    }

    /**
     * Fades the screen to black, then runs a runnable after it is done
     * @param runnable A runnable to execute after fading is finished
     */
    public void fadeToBlack(RunnableAction runnable) {
        gameScreen.blackScreen.addAction(Actions.sequence(Actions.fadeIn(FADE_DURATION), runnable));
    }

    /**
     * Fades the screen back in from black, displays a good morning message if the player was sleeping
     */
    public void fadeFromBlack() {
        // If the player is sleeping, queue up a message to be sent
        if (gameScreen.getSleeping()) {
            RunnableAction setTextAction = new RunnableAction();
            setTextAction.setRunnable(() -> {
                if (gameScreen.getSleeping()) {
                    gameScreen.dialogueBox.show();
                    // Show a text displaying how many days they have left in the game
                    gameScreen.dialogueBox.setText(gameScreen.getWakeUpMessage());
                    gameScreen.setSleeping(false);
                }
            });

            // Queue up events
            gameScreen.blackScreen.addAction(Actions.sequence(Actions.fadeOut(FADE_DURATION), setTextAction));
        } else {
            gameScreen.blackScreen.addAction(Actions.fadeOut(FADE_DURATION));
        }
    }

    /**
     * @return Strings of format "Achievement Name: Achievement Description  (x points)" for each 'achieved' achievement.
     */
    public Collection<String> getAchievements() {
        Collection<String> data = new ArrayList<>();

        for (Achievement achievement : achievements) {
            if (achievement.isAchieved()) {
                data.add(achievement.getName() + ": " + achievement.getDescription() + " (" + achievement.score + " points)");
            }
        }

        return data;
    }

    public Collection<Event> getEvents() {
        Collection<Event> eventData = new ArrayList<>();
        for (Event event : events.values()) {
            eventData.add(event.copy());
        }
        return eventData;
    }

    public int getAchievementScore() {
        int score = 0;
        for (Achievement achievement : achievements) {
            if (achievement.isAchieved()) {
                score += achievement.score;
            }
        }
        return score;
    }

    private boolean isDay() {
        // Daytime is between 7am and 9pm
        return !isVeryEarly() && !isNight();
    }

    private boolean isVeryEarly() {
        return gameScreen.getSeconds() < 7*60;
    }

    private boolean isNight() {
        return gameScreen.getSeconds() > 21*60;
    }
}
