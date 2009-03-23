/* GeomEcotoneSquare.java ~ Nov 19, 2008 */
package ecotone;

import ecotone.fauna.Animal;
import ecotone.fauna.CarnivoreAngus;
import ecotone.fauna.Herbivore;
import ecotone.flora.Plant;
import ecotone.nutrients.Nutrient;
import geometry.GeomFixedVector;
import geometry.GeomRect;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import javax.media.opengl.GL;
import javax.media.opengl.glu.GLU;
import javax.vecmath.Point3f;
import utils.GeomUtils;
import utils.Utils;

/**
 *
 * @author angus
 */
public class GeomEcotoneSquare extends GeomRect
{

  int col, row;
  //each nutrient and how many of that nutrient
  //EnumMap<Nutrient, Integer> nutrients = new EnumMap<Nutrient, Integer>(Nutrient.class);
  List<Plant> plants;
  List<Animal> animals;
  Map<Direction, GeomEcotoneSquare> neighborMap;
  public Soil soil;
  GeomEcotone ecotone;
  GeomFixedVector windVectorGeom = null;
  public float windResistance = 1f;
  public float windFunnel = 0f; //changes wind angle slightly
  public float windFunnelRate = 0f; //changes wind angle slightly
  float distanceToSun;

  public String printColAndRow()
  {
    return " (col/row: " + col + "/" + row + ")";
  }

  @Override
  public String toString()
  {
    return "Square " + col + "/" + row + ": distanceToSun=" + distanceToSun + " : oxygen=" + soil.nutrients.get(Nutrient.OXYGEN);
  }

  public GeomEcotoneSquare(GeomEcotone ecotone, Point3f p3f, float w, float h, int col, int row)
  {
    super(p3f, w, h);
    this.ecotone = ecotone; //keep pointer to specifcally typed parent for convenience

    this.col = col;
    this.row = row;
    neighborMap = new HashMap<Direction, GeomEcotoneSquare>();

    soil = new Soil();


    //all
    //soil.nutrientAmount(Nutrient.CARBON, Utils.randomFloat(0f,3f));

    //windResistance = Utils.randomFloat(.5f, 1.5f);
    //windFunnel = Utils.randomFloat(-20f, 20f);
    windFunnelRate = Utils.randomFloat(-2f, 2f);
    windFunnel = Utils.randomFloat(0f, 0f);
    windVectorGeom = new GeomFixedVector(getCenter(), 0f, 0f);
    //System.out.println("adding new windVectorGeom : " + windVectorGeom);
    addGeom(windVectorGeom, false);

    registerDraggableObject(null); //make it so that we can not drag
  }

  public void releaseAction(MouseEvent e)
  {
    if (Main.nutrientDropMode == true)
    {

      if (Nutrient.CARBON.visualize == true)
      {
        soil.nutrientAmount(Nutrient.CARBON, 10f);
      }
      if (Nutrient.OXYGEN.visualize == true)
      {
        soil.nutrientAmount(Nutrient.OXYGEN, 10f);
      }
      if (Nutrient.NITROGEN.visualize == true)
      {
        soil.nutrientAmount(Nutrient.NITROGEN, 10f);
      }
      if (Nutrient.URANIUM.visualize == true)
      {
        soil.nutrientAmount(Nutrient.URANIUM, 10f);
      }
    }


    if (Main.herbivoreDropMode == true)
    {
      System.out.println("ADDING HERBIVORE!!!!");
      Point3f cellCenter = ecotone.getCellCenter(this.col, this.row);

      Point3f npt1 = new Point3f(Utils.randomFloat(-.1f, .1f), Utils.randomFloat(-.1f, .1f), 0f);
      cellCenter.add(npt1);

      Animal child = new Herbivore(ecotone, cellCenter, 2001);
      child.size = .3f;
      ecotone.fitAnchor(child.anchor);
      ecotone.addAnimal(child);
    }

    if (Main.carnivoreDropMode == true)
    {
      System.out.println("ADDING CARNIVORE!!!!");
      Point3f cellCenter = ecotone.getCellCenter(this.col, this.row);

      Point3f npt1 = new Point3f(Utils.randomFloat(-.1f, .1f), Utils.randomFloat(-.1f, .1f), 0f);
      cellCenter.add(npt1);

      Animal child = new CarnivoreAngus(ecotone, cellCenter, 2001);
      child.size = .3f;
      ecotone.fitAnchor(child.anchor);
      ecotone.addAnimal(child);
    }

    if (Main.plantDropMode == true)
    {
      System.out.println("ADDING PLANT!!!!");
      Point3f cellCenter = ecotone.getCellCenter(this.col, this.row);
      for (int i = 0; i < 10; i++)
      {
      Point3f npt1 = new Point3f(Utils.randomFloat(-.1f, .1f), Utils.randomFloat(-.1f, .1f), 0f);
      cellCenter.add(npt1);

      Plant plant = new Plant(ecotone, new Point3f(cellCenter), 1001);
      plant.size = .1f * Utils.randomInt(1, 10);
      plant.randomize();
      ecotone.addPlant(plant);
      }
    }
  }

  public void distanceToSun(Sun sun)
  {
    distanceToSun = GeomUtils.getDistanceBetweenPoints(new Point3f(col, row, 0), new Point3f(sun.col, sun.row, 0));
  }

  public void addNeighbor(Direction d, GeomEcotoneSquare s)
  {
    neighborMap.put(d, s);
  }

  @Override
  public void draw(GL gl, GLU glu, float offset)
  {
    windVectorGeom.isActive = ecotone.wind.visualize;
    //don't even bother drawing parent
    //super.draw(gl, glu, offset);

    //first draw soil nutrients
    switch (ecotone.drawMode)
    {
      case DRAW_ALL_BAR_CHART:
        drawAllNutrientsBarChart(gl);

        break;

      case DRAW_ALL_BLEND:
        drawAllNutrientsBlend(gl);

        break;
    }

  //then draw plants


  //then draw animals... or may draw them as individual creatures...
  }

  private void drawAllNutrientsBlend(GL gl)
  {

    Set<Map.Entry<Nutrient, Float>> nutrientSet = soil.nutrients.entrySet();
    float nh = this.h / nutrientSet.size();
    float sh = 0f;
    for (Map.Entry<Nutrient, Float> entry : nutrientSet)
    {
      Nutrient nutrient = entry.getKey();

      if (nutrient.visualize == false)
      {
        continue;
      }

      float amount = entry.getValue();
      float nw = (amount * this.w);
      //System.out.println("nutrient = " + nutrient + " amount = " + amount);
      //gl.glColor4fv(nutrient.color.array(), 0);
      gl.glColor4f(nutrient.color.r, nutrient.color.g, nutrient.color.b, nutrient.color.a * nw);
      gl.glBegin(gl.GL_POLYGON);
      gl.glVertex3f(0f, 0f, 0f);
      gl.glVertex3f(w, 0f, 0f);
      gl.glVertex3f(w, h, 0f);
      gl.glVertex3f(0f, h, 0f);
      gl.glEnd();
    }
  }

  private void drawAllNutrientsBarChart(GL gl)
  {
    Set<Map.Entry<Nutrient, Float>> nutrientSet = soil.nutrients.entrySet();
    float nh = this.h / nutrientSet.size();
    float sh = 0f;
    for (Map.Entry<Nutrient, Float> entry : nutrientSet)
    {
      Nutrient nutrient = entry.getKey();
      float amount = entry.getValue();
      float nw = (amount * this.w);
      //System.out.println("nutrient = " + nutrient + " amount = " + amount);
      gl.glColor4fv(nutrient.color.array(), 0);
      gl.glBegin(gl.GL_POLYGON);
      gl.glVertex3f(0f, sh, 0f);
      gl.glVertex3f(nw, sh, 0f);
      gl.glVertex3f(nw, sh + nh, 0f);
      gl.glVertex3f(0f, sh + nh, 0f);
      gl.glEnd();

      sh += nh;
    }
  //count total nutrients



  }
}
