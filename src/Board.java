public class Board implements Cloneable {
  private Square[][] squares;

  public Board(char whiteGap, char blackGap) {
    squares = new Square[8][8];
    int white = whiteGap - 'a';
    int black = blackGap - 'a';
    for (int i = 0; i <= 7; i++) {
      for (int j = 0; j <= 7; j++) {
        squares[i][j] = new Square(i, j);
        if (i == 1 && j != black) {
          squares[i][j].setOccupier(Colour.BLACK);
        }
        if (i == 6 && j != white) {
          squares[i][j].setOccupier(Colour.WHITE);
        }
      }
    }
  }

  public Square getSquare(int x, int y) {
    return squares[x][y];
  }

  public void applyMove(Move move) {
    Square from = move.getFrom();
    Colour player = from.getOccupier();
    Square to = move.getTo();
    squares[to.getX()][to.getY()].setOccupier(player);
    squares[from.getX()][from.getY()].setOccupier(Colour.NONE);
    if (move.isEnPassantCapture()) {
      squares[from.getX()][to.getY()].setOccupier(Colour.NONE);
    }
  }

  public void unapplyMove(Move move) {
    Square from = move.getFrom();
    Colour playerFrom = from.getOccupier();
    Square to = move.getTo();
    Colour playerTo = to.getOccupier();
    squares[from.getX()][from.getY()].setOccupier(playerTo);
    if (move.isEnPassantCapture()) {
      squares[to.getX()][to.getY()].setOccupier(Colour.NONE);
      squares[to.getX() - playerTo.offset][to.getY()].setOccupier(playerTo.opposite());
    }
    else if (move.isCapture()) {
      squares[to.getX()][to.getY()].setOccupier(playerTo.opposite());
    } else {
      squares[to.getX()][to.getY()].setOccupier(playerFrom);
    }
  }

  public void display() {
    System.out.print("    ");
    for (int i = 0; i <= 7; i++) {
      System.out.print(" " + (char) ('A' + i) + "  ");
    }
    System.out.print("\n   ┌─");
    for (int j = 0; j <= 6; j++) {
      System.out.print("──┬─");
    }
    System.out.println("──┐");
    for (int i = 0; i <= 7; i++) {
      System.out.print((8 - i) + "  │");
      for (int j = 0; j <= 7; j++) {
        switch (squares[i][j].getOccupier()) {
          case NONE:
            System.out.print("   │");
            break;
          case WHITE:
            System.out.print(" " + (char) 9817 + " │");
            break;
          case BLACK:
            System.out.print(" " + (char) 9823 + " │");
            break;
        }
      }
      System.out.println("  " + (8 - i));
      if (i < 7) {
        System.out.print("   ├─");
      } else {
        System.out.print("   └─");
      }
      if (i < 7) {
        for (int j = 0; j <= 6; j++) {
          System.out.print("──┼─");
        }
      } else {
        for (int j = 0; j <= 6; j++) {
          System.out.print("──┴─");
        }
      }
      if (i < 7) {
        System.out.println("──┤");
      } else {
        System.out.println("──┘");
      }
    }
    System.out.print("    ");
    for (int i = 0; i <= 7; i++) {
      System.out.print(" " + (char) ('A' + i) + "  ");
    }
    System.out.println();
  }
}
