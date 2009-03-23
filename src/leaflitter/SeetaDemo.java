/* SeetaDemo.java ~ Mar 13, 2009 */

package leaflitter;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

/*
 * Plan of attack:
 *
 * 1: pass around N without losing any or blowing up the world with too much N.
 * 2: pass around C. Make sure we don't lose any, even though there is a ton of it.
 * 3: add in "crazy" ratios... SWITCHEROO functions
 * 4: add in Michelson-Michel functions instead of "crzay" approximations
 * everything will be beautiful.
 * 5: add in fun temparature ?LISTEN temparture cylones don't you hear what i'm saying???
 * lots of fun...
 */

public class SeetaDemo 
{
  int NUM_STEPS = 500; //100;

  double PLANT_BIOMASS_C = 3000;
  double PLANT_BIOMASS_N = 30;
  double MICROBE_BIOMASS_C = 100;
  double MICROBE_BIOMASS_N = 10;
  double C_POOL_C = 3000;
  double N_POOL_N = 300;
  double RC_POOL_C = 0;
  double RN_POOL_N = 0;

  public static Microbe microbe;
  public static Plant plant;
  public static LeafLitterCarbonPool llcPool;
  public static LeafLitterNitrogenPool llnPool;
  public static CarbonPool cPool;
  public static NitrogenPool nPool;
  public static RecalcitrantCarbonPool rcPool;
  public static RecalcitrantNitrogenPool rnPool;

  NumberFormat formatter = new DecimalFormat(".000");
  List<Variable> variables = new ArrayList<Variable>();

  public static void main(String[] args)
  {
    new SeetaDemo();
  }
  
  public SeetaDemo()
  {
    initModel();
    driveModel();
  }

  public void initModel()
  {
    plant = new Plant(PLANT_BIOMASS_C, PLANT_BIOMASS_N);
    microbe = new Microbe(MICROBE_BIOMASS_C, MICROBE_BIOMASS_N);
    cPool = new CarbonPool(C_POOL_C);
    nPool = new NitrogenPool(N_POOL_N);
    rcPool = new RecalcitrantCarbonPool(0);
    rnPool = new RecalcitrantNitrogenPool(0);
    llcPool = new LeafLitterCarbonPool(0);
    llnPool = new LeafLitterNitrogenPool(0);

    variables.add(plant);
    variables.add(microbe);
    variables.add(cPool);
    variables.add(nPool);
    variables.add(rcPool);
    variables.add(rnPool);
    variables.add(llcPool);
    variables.add(llnPool);
  }

  public void driveModel()
  {
    for (int i = 0; i < NUM_STEPS; i++)
    {
       printModel("START STEP " + i);

       for (Variable v : variables)
       {
          v.tick();
       }
       for (Variable v : variables)
       {
          v.update();
       }
       for (Variable v : variables)
       {
          v.reset();
       }


       printModel("END STEP " + i);

    }
  }


       /*
       System.out.println("\n**********\nSTART STEP " + i + " : " +
        "\n Pools   C, N : " + formatter.format(CarbonPool.C) + ", " + formatter.format(NitrogenPool.N) + "\n" +
        " Plant   C, N : " + formatter.format(plant.biomassC) + ", " + formatter.format(plant.biomassN) + "\n" +
        " Microbe C, N : " + formatter.format(microbe.biomassC) + ", " + formatter.format(microbe.biomassN) + "\n"
        );
        */

  /*
       System.out.println("before PLANT, N = " + (NitrogenPool.N + plant.biomassN + microbe.biomassN));
       plant.tick();
       System.out.println("after PLANT, N = " + (NitrogenPool.N + plant.biomassN + microbe.biomassN + plant.lossN));
       System.out.println("plant N = " + plant.biomassN);


       System.out.println(" MIDDLE : Total Carbon in System = " + formatter.format(CarbonPool.C + RecalcitrantCarbonPool.C + plant.biomassC + microbe.biomassC + plant.lossC) );
       System.out.println(" MIDDLE : Total Nitrogen in System = " + formatter.format(NitrogenPool.N + RecalcitrantNitrogenPool.N + plant.biomassN + microbe.biomassN + plant.lossN) );

      microbe.change(plant.lossC, plant.lossN);

      printModel("END STEP " + i);
      System.out.println("**********\n\n");
    }
  }
  */

  public double totalCInSystem()
  {
    double tc = 0;
    for (Variable v : variables)
    {
      tc += v.C;
    }

    return tc;
  }

  public double totalNInSystem()
  {
    double tn = 0;
    for (Variable v : variables)
    {
      tn += v.N;
    }

    return tn;
  }

  public void printModel(String msg)
  {
   System.out.println(msg + " : " +
        "\n Pools   C, N : " + formatter.format(cPool.C) + ", " + formatter.format(nPool.N) + "\n" +
        " RPools  C, N : " + formatter.format(rcPool.C) + ", " + formatter.format(rnPool.N) + "\n" +
        " Plant   C, N : " + formatter.format(plant.C) + ", " + formatter.format(plant.N) + "\n" +
        " Microbe C, N : " + formatter.format(microbe.C) + ", " + formatter.format(microbe.N) + "\n" +
        " Total Carbon in System = " + formatter.format(totalCInSystem()) + "\n" +
        " Total Nitrogen in System = " + formatter.format(totalNInSystem())
        );

  }
}
