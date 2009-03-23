/* SpecificPlantKnowledge.java ~ Dec 1, 2008 */

package ecotone.knowledge;

import javax.vecmath.Point3f;

/**
 *
 * @author angus
 */
public class SpecificPlantKnowledge extends SpecificKnowledge
{
  public Point3f location;

  public SpecificPlantKnowledge(Point3f location)
  {
    this.location = location;
  }
}
