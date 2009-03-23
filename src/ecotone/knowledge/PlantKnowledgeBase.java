/* PlantKnowledgeBase.java ~ Dec 6, 2008 */

package ecotone.knowledge;

import ecotone.flora.Plant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;

/**
 *
 * @author angus
 */
public class PlantKnowledgeBase 
{
 //knowledge about individual animals
  public Map<Plant, SortedMap<Integer, SpecificPlantKnowledge>> specificPlants =
    new HashMap<Plant, SortedMap<Integer, SpecificPlantKnowledge>>();

  //knowledge about various species
  public List<GeneralPlantKnowledge> generalPlants = new ArrayList<GeneralPlantKnowledge>();

  /*
  //knowledge about individual plants
  public Map<Plant, LinkedList<SpecificPlantKnowledge>> specificPlants =
    new HashMap<Plant, LinkedList<SpecificPlantKnowledge>>();

  //knowledge about various species
  public List<GeneralPlantKnowledge> generalPlants = new ArrayList<GeneralPlantKnowledge>();
  */

  @Override
  public String toString()
  {
    String output = "";

    output += "SpecificPlant knowledge:\n";
    for (Map.Entry<Plant, SortedMap<Integer, SpecificPlantKnowledge>> entry : specificPlants.entrySet())
    {
      Plant animal = entry.getKey();
      System.out.println("\tPlant " + animal.id + ": ");

      SortedMap<Integer, SpecificPlantKnowledge> map = entry.getValue();

      for (Map.Entry<Integer, SpecificPlantKnowledge> entry2 : map.entrySet())
      {
        System.out.println("\t\tturn " + entry2.getKey());
        SpecificPlantKnowledge sak = entry2.getValue();
        System.out.println("\t\t\tlocation:" + sak.location);
      }
    }

    return output;
  }
}
