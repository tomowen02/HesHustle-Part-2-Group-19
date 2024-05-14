package screens;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.Collection;

public class TextDisplayWindow {
    private final Window window;
    private final Skin skin;
    private final Viewport viewport;


    public TextDisplayWindow(String title, Collection<String> data, Stage parentStage, Skin skin, Viewport viewport) {
        window = new Window("", skin);
        this.skin = skin;
        this.viewport = viewport;

        parentStage.addActor(buildWindow(data, title));
    }

    public void show() {
        window.setVisible(true);
        window.toFront();
    }

    public void hide() {
        window.setVisible(false);
    }

    private Window buildWindow(Collection<String> data, String titleText) {
        window.setVisible(false);
        Table table = new Table();
        window.add(table).prefHeight(600).prefWidth(800-20);

        // Title
        Label title = new Label(titleText, skin, "button");
        table.add(title).padTop(10);
        table.row();

        // Table for things inside the scrollable widget
        Table scrollTable = new Table();

        // Scrollable widget
        ScrollPane scrollWindow = new ScrollPane(scrollTable, skin);
        scrollWindow.setFadeScrollBars(false);

        table.add(scrollWindow).padTop(20).height(350).width(870);
        table.row();

        // Display the achievements
        for (String achievement : data) {
            Label text = new Label(achievement, skin, "interaction");
            text.setWrap(true);
            scrollTable.add(text).width(320f).padLeft(20);
        }

        // Exit button
        TextButton continueButton = new TextButton("Back", skin);
        table.add(continueButton).bottom().width(300).padTop(10);

        window.pack();

        window.setSize(900, 600);

        // Centre the window
        window.setX((viewport.getWorldWidth() / 2) - (window.getWidth() / 2));
        window.setY((viewport.getWorldHeight() / 2) - (window.getHeight() / 2));

        continueButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                hide();
            }
        });

        return window;
    }
}
