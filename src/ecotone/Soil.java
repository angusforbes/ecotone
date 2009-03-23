/* Soil.java ~ Nov 20, 2008 */

package ecotone;

import ecotone.nutrients.Nutrient;
import java.util.EnumMap;
import utils.Utils;

/**
 *
 * @author angus
 */
public class Soil 
{
  public EnumMap<Nutrient, Float> nutrients = new EnumMap<Nutrient, Float>(Nutrient.class);

  public Soil()
  {
    nutrientAmount(Nutrient.CARBON, Utils.randomFloat(0f,0f));
    nutrientAmount(Nutrient.OXYGEN, Utils.randomFloat(0f,0f));
    nutrientAmount(Nutrient.NITROGEN, Utils.randomFloat(0f,0f));
    nutrientAmount(Nutrient.URANIUM, Utils.randomFloat(0f,0f));
  }

  public void nutrientIncrement(Nutrient nutrient, float inc)
  {
    nutrientAmount(nutrient, nutrients.get(nutrient) + inc);
  }
  public void nutrientDecrement(Nutrient nutrient, float dec)
  {
    nutrientAmount(nutrient, nutrients.get(nutrient) - dec);
  }

  public void nutrientAmount(Nutrient nutrient, float amount)
  {
    if (amount < 0f)
    {
      amount = 0f;
    }
    if (amount > 1f)
    {
      //amount = 1f;
    }

    nutrients.put(nutrient, amount);
  }

}
