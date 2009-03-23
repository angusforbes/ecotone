/* Nutrient.java ~ Nov 19, 2008 */

package ecotone.nutrients;

import geometry.Colorf;
import java.awt.event.KeyEvent;

/**
 *
 * @author angus
 */
public enum Nutrient
{
  CARBON(KeyEvent.VK_1, .05f, new Colorf(1f,0f,0f,1f)),
  NITROGEN(KeyEvent.VK_2, .5f, new Colorf(0f,1f,0f,1f)),
  OXYGEN(KeyEvent.VK_3, 1f, new Colorf(0f,0f,1f,1f)),
  URANIUM(KeyEvent.VK_4, 0f, new Colorf(0f,1f,1f,1f));

  /**
   * The color of the element in the visualization.
   */
  public Colorf color;

  /**
   * The weight controls how much or how little (and how far or close) the wind
   * will distribute the element. Value is between 0f and 1f. If it is 0f it will move
   * entirely with the wind. If it is 1f it will not move at all.
   */
  public float weight;

  /**
   * What key is assigned to toggle the view of this nutrient on or off.
   */
  public int key;

  /**
   * Indicates whether or not we will visualize this nutrient.
   */
  //public boolean visualize = true;
  public boolean visualize = false;

  private Nutrient(int key, float weight, Colorf color)
  {
    this.key = key;
    this.weight = weight;
    this.color = color;
  }

  public void toggle()
  {
    visualize = !visualize;
  }
}
