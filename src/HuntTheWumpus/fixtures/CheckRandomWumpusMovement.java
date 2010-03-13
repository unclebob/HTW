package HuntTheWumpus.fixtures;

import static HuntTheWumpus.fixtures.GameDriver.g;
import static HuntTheWumpus.fixtures.GameDriver.p;

public class CheckRandomWumpusMovement {
  private int carvernCounts[] = new int[6];
  private int cavern;

  public CheckRandomWumpusMovement() {
    int wumpusCavern = 2;
    for (int i = 0; i < 1000; i++) {
      g.putWumpusInCavern(wumpusCavern);
      p.execute("R");
      carvernCounts[g.getWumpusCavern()]++;
    }
  }

  public int count() {
    return carvernCounts[cavern];
  }

  public void setCavern(int cavern) {
    this.cavern = cavern;
  }
}
