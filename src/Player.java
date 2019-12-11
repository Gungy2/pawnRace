import java.util.*;

public class Player {
  private Game game;
  private Board board;
  private Colour colour;
  private boolean isComputerPlayer;
  private Player opponent;
  private char gap;

  public Player(Game game, Board board, Colour colour, boolean isComputerPlayer, char gap) {
    this.game = game;
    this.board = board;
    this.colour = colour;
    this.isComputerPlayer = isComputerPlayer;
    this.gap = gap;
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
    return squares.toArray(new Square[squares.size()]);
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

  List<Move> getCaptureMoves(Square square) {
    List<Move> captureMoves = new ArrayList<>();
    for (Move move : getAllValidMoves()) {
      if (move.isCapture() && move.getTo() == square) {
        captureMoves.add(move);
      }
    }
    return captureMoves;
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
      boolean ok, goodMove;
      if (getPassedPawn() != null) {
        moves = getPawnValidMoved(getPassedPawn());
      }
      else {
        List<Move> movesTrial = getAllValidMoves();
        for (Move move : movesTrial) {
          game.applyMove(move);
          goodMove = true;
          if (getPassedPawn() != null) {
            return;
          }
          else if (opponent.getPassedPawn() == null) {
            List<Move> opponentMoves = opponent.getAllValidMoves();
            if (getPassedPawn() == null) {
              goodMove = false;
            }
            ok = true;
            for (Move opponentMove : opponentMoves) {
              game.applyMove(opponentMove);
              if (opponent.getPassedPawn() != null || opponentMove.isCapture()) {
                ok = false;
              }
              game.unapplyMove();
            }
            if (ok || goodMove) {
              moves.add(move);
            }
          }
          game.unapplyMove();
        }
      }
      Square opponentGoodPawn = null;
      for (Move move : getAllValidMoves()) {
        game.applyMove(move);
        for (Move opponentMove : opponent.getAllValidMoves()) {
          game.applyMove(opponentMove);
          if (opponent.getPassedPawn() != null) {
            opponentGoodPawn = opponentMove.getFrom();
          }
          game.unapplyMove();
        }
        game.unapplyMove();
      }

      if (opponentGoodPawn != null && getCaptureMoves(opponentGoodPawn).size() != 0) {
        moves.clear();
        moves = getCaptureMoves(opponentGoodPawn);
      }

      if (moves.size() == 0) {
        moves = getAllValidMoves();
      }
      List<Move> captureMoves = new ArrayList<>();
      for (Move move : moves) {
        if (move.isCapture()) {
          game.applyMove(move);
          if (!(game.isFinished() && game.getGameResult() == Colour.NONE)) {
            captureMoves.add(move);
          }
          game.unapplyMove();
        }
      }

      if (captureMoves.size() != 0) {
        moves = captureMoves;
      }

      if (game.getNrMoves() < 4) {
        String line;
        switch (colour) {
          case BLACK:
            line = "6";
            break;
          default:
            line = "3";
            break;
        }
        if (game.parseMove((char)(gap - 2) + line) != null || game.parseMove((char)(gap + 2) + line) != null) {
          moves.clear();
        }
        if (game.parseMove((char)(gap - 2) + line) != null) {
          moves.add(game.parseMove((char)(gap - 2) + line));
        }
        if (game.parseMove((char)(gap + 2) + line) != null) {
          moves.add(game.parseMove((char)(gap + 2) + line));
        }
      }

      if (moves.size() == 0) {
        moves = getAllValidMoves();
      }

      int n = new Random().nextInt(moves.size());
      game.applyMove(moves.get(n));
      System.out.println("\nThe computer moved: " + moves.get(n).getSAN());
    }
  }
}
