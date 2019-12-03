public enum Colour {
  BLACK(1),
  WHITE(-1),
  NONE(0)
  ;

  public final int offset;

  Colour(int offset) {
    this.offset = offset;
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