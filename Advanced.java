// Advanced.java
// Dylan Connolly

public class Advanced extends Computer
{

  public Advanced( int pN, Board pB )
  {
    super( pN, pB );
  }

  protected int pickRandomCorner()
  {
    // define the four corners
    int[] corners = 
    {
      1,                                  // tL
      pB.gridSize,                        // tR
      pB.cellCount - pB.gridSize + 1,     // bL
      pB.cellCount                        // bR
    };

    // count the number of available corners
    int availableCount = 0;
    for ( int corner : corners ) 
    {
      if ( pB.cellBlank( corner ) )
        availableCount++;
    }

    // if no corners are available, return -1
    if ( availableCount == 0 )
      return -1;

    // Allocate an array for the available corners
    int[] availableCorners = new int[availableCount];
    int index = 0;

    // Second pass: populate the available corners array
    for ( int corner : corners ) 
    {
      if ( pB.cellBlank( corner ) ) 
      {
        availableCorners[index] = corner;
        index++;
      }
    }

    // pick a random corner from the available ones
    int randomIndex = ( int )( Math.random() * availableCount );
    return availableCorners[ randomIndex ];

  }

  // all relevant Advanced and Computer functions use -1 as a sentinel for failure
  protected int playerInput()
  {  
    // first, check if win is possible, store result in advancedLogic
    int advancedLogic = super.winPossible( pSymbol );
    if ( advancedLogic != -1 )
      return advancedLogic;

    // second, check if loss is possible, store result in advancedLogic
    advancedLogic = super.lossThreat();
    if ( advancedLogic != -1 )
      return advancedLogic;

    // third, check if center square is available, return position if so
    if ( super.middleFree() )
      return ( ( pB.cellCount / 2 ) + 1 );

    // fourth, check if any corners are available, and if so, pick one randomly
    advancedLogic = pickRandomCorner();
    if ( advancedLogic != -1 )
      return advancedLogic;

    // third, check if center square is available, return position if so
    if ( super.middleFree() )
      return ( ( pB.cellCount / 2 ) + 1 );

    // otherwise, return random available space
    return super.randomChoice();
  }

  protected void playerOutput()
  {
    int advancedChoice = playerInput();
    System.out.printf( "Player %d (advanced) chooses position %d", (pNum + 1), advancedChoice );

    super.makeMove( advancedChoice );

    System.out.print( "\n\n\n\n" );
  }
}
