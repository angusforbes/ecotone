/* Microbe.java ~ Mar 13, 2009 */

package leaflitter;

/**
 *
 * @author angus
 */
public class Microbe extends Biomass
{
  //double decompositionRate = .5;
  //double C; //what gets passed back to the C pool
  //double N; //what gets passed back to the N pool

  public Microbe(double C, double N)
  {
    this.C = C;
    this.N = N;
  }

  //MICROBE LAND
  //1. takes up N from N_POOL proportionate to C/N ratio

  //2. takes up N (biomass N grows) from PLANT at some specificed rate proportional to biomassN

  //3. takes up C (biomass C grows) from PLANT at some specified rate proportional to biomassC

  //4. GROW MICROBE BIOMASS

  //5. loses some C (respires into nothing) at some rate proportional to biomassC and C/N ratio

  //6. puts some N into N_POOL proportionate at some rate proportional to biomassN to C/N ratio

  double IDEAL_NC_RATIO = .07;
  double IDEAL_CN_RATIO = 14.35;
  double NITROGEN_POOL_UPTAKE_PERC = .1;
  double NITROGEN_LITTER_UPTAKE_PERC = .8;
  double CARBON_LITTER_UPTAKE_PERC = .8;
  double CARBON_RESPIRATION_PERC = .1;
  double NITROGEN_MINERALIZATION_PERC = .01;
  double C_kM = 10;
  double C_kM_2 = 1;
  double N_kM = 1;
  double N_kM_2 = 1;
  double MINERALIZATION = .4;
  double BIOMASS_DAMPING_FACTOR = .005;



  public void update()
  {
    C += dC;
    N += dN;
  }
  
  public void tick() //double C, double N)
  {
    double CURRENT_NC_RATIO = N / C;
    double CURRENT_CN_RATIO = C / N;

    double idealToCurrent = (IDEAL_CN_RATIO / CURRENT_CN_RATIO);
    double C_from_R_POOL = SeetaUtils.uptake(idealToCurrent, SeetaDemo.rcPool.C, C, C_kM);
    dC += C_from_R_POOL;
    SeetaDemo.rcPool.dC -= C_from_R_POOL;

    double C_from_Plant = SeetaUtils.uptake(IDEAL_CN_RATIO / CURRENT_CN_RATIO, SeetaDemo.llcPool.C, C, C_kM);
    dC += C_from_Plant;
    SeetaDemo.llcPool.dC -= C_from_Plant;

    double N_from_R_POOL = SeetaUtils.uptake(IDEAL_NC_RATIO / CURRENT_NC_RATIO,
      SeetaDemo.rnPool.N,
      N,
      N_kM);

    dN += N_from_R_POOL;
    SeetaDemo.rnPool.dN -= N_from_R_POOL;

    double N_from_Plant = SeetaUtils.uptake(IDEAL_NC_RATIO / CURRENT_NC_RATIO,
      SeetaDemo.llnPool.N,
      N,
      N_kM);

    dN += N_from_Plant;
    SeetaDemo.llnPool.dN -= N_from_Plant;

    double N_mineralization;
    double N_from_POOL;

    if (CURRENT_CN_RATIO < IDEAL_CN_RATIO)
    {
      N_from_POOL = 0;
    }
    else
    {
      N_from_POOL = BIOMASS_DAMPING_FACTOR *
        SeetaUtils.uptake(IDEAL_NC_RATIO / CURRENT_NC_RATIO, SeetaDemo.nPool.N, N, N_kM_2);
    }

    dN += N_from_POOL;
    SeetaDemo.nPool.dN -= N_from_POOL;

    double C_mineralization;

    if (CURRENT_CN_RATIO < IDEAL_CN_RATIO)
    {
      C_mineralization = .9 * C;
    }
    else
    {
      C_mineralization = .8 * C;
    }

    dC -= C_mineralization;
    SeetaDemo.cPool.dC += C_mineralization;

    if (CURRENT_CN_RATIO < IDEAL_CN_RATIO)
    {
      N_mineralization = MINERALIZATION * N;
    }
    else
    {
      N_mineralization = 0;
    }

    dN -= N_mineralization;
    SeetaDemo.nPool.dN += N_mineralization;
  }
}
