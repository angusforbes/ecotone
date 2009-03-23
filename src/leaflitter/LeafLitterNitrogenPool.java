/* LeafLitterNitrogenPool.java ~ Mar 18, 2009 */

package leaflitter;

/**
 *
 * @author angus
 */
public class LeafLitterNitrogenPool extends Pool
{

  public LeafLitterNitrogenPool(double N)
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
