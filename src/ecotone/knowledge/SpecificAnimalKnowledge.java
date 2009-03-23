/* SpecificAnimalKnowledge.java ~ Dec 1, 2008 */

package ecotone.knowledge;

import javax.vecmath.Point3f;

/**
 * A set of knowledge about a specific animal at a specfic turn.
 * @author angus
 */
public class SpecificAnimalKnowledge extends SpecificKnowledge
{
  public Point3f location = null;
  public float direction;
  public float speed;
  public boolean directionKnown = false;
  public boolean speedKnown = false;
  public boolean locationKnown = false;

  public SpecificAnimalKnowledge(Point3f location)
  {
    this.location = location;
    this.locationKnown = true;
  }

  public SpecificAnimalKnowledge(Point3f location, float direction, float speed)
  {
    this.location = location;
    this.direction = direction;
    this.speed = speed;
    this.locationKnown = true;
    this.directionKnown = true;
    this.speedKnown = true;
  }
}
