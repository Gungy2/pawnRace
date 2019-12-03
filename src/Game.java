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
    whitePawns = blackPawns = 7;
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
    if (index > 0 && (getLastMove().getTo().getX() == 0 || getLastMove().getTo().getX() == 7)) {
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
    if (san.indexOf('x') == -1) {
      char letter = Character.toLowerCase(san.charAt(0));
      char digit = san.charAt(1);
      if (san.length() > 2 || letter < 'a' || letter > 'h' || digit < '1' || digit > '8') {
        return null;
      }
      int y = letter - 'a';
      int x = 8 - (digit - '0');
      if (x == 0 && currentPlayer == Colour.BLACK) {
        return null;
      }
      if (x == 7 && currentPlayer == Colour.WHITE) {
        return null;
      }
      Square to = board.getSquare(x, y);
      if (to.getOccupier() != Colour.NONE) {
        return null;
      }
      Square from;
      if (board.getSquare(x - currentPlayer.offset, y).getOccupier() != currentPlayer) {
        int line = currentPlayer == Colour.WHITE ? 6 : 1;
        if (x - 2 * currentPlayer.offset == line && board.getSquare(line, y).getOccupier() == currentPlayer) {
          from = board.getSquare(line, y);
        } else {
          return null;
        }
      } else {
        from = board.getSquare(x - currentPlayer.offset, y);
      }

      return new Move(from, to, false, false);
    }
    else if (san.indexOf('x') == 1) {
      if (san.length() != 4) {
        return null;
      }
      char firstLetter = Character.toLowerCase(san.charAt(0));
      char secondLetter = Character.toLowerCase(san.charAt(2));
      char digit = san.charAt(3);
      if (firstLetter < 'a' || firstLetter > 'h') {
        return null;
      }
      if (secondLetter < 'a' || secondLetter > 'h' || digit < '1' || digit > '8') {
        return null;
      }
      int fromY = firstLetter -'a';
      int toY = secondLetter - 'a';
      int toX = 8 - (digit - '0');
      Square from;
      if (fromY == toY - 1 || fromY == toY + 1) {
        from = board.getSquare(toX - currentPlayer.offset, fromY);
        if (from.getOccupier() != currentPlayer) {
          return null;
        }
      } else {
        return null;
      }
      Square to = board.getSquare(toX, toY);
      if (to.getOccupier() == currentPlayer.opposite()) {
        return new Move(from, to, true, false);
      }
      Square lastMoveFrom = getLastMove().getFrom();
      Square lastMoveTo = getLastMove().getTo();
      if (lastMoveTo.getX() == toX + 1 && lastMoveTo.getY() == toY) {
        if (lastMoveFrom.getX() == toX - 1 && lastMoveFrom.getY() == toY) {
          return new Move (from, to, true, true);
        }
      }
      return null;
    }
    return null;
  }

  public void displayBoard() {
    System.out.println();
    board.display();
    System.out.println();
  }
}
