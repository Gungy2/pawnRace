import java.util.Scanner;

public class PawnRace {
  public static void main(String[] args) {
    while (true) {
      Scanner in = new Scanner(System.in);

      char whiteGap, blackGap;
      do {
        System.out.print("\nWhite, introduce the line where you will have a gap: ");
        whiteGap = in.nextLine().charAt(0);
      } while (whiteGap < 'a' || whiteGap > 'h');
      do {
        System.out.print("\nBlack, introduce the line where you will have a gap: ");
        blackGap = in.nextLine().charAt(0);
      } while (blackGap < 'a' || blackGap > 'h');

      Board board = new Board (whiteGap, blackGap);
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
      game.displayBoard();

      System.out.println("Congratulations, " + game.getGameResult() + "! You won!");
      System.out.println("Do you want to play again? (Y/N)");

      char input = in.next().charAt(0);
      if (Character.toLowerCase(input) != 'y') {
        System.out.println("Thank you for playing!");
        break;
      }
    }
  }
}
