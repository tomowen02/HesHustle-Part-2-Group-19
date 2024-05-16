import com.badlogic.gdx.Gdx;
import com.skloch.game.SoundManager;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

@RunWith(GdxTestRunner.class)
public class SoundManagerTests {
    //  The test to check for the existence of sound assets
    @Test
    public void testSetMusicVolume() {
        SoundManager soundManager = new SoundManager();

        // Set volume to a normal level
        soundManager.setMusicVolume(50);
        assertEquals("The volume was not set correctly",
        soundManager.getMusicVolume(), 50, 0.0001);

        // Set volume a level that is too high
        soundManager.setMusicVolume(101);
        assertEquals("The volume was not set correctly when it was too high",
                soundManager.getMusicVolume(), 100, 0.0001);

        // Set volume a level that is too low
        soundManager.setMusicVolume(-1);
        assertEquals("The volume was not set correctly when it was too low",
                soundManager.getMusicVolume(), 0, 0.0001);
    }

    @Test
    public void testSetSfxVolume() {
        SoundManager soundManager = new SoundManager();

        // Set volume to a normal level
        soundManager.setSfxVolume(50);
        assertEquals("The volume was not set correctly",
                soundManager.getSfxVolume(), 50, 0.0001);

        // Set volume a level that is too high
        soundManager.setSfxVolume(101);
        assertEquals("The volume was not set correctly when it was too high",
                soundManager.getSfxVolume(), 100, 0.0001);

        // Set volume a level that is too low
        soundManager.setSfxVolume(-1);
        assertEquals("The volume was not set correctly when it was too low",
                soundManager.getSfxVolume(), 0, 0.0001);
    }

    @Test
    public void testPlayPauseSound() {
        // Test that the playPauseSound method executes without exception
        SoundManager soundManager = new SoundManager();
        soundManager.playPauseSound();
    }

    @Test
    public void testPlayDialogueOpen() {
        // Test that the playDialogueOpen method executes without exception
        SoundManager soundManager = new SoundManager();
        soundManager.playDialogueOpen();
    }

    @Test
    public void testPlayDialogueOption() {
        // Test that the playDialogueOption method executes without exception
        SoundManager soundManager = new SoundManager();
        soundManager.playDialogueOption();
    }

    @Test
    public void testPlayButton() {
        // Test that the playButton method executes without exception
        SoundManager soundManager = new SoundManager();
        soundManager.playButton();
    }

    @Test
    public void testPlayOverworldMusic() {
        // Test that the playOverworldMusic method executes without exception
        SoundManager soundManager = new SoundManager();
        soundManager.playOverworldMusic();
    }

    @Test
    public void testStopOverworldMusic() {
        // Test that the stopOverworldMusic method executes without exception
        SoundManager soundManager = new SoundManager();
        soundManager.stopOverworldMusic();
    }

    @Test
    public void testPlayMenuMusic() {
        // Test that the playMenuMusic method executes without exception
        SoundManager soundManager = new SoundManager();
        soundManager.playMenuMusic();

    }

}
