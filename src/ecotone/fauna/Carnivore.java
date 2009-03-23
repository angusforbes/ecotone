/* Carnivore.java ~ Nov 25, 2008 */

package ecotone.fauna;

import ecotone.GeomEcotone;
import javax.vecmath.Point3f;
import utils.Utils;
/**
 *
 * @author angus
 */
public class Carnivore extends Animal
{
  public Carnivore(GeomEcotone ecotone, Point3f p3f, int id)
  {
    super(ecotone, p3f, id);
    setColor(1f,0f,0f,1f);
  }


    @Override
  public void takeTurn (int t)
  {
    move (Utils.randomFloat (-15f, 15f), Utils.randomFloat (-.1f, .1f));
  }

}
