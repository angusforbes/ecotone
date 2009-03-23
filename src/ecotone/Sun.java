package ecotone;

public class Sun
{
  GeomEcotone ecotone;

  float power = 7f; //distance for which the sun will have an effect...
  float col;
  float row;
  float moveX;
  float moveY;

  public Sun(GeomEcotone ecotone, float col, float row, float moveX, float moveY)
  {
    this.ecotone = ecotone;
    this.col = col;
    this.row = row;
    this.moveX = moveX;
    this.moveY = moveY;

  }

  public void move()
  {
    col += moveX;
    row += moveY;

    if (col >= ecotone.numCols + (ecotone.numCols / 2) + 1)
    {
      col -= ecotone.numCols * 2;
    }
    else if (col <  -(ecotone.numCols / 2) - 1)
    {
      col += ecotone.numCols * 2;
    }

    if (row >= ecotone.numRows + (ecotone.numRows / 2) + 1)
    {
      row -= ecotone.numRows * 2;
    }
    else if (row < -(ecotone.numRows / 2) - 1)
    {
      row += ecotone.numRows * 2;
    }
  }

}
