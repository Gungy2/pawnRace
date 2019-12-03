import java.util.Scanner;

public class PawnRace {
  public static void main(String[] args) {
    while (true) {
      Scanner in = new Scanner(System.in);

      Board board = new Board ('a', 'd');
      Game game = new Game(board);

      Player white = new Player(game, board, Colour.WHITE, false);
      Player black = new Player(game, board, Colour.BLACK, true);
      white.setOpponent(black);
      white.setOpponent(black);

      while (!game.isFinished()) {
        game.displayBoard();
        Player player = null;
        switch (game.getCurrentPlayer()) {
          case WHITE:
            player = white;
            break;
          case BLACK:
            player = black;
            break;
        }
        if (player.isComputerPlayer()) {
          player.makeMove();
        } else {
          String input;
          Move move;
          do {
            System.out.print("Please introduce your next move: ");
            input = in.nextLine();
            move = game.parseMove(input);
          } while (move == null);
          game.applyMove(move);
        }
      }
    }
    /*
    Move move1 = game.parseMove("b3");
    game.applyMove(move1);
    game.displayBoard();
    computer.makeMove();
    game.displayBoard();


    Move move2 = game.parseMove("f6");
    game.applyMove(move2);
    game.displayBoard();

    Move move3 = game.parseMove("b4");
    game.applyMove(move3);
    game.displayBoard();

    Move move4 = game.parseMove("f5");
    game.applyMove(move4);
    game.displayBoard();

    Move move5 = game.parseMove("b5");
    game.applyMove(move5);
    game.displayBoard();

    Move move6 = game.parseMove("c5");
    game.applyMove(move6);
    game.displayBoard();

    Move move7 = game.parseMove("bxc6");
    game.applyMove(move7);
    game.displayBoard();

    Move move8 = game.parseMove("bxc6");
    game.applyMove(move8);
    game.displayBoard();
    */
  }
}
