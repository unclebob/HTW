package HuntTheWumpus.fixtures;

import static HuntTheWumpus.Game.EAST;
import static HuntTheWumpus.fixtures.GameDriver.g;

public class CheckRandomBatTransport {
  private int cavern;
  private int counts[] = new int[6];

  public CheckRandomBatTransport() {
    g.putBatsInCavern(2);

    for (int i=0; i<1000; i++) {
      g.putPlayerInCavern(1);
      g.move(EAST);
      counts[g.playerCavern()]++;
    }
  }

  public int count() {
    return counts[cavern];
  }

  public void setCavern(int cavern) {
    this.cavern = cavern;
  }
}
