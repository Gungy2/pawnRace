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
    return switch (this) {
      case BLACK -> Colour.WHITE;
      case WHITE -> Colour.BLACK;
      default -> Colour.NONE;
    };
  }
}