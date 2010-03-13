package HuntTheWumpus.fixtures;


public class MakeMap {
  private int start;
  private int end;
  private String direction;

  public void execute() throws Exception {
    GameDriver.p.getGame().addPath(start, end, direction);
  }

  public void setStart(int start) {
    this.start = start;
  }

  public void setEnd(int end) {
    this.end = end;
  }

  public void setDirection(String direction) {
    this.direction = direction;
  }
}
