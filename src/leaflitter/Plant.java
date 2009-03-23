/* Plant.java ~ Mar 13, 2009 */
package leaflitter;

/**
 *
 * @author angus
 */
public class Plant extends Biomass
{
  public Plant(double C, double N)
  {
    this.C = C;
    this.N = N;
  }

  double IDEAL_CN_RATIO = 100.0;
  double IDEAL_NC_RATIO = .01;
  double SURFACE_AREA_C_EXPONENT = .5;
  double SURFACE_AREA_N_EXPONENT = .6;
  double C_HALF_SATURATION = 1;
  double N_HALF_SATURATION = 100;
  double RECALCITRANT_LOSS_PERC = .02;

  public void update()
  {
    C += dC;
    N += dN;
  }

  public void tick()
  {
    double CURRENT_CN_RATIO = C / N;
    double CURRENT_NC_RATIO = N / C;

    double idealToCurrent = (IDEAL_CN_RATIO / CURRENT_CN_RATIO);
    double surfaceAreaC = Math.pow(C, SURFACE_AREA_C_EXPONENT);

    double C_from_C_POOL = SeetaUtils.uptake(idealToCurrent, SeetaDemo.cPool.C, C, C_HALF_SATURATION);
    dC += C_from_C_POOL;
    SeetaDemo.cPool.dC -= C_from_C_POOL;

    idealToCurrent = IDEAL_NC_RATIO / CURRENT_NC_RATIO;
    double surfaceAreaN = Math.pow(C, SURFACE_AREA_N_EXPONENT);

    double N_from_N_POOL = SeetaUtils.uptake(idealToCurrent, SeetaDemo.nPool.N, N, N_HALF_SATURATION);
    dN += N_from_N_POOL;
    SeetaDemo.nPool.dN -= N_from_N_POOL;

    double lossN, lossC;
    if (CURRENT_CN_RATIO < IDEAL_CN_RATIO)
    {
      lossC = C * .009; //.09;
      lossN = N * .03;
    }
    else
    {
      lossC = C * .09;
      lossN = N * .01;
    }

    dC -= lossC;
    dN -= lossN;

    double lossCToRCPOOL =  lossC * RECALCITRANT_LOSS_PERC;
    double lossCToLLCPOOL = lossC - lossCToRCPOOL;
    double lossNToRNPOOL =  lossN * RECALCITRANT_LOSS_PERC;
    double lossNToLLNPOOL = lossN - lossNToRNPOOL;

    SeetaDemo.llcPool.dC += lossCToLLCPOOL;
    SeetaDemo.llnPool.dN += lossNToLLNPOOL;
    SeetaDemo.rcPool.dC += lossCToRCPOOL;
    SeetaDemo.rnPool.dN += lossNToRNPOOL;
  }
}
