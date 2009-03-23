/* CarbonPool.java ~ Mar 13, 2009 */

package leaflitter;

/**
 *
 * @author angus
 */
public class CarbonPool extends Pool
{
  //public static double C = 3900; //10000.0; //Float.POSITIVE_INFINITY;

  public CarbonPool(double C)
  {
    this.C = C;
  }

  public void tick()
  {
  }

  public void update()
  {
    C += dC;
  }

  /*
  public void reset()
  {
    C = 390;
  }
  */
}
