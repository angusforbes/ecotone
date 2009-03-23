/* CarnivoreAngus.java ~ Dec 6, 2008 */

package ecotone.fauna;

import ecotone.GeomEcotone;
import javax.vecmath.Point3f;

/**
 *
 * @author angus
 */
public class CarnivoreAngus extends Animal
{
 public CarnivoreAngus(GeomEcotone ecotone, Point3f p3f, int id)
  {
    super(ecotone, p3f, id);
    setColor(1f,0f,0f,1f);
  }
}
