/* RecalcitrantCarbonPool.java ~ Mar 18, 2009 */
package leaflitter;

/**
 *
 * @author angus
 */
public class RecalcitrantCarbonPool extends Pool
{

  public RecalcitrantCarbonPool(double C)
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
