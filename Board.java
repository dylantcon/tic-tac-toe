// Board.java

public class Board
{

  private char[][] matrix;
  private char[][] key;
  public static final int gridSize = 3;
  public static final int cellCount = gridSize * gridSize;

  public final int[][][] wins;

  /*--------------------------------------------------*
   * constructs a matrix, initializes cells to ' '.   *
   *  Also constructs the matrix key for player cell  *
   *  choices, based on 1 -> gridSize * gridSize.     *
   *                                                  *
   *  We also construct a three-dimensional array,    *
   *  which contains all possible winning patterns    *
   *  for the gridSize we are using. This simplifies  *
   *  the win checks (and AI behavior) significantly. *
   *--------------------------------------------------*/
  public Board()
  {
    char keyVal = '1';
    matrix = new char[gridSize][gridSize];
    key = new char[gridSize][gridSize];
    
    for ( int row = 0; row < gridSize; row++ )
    {
      for ( int cell = 0; cell < gridSize; cell++ )
      {
        matrix[row][cell] = ' ';
        key[row][cell] = keyVal++;
      }
    }
    // run our array builder function
    wins = initWinConditions();
  }

  // builds our array of winconds
  public int[][][] initWinConditions()
  {
    // I use this array for: (all win combinations)(cells/line)(cell coords in matrix)
    int winCon[][][] = 
    {
      { { 0, 0 }, { 0, 1 }, { 0, 2 } },   // row 1 win      ( n = 0 )
      { { 1, 0 }, { 1, 1 }, { 1, 2 } },   // row 2 win      ( n = 1 )
      { { 2, 0 }, { 2, 1 }, { 2, 2 } },   // row 3 win      ( n = 2 )
      { { 0, 0 }, { 1, 0 }, { 2, 0 } },   // column 1 win   ( n = 3 )
      { { 0, 1 }, { 1, 1 }, { 2, 1 } },   // column 2 win   ( n = 4 )
      { { 0, 2 }, { 1, 2 }, { 2, 2 } },   // column 3 win   ( n = 5 )
      { { 0, 0 }, { 1, 1 }, { 2, 2 } },   // neg. diag. win ( n = 6 )
      { { 2, 0 }, { 1, 1 }, { 0, 2 } }    // pos. diag. win ( n = 7 )
    };
    return winCon;
  }

  // duplicates the array so the AI can test future moves
  public char[][] getMatrix()
  {
    char[][] duplicate = new char[gridSize][gridSize];
    for ( int row = 0; row < gridSize; row++ )
      for ( int cell = 0; cell < gridSize; cell++ )
        duplicate[row][cell] = matrix[row][cell];
    return duplicate;
  }

  // validates a linear, 1-indexed 'choice' from player 'symbol'
  public boolean placeMove( int choice, char symbol )
  {
    if ( choice < 1 || choice > cellCount )
      return false;

    if ( cellBlank( choice ) == false )
      return false;

    // translate choice into matrix coordinates
    int coordinates[] = choiceToGrid( choice );
    int r = coordinates[0];
    int c = coordinates[1];
    
    matrix[r][c] = symbol;
    return true;
  }

  // check to see if 'symbol' has a win on board
  public boolean boardWon( char symbol )
  {
    if ( checkWin( symbol ) )
      return true;
    return false;
  }
  
  private boolean checkWin( char c )
  {
    // iterate through all winning coordinate combinations,
    // to see if the character 'c' (player symbol) matches
    for ( int n = 0; n < 8; n++ )
    {
      if ( matrix[ wins[n][0][0] ][ wins[n][0][1] ] == c &&
           matrix[ wins[n][1][0] ][ wins[n][1][1] ] == c &&
           matrix[ wins[n][2][0] ][ wins[n][2][1] ] == c ) {
           return true;
      }
    }
    // if we have not returned, there must be no win for 'c'
    return false;
  }

  // this is intended for use by Computer and Advanced Players ONLY
  public boolean checkChoiceWin( char[][] testGrid, char sym, int choice )
  {
    // compute choice's equivalent coordinates
    int coordinates[] = choiceToGrid( choice );
    int r = coordinates[0];
    int c = coordinates[1];

    // change position [r][c] to the symbol we want to test
    testGrid[r][c] = sym;

    // see if our testGrid has a win in it
    for ( int n = 0; n < 8; n++ )
    {
      if ( testGrid[ wins[n][0][0] ][ wins[n][0][1] ] == sym &&
           testGrid[ wins[n][1][0] ][ wins[n][1][1] ] == sym &&
           testGrid[ wins[n][2][0] ][ wins[n][2][1] ] == sym )  {
           return true;
      }
    }
    // if we have not returned, there must be no win for our testGrid
    return false;
  }

  // checks to see if all cells are filled & no wins
  public boolean boardTied()
  {
    if ( boardWon( 'X' ) || boardWon( 'O' ) )
      return false;

    int c = 1;

    while( c <= cellCount && !cellBlank( c ) )
      c++;

    return ( c > cellCount );
  }

  // for printing, helper
  private boolean isEdgeCell( int cellInd )
  {
    if ( cellInd < gridSize && cellInd % ( gridSize - 1 ) == 0 )
      return true;
    return false;
  }

  public boolean cellBlank( int choice )
  {
    // outside range, cell does not exist
    if ( choice < 1 || choice > cellCount )
      return false;
      
    // translate choice into matrix coordinates
    int coordinates[] = choiceToGrid( choice );
    int r = coordinates[0];
    int c = coordinates[1];
      
    if ( matrix[r][c] != ' ' )
      return false;
        
    return true;
  }

  // draws gameplay grid and key grid
  public void drawBoard()
  {
    // first, build header
    StringBuilder b = new StringBuilder( "Game Board:\t\t" +
                                         "Positions:\n\n" );
    // now, build rows and hLines
    for ( int r = 0; r < gridSize; r++ )
    {
      b.append( drawBoardRow( r ) + "\n" ); // append board row + newline to board string
      
      // only print horiz. line if we're not at bottom index
      if ( r < gridSize - 1 )
      {
        b.append( drawHorizLines() );   // append horizontal line to board string
        b.append( "\n" );               // indent
      }
    }
    // finally, indent after the row cells ( for playerOutput() placement )
    b.append( "\n" );
    
    // now, our board has been built. print it
    System.out.print( b.toString() );
  }

  private String drawCell( int rowInd, int cellInd, char[][] source )
  {
    StringBuilder bCell = new StringBuilder();
    bCell.append( " " + String.valueOf( source[rowInd][cellInd] ) + " " );  // ' c '
    int nextCell;

    // if 'source' cell isn't on the edge, it's either:
    if ( isEdgeCell( cellInd ) == false )
    {
      nextCell = cellInd + 1;
      if ( isEdgeCell( nextCell ) == false )  // '| c '
        bCell.insert( 0, "|" );
                                                // OR

      else if ( isEdgeCell( nextCell ) == true ) // '| c |'
      {
        bCell.insert( 0, "|" );
        bCell.append( "|" );
      }
    }
    // all cases handled, return complete cell (as string)
    return bCell.toString();
  }

  // draws row of gameplay grid and key grid ( no hLine or \n )
  private String drawBoardRow( int rowInd )
  {
    StringBuilder bRow = new StringBuilder();
    // check that row index is valid
    if ( rowInd < gridSize )
    {
      // call drawCell for all cells in row rowInd of matrix
      for ( int mC = 0; mC < gridSize; mC++ )
        bRow.append( drawCell( rowInd, mC, matrix ) );

      // 2 tab spacing between matrix row and key row
      bRow.append( "\t\t" );

      for ( int kC = 0; kC < gridSize; kC++ )
        bRow.append( drawCell( rowInd, kC, key ) );
    }
    return bRow.toString();
  }

  // creates horizontal lines for both matrix and key (no \n)
  private static String drawHorizLines()
  {
    // center cells need 5 dashes
    int centerCells = gridSize - 2;
    int lineLength = ( 5 * centerCells ) + ( 2 * 3 );

    StringBuilder hLine = new StringBuilder();

    for ( int i = 0; i < lineLength; i++ )
      hLine.append( "-" );

    // 2 tab spacing between matrix row and key row
    hLine.append( "\t\t" );
    hLine.append( hLine.toString() );
   
    return hLine.toString();
  }

  // index 0 is row choice, index 1 is cell choice
  public static int[] choiceToGrid( int choice )
  {
    // check that choice is relevant
    if ( choice < 1 || choice > 9 )
      throw new IllegalArgumentException( "Choice is between 1 and cellCount" );

    //  row and cell are fetched nicely by int div and modulo
    int rowInd = ( choice - 1 ) / gridSize;
    int cellInd = ( choice - 1 ) % gridSize;

    // make our 2d coordinates ( i = 0 )( i = 1 )
    int[] gridConv = new int[] { rowInd, cellInd };
    return gridConv;
  }
} // end class Board
