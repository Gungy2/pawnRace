public class Square {
  private int x, y;
  private Colour colour;

  public Square (int x, int y) {
    this.x = x;
    this.y = y;
    colour = Colour.NONE;
  }

  public int getX() {
    return x;
  }

  public int getY() {
    return y;
  }

  public Colour getOccupier () {
    return colour;
  }

  void setOccupier (Colour colour) {
    this.colour = colour;
  }

  String getSAN() {
    var c1 = Integer.valueOf(8 - x);
    char c2 = (char) (y + 'a');
    return c2 + c1.toString();
  }
}