/* Plant.java ~ Nov 19, 2008 */

package ecotone.flora;

import ecotone.DNA;
import ecotone.GeomEcotone;
import ecotone.GeomEcotoneSquare;
import ecotone.Soil;
import ecotone.nutrients.Nutrient;
import geometry.Colorf;
import javax.media.opengl.GL;
import javax.media.opengl.glu.GLU;
import javax.vecmath.Point3f;
import utils.Utils;

/**
 *
 * @author angus
 */
public class Plant extends DNA
{
  private Nutrient whatToEat;
  private Nutrient whatToPoop;
  
  //fake one for now
  public Plant(GeomEcotone ecotone, Point3f p3f, int id)
  {
    super(ecotone, p3f, id);
    setColor(new Colorf(0f, 1f, 0f, .2f));
    whatToEat = Nutrient.OXYGEN;
    whatToPoop = Nutrient.CARBON;
  }

  public void randomize ()
  {
    do
    {
      for (Nutrient n : Nutrient.values ())
        if (.5 > Utils.randomFloat (0f, 1f))
          whatToEat = n;
      for (Nutrient n : Nutrient.values ())
        if (.5 > Utils.randomFloat (0f, 1f))
          whatToPoop = n;
      
    }
    while (whatToEat == whatToPoop);
//    System.out.println (this);
    setColor (whatToEat.color);
  }

  public void takeTurn(int turn)
  {
    //for simplicity just keep track of things in nearby cells.
    int col = ecotone.getColContaining(this.anchor);
    int row = ecotone.getRowContaining(this.anchor);

    GeomEcotoneSquare square = ecotone.getSquareAt(col, row);
    if (square == null)
    {
      return;
    }

    Soil soil = square.soil;
    float amountNutrient = soil.nutrients.get(whatToEat);

    if (amountNutrient > 0f)
    {
      if (size <= .9f)
      {
        size += .02f;
        soil.nutrientDecrement(whatToEat, .1f);
        soil.nutrientIncrement(whatToPoop, .01f);
      }
    }

      if (size <= .2f)
      {
        size = .1f; //and die...
        //System.out.println("this plant has died!!!!!!!!!");
        isDead = true;
        return;
      }
      else
      {
        size -= .01f;
      }
 
    
    if (size >= .9f) //clone myself...
    {
      isCloning = true;
    }
  }


  public void transmute(String dna)
  {
  }

  public void knowledge()
  {
    //a plant can sense certain things (potentially):
    //  1. what elements are in the ground
    //  2. what elements are in the air
    //  3. where the sun is (and where it will be in the near future...)
    //  4. does NOT know anything about other plants or animals
  }
  
  public void breathe()
  {
    //1. input carbon --> output oxygen

    //2. if no carbon, die immediately
  }

  public void die()
  {
    // animals will eat part(or all) of the plant, and remove some of its elements
    //1. if dead, dump all dna into soil
  }
  
  public void grow()
  {
    //1. input (some elements), increase in size
    //(benefit of being bigger in size means that that plant gets to "go" first) 
  
    //requirs a certain amount of energy per turn to stay a certain size

    //2, or shrink if we can't maintain the energy to be a certain size
  }

  public void reproduce()
  {
    //1. if have enough of (some element), then break off a part of my DNA
    //and put it into the wind as a seed

    //maybe there can be different ways to reproduce?
    //seeds-in-wind; picked up by animals that eat it; or just scatter it very close by. 
  }

  public void draw(GL gl, GLU glu, float offset)
  {
    gl.glColor4f(r, g, b, a);
    gl.glPointSize(41f * size);

    gl.glBegin(gl.GL_POINTS);
    //gl.glVertex3f(anchor.x, anchor.y , anchor.z);  //draws the point, it should be the point plus anchor
    gl.glVertex3f(0f, 0f, 0f);  //draws the point, it should be the point plus anchor
    gl.glEnd();
  }

  public String toString ()
  {
    return "Plant" +
      "\n  eats "  + whatToEat +
      "\n  poops " + whatToPoop +
      "\n";
  }
}
