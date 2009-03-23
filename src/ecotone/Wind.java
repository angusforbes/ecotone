/* Wind.java ~ Nov 20, 2008 */

package ecotone;

import utils.Utils;

/**
 *
 * @author angus
 */
public class Wind 
{
  GeomEcotone ecotone;
  float direction; //i.e. angle
  float power;
  float minPower = .0001f; //too much?
  float maxPower = 1f; //3f; //too much?
  boolean visualize = false;

  @Override
  public String toString()
  {
    return "Wind: direction=" + this.direction + ", power="+power;
  }

  public Wind(GeomEcotone ecotone, float direction, float power)
  {
    this.ecotone = ecotone;
    this.direction = direction;
    this.power = power;
  }

  public void toggle()
  {
    this.visualize = !this.visualize;
  }

  public void changeDirection()
  {
    //this.direction += Utils.randomFloat(0f, 5f);
    //this.direction += Utils.randomFloat(-1f, 5f);
    this.direction += 1f;

    if (this.direction > 360f)
    {
      this.direction -= 360f;
    }
    if (this.direction < 0f)
    {
      this.direction += 360f;
    }
  }

  public void changePower()
  {
    this.power += Utils.randomFloat(-.01f, .03f);
    //this.power += Utils.randomFloat(-.03f, .03f);
  
    if (this.power > maxPower)
    {
      this.power = maxPower;
    }
    if (this.power < minPower)
    {
      this.power = minPower;
    }
  }

}
