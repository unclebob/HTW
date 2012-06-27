package HuntTheWumpus;

public class ShootResponse {
  private boolean shot = false;

  public void setShot() {
    shot = true;
  }

  public boolean wasShot() {
    return shot;
  }

}
