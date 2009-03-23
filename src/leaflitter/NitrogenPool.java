/* NitrogenPool.java ~ Mar 13, 2009 */

package leaflitter;

/**
 *
 * @author angus
 */
public class NitrogenPool extends Pool
{

  public NitrogenPool(double N)
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
