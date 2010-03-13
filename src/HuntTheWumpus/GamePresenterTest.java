package HuntTheWumpus;

import junit.framework.TestCase;
import static HuntTheWumpus.Game.*;
import HuntTheWumpus.fixtures.MockConsole;

public class GamePresenterTest extends TestCase {
  private GamePresenter p;
  private MockConsole mc;
  private Game g;

  protected void setUp() throws Exception {
    mc = new MockConsole();
    p = new GamePresenter(mc);
    g = p.getGame();
  }

  public void testDirectionErrorMessages() throws Exception {
    g.putPlayerInCavern(1);
    p.execute(EAST);
    assertTrue(mc.check("You can't go east from here."));
    p.execute(WEST);
    assertTrue(mc.check("You can't go west from here."));
    p.execute(SOUTH);
    assertTrue(mc.check("You can't go south from here."));
    p.execute(NORTH);
    assertTrue(mc.check("You can't go north from here."));
  }

  public void testAvailableDirectionsWithNoPlaceToGo() {
    g.putPlayerInCavern(1);
    assertEquals("There are no exits!", p.getAvailableDirections());
  }

  public void testSouthIsAvailable() throws Exception {
    g.addPath(1, 2, SOUTH);
    g.putPlayerInCavern(1);
    assertEquals("You can go south from here.", p.getAvailableDirections());
  }

  public void testNortAndSouthAvailable() throws Exception {
    g.addPath(1, 2, SOUTH);
    g.addPath(1, 3, NORTH);
    g.putPlayerInCavern(1);
    assertEquals("You can go north and south from here.", p.getAvailableDirections());
  }

  public void testFourDirections() throws Exception {
    g.addPath(1, 2, EAST);
    g.addPath(1, 3, WEST);
    g.addPath(1, 4, SOUTH);
    g.addPath(1, 5, NORTH);
    g.putPlayerInCavern(1);
    assertEquals("You can go north, south, east and west from here.", p.getAvailableDirections());
  }
}
