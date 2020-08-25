public class Move {
  private final Square from, to;
  private final boolean isCapture, isEnPassantCapture;

  public Move(Square from, Square to, boolean isCapture, boolean isEnPassantCapture) {
    this.from = from;
    this.to = to;
    this.isCapture = isCapture;
    this.isEnPassantCapture = isEnPassantCapture;
  }

  public Square getFrom() {
    return from;
  }

  public Square getTo() {
    return to;
  }

  public boolean isCapture() {
    return isCapture;
  }

  public boolean isEnPassantCapture() {
    return isEnPassantCapture;
  }

  public String getSAN() {
    return from.getSAN() + " -> " + to.getSAN();
  }
}
