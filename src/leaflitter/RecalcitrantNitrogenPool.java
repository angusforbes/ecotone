/* RecalcitrantNitrogenPool.java ~ Mar 18, 2009 */

package leaflitter;

/**
 *
 * @author angus
 */
public class RecalcitrantNitrogenPool extends Pool
{
  public RecalcitrantNitrogenPool(double N)
  {
    this.N = N;
  }
  public void tick()
  {
  }

  public void update()
  {
    N += dN;
  }

}
