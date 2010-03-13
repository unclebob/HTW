package HuntTheWumpus;

import java.util.*;

public class GamePresenter {

  private Console console;
  private final Game game = new Game();

  public GamePresenter() {
    this(null);
  }

  public GamePresenter(Console console) {
    this.console = console;
  }

  public boolean execute(String command) {
    boolean valid = true;
    int arrowsInQuiver = game.getQuiver();
    String[] tokens = command.toLowerCase().split(" ");
    if (isShootCommand(tokens))
      shootArrow(directionFromName(tokens[1]));
    else if (isSingleWordShootCommand(tokens))
      shootArrow(directionFromName(tokens[0].substring(1)));
    else if (isGoCommand(tokens))
      movePlayer(directionFromName(tokens[1]));
    else if (isRestCommand(tokens))
      game.rest();
    else if (isImplicitGoCommand(tokens)) {
      String direction = directionFromName(tokens[0]);
      if (direction != null)
        movePlayer(direction);
    } else {
      console.print("I don't know how to " + command + ".");
      valid = false;
    }

    printEndOfTurnMessages(arrowsInQuiver);
    return valid;
  }

  private boolean isSingleWordShootCommand(String[] tokens) {
    return tokens[0].charAt(0) == 's' &&
           directionFromName(tokens[0].substring(1)) != null;
  }

  private boolean isImplicitGoCommand(String[] tokens) {
    return tokens.length == 1 &&
           directionFromName(tokens[0]) != null;
  }

  private boolean isRestCommand(String[] tokens) {
    return tokens[0].equals("r") ||
           tokens[0].equals("rest");
  }

  private boolean isGoCommand(String[] tokens) {
    return tokens.length == 2 &&
           tokens[0].equals("go");
  }

  private boolean isShootCommand(String[] tokens) {
    return tokens.length == 2 &&
           (tokens[0].equals("shoot") ||
            tokens[0].equals("s"));
  }

  private void printEndOfTurnMessages(int arrowsInQuiver) {
    if (game.gameTerminated()) {
      printCauseOfTermination();
      console.print("Game over.");
    } else {
      printTransportMessage();
      printArrowsFound(arrowsInQuiver);
      printQuiverStatus();
      printWumpusOdor();
      printPitSounds();
      printBatSounds();
      printAvailableDirections();
    }
  }

  private void printTransportMessage() {
    if (game.batTransport()) {
      console.print("A swarm of angry bats has carried off.");
      game.resetBatTransport();
    }
  }

  private void printBatSounds() {
    if (game.canHearBats())
      console.print("You hear chirping.");
  }

  private void printPitSounds() {
    if (game.canHearPit())
      console.print("You hear wind.");
  }

  private void printAvailableDirections() {console.print(getAvailableDirections());}

  private void printWumpusOdor() {
    if (game.canSmellWumpus()) {
      console.print("You smell the Wumpus.");
    }
  }

  private void printQuiverStatus() {
    if (game.getQuiver() == 0)
      console.print("You have no arrows.");
    else if (game.getQuiver() == 1)
      console.print("You have 1 arrow.");
    else
      console.print("You have " + game.getQuiver() + " arrows.");
  }

  private void printArrowsFound(int arrowsInQuiver) {
    if (game.getQuiver() > arrowsInQuiver)
      console.print("You found an arrow.");
  }

  private void printCauseOfTermination() {
    if (game.wasKilledByArrowBounce())
      console.print("The arrow bounced off the wall and killed you.");
    else if (game.fellInPit())
      console.print("You fall into a pit and die.");
    else if (game.wumpusHitByArrow())
      console.print("You have killed the Wumpus.");
    else if (game.eatenByWumpus())
      console.print("The ravenous snarling Wumpus gobbles you down.");
    else if (game.hitByOwnArrow())
      console.print("You were hit by your own arrow.");
  }

  private void shootArrow(String direction) {
    if (game.shoot(direction) == false)
      console.print("You don't have any arrows.");
    else
      console.print("The arrow flies away in silence.");
  }

  private void movePlayer(String direction) {
    if (game.move(direction) == false)
      console.print("You can't go " + directionName(direction) + " from here.");
  }

  public String getAvailableDirections() {
    AvailableDirections directions = new AvailableDirections();

    for (Game.Path p : game.paths) {
      if (p.start == game.playerCavern) {
        directions.addDirection(p.direction);
      }
    }
    return directions.toString();
  }

  private String directionFromName(String name) {
    if (name.equals("e") || name.equals("east")) return Game.EAST;
    else if (name.equals("w") || name.equals("west")) return Game.WEST;
    else if (name.equals("n") || name.equals("north")) return Game.NORTH;
    else if (name.equals("s") || name.equals("south")) return Game.SOUTH;
    else return null;
  }

  private String directionName(String direction) {
    if (direction.equals(Game.NORTH))
      return "north";
    else if (direction.equals(Game.SOUTH))
      return "south";
    else if (direction.equals(Game.EAST))
      return "east";
    else if (direction.equals(Game.WEST))
      return "west";
    else
      return "tilt";
  }

  public Game getGame() {
    return game;
  }

  //
  // Private Classes
  //
  private class AvailableDirections {
    private Set<String> directions = new HashSet<String>();
    private int nDirections;
    private StringBuffer available;
    private int directionsPlaced;

    public String toString() {
      if (directions.isEmpty())
        return "There are no exits!";
      else {
        return assembleDirections();
      }
    }

    private String assembleDirections() {
      available = new StringBuffer();
      nDirections = directions.size();
      directionsPlaced = 0;
      for (String dir : new String[]{Game.NORTH, Game.SOUTH, Game.EAST, Game.WEST}) {
        if (directions.contains(dir)) {
          placeDirection(dir);
        }
      }
      return "You can go " + available.toString() + " from here.";
    }

    private void placeDirection(String dir) {
      directionsPlaced++;
      if (isLastOfMany())
        available.append(" and ");
      else if (notFirst())
        available.append(", ");
      available.append(directionName(dir));
    }

    private boolean notFirst() {return directionsPlaced > 1;}

    private boolean isLastOfMany() {return nDirections > 1 && directionsPlaced == nDirections;}

    public void addDirection(String direction) {
      directions.add(direction);
    }
  } // private class AvailableDirections
} // public class GamePresenter

