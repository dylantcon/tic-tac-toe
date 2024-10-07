// Player.java
// Dylan Connolly

// is the superclass of our hierarchy
public abstract class Player
{
  // all subclasses have these at least
  public final char pSymbol;
  protected final int pNum;
  protected Board pB;

  // we'll delegate this in all 3 subclasses
  public Player( int pN, Board b )
  {
    pB = b;
    pNum = pN;
    pSymbol = setPlayerSymbol( pN );
  }

  // this process is the same for all
  private char setPlayerSymbol( int n )
  {
    return ( pNum == 0 ) ? 'X' : 'O';
  }

  // getter I needed for debugging
  public char getSymbol()
  {
    return pSymbol;
  }

  // makeMove is called by all subclasses' playerOutput()
  public void makeMove( int choice )
  {
    pB.placeMove( choice, pSymbol );
  }

  // these are overridden in all subclasses
  protected abstract int playerInput();       // handles fetching the choice
  protected abstract void playerOutput();     // handles playerspecific text and choice storage

} // end class Player
