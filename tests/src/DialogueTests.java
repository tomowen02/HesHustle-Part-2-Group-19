import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.skloch.game.DialogueBox;
import com.skloch.game.DialogueBox.SelectBox;
import com.skloch.game.EventManager;
import com.skloch.game.HustleGame;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(GdxTestRunner.class)
public class DialogueTests {
    private Skin skin;

    @Before
    public void setUp() {
        skin = new Skin(Gdx.files.internal(HustleGame.PRIMARY_SKIN));
    }

    @Test
    public void testDialogueVisibility() {
        DialogueBox dialogueBox = new DialogueBox(skin);

        //Test initial visibility
        Boolean test = dialogueBox.isVisible();
        assertTrue("Dialogue box is initially supposed to be visible but it is invisible",
                dialogueBox.isVisible());

        // Test visibility when hidden
        dialogueBox.hide();
        assertFalse("Dialogue box is currently supposed to be invisible but it is visible after hide() is called",
                dialogueBox.isVisible());

        // Test visibility when hidden
        dialogueBox.show();
        assertTrue("Dialogue box is currently supposed to be visible but it is invisible after show() is called",
                dialogueBox.isVisible());
    }

    @Test
    public void testLabelUpdates() {
        DialogueBox dialogueBox = new DialogueBox(skin);
        String testText = "Hello there :)";
        dialogueBox.setText(testText);
        String labelText = dialogueBox.getText().replace("\n", "");
        assertEquals("The label text was not as expected",
                testText, labelText);
    }

    @Test
    public void testEnterAdvancesTextOrActivatesEvent() {
        // Ensure that the Enter key advances the text on a long dialogue message,
        // and then activates an event after pressing Enter again

        DialogueBox dialogueBox = new DialogueBox(skin);
        EventManager mockEventManager = mock(EventManager.class);

        dialogueBox.show();
        dialogueBox.setText("Some text that spans multiple lines for testing.", "testEvent", "param");
        dialogueBox.hide();

        dialogueBox.enter(mockEventManager);

        // Check that Enter has not activated the event yet
        verify(mockEventManager, never()).event(anyString(), anyString());

        // Check that enter activates the event and hides the dialogue box
        dialogueBox.enter(mockEventManager);
        assertFalse("Dialogue box should be invisible after processing all text and events",
                dialogueBox.isVisible());
        verify(mockEventManager).event("testEvent", "param");
        assertFalse("Dialogue box is currently supposed to be invisible but it is visible after an event is activated",
                dialogueBox.isVisible());
    }

    @Test
    public void testChoiceNavigation() {
        DialogueBox dialogueBox = new DialogueBox(skin);

        String[] options = {"Yes", "No"};
        String[] events = {"event1", "event2"};
        dialogueBox.getSelectBox().setOptions(options, events);

        assertEquals("Choice should be the first option",
                "event1", dialogueBox.getSelectBox().getChoice());

        dialogueBox.getSelectBox().choiceDown();
        assertEquals("Choice index should increase by 1",
                "event2", dialogueBox.getSelectBox().getChoice());

        dialogueBox.getSelectBox().choiceUp();
        assertEquals("Choice index should decrease to 0",
                "event1", dialogueBox.getSelectBox().getChoice());

        // Check that increasing or decreasing the choice too far, limit the choice to the bounds of the options
        dialogueBox.getSelectBox().choiceUp();
        assertEquals("Decreasing the choice too far should be limited to the first option",
                "event1", dialogueBox.getSelectBox().getChoice());
        dialogueBox.getSelectBox().choiceDown();
        assertEquals("Increasing the choice too far should be limited to the last option",
                "event2", dialogueBox.getSelectBox().getChoice());
    }

}
