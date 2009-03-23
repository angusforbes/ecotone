/* LeafLitterCarbonPool.java ~ Mar 18, 2009 */
package leaflitter;

/**
 *
 * @author angus
 */
public class LeafLitterCarbonPool extends Pool
{

  public LeafLitterCarbonPool(double C)
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
}
