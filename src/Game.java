public class Game {
  private Board board;
  private Move[] moves;
  private int index, whitePawns, blackPawns;
  private Colour currentPlayer;

  public Game(Board board) {
    this.board = board;
    moves = new Move[100];
    index = 0;
    currentPlayer = Colour.WHITE;
  }

  public Colour getCurrentPlayer() {
    return currentPlayer;
  }

  public Move getLastMove() {
    if (index > 0) {
      return moves[index-1];
    }
    return null;
  }

  private void changePlayer() {
    if (currentPlayer == Colour.BLACK) {
      currentPlayer = Colour.WHITE;
    } else {
      currentPlayer = Colour.BLACK;
    }
  }

  public void applyMove(Move move) {
    moves[index++] = move;
    board.applyMove(move);
    if (move.isCapture()) {
      switch (currentPlayer) {
        case BLACK:
          blackPawns--;
          break;
        case WHITE:
          whitePawns--;
          break;
      }
    }
    changePlayer();

  }

  public void unapplyMove() {
    if (index > 0) {
      Move move = moves[index-1];
      board.unapplyMove(move);
      index--;
      if (move.isCapture()) {
        switch (currentPlayer) {
          case BLACK:
            blackPawns++;
            break;
          case WHITE:
            whitePawns++;
            break;
        }
      }
      changePlayer();
    }
  }

  public boolean isFinished() {
    int whitePawns = 0, blackPawns = 0;
    if (getLastMove().getTo().getX() == 0 || getLastMove().getTo().getX() == 7) {
      return true;
    }
    if (whitePawns == 0 || blackPawns == 0) {
      return true;
    }
    for (int i = 0; i <= 7; i++) {
      for (int j = 0; j <= 7; j++) {
        switch (board.getSquare(i, j).getOccupier()) {
          case BLACK:
            if (board.getSquare(i + 1, j).getOccupier() == Colour.NONE) {
              return false;
            }
            if (j > 0 && board.getSquare(i + 1, j - 1).getOccupier() == Colour.WHITE) {
              return false;
            }
            if (j < 7 && board.getSquare(i + 1, j + 1).getOccupier() == Colour.WHITE) {
              return false;
            }
            break;
          case WHITE:
            if (board.getSquare(i - 1, j).getOccupier() == Colour.NONE) {
              return false;
            }
            if (j > 0 && board.getSquare(i - 1, j - 1).getOccupier() == Colour.BLACK) {
              return false;
            }
            if (j < 7 && board.getSquare(i - 1, j + 1).getOccupier() == Colour.BLACK) {
              return false;
            }
            break;
        }
      }
    }
    return true;
  }

  public Colour getGameResult() {
    if (blackPawns == 0 || getLastMove().getTo().getX() == 0) return Colour.WHITE;
    if (whitePawns == 0 || getLastMove().getTo().getX() == 7) return Colour.BLACK;
    return Colour.NONE;
  }

  public Move parseMove(String san) {
    char letter = Character.toLowerCase(san.charAt(0));
    char digit = san.charAt(1);
    if (san.length() > 2 || letter < 'a' || letter > 'h' || digit < '1' || digit > '8') {
      return null;
    }
    int y = san.charAt(0) - 'a';
    int x = 8 - san.charAt(1) - '0';
    Square to = board.getSquare(x, y);
    Square from = board.getSquare(x+1, y);
    return new Move(from, to, true, false);
  }
}
