package HuntTheWumpus;

import static HuntTheWumpus.Game.EAST;
import static HuntTheWumpus.Game.WEST;
import HuntTheWumpus.fixtures.MockConsole;
import junit.framework.TestCase;

public class GameTest extends TestCase {
  private Game g;
  private ShootInteractor shootInteractor;
  private MockConsole mc;

  protected void setUp() throws Exception {
    g = new Game();
    shootInteractor = g.getShootInteractor();
  }

  public void testGoEast() throws Exception {
    g.addPath(1, 2, EAST);
    g.putPlayerInCavern(1);
    g.move(EAST);
    assertEquals(2, g.playerCavern());
  }

  public void testGoEastTwice() throws Exception {
    g.addPath(1, 2, EAST);
    g.addPath(2, 3, EAST);
    g.putPlayerInCavern(1);
    g.move(EAST);
    g.move(EAST);
    assertEquals(3, g.playerCavern());
  }

  public void testSmellWumpus() throws Exception {
    g.addPath(1, 2, EAST);
    g.putPlayerInCavern(1);
    g.putWumpusInCavern(2);
    assertTrue(g.canSmellWumpus());
  }

  public void testCantSmellWumpus() throws Exception {
    g.addPath(1, 2, EAST);
    g.addPath(2, 3, EAST);
    g.putPlayerInCavern(1);
    g.putWumpusInCavern(3);
    assertFalse(g.canSmellWumpus());
  }

  public void testPickUpArrows() throws Exception {
    g.addPath(1, 2, EAST);
    g.putPlayerInCavern(1);
    g.setQuiver(0);
    g.putArrowInCavern(2);
    g.move(EAST);
    assertEquals(1, g.getQuiver());
    assertEquals(0, g.arrowsInCavern(2));
  }

  public void testShootArrow() throws Exception {
    g.addPath(1, 2, EAST);
    g.addPath(2, 3, EAST);
    g.putPlayerInCavern(1);
    g.setQuiver(1);
    assertTrue(shootInteractor.shoot(EAST).wasShot());
    assertEquals(0, g.getQuiver());
    assertEquals(1, g.arrowsInCavern(3));
    assertEquals(0, g.arrowsInCavern(2));
    assertFalse(g.gameTerminated());
  }

  public void testShootArrowWhenQuiverEmpty() throws Exception {
    g.addPath(1, 2, EAST);
    g.putPlayerInCavern(1);
    g.setQuiver(0);
    assertFalse(shootInteractor.shoot(EAST).wasShot());
    assertEquals(0, g.getQuiver());
    assertEquals(0, g.arrowsInCavern(2));
  }

  public void testPlayerDiesIfShootsAtWall() throws Exception {
    g.putPlayerInCavern(1);
    g.setQuiver(1);
    assertTrue(shootInteractor.shoot(EAST).wasShot());
    assertTrue(g.gameTerminated());
  }

  public void testFallInPit() throws Exception {
    g.addPath(1, 2, EAST);
    g.putPlayerInCavern(1);
    g.putPitInCavern(2);
    g.move(EAST);
    assertTrue(g.gameTerminated());
    assertTrue(g.fellInPit());
  }

  public void testHearPit() throws Exception {
    g.addPath(1, 2, EAST);
    g.addPath(2, 3, EAST);
    g.putPlayerInCavern(1);
    g.putPitInCavern(3);
    g.move(EAST);
    assertTrue(g.canHearPit());
    g.move(WEST);
    assertFalse(g.canHearPit());
  }

  public void testKillWumpusAtDistance() throws Exception {
    g.addPath(1, 2, EAST);
    g.addPath(2, 3, EAST);
    g.putWumpusInCavern(3);
    g.putPlayerInCavern(1);
    g.setQuiver(1);
    g.getShootInteractor().shoot(EAST).wasShot();
    assertTrue(g.wumpusHitByArrow());
    assertTrue(g.gameTerminated());
  }

  public void testKillWumpusUpClose() throws Exception {
    g.addPath(1, 2, EAST);
    g.putWumpusInCavern(2);
    g.putPlayerInCavern(1);
    g.setQuiver(1);
    ShootResponse shootResponse = shootInteractor.shoot(EAST);
    assertTrue(shootResponse.wasShot());
    assertTrue(g.wumpusHitByArrow());
    assertTrue(g.gameTerminated());
  }

  public void testRandomWumpusMovement() throws Exception {
    boolean moved = false;
    g.addPath(1, 2, EAST);
    g.putWumpusInCavern(1);
    for (int i = 0; i < 100; i++) {
      g.moveWumpus();
      if (g.getWumpusCavern() == 2) {
        moved = true;
        break;
      }
    }
    assertTrue(moved);
  }

  public void testHearBats() throws Exception {
    g.addPath(1,2,EAST);
    g.putPlayerInCavern(1);
    g.putBatsInCavern(2);
    assertTrue(g.canHearBats());
  }

  public void testBatsCarryYouAway() throws Exception {
    g.addPath(1,2, EAST);
    g.putPlayerInCavern(1);
    g.putBatsInCavern(2);
    g.move(EAST);
    assertTrue(g.batTransport());
    assertEquals(1, g.playerCavern());
  }
}
