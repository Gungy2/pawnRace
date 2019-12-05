public enum Colour {
  BLACK(1, 7),
  WHITE(-1, 0),
  NONE(0, -1)
  ;

  public final int offset, margin;

  Colour(int offset, int margin) {
    this.offset = offset;
    this.margin = margin;
  }

  Colour opposite() {
    switch (this) {
      case BLACK:
        return Colour.WHITE;
      case WHITE:
        return Colour.BLACK;
      default:
        return Colour.NONE;
    }
  }
}