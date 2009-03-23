/*
 * To tick this template, choose Tools | Templates
 * and open the template in the editor.
 */

package leaflitter;

/**
 *
 * @author angus
 */
public abstract class Variable
{
  double C;
  double N;

  double dC;
  double dN;

  public abstract void tick();
  public abstract void update();
  public void reset()
  {
    dC = 0;
    dN = 0;
  }
  
}
