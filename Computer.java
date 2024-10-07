// Computer.java
// Dylan Connolly

// simple AI, makes blunders
public class Computer extends Player
{
  // used for blocking opponent moves
  protected final char opponentSymbol;

  // delegate superclass constructor, then init oppSym
  public Computer( int pN, Board b )
  {
    super( pN, b );
    opponentSymbol = computeOppSym();
  }

  // it's just the opposite of pSymbol, thus opposite of pNum
  protected char computeOppSym()
  {
    return ( pNum == 0 ) ? 'O' : 'X';
  }

  // getter for debugging mainly
  public char getOppSym()
  {
    return opponentSymbol;
  }

  // allocates a linear array with current open key positions
  protected int[] getFreePositions()
  {
    int[] free;
    
    int count = 0;
    for ( int pos = 1; pos <= pB.cellCount; pos++ )
    {
      if ( pB.cellBlank( pos ) == true )
        count++;
    }

    free = new int[count];
    int index = 0;

    for ( int pos = 1; pos <= pB.cellCount; pos++ )
    {
      if ( pB.cellBlank( pos ) == true )
      {
        free[index] = pos;
        index++;
      }
    }
    return free;
  }

  // main moving part for testing Computer's moves and
  //  that of its opponent
  protected int winPossible( char s )
  {
    // track whether or not we've found a win for s
    boolean winFound = false;
    int location = 0;

    // we'll duplicate the gameboard into this
    char[][] testM;
    int testList[] = getFreePositions();          // find open key position list
    for ( int t = 0; t < testList.length; t++ )   // now, iterate through the list
    {                                             //  every time we'll reset testM
      testM = pB.getMatrix();
      if ( pB.checkChoiceWin( testM, s, testList[t] ) == true )
        winFound = true;

      // ^this^ allows us to try all of the combinations of chars and positions
      
      if ( winFound == true )
        return testList[t];
    }
    // if there isn't a win possible for char s on sB, return -1
    return -1;
  }

  // just delegates the above function with opponent's info
  protected int lossThreat()
  {
    int t = winPossible( opponentSymbol );
    return ( t );
  }

  // for gameplay rule semantics
  protected boolean middleFree()
  {
    // for N x N grid, where N satisfies N % 2 == 1
    if ( pB.cellBlank( ( pB.cellCount / 2 ) + 1 ) )
      return true;
    return false;
  }

  // if we've exhausted our options, go random from freepositions
  protected int randomChoice()
  {
    int[] available = getFreePositions();
    int randomInd = (int)( Math.random() * available.length );
    return available[randomInd];
  }
  
  // all relevant Advanced and Computer functions use -1 as a sentinel for failure  
  protected int playerInput()
  {
    // first, check if win is possible, store result in computerLogic
    int computerLogic = winPossible( pSymbol );
    if ( computerLogic > 0 )
      return computerLogic;

    // second, check if loss is possible, store result in computerLogic
    computerLogic = lossThreat();
    if ( computerLogic > 0 )
      return computerLogic;

    // third, check if center square is available, return position if so
    if ( middleFree() )
      return ( ( pB.cellCount / 2 ) + 1 );

    // otherwise, return random available space
    return randomChoice();
  }

  protected void playerOutput()
  {
    int computerChoice = playerInput();
    System.out.printf( "Player %d (computer) chooses position %d", (pNum + 1), computerChoice );

    super.makeMove( computerChoice );

    System.out.print( "\n\n\n\n" );
  }
  
} // end class Computer
