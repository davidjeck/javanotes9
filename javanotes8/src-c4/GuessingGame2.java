/**
 *  Implements a simple guessing game.  The computer selects a random
 *  number in the range 1 to 100, inclusive, and the user tries to guess
 *  it.  The user gets up to six guesses.  After each wrong guess, the
 *  computer tells the user whether the guess was high or low.  If the
 *  user guesses the number within six guesses, the user wins; if the user
 *  makes six incorrect guesses, the user loses.  After the game ends,
 *  the computer asks whether the user would like to play again.
 */
public class GuessingGame2 {
 
    static int gamesPlayed;   // The number of games played.
    static int gamesWon;      // The number of games won.
 
    public static void main(String[] args) {
       gamesPlayed = 0;
       gamesWon = 0;  // This is actually redundant, since 0 is 
                      //                 the default initial value.
       System.out.println("Let's play a game.  I'll pick a number between");
       System.out.println("1 and 100, and you try to guess it.");
       boolean playAgain;
       do {
          playGame();  // call subroutine to play one game
          System.out.print("Would you like to play again? ");
          playAgain = TextIO.getlnBoolean();
       } while (playAgain);
       System.out.println();
       System.out.println("You played " + gamesPlayed + " games,");
       System.out.println("and you won " + gamesWon + " of those games.");
       System.out.println("Thanks for playing.  Goodbye.");
    } // end of main()            
    
    static void playGame() {
        int computersNumber; // A random number picked by the computer.
        int usersGuess;      // A number entered by user as a guess.
        int guessCount;      // Number of guesses the user has made.
        gamesPlayed++;  // Count this game.
        computersNumber = (int)(100 * Math.random()) + 1;
                 // The value assigned to computersNumber is a randomly
                 //    chosen integer between 1 and 100, inclusive.
        guessCount = 0;
        System.out.println();
        System.out.print("What is your first guess? ");
        while (true) {
           usersGuess = TextIO.getInt();  // Get the user's guess.
           guessCount++;
           if (usersGuess == computersNumber) {
              System.out.println("You got it in " + guessCount
                      + " guesses!  My number was " + computersNumber);
              gamesWon++;  // Count this win.
              break;       // The game is over; the user has won.
           }
           if (guessCount == 6) {
              System.out.println("You didn't get the number in 6 guesses.");
              System.out.println("You lose.  My number was " + computersNumber);
              break;  // The game is over; the user has lost.
           }
           // If we get to this point, the game continues.
           // Tell the user if the guess was too high or too low.
           if (usersGuess < computersNumber)
              System.out.print("That's too low.  Try again: ");
           else if (usersGuess > computersNumber)
              System.out.print("That's too high.  Try again: ");
        }
        System.out.println();
    } // end of playGame()
                
} // end of class GuessingGame2
