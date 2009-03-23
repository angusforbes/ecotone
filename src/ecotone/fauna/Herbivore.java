/* Herbivore.java ~ Nov 25, 2008 */

package ecotone.fauna;

import ecotone.GeomEcotone;
import javax.vecmath.Point3f;

/**
 *
 * @author angus
 */
public class Herbivore extends Animal
{
  public Herbivore(GeomEcotone ecotone, Point3f p3f, int id)
  {
    super(ecotone, p3f, id);
    setColor(0f,1f,0f,1f);
  }

}
