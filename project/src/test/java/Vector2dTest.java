import agh.ics.darwinworld.Model.Util.Vector2d;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


public class Vector2dTest {
        @Test
        void testToString() {
            Vector2d vector = new Vector2d(3, 4);
            assertEquals("(3,4)", vector.toString());
        }

        @Test
        void testEquals() {
            Vector2d vector1 = new Vector2d(3, 4);
            Vector2d vector2 = new Vector2d(3, 4);
            Vector2d vector3 = new Vector2d(5, 6);
            assertEquals(vector1, vector2);
            assertNotEquals(vector1, vector3);
        }

        @Test
        void testHashCode() {
            Vector2d vector1 = new Vector2d(3, 4);
            Vector2d vector2 = new Vector2d(3, 4);
            assertEquals(vector1.hashCode(), vector2.hashCode());
        }

        @Test
        void testPrecedes() {
            Vector2d vector1 = new Vector2d(2, 2);
            Vector2d vector2 = new Vector2d(3, 3);
            assertTrue(vector1.precedes(vector2));
            assertFalse(vector2.precedes(vector1));
        }

        @Test
        void testFollows() {
            Vector2d vector1 = new Vector2d(3, 3);
            Vector2d vector2 = new Vector2d(2, 2);
            assertTrue(vector1.follows(vector2));
            assertFalse(vector2.follows(vector1));
        }

        @Test
        void testAdd() {
            Vector2d vector1 = new Vector2d(1, 1);
            Vector2d vector2 = new Vector2d(2, 3);
            Vector2d result = vector1.add(vector2);
            assertEquals(new Vector2d(3, 4), result);
        }

        @Test
        void testSubtract() {
            Vector2d vector1 = new Vector2d(5, 5);
            Vector2d vector2 = new Vector2d(2, 3);
            Vector2d result = vector1.subtract(vector2);
            assertEquals(new Vector2d(3, 2), result);
        }

        @Test
        void testUpperRight() {
            Vector2d vector1 = new Vector2d(1, 4);
            Vector2d vector2 = new Vector2d(3, 2);
            Vector2d result = vector1.upperRight(vector2);
            assertEquals(new Vector2d(3, 4), result);
        }

        @Test
        void testLowerLeft() {
            Vector2d vector1 = new Vector2d(1, 4);
            Vector2d vector2 = new Vector2d(3, 2);
            Vector2d result = vector1.lowerLeft(vector2);
            assertEquals(new Vector2d(1, 2), result);
        }

        @Test
        void testOpposite() {
            Vector2d vector = new Vector2d(3, -4);
            Vector2d result = vector.opposite();
            assertEquals(new Vector2d(-3, 4), result);
        }
}
