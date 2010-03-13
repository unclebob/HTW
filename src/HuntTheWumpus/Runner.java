package HuntTheWumpus;

import java.io.*;
import static HuntTheWumpus.Game.*;
public class Runner {
  public static void main(String[] args) throws Exception {
    GamePresenter p = new GamePresenter(new Console() {
      public void print(String message) {
        System.out.println(message);
      }
    });
    Game g = p.getGame();

    g.addPath(1,2,EAST);
    g.addPath(2,3,EAST);
    g.addPath(3,4,SOUTH);
    g.addPath(4,5,SOUTH);
    g.addPath(5,6,SOUTH);
    g.addPath(5,7,EAST);
    g.addPath(7,8,EAST);
    g.addPath(8,9,NORTH);
    g.addPath(9,10,EAST);
    g.addPath(10,11,EAST);
    g.addPath(11,12,NORTH);
    g.addPath(12,13,NORTH);
    g.addPath(8,14,SOUTH);
    g.addPath(14,15,SOUTH);
    g.addPath(14,16,EAST);
    g.addPath(16,17,EAST);
    g.addPath(17,18,NORTH);
    g.addPath(18,11,NORTH);
    g.addPath(3,19,NORTH);
    g.addPath(19,20,NORTH);
    g.addPath(20,21,EAST);
    g.addPath(21,22,EAST);
    g.addPath(22,23,EAST);
    g.addPath(23,24,EAST);
    g.addPath(24,13,SOUTH);
    g.addPath(1,25,SOUTH);
    g.addPath(25,26,SOUTH);
    g.addPath(26,15,EAST);
    g.addPath(15,27,EAST);
    g.addPath(27,16,NORTH);
    g.addPath(15,21,SOUTH);
    g.addPath(25,20,EAST);
    g.addPath(8,18,EAST);
    g.addPath(21,9,SOUTH);

    g.putPlayerInCavern(1);
    g.putWumpusInCavern(15);
    g.putPitInCavern(22);
    g.putPitInCavern(17);
    g.putBatsInCavern(8);
    g.setQuiver(5);

    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    p.execute("r");
    while (g.gameTerminated() == false) {
      String command = br.readLine();
      p.execute(command);
    }
  }
}
