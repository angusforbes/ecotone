/* AnimalKnowledgeBase.java ~ Dec 1, 2008 */

package ecotone.knowledge;

import ecotone.fauna.Animal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;

/**
 * What a particular Animal knows about its world.
 * @author angus
 */
public class AnimalKnowledgeBase extends KnowledgeBase
{
  //knowledge about individual animals
  public Map<Animal, SortedMap<Integer, SpecificAnimalKnowledge>> specificAnimals =
    new HashMap<Animal, SortedMap<Integer, SpecificAnimalKnowledge>>();

  //knowledge about various species
  public List<GeneralAnimalKnowledge> generalAnimals = new ArrayList<GeneralAnimalKnowledge>();

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

    output += "SpecificAnimal knowledge:\n";
    for (Map.Entry<Animal, SortedMap<Integer, SpecificAnimalKnowledge>> entry : specificAnimals.entrySet())
    {
      Animal animal = entry.getKey();
      System.out.println("\tAnimal " + animal.id + ": ");

      SortedMap<Integer, SpecificAnimalKnowledge> map = entry.getValue();

      for (Map.Entry<Integer, SpecificAnimalKnowledge> entry2 : map.entrySet())
      {
        System.out.println("\t\tturn " + entry2.getKey());
        SpecificAnimalKnowledge sak = entry2.getValue();
        System.out.println("\t\t\tlocation:" + sak.location + ", direction: " + sak.direction + ", speed: " + sak.speed);
      }
    }

    return output;
  }
}
