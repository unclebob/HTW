package HuntTheWumpus;

import java.util.*;

public class Game {
  public static final String EAST = "e";
  public static final String WEST = "w";
  public static final String NORTH = "n";
  public static final String SOUTH = "s";
  List<Path> paths = new ArrayList<Path>();
  int playerCavern = -1;
  private int wumpusCavern = -1;
  private int quiver = 0;
  private ArrayList<Integer> arrows = new ArrayList<Integer>();
  private boolean gameTerminated = false;
  private boolean killedByArrowBounce = false;
  private ArrayList<Integer> pits = new ArrayList<Integer>();
  private boolean fellInPit = false;
  private boolean wumpusHitByArrow = false;
  private boolean wumpusFrozen = false;
  private boolean eatenByWumpus = false;
  private boolean hitByOwnArrow = false;
  private ArrayList<Integer> bats = new ArrayList<Integer>();
  private boolean batTransport = false;

  public void addPath(int start, int end, String direction) throws Exception {
    direction = direction.toLowerCase();
    addSinglePath(start, end, direction);
    addSinglePath(end, start, oppositeDirection(direction));
  }

  public void clearMap() {
    paths.clear();
  }

  private String oppositeDirection(String direction) throws Exception {
    if (direction.equals(EAST)) return WEST;
    else if (direction.equals(WEST)) return EAST;
    else if (direction.equals(NORTH)) return SOUTH;
    else if (direction.equals(SOUTH)) return NORTH;
    else
      throw new Exception("No such direction: " + direction);
  }

  private void addSinglePath(int start, int end, String direction) {
    Path p = new Path(start, end, direction);
    paths.add(p);
  }

  public void putPlayerInCavern(int cavern) {
    playerCavern = cavern;
  }

  private void removeArrowFrom(int cavern) {
    for (int i = 0; i < arrows.size(); i++) {
      int c = arrows.get(i);
      if (c == cavern) {
        arrows.remove(i);
      }
    }
  }

  private boolean arrowInCavern(int cavern) {
    for (int c : arrows)
      if (c == cavern) return true;
    return false;
  }

  public void rest() {
    moveWumpus();
  }

  public boolean move(String direction) {
    int destination = adjacentTo(direction, playerCavern);
    if (destination != 0) {
      playerCavern = destination;
      checkWumpusEatsPlayer();
      checkForPit();
      checkForBats();
      pickUpArrow();
      moveWumpus();
      return true;
    }
    return false;
  }

  private void checkForBats() {
    while (bats.contains(playerCavern)) {
      transportPlayer();
      batTransport = true;
    }
  }

  private void transportPlayer() {
    Path selectedPath = paths.get((int)(Math.random() * paths.size()));
    playerCavern = selectedPath.start;
  }

  private void checkWumpusEatsPlayer() {
    if (playerCavern == wumpusCavern) {
      gameTerminated = true;
      eatenByWumpus = true;
    }
  }

  private void checkForPit() {
    if (pits.contains(playerCavern)) {
      gameTerminated = true;
      fellInPit = true;
    }
  }

  private void pickUpArrow() {
    if (arrowInCavern(playerCavern)) {
      removeArrowFrom(playerCavern);
      quiver++;
    }
  }

  public int playerCavern() {
    return playerCavern;
  }

  public void putWumpusInCavern(int where) {
    wumpusCavern = where;
  }

  public boolean canSmellWumpus() {
    return areAdjacent(playerCavern, wumpusCavern);
  }

  private boolean areAdjacent(int c1, int c2) {
    for (Path p : paths) {
      if (p.start == c1 && p.end == c2)
        return true;
    }
    return false;
  }

  public void setQuiver(int arrows) {
    quiver = arrows;
  }

  public void putArrowInCavern(int cavern) {
    arrows.add(cavern);
  }

  public int getQuiver() {
    return quiver;
  }

  public int arrowsInCavern(int cavern) {
    int count = 0;
    for (int i : arrows) {
      if (i == cavern)
        count++;
    }
    return count;
  }

  public boolean wasKilledByArrowBounce() {
    return killedByArrowBounce;
  }

  public boolean shoot(String direction) {
    if (quiver > 0) {
      quiver--;
      if (adjacentTo(direction, playerCavern) == 0) {
        gameTerminated = true;
        killedByArrowBounce = true;
      } else {
        int endCavern = shootAsFarAsPossible(direction, playerCavern);
        putArrowInCavern(endCavern);
        moveWumpus();
      }
      return true;
    } else
      return false;
  }

  private int shootAsFarAsPossible(String direction, int cavern) {
    int nextCavern = adjacentTo(direction, cavern);
    if (nextCavern == 0)
      return cavern;
    else {
      if (nextCavern == wumpusCavern) {
        wumpusHitByArrow = true;
        gameTerminated = true;
        return nextCavern;
      } else if (nextCavern == playerCavern) {
        gameTerminated = true;
        hitByOwnArrow = true;
        return nextCavern;
      }
      return shootAsFarAsPossible(direction, nextCavern);
    }
  }

  private int adjacentTo(String direction, int cavern) {
    for (Path p : paths) {
      if (p.start == cavern && p.direction.equals(direction))
        return p.end;
    }
    return 0;
  }

  public boolean gameTerminated() {
    return gameTerminated;
  }

  public void putPitInCavern(int cavern) {
    pits.add(cavern);
  }

  public boolean fellInPit() {
    return fellInPit;
  }

  public boolean canHearPit() {
    for (int pit : pits)
      if (areAdjacent(playerCavern, pit))
        return true;
    return false;
  }

  public boolean wumpusHitByArrow() {
    return wumpusHitByArrow;
  }

  public void reset() {
    gameTerminated = false;
    wumpusHitByArrow = false;
    fellInPit = false;
    killedByArrowBounce = false;
    eatenByWumpus = false;
    hitByOwnArrow = false;
    batTransport = false;
  }

  public int getWumpusCavern() {
    return wumpusCavern;
  }

  public boolean eatenByWumpus() {
    return eatenByWumpus;
  }

  public boolean hitByOwnArrow()
  {
    return hitByOwnArrow;
  }

  public void moveWumpus() {
    if (wumpusFrozen)
      return;
    List<Integer> moves = new ArrayList<Integer>();
    addPossibleMove(EAST, moves);
    addPossibleMove(WEST, moves);
    addPossibleMove(NORTH, moves);
    addPossibleMove(SOUTH, moves);
    moves.add(0); // rest;

    int selection = (int) (Math.random() * moves.size());
    int selectedMove = moves.get(selection);
    if (selectedMove != 0) {
      wumpusCavern = selectedMove;
      checkWumpusEatsPlayer();
    }
  }

  private void addPossibleMove(String dir, List<Integer> moves) {
    int possibleMove;
    possibleMove = adjacentTo(dir, wumpusCavern);
    if (possibleMove != 0)
      moves.add(possibleMove);
  }

  public void freezeWumpus() {
    wumpusFrozen = true;
  }

  public void putBatsInCavern(int cavern) {
    bats.add(cavern);
  }

  public boolean canHearBats() {
    for (int batCave : bats)
      if (areAdjacent(batCave, playerCavern))
        return true;
    return false;
  }

  public boolean batTransport() {
    return batTransport;
  }

  public void resetBatTransport() {
    batTransport = false;
  }

  class Path {
    public int start;
    public int end;
    public String direction;

    public Path(int start, int end, String direction) {
      this.start = start;
      this.end = end;
      this.direction = direction;
    }
  }// private class Path
}
