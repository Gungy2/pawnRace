public class PawnRace {
  public static void main(String[] args) {
    Board board = new Board ('a', 'd');
    /*var from = board.getSquare(1, 1);
    var to = board.getSquare(2, 1);
    var move = new Move(from, to, true, true);*/

    Game game = new Game(board);
    game.displayBoard();

    Move move1 = game.parseMove("b3");
    game.applyMove(move1);
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
  }
}
