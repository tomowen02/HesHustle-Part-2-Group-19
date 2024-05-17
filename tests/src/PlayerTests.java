import com.badlogic.gdx.math.Vector2;
import com.skloch.game.GameObject;
import com.skloch.game.Player;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import static org.junit.Assert.*;

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
    public void testPlayerMoveAllDirections(){
        player.setPos(0,0);
        player.move(true, true, true, true, delta);
        assertEquals("The player is not where it is expected to be when trying to move with no input",
                player.getX(), 0, 0.0001);
        assertEquals("The player is not where it is expected to be when trying to move with no input",
                player.getY(), 0, 0.0001);
    }
    @Test
    public void testPlayerMovementEdgeCases() {
        // Move the player to the edge of a collision object
        player.setPos(0,0);
        collisionObject.setPosition(defaultFeetPos.x+feetWidth+movementMagnitude, defaultFeetPos.y);
        player.move(false, true, false, false, delta); // Move right
        assertEquals("The player is not where it is expected to be when trying to move to the edge of a collision object",
                player.getX(), movementMagnitude, 0.0001);

        // Move the player from inside a collision object to outside a collision object
        player.setPos(0,0);
        collisionObject.setPosition(defaultFeetPos.x-collisionObjectSize+movementMagnitude/2, defaultFeetPos.y); // Will move the object to a location where the player can step out of it to the right
        player.move(false, true, false, false, delta); // Move right
        assertEquals("The player is not where it is expected to be when trying to move from inside a collision object to outside",
                player.getX(), movementMagnitude, 0.0001);
    }
    @Test
    public void testSetGetPlayerPosition(){
        player.setPos(1,2);
        assertEquals("The player's position is not what it is expected to be",
                player.getX(), 1, 0.0001);
        assertEquals("The player's position is not what it is expected to be",
                player.getY(), 2, 0.0001);
    }

    // Player interaction tests
    @Test
    public void testNearObject(){
        player.setPos(0,0);
        GameObject interactableObject = new GameObject(0,0, player.eventHitbox.getWidth(), player.eventHitbox.getHeight());
        interactableObject.put("event", "test");
        ArrayList<GameObject> interactableObjects = new ArrayList<>();
        interactableObjects.add(interactableObject);
        player.setInteractables(interactableObjects);

        // Test that the player is able to detect an object that is within the hitbox
        player.move(false, false, false, false, delta); // Updates the player's perception of objects
        assertTrue("The player is not near an object when it is expected to be", player.nearObject());

        // Test that the player is not able to detect an object that is outside the hitbox
        interactableObject.setPosition(player.eventHitbox.getWidth()+1, 0);
        player.move(false, false, false, false, delta); // Updates the player's perception of objects
        assertFalse("The player is near an object when it is not expected to be", player.nearObject());
    }

    @Test
    public void testClosestObject() {
        player.setPos(0,0);
        ArrayList<GameObject> interactableObjects = new ArrayList<>();
        GameObject object1 = new GameObject(10,10, player.eventHitbox.getWidth(), player.eventHitbox.getHeight());
        object1.put("event", "test");
        interactableObjects.add(object1);
        GameObject object2 = new GameObject(20,20, player.eventHitbox.getWidth(), player.eventHitbox.getHeight());
        object2.put("event", "test");
        interactableObjects.add(object2);
        player.setInteractables(interactableObjects);

        // Test that the closest object is returned
        player.move(false, false, false, false, delta); // Updates the player's perception of objects
        assertEquals("The closest object is not what is expected",
                player.getClosestObject(), object1);

        // Move the player closer to object2 and check that it is now the closest object
        player.setPos(100, 100);
        player.move(false, false, false, false, delta); // Updates the player's perception of objects
        assertEquals("The closest object is not what is expected",
                player.getClosestObject(), object2);
    }
}
