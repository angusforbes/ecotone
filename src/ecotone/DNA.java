/* DNA.java ~ Nov 20, 2008 */

package ecotone;

import ecotone.nutrients.Nutrient;
import geometry.Geom;
import java.util.EnumMap;
import javax.vecmath.Point3f;

/**
 * The DNA is the set of operations which transmute substances into some other substance
 * @author angus
 */
public abstract class DNA extends Geom
{
  public GeomEcotone ecotone = null;

  public String dna = "";

  public EnumMap<Nutrient, Integer> nutrients = new EnumMap<Nutrient, Integer>(Nutrient.class);
  public float energy = .1f;
  public float speed = .1f;
  public float size = .1f;

  public boolean isDead = false;
  public boolean isCloning = false;

  //possible attributes:
  //aggressiveness - fight or flight
  //

  public int id;

  public DNA(){}
  public DNA(GeomEcotone ecotone, Point3f p3f, int id)
  {
    super(p3f);
    this.id = id;
    this.ecotone = ecotone; //for convenience, keep pointer to parent here.
  }

  //public abstract void transmute(String dna);
}
