// Human.java
// Dylan Connolly

import java.util.Scanner;

public class Human extends Player
{
  // Humans don't differ much from Players
  public Human( int pN, Board b )
  {
    super( pN, b );
  }

  // tames the keyboard monkey
  protected int playerInput()
  {
    int choice = TicTacToe.input.nextInt(); 

    // must ensure their choice isn't already taken
    // must ensure their pick is within game board ( 1-cellCount )
    while ( ( choice < 1 || choice > pB.cellCount ) || ( pB.cellBlank( choice ) == false ) )
    {
      System.out.print( "Please select a valid position: ");
      choice = TicTacToe.input.nextInt();
    }

    // now, we've guaranteed their choice is valid
    return choice;
  }

  // tells them what to do, fetches response, then stores it
  protected void playerOutput()
  {
    System.out.printf( "Player %d, please enter a move (1-%d): ", (pNum + 1), pB.cellCount );
    int humanChoice = playerInput();

    super.makeMove( humanChoice );

    System.out.print( "\n\n\n" );   // spacing
  }
  
} // end subclass Human
