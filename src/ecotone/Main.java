/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ecotone;

import java.util.Properties;
import behaviorism.BehaviorismDriver;
import behaviors.Behavior.LoopEnum;
import behaviors.BehaviorDiscrete.DiscreteBehaviorBuilder;
import ecotone.fauna.Animal;
import ecotone.nutrients.Nutrient;
import geometry.text.GeomTextOutset;
import geometry.text.GeomTextOutset.GeomTextBuilder;
import javax.vecmath.Point3f;
import utils.Utils;
import worlds.WorldGeom;
import static java.awt.event.KeyEvent.*;

/**
 * @author angus
 */
public class Main extends WorldGeom
{

  int numCols = 15;
  int numRows = 15;
  //List<GeomRect> squares = new ArrayList<GeomRect>();
  GeomEcotone ecotone = null;

  public static void main(String[] args)
  {
    WorldGeom world = new Main();
    Properties properties = loadPropertiesFile("behaviorism.properties");
    new BehaviorismDriver(world, properties);
  }

  @Override
  public void setUpWorld()
  {
    //GeomTextOutset phrase = new GeomTextBuilder("hi Karl!").anchor(new Point3f()).
      //exactBounds(true).constrainByHeight(1f).build();
    //addGeom(phrase);

    //make an ecotone builder -- i.e. seed with nutrients, plants, etc, various parameters
    //right now jsut doing from within the constructor of GeomEcotone
    ecotone = new GeomEcotone(numCols, numRows, new Point3f(-2f, -2f, 0f), 4f, 4f);
    EcotoneSingleton.INSTANCE.geomEcotone = ecotone;
    addGeom(ecotone);

    Nutrient.NITROGEN.visualize = false;
    ecotone.isVisible = false;

    Utils.sleep(500);

    //this is proper way, but there is a bug in BehaviorDiscrete when a single turn > the lengthMS
    //(which happens occasionally when there is an os hiccup or whatever)
    
    BehaviorTakeTurn btt = new BehaviorTakeTurn(
    new DiscreteBehaviorBuilder(System.nanoTime(), 0L, GeomEcotone.TIME_BETWEEN_TURNS).loop(LoopEnum.LOOP));

    ecotone.attachBehavior(btt);

    /*
    int i = 0;
    while (true)
    {
      Utils.sleep(GeomEcotone.TIME_BETWEEN_TURNS);
      ecotone.takeTurn(i++);
    }
     */
  }

  public static boolean nutrientDropMode = false;
  public static boolean herbivoreDropMode = true;
  public static boolean carnivoreDropMode = false;
  public static boolean plantDropMode = false;
  public static boolean traceIsVisible = true;

  @Override
  public boolean checkKeys(boolean[] keys, boolean[] keysPressing)
  {
    if (keys[VK_0]) //toggle nutrient drop mode
    {
      if (keysPressing[VK_0] == false)
      {
        nutrientDropMode = !nutrientDropMode;
        herbivoreDropMode = false;
        carnivoreDropMode = false;
        plantDropMode = false;

        keysPressing[VK_0] = true;
      }
      return true;
    }
    if (keys[VK_9]) //toggle herbivore drop mode
    {
      if (keysPressing[VK_9] == false)
      {
        nutrientDropMode = false;
        herbivoreDropMode = !herbivoreDropMode;
        carnivoreDropMode = false;
        plantDropMode = false;

        keysPressing[VK_9] = true;
      }
      return true;
    }
    if (keys[VK_8]) //toggle herbivore drop mode
    {
      if (keysPressing[VK_8] == false)
      {
        nutrientDropMode = false;
        herbivoreDropMode = false;
        carnivoreDropMode = !carnivoreDropMode;
        plantDropMode = false;

        keysPressing[VK_8] = true;
      }
      return true;
    }
    if (keys[VK_7]) //toggle herbivore drop mode
    {
      if (keysPressing[VK_7] == false)
      {
        nutrientDropMode = false;
        herbivoreDropMode = false;
        carnivoreDropMode = false;
        plantDropMode = !plantDropMode;

        keysPressing[VK_7] = true;
      }
      return true;
    }


    if (keys[Nutrient.CARBON.key])
    {
      if (keysPressing[Nutrient.CARBON.key] == false)
      {
        Nutrient.CARBON.toggle();
        keysPressing[Nutrient.CARBON.key] = true;
      }
      return true;
    }
    if (keys[Nutrient.NITROGEN.key])
    {
      if (keysPressing[Nutrient.NITROGEN.key] == false)
      {
        Nutrient.NITROGEN.toggle();
        keysPressing[Nutrient.NITROGEN.key] = true;
      }
      return true;
    }
    if (keys[Nutrient.OXYGEN.key])
    {
      if (keysPressing[Nutrient.OXYGEN.key] == false)
      {
        Nutrient.OXYGEN.toggle();
        keysPressing[Nutrient.OXYGEN.key] = true;
      }
      return true;
    }
    if (keys[Nutrient.URANIUM.key])
    {
      if (keysPressing[Nutrient.URANIUM.key] == false)
      {
        Nutrient.URANIUM.toggle();
        keysPressing[Nutrient.URANIUM.key] = true;
      }
      return true;
    }

    if (keys[VK_W]) //toggle view wind
    {
      if (keysPressing[VK_W] == false)
      {
        ecotone.wind.toggle();
        keysPressing[VK_W] = true;
      }
      return true;

    }


    if (keys[VK_T]) //toggle view animal trace
    {
      if (keysPressing[VK_T] == false)
      {
        keysPressing[VK_T] = true;
        traceIsVisible = !traceIsVisible;
        
        for (Animal animal : ecotone.animals)
        {
          //animal.trace.toggleIsVisible();
          animal.trace.isVisible = traceIsVisible;
        }
      }
      return true;
    }

    if (keys[VK_G]) //toggle view grid lines
    {
      if (keysPressing[VK_G] == false)
      {
        ecotone.toggleIsVisible();
        keysPressing[VK_G] = true;
      }
      return true;
    }

    if (keys[VK_J]) //toggle nutrient view - set to bar chart view
    {
      if (keysPressing[VK_J] == false)
      {
        ecotone.drawMode = DrawModeNutrientEnum.DRAW_ALL_BAR_CHART;
        keysPressing[VK_J] = true;
      }
      return true;
    }

    if (keys[VK_K]) //toggle nutrient view - set to blend view
    {
      if (keysPressing[VK_K] == false)
      {
        ecotone.drawMode = DrawModeNutrientEnum.DRAW_ALL_BLEND;
        keysPressing[VK_K] = true;
      }
      return true;
    }

    return false;
  }
}
