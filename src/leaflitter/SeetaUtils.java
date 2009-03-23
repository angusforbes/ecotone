/* SeetaUtils.java ~ Mar 17, 2009 */

package leaflitter;

/**
 *
 * @author angus
 */
public class SeetaUtils 
{

  public static double uptake(
    double S,
    double vMax,
    double kM)
  {
    return uptake(1, S, vMax, kM);
  }

  public static double uptake(
    double idealToCurrent,
    double S,
    double vMax,
    double kM
    )
  {
    return Math.min ( S , idealToCurrent * ( ( vMax * S) / ( kM + S ) ) );
  }



}
