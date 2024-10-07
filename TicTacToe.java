// TicTacToe.java
// Dylan Connolly

import java.util.Scanner;   // this is really all we need

public class TicTacToe
{

  private static final int playerCount = 2;     // for when I implement new features
  public int playerTurn = 0;                   // playerTurn 0 to playerCount - 1

  // array of an abstract Player class, and the board
  private Player[] players = new Player[playerCount];
  private Board gameBoard;

  // only one scanner is used for entire program, more resource efficient
  public static final Scanner input = new Scanner( System.in );

  // initializes member data, checks for any re-initialization
  public TicTacToe( String[] args ) 
  { 
    gameStart( args );
  }

  // inits Players according to commandline args
  private Player[] initPlayers( String[] args )
  {
    int comPlayerInd = -1;
    int argc = args.length;         // num args, mainly for conciseness
    int curArg = 0;

    // isAI marks indices to true if player N is computer
    boolean advanced = false;
    boolean[] isAI = { false, false };
    Player[] pBuild = new Player[ playerCount ];

    // if argc > 0, one or more computer players.
    //  implicitly, we know '-c' has been used (args[0]).
    if ( argc > 0 )
    {
      curArg++;    // thus, we can skip the '-c' arg string
      
      // while there are remaining args
      while ( curArg < argc )
      {
        switch ( args[curArg] )
        {
          case "1":
          case "2":

            // extract comPlayerNum using Integer's static method (-1 for indexing)
            comPlayerInd = Integer.valueOf( args[curArg] ) - 1;
            isAI[comPlayerInd] = true;
            curArg++;            
            break;

          case "-a":
          
            // set advanced boolean to true          
            advanced = true;
            curArg++;
            break;

          default:
            break;
        }
      }
      // if computer player index is still our negative sentinel, no humans
      if ( comPlayerInd == -1 )
        isAI = new boolean[] { true, true };    
    }
    // now, based on our booleans, initialize
    for ( int i = 0; i < playerCount; i++ )
    {
      if ( isAI[i] == true )
        pBuild[i] = advanced ? new Advanced( i, gameBoard ) : new Computer( i, gameBoard );

      else
        pBuild[i] = new Human( i, gameBoard );
    }
    // finished building, return
    return pBuild;
  }
   
  // runs parameterized Board constructor 
  private Board initBoard()
  {
    return new Board();
  }

    // calls initPlayers and initBoard only if needed
  public void gameStart( String[] args )
  {
    gameBoard = new Board();
    players = initPlayers( args );
  }

  // checks for a tie or win
  public int gameEnd()
  {
    // sentinel
    int wonBy = -1;

    // check the player symbols to see if one has won
    for ( int pl = 0; pl < playerCount; pl++ )
    {
      // if they did win, wonBy stores 1-indexed player number
      if ( gameBoard.boardWon( players[pl].pSymbol ) )
        wonBy = pl + 1;
    }

    // check the board to see if there's a tie
    if ( gameBoard.boardTied() )
      wonBy = 0;

    // now, return our value
    return wonBy;
  }

  /* gamePrompt handles each respective turn of the
   * game, by displaying the board's contents, then
   * printing the current participating playertype's 
   * 'makeMove()' method. For computers, is algorithmic,
   * for humans, requires I/O parsing
   */
  public void gamePrompt()
  {
    gameBoard.drawBoard();
    players[ playerTurn ].playerOutput();

    if ( gameEnd() != -1 )
      return;
    
    playerTurn = ( playerTurn + 1 ) % playerCount;
  }

  public static void main( String[] args )
  {
    StringBuilder message = new StringBuilder();
    // instantiate TicTacToe object,
    TicTacToe t = new TicTacToe( args );

    while ( t.gameEnd() == -1 )
      t.gamePrompt();

    // if gameEnd returns a value greater than zero, WINNER
    if ( t.gameEnd() > 0 )
      message = new StringBuilder( "Player " + Integer.toString( t.gameEnd() ) + " wins!" );

    // otherwise, it's zero, so TIE...
    else
      message = new StringBuilder( "It's a tie!" );

    t.gameBoard.drawBoard();
    message.append( '\n' );
    System.out.print( message.toString() );
    
    // clean up clean up
    input.close();    
  }                      

} // end TicTacToe class
