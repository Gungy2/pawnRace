public class Board {
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
  }

  public void unapplyMove(Move move) {
    Square from = move.getFrom();
    Colour playerFrom = from.getOccupier();
    Square to = move.getTo();
    Colour playerTo = to.getOccupier();
    squares[from.getX()][from.getY()].setOccupier(playerFrom);
    squares[to.getX()][to.getY()].setOccupier(playerTo);
  }

  public void display() {
    System.out.print("    ");
    for (int i = 0; i <= 7; i++) {
      System.out.print(" " + (char) ('A' + i) + " ");
    }
    System.out.print("\n\n");
    for (int i = 0; i <= 7; i++) {
      System.out.print((8 - i) + "   ");
      for (int j = 0; j <= 7; j++) {
        switch (squares[i][j].getOccupier()) {
          case NONE:
            System.out.print(" . ");
            break;
          case WHITE:
            System.out.print(" W ");
            break;
          case BLACK:
            System.out.print(" B ");
            break;
        }
      }
      System.out.println("   " + (8 - i));
    }
    System.out.print("\n    ");
    for (int i = 0; i <= 7; i++) {
      System.out.print(" " + (char) ('A' + i) + " ");
    }
    System.out.println();
  }
}
