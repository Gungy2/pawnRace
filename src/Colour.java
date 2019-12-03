public enum Colour {
  BLACK(1),
  WHITE(-1),
  NONE(0)
  ;

  public final int offset;

  Colour(int offset) {
    this.offset = offset;
  }
}