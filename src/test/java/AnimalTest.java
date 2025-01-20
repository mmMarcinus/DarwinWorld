import agh.ics.darwinworld.Model.AnimalModel.Animal;
import agh.ics.darwinworld.Model.AnimalModel.Genome;
import agh.ics.darwinworld.Model.Enums.MapDirection;
import agh.ics.darwinworld.Model.Util.Vector2d;
import agh.ics.darwinworld.Model.WorldModel.NormalWorldMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AnimalTest {

    private Animal animal;
    private Genome genome;
    private Vector2d initialPosition;

    @BeforeEach
    void setUp() {
        initialPosition = new Vector2d(2, 2);
        genome = new Genome("00000", 5); // assuming the genome has 32 genes
        animal = new Animal(initialPosition, genome, 100, 0, null, null, 0);
    }

    @Test
    void testInitialValues() {
        assertEquals(initialPosition, animal.getPosition());
        assertEquals(MapDirection.NORTH, animal.getDirection());
        assertEquals(genome, animal.getGenome());
        assertEquals(100, animal.getEnergyLevel());
        assertEquals(0, animal.getAge());
        assertEquals(0, animal.getKidsNumber());
        assertFalse(animal.isHighlighted());
        assertEquals(0, animal.getPlantsEaten());
    }

    @Test
    void testMoveWithinBounds() {
        NormalWorldMap map = new NormalWorldMap(5, 5, null);
        String movement = "1"; // rotate 1 step clockwise

        animal.move(movement, map);
        assertEquals(MapDirection.NORTH_EAST, animal.getDirection());
        assertEquals(new Vector2d(3, 3), animal.getPosition());
    }

    @Test
    void testMoveOutOfBounds() {
        NormalWorldMap map = new NormalWorldMap(5, 5, null);
        animal = new Animal(new Vector2d(4, 4), genome, 100, 0, null, null, 0);
        String movement = "2"; // rotate 1 step clockwise

        animal.move(movement, map);
        assertEquals(new Vector2d(0, 4), animal.getPosition());
    }


    @Test
    void testEnergyConsumptionOnMove() {
        NormalWorldMap map = new NormalWorldMap(5, 5, null);
        String movement = "2"; // rotate 2 steps clockwise
        int initialEnergy = animal.getEnergyLevel();

        animal.move(movement, map);
        assertEquals(initialEnergy, animal.getEnergyLevel()); // energy isn't reduced directly in move; modify if it should be
    }

    @Test
    void testEatPlant() {
        int initialEnergy = animal.getEnergyLevel();
        animal.eatPlant(20);

        assertEquals(initialEnergy + 20, animal.getEnergyLevel());
        assertEquals(1, animal.getPlantsEaten());
    }

    @Test
    void testHighlight() {
        assertFalse(animal.isHighlighted());
        animal.highlight();
        assertTrue(animal.isHighlighted());
        animal.unhighlight();
        assertFalse(animal.isHighlighted());
    }

    @Test
    void testAgeUpdate() {
        int initialAge = animal.getAge();
        animal.updateAge(initialAge + 1);
        assertEquals(initialAge + 1, animal.getAge());
    }

    @Test
    void testKidsNumberUpdate() {
        int initialKids = animal.getKidsNumber();
        animal.updateKidsNumber(initialKids + 2);
        assertEquals(initialKids + 2, animal.getKidsNumber());
    }

    @Test
    void testFamilyNumberUpdate() {
        int initialFamily = animal.getFamilyNumber();
        animal.updateFamilyNumber(initialFamily + 1);
        assertEquals(initialFamily + 1, animal.getFamilyNumber());
    }

    @Test
    void testChangeDirection() {
        animal.updateCurrentGene(3); // assuming gene 3 corresponds to some direction change
        assertEquals(3, animal.getCurrentGene());
    }


    @Test
    void testEquality() {
        Animal anotherAnimal = new Animal(initialPosition, genome, 100, 0, null, null, 0);
        assertTrue(animal.isAt(anotherAnimal.getPosition()));
    }

    @Test
    void testRotation() {
        Animal animal2 = new Animal(new Vector2d(5,5), new Genome("00000", 5), 100, 0, null, null, 0);
        animal2.move("2", new NormalWorldMap(10, 10, null));
        assertEquals(MapDirection.EAST, animal2.getDirection());
    }
}
