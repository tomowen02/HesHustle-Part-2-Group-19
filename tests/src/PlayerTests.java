import com.badlogic.gdx.math.Vector2;
import com.skloch.game.GameObject;
import com.skloch.game.Player;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

@RunWith(GdxTestRunner.class)
public class PlayerTests {
    private Player player;
    private float delta;
    private float movementMagnitude;
    private float feetWidth;
    private float feetHeight;
    private GameObject collisionObject;
    private float collisionObjectSize;
    private Vector2 defaultFeetPos;

    @Before
    public void setUp() {
        player = new Player("avatar1");
        delta = 0.017f;
        movementMagnitude = delta*player.SPEED;
        feetWidth = player.feet.width;
        feetHeight = player.feet.height;
        collisionObjectSize = 100;
        collisionObject = new GameObject(0,0, collisionObjectSize, collisionObjectSize);
        defaultFeetPos = new Vector2(player.feet.x, player.feet.y);
        ArrayList<GameObject> collidableObjects = new ArrayList<>();
        collidableObjects.add(collisionObject);
        player.setCollidables(collidableObjects);
    }

    // Player movement tests
    @Test
    public void testPlayerMoveLeft(){
        // Move with no collision
        player.setPos(0,0);
        player.move(true, false, false, false, delta);
        assertEquals("The player is not where it is expected to be when trying to move left with no collision",
                player.getX(),-movementMagnitude,0.0001);

        // Move with collision
        player.setPos(0,0);
        collisionObject.setPosition(defaultFeetPos.x-collisionObjectSize-movementMagnitude/2, defaultFeetPos.y);
        player.move(true, false, false, false, delta);
        assertEquals("The player is not where it is expected to be when trying to move left with a collision",
                player.getX(),0,0.0001);
    }
    @Test
    public void testPlayerMoveRight(){
        // Move with no collision
        player.setPos(0,0);
        player.move(false, true, false, false, delta);
        assertEquals("The player is not where it is expected to be when trying to move right with no collision",
                player.getX(),movementMagnitude,0.0001);

        // Move with collision
        player.setPos(0,0);
        collisionObject.setPosition(defaultFeetPos.x+feetWidth+movementMagnitude/2, defaultFeetPos.y);
        player.move(false, true, false, false, delta);
        assertEquals("The player is not where it is expected to be when trying to move right with a collision",
                player.getX(),0,0.0001);
    }
    @Test
    public void testPlayerMoveUp(){
        // Move with no collision
        player.setPos(0,0);
        player.move(false, false, true, false, delta);
        assertEquals("The player is not where it is expected to be when trying to move up with no collision",
                player.getY(),movementMagnitude,0.0001);

        // Move with collision
        player.setPos(0,0);
        collisionObject.setPosition(defaultFeetPos.x, defaultFeetPos.y+feetHeight+movementMagnitude/2);
        player.move(false, false, true, false, delta);
        assertEquals("The player is not where it is expected to be when trying to move up with a collision",
                player.getX(),0,0.0001);
    }
    @Test
    public void testPlayerMoveDown(){
        // Move with no collision
        player.setPos(0,0);
        player.move(false, false, false, true, delta);
        assertEquals("The player is not where it is expected to be when trying to move down with no collision",
                player.getY(),-movementMagnitude,0.0001);

        // Move with collision
        player.setPos(0,0);
        collisionObject.setPosition(defaultFeetPos.x, defaultFeetPos.y-collisionObjectSize-movementMagnitude/2);
        player.move(false, false, false, true, delta);
        assertEquals("The player is not where it is expected to be when trying to move down with a collision",
                player.getX(),0,0.0001);
    }
    @Test
    public void testPlayerMoveLeftUp(){
        // Move with no collision
        player.setPos(0,0);
        player.move(true, false, true, false, delta);
        float axisPositionScalar = (movementMagnitude) / (float)Math.sqrt(2); // We divide by root 2 to get the resultant magnitude of two perpendicular forces
        assertEquals("The player is not where it is expected to be when trying to move left and up with no collision",
                player.getX(), -axisPositionScalar, 0.0001);
        assertEquals("The player is not where it is expected to be when trying to move left and up with no collision",
                player.getY(), axisPositionScalar, 0.0001);

        // Move with collision
        player.setPos(0,0);
        collisionObject.setPosition(defaultFeetPos.x-collisionObjectSize-movementMagnitude/2, defaultFeetPos.y+feetHeight+movementMagnitude/2);
        player.move(true, false, true, false, delta);
        assertEquals("The player is not where it is expected to be when trying to move left and up with a collision",
                player.getX(), 0, 0.0001);
        assertEquals("The player is not where it is expected to be when trying to move left and up with a collision",
                player.getY(), 0, 0.0001);
    }
    @Test
    public void testPlayerMoveRightUp(){
        // Move with no collision
        player.setPos(0,0);
        player.move(false, true, true, false, delta);
        float axisPositionScalar = (movementMagnitude) / (float)Math.sqrt(2); // We divide by root 2 to get the resultant magnitude of two perpendicular forces
        assertEquals("The player is not where it is expected to be when trying to move right and up with no collision",
                player.getX(), axisPositionScalar, 0.0001);
        assertEquals("The player is not where it is expected to be when trying to move right and up with no collision",
                player.getY(), axisPositionScalar, 0.0001);

        // Move with collision
        player.setPos(0,0);
        collisionObject.setPosition(defaultFeetPos.x+feetWidth+movementMagnitude/2, defaultFeetPos.y+feetHeight+movementMagnitude/2);
        player.move(false, true, true, false, delta);
        assertEquals("The player is not where it is expected to be when trying to move right and up with a collision",
                player.getX(), 0, 0.0001);
        assertEquals("The player is not where it is expected to be when trying to move right and up with a collision",
                player.getY(), 0, 0.0001);
    }
    @Test
    public void testPlayerMoveRightDown(){
        // Move with no collision
        player.setPos(0,0);
        player.move(false, true, false, true, delta);
        float axisPositionScalar = (movementMagnitude) / (float)Math.sqrt(2); // We divide by root 2 to get the resultant magnitude of two perpendicular forces
        assertEquals("The player is not where it is expected to be when trying to move right and down with no collision",
                player.getX(), axisPositionScalar, 0.0001);
        assertEquals("The player is not where it is expected to be when trying to move right and down with no collision",
                player.getY(), -axisPositionScalar, 0.0001);

        // Move with collision
        player.setPos(0,0);
        collisionObject.setPosition(defaultFeetPos.x+feetWidth+movementMagnitude/2, defaultFeetPos.y-collisionObjectSize-movementMagnitude/2);
        player.move(false, true, false, true, delta);
        assertEquals("The player is not where it is expected to be when trying to move right and down with a collision",
                player.getX(), 0, 0.0001);
        assertEquals("The player is not where it is expected to be when trying to move right and down with a collision",
                player.getY(), 0, 0.0001);
    }
    @Test
    public void testPlayerMoveLeftDown(){
        // Move with no collision
        player.setPos(0,0);
        player.move(true, false, false, true, delta);
        float axisPositionScalar = (movementMagnitude) / (float)Math.sqrt(2); // We divide by root 2 to get the resultant magnitude of two perpendicular forces
        assertEquals("The player is not where it is expected to be when trying to move left and down with no collision",
                player.getX(), -axisPositionScalar, 0.0001);
        assertEquals("The player is not where it is expected to be when trying to move left and down with no collision",
                player.getY(), -axisPositionScalar, 0.0001);

        // Move with collision
        player.setPos(0,0);
        collisionObject.setPosition(defaultFeetPos.x-collisionObjectSize-movementMagnitude/2, defaultFeetPos.y-collisionObjectSize-movementMagnitude/2);
        player.move(true, false, false, true, delta);
        assertEquals("The player is not where it is expected to be when trying to move left and down with a collision",
                player.getX(), 0, 0.0001);
        assertEquals("The player is not where it is expected to be when trying to move left and down with a collision",
                player.getY(), 0, 0.0001);
    }
    @Test
    public void testPlayerMoveNoInput(){
        player.setPos(0,0);
        player.move(false, false, false, false, delta);
        assertEquals("The player is not where it is expected to be when trying to move with no input",
                player.getX(), 0, 0.0001);
        assertEquals("The player is not where it is expected to be when trying to move with no input",
                player.getY(), 0, 0.0001);
    }

    @Test
    public void testSetGetPlayerPosition(){
        player.setPos(1,2);
        assertEquals("The player's position is not what it is expected to be",
                player.getX(), 1, 0.0001);
        assertEquals("The player's position is not what it is expected to be",
                player.getY(), 2, 0.0001);
    }

}
