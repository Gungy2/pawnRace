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

  public List<Move> getAllValidMoves() {
    List<Move> moves = new ArrayList<>();
    for (Square pawn : getAllPawns()) {
      moves.addAll(getPawnValidMoved(pawn));
    }
    return moves;
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
    if (square.getOccupier() != colour) {
      return false;
    }
    int margin = colour == Colour.WHITE ? 0 : 7;
    for (int i = square.getX() + colour.offset; isOK(i, margin, colour); i += colour.offset) {
      for (int j = Math.max(square.getY() - 1, 0); j <= Math.min(square.getY() + 1, 7); j++) {
        if (board.getSquare(i, j).getOccupier() == colour.opposite()) {
          return false;
        }
      }
    }
    return true;
  }

  private boolean isOK (int i, int margin, Colour colour) {
    switch (colour) {
      case WHITE:
        return i > margin;
      default:
        return i < margin;
    }
  }

  private List<Move> getPawnValidMoved(Square pawn) {
    List<Move> moves = new ArrayList<>();
    String[] possibilities = getPossibilities(pawn.getSAN());
    for (String possibility : possibilities) {
      Move move = game.parseMove(possibility);
      if (move != null) {
        moves.add(move);
      }
    }
    return moves;
  }

  public Colour getBestPawn() {
    if (getPassedPawn() != null && opponent.getPassedPawn() != null) {
      if (Math.abs(colour.margin - getPassedPawn().getX()) < Math.abs(opponent.colour.margin - opponent.getPassedPawn().getX())) {
        return colour;
      }
      return opponent.colour;
    }
    if (getPassedPawn() != null) {
      return colour;
    }
    if (opponent.getPassedPawn() != null) {
      return opponent.colour;
    }
    return Colour.NONE;
  }

  private Square getPassedPawn() {
    switch (colour) {
      case WHITE:
        for (int i = 1; i < 7; i++) {
          for (int j = 0; j <= 7; j++) {
            if (isPassedPawn(board.getSquare(i, j))) {
              return board.getSquare(i, j);
            }
          }
        }
        break;
      case BLACK:
        for (int i = 6; i > 0; i--) {
          for (int j = 7; j >= 0; j--) {
            if (isPassedPawn(board.getSquare(i, j))) {
              return board.getSquare(i, j);
            }
          }
        }
        break;
    }
    return null;
  }

  void makeMove() {
    if (isComputerPlayer) {
      List<Move> moves = new ArrayList<>();
      boolean ok;
      if (getPassedPawn() != null) {
        moves = getPawnValidMoved(getPassedPawn());
        System.out.println("I HAVE A PAST PAWN!!!");
      }
      else {
        List<Move> movesTrial = getAllValidMoves();
        for (Move move : movesTrial) {
          game.applyMove(move);
          if (getPassedPawn() != null) {
            moves.add(move);
            game.unapplyMove();
            break;
          }
          else if (opponent.getPassedPawn() == null) {
            List<Move> opponentMoves = opponent.getAllValidMoves();
            ok = true;
            for (Move opponentMove : opponentMoves) {
              game.applyMove(opponentMove);
              if (opponent.getPassedPawn() != null || opponentMove.isCapture()) {
                ok = false;
              }
              game.unapplyMove();
            }
            if (ok) {
              moves.add(move);
            }
          }
          game.unapplyMove();
        }
        if (moves.size() == 0) {
          moves = getAllValidMoves();
        }
      }
      System.out.println(moves.size());
      List<Move> captureMoves = new ArrayList<>();
      for (Move move : moves) {
        if (move.isCapture()) {
          captureMoves.add(move);
        }
      }
      if (captureMoves.size() != 0) {
        moves = captureMoves;
      }
      int n = new Random().nextInt(moves.size());
      game.applyMove(moves.get(n));
      System.out.println("\nThe computer moved: " + moves.get(n).getSAN());
    }
  }
}
