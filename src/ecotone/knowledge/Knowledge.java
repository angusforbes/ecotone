/* Knowledge.java ~ Nov 25, 2008 */

package ecotone.knowledge;

import ecotone.fauna.Animal;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author angus
 */
public class Knowledge
{

  List<String> knowledge = new ArrayList<String>();

  public static Knowledge sense(Animal animal)
  {
    Knowledge k = new Knowledge();

    //based on position, get knowledge

    //ex.
    //Me at (1,1)
    //Sun at(4,2)
    //Plant1 of type (Edible) at (1.2, 2.1)
    //Plant2 of type (Unknown) at (.9, 1.1)
    //Plant3 of type (Posionous) at (.8, 1.7)
    //Animal of type (Carnivore) of species (C1) at (1,1.5)
    //Animal of type (Herbavore) of species (H1) move at least (.5 per/sec) at (1,1.5)
    //Animal of type (unknown) of species (unknown) at (1.1, 2.1)



    return k;
  }

  public static void /*Action*/ reason(Animal animal, Knowledge k)
  {
    //based on my knowldege, what action(s) do i take?

    //flee from Animal1
    //eat Plant3
    //head towards Sun
    //wait for plant seed downwind
    //reproduce
    //hunt Animal4
  }
}
