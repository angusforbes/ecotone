/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ecotone;

/**
 *
 * @author ky
 */
public enum EcotoneSingleton {
  INSTANCE;

  GeomEcotone geomEcotone;
  public GeomEcotone getEcotone ()
  {
      return geomEcotone;
  }
}
