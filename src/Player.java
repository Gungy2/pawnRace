import java.util.*;

public class Player {
  private Game game;
  private Board board;
  private Colour colour;
  private boolean isComputerPlayer;
  private Player opponent;

  public Player(Game game, Board board, Colour colour, boolean isComputerPlayer) {
    this.game = game;
    this.board = board;
    this.colour = colour;
    this.isComputerPlayer = isComputerPlayer;
  }

  public void setOpponent(Player opponent) {
    this.opponent = opponent;
  }

  public Colour getColour() {
    return colour;
  }

  public boolean isComputerPlayer() {
    return isComputerPlayer;
  }

  public Square[] getAllPawns() {
    List<Square> squares = new ArrayList<>();
    for (int i = 0; i <= 7; i++) {
      for (int j = 0; j <= 7; j++) {
        if (board.getSquare(i, j).getOccupier() == colour) {
          squares.add(board.getSquare(i, j));
        }
      }
    }
    Square[] squaresArray = squares.toArray(new Square[squares.size()]);
    return squaresArray;
  }

  public Move[] getAllValidMoves() {
    List<Move> moves = new ArrayList<>();
    for (Square pawn : getAllPawns()) {
      String[] possibilities = getPossibilities(pawn.getSAN());
      for (String possibility : possibilities) {
        Move move = game.parseMove(possibility);
        if (move != null) {
          moves.add(move);
        }
      }
    }
    Move[] movesArray = moves.toArray(new Move[moves.size()]);
    return movesArray;
  }

  private String[] getPossibilities(String san) {
    List<String> str = new ArrayList<>();
    char pawnY = san.charAt(0);
    char pawnX = san.charAt(1);
    String coordX1 = String.valueOf((char) (pawnX - colour.offset));
    String coordX2 = String.valueOf((char) (pawnX - 2 * colour.offset));
    String coordY1 = String.valueOf((char) (pawnY - 1));
    String coordY2 = String.valueOf((char) (pawnY + 1));
    str.add(pawnY + coordX1);
    str.add(pawnY + coordX2);
    str.add(pawnY + "x" + coordY1 + coordX1);
    str.add(pawnY + "x" + coordY2 + coordX1);
    return str.toArray(String[]::new);
  }

  public boolean isPassedPawn(Square square) {
    int margin = colour == Colour.WHITE ? 0 : 7;
    for (int i = square.getX() + colour.offset; i < margin; i += colour.offset) {
      for (int j = square.getY() - 1; i <= square.getY() + 1; j++) {
        if (board.getSquare(i, j).getOccupier() == colour.opposite()) {
          return false;
        }
      }
    }
    return true;
  }

  void makeMove() {
    if (isComputerPlayer) {
      Move[] moves = getAllValidMoves();
      int n = new Random().nextInt(moves.length);
      game.applyMove(moves[n]);
      System.out.println("\nThe computer moved: " + moves[n].getSAN());
    }
  }
}
