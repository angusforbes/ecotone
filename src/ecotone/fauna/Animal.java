/* Animal.java ~ Nov 19, 2008 */
package ecotone.fauna;

import behaviors.geom.continuous.BehaviorTranslate;
import ecotone.DNA;
import ecotone.GeomEcotone;
import ecotone.flora.Plant;
import ecotone.knowledge.AnimalKnowledgeBase;
import ecotone.knowledge.PlantKnowledgeBase;
import ecotone.knowledge.SpecificAnimalKnowledge;
import ecotone.knowledge.SpecificPlantKnowledge;
import geometry.GeomTrace;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import javax.media.opengl.GL;
import javax.media.opengl.glu.GLU;
import javax.vecmath.Point3f;
import utils.GeomUtils;
import utils.Utils;

/**
 *
 * @author angus
 */
public class Animal extends DNA
{

  final AnimalKnowledgeBase animalKnowledgeBase = new AnimalKnowledgeBase();
  final PlantKnowledgeBase plantKnowledgeBase = new PlantKnowledgeBase();

  private AnimalTraitSet traitSet;

  //for fun...
  public GeomTrace trace = null;
  float angle = 90f;
  //float speed = 0f;

  /**
   * Animals are made up of nutrients. Their dna lets them turn other nutrients into energy.More energy lets them
   * move farther and to have more of a view into their knowledge base.
   * 
   * ie a dna of BBC lets the animal turn one Oxygen into one energy
   */
  public Animal()
  {
    dna = "haha";
    traitSet = AnimalTraitSet.newAnimalTraitSet();
  }

  //fake one for now
  public Animal(GeomEcotone ecotone, Point3f p3f, int id)
  {
    super(ecotone, p3f, id);
    this.size = .3f;

    //this.w = .1f;
    //this.h = .1f;
    this.angle = 90f;
    this.speed = Utils.randomFloat(.05f, .3f);
    //this.trace = new ArrayList<Point3f>();
    traitSet = AnimalTraitSet.newAnimalTraitSet();
    /*
    traitSet.minimumTurnDistance = .05f;
    traitSet.maximumTurnDistance = .3f;
    //traitSet.minimumTurnAngle = -20f;
    //traitSet.maximumTurnAngle = +20f;
    traitSet.minimumTurnAngle = -26f;
    traitSet.maximumTurnAngle = +26f;
    */

  }

  public void averageTraitsWith (Animal a)
  {
    //System.out.println ("child born....");
    //System.out.println ("parent traits:" + a.traitSet);
    //System.out.println ("natural child traits:" + traitSet);
    traitSet = AnimalTraitSet.averageOf (traitSet, a.traitSet);
    //System.out.println ("average child/parent traits:" + traitSet);
  }

    private void eat(Animal closestAnimal) {
      closestAnimal.size -= .5;
      size += .1;
      System.out.println ("animal eats animal");
    }

  private void setAnimalTraitSet (AnimalTraitSet t)
  {
    traitSet = t;
  }

  public void addTrace(GeomTrace trace)
  {
    this.trace = trace;
  }

  /*
  @Override
  public void transmute(String s)
  {
  }
   */
  public List<Plant> getAllPlantsInSight(List<Plant> allPlants)
  {

    List<Plant> plantsInSight = Collections.synchronizedList(new ArrayList<Plant>());

    if (GeomEcotone.CARNIVORES_CAN_EAT_PLANTS == false && this instanceof CarnivoreAngus)
    {
      if (this instanceof CarnivoreAngus) return plantsInSight;
    }

    for (Plant plant : allPlants)
    {
      float angleBetween = (float) Math.toDegrees(GeomUtils.getAngleBetweenPoints(this.anchor, plant.anchor));
      float distanceBetween = this.anchor.distance(plant.anchor);

      if (distanceBetween < 3f // && angleBetween <= this.angle + 40f
        // && angleBetween >= this.angle - 40f
        )
      {
        plantsInSight.add(plant);

      //test follow....

      //move(angleBetween - this.angle, .1f);
      }
    }

    return plantsInSight;
  }

  public List<Animal> getAllAnimalsInSight(List<Animal> allAnimals)
  {
    List<Animal> animalsInSight = Collections.synchronizedList(new ArrayList<Animal>());

    for (Animal animal : allAnimals)
    {
      if (animal == this) //ignore myself
      {
        continue;
      }

      float angleBetween = (float) Math.toDegrees(GeomUtils.getAngleBetweenPoints(this.anchor, animal.anchor));
      float distanceBetween = this.anchor.distance(animal.anchor);

      //System.out.println("Animal " + getClass() + " is heading " + angle);
      //System.out.println("angleBetween = " + angleBetween +  " and distanceBetween = " + distanceBetween);
      //System.out.println("this.angle = " + this.angle);

      float closestDist = 100f;

      if (distanceBetween < traitSet.maximumViewDistance) //&&
      //angleBetween <= this.angle + 40f &&
      //angleBetween >= this.angle - 40f)
      {
          if (this instanceof CarnivoreAngus && animal instanceof CarnivoreAngus)
              continue;

          if (this instanceof Herbivore && animal instanceof Herbivore)
              continue;

          if (distanceBetween < closestDist && distanceBetween < traitSet.maximumViewDistance * 1f)
          {
            animalsInSight.clear();
            closestDist = distanceBetween;
          }

          animalsInSight.add(animal);

        //System.out.println(" ************ " + getClass() + " sees " + animal.getClass() + "!");
      //test follow....

      //move(angleBetween - this.angle, .1f);
        /*
      if (angleBetween >= this.angle)
      {
      move(Utils.randomFloat(20f, 20f), Utils.randomFloat(.2f, .2f));
      }
      else
      {
      move(Utils.randomFloat(-20f, -20f), Utils.randomFloat(.2f, .2f));
      }
       */
      }
    }

    return animalsInSight;
  }

  public void takeTurn(int turn)
  {
    //System.out.println("Animal #" + id + " taking turn...");

    //each turn reduces size...
    size -= .005f;

    if (size < 0f)
    {
      //System.out.println("animal is DEAD!!!");
      this.isDead = true;
      size = .0001f;
    }
    if (size >= .9f) //clone myself...
    {
      isCloning = true;
    }
    
    //find animals within range
    List<Animal> animalsInSight = getAllAnimalsInSight(ecotone.animals);
    //System.out.println("there are " + animalsInSight.size() + " in sight...");
    updateAnimalKnowledgeBase(animalsInSight, turn);
    //System.out.println(animalKnowledgeBase);

    //find plants within range
    List<Plant> plantsInSight = getAllPlantsInSight(ecotone.plants);
    updatePlantKnowledgeBase(plantsInSight, turn);




    //okay, now that we have updated the knowledge base, we can reason about what to do...

    reason(turn);

  }

  /**
   * Reason to determine the best action to take.
   * @param turn The current turn number
   */
  public void reason(int turn)
  {

    //System.out.println("Animal #" + id + " is reasoning... ");
    //System.out.println("\the knows about " + animalKnowledgeBase.specificAnimals.size() + " specific animals.");

    //think about animals
    //hunt this fucker...
    int num = 0;
    float angleBetween = 0f;
    float distanceBetween = 0f;
    Point3f moveToward = new Point3f();
    Animal closestAnimalOfInterest = null;
    //loop through all animals we know about...
    for (Map.Entry<Animal, SortedMap<Integer, SpecificAnimalKnowledge>> animalsToKnowledgeMap : animalKnowledgeBase.specificAnimals.entrySet())
    {
      //current turn info..
      SpecificAnimalKnowledge thisTurnInfo = animalsToKnowledgeMap.getValue().get(turn);

      if (thisTurnInfo.location.distance(this.anchor) < .1)
      {
          // this animal is close enough to eat
          closestAnimalOfInterest = animalsToKnowledgeMap.getKey ();
      }

      moveToward.add(thisTurnInfo.location);


      //angleBetween = 360f + (float)Math.toDegrees(GeomUtils.getAngleBetweenPoints(this.anchor, thisTurnInfo.location));
      //distanceBetween = this.anchor.distance(thisTurnInfo.location);
      num++;
    }

    if (num > 0)
    {
      moveToward.x /= num;
      moveToward.y /= num;
      moveToward.z /= num;


      //System.out.println("angleBetween was = " + angleBetween);
      //angleBetween = between180(angleBetween /= num);
      //System.out.println("angleBetween is = " + angleBetween);

      if (this instanceof CarnivoreAngus)
      {
        angleBetween = (float) Math.toDegrees(GeomUtils.getAngleBetweenPoints(this.anchor, moveToward));
        distanceBetween = this.anchor.distance(moveToward);
        
        if (closestAnimalOfInterest != null)
          if (closestAnimalOfInterest instanceof Herbivore)
            eat (closestAnimalOfInterest);

        move(angleBetween - this.angle, 0f);
      }
      else if (this instanceof Herbivore)
      {
        angleBetween = (float) Math.toDegrees(GeomUtils.getAngleBetweenPoints(moveToward, this.anchor));
        distanceBetween = this.anchor.distance(moveToward);
        move(angleBetween - this.angle, 0f);
      }

      return;
    }

    //no animals to worry about... look for food

    num = 0;
    angleBetween = 0f;
    distanceBetween = 0f;
    Point3f closestPlantPt = null;
    Plant closestPlant = null;
    //loop through all animals we know about...
    for (Map.Entry<Plant, SortedMap<Integer, SpecificPlantKnowledge>> plantToKnowledgeMap : plantKnowledgeBase.specificPlants.entrySet())
    {
      //current turn info..
      SpecificPlantKnowledge thisTurnInfo = plantToKnowledgeMap.getValue().get(turn);


//      System.out.println("there is a plant at " + thisTurnInfo.location +
//        " which is " + thisTurnInfo.location.distance(this.anchor) + " from me (" + this.anchor + ")");

      if (closestPlantPt == null)
      {
        closestPlantPt = thisTurnInfo.location;
        closestPlant = plantToKnowledgeMap.getKey();
      }
      else if (thisTurnInfo.location.distance(this.anchor) < closestPlantPt.distance(this.anchor))
      {
        //System.out.println("and it is the closest plant!");
        closestPlantPt = thisTurnInfo.location;
        closestPlant = plantToKnowledgeMap.getKey();
      }
    }
    if (closestPlantPt != null)
    {
      angleBetween = (float) Math.toDegrees(GeomUtils.getAngleBetweenPoints(this.anchor, closestPlantPt));
      distanceBetween = this.anchor.distance(closestPlantPt);

//      System.out.println("moving toward plant at " + closestPlantPt);
//      System.out.println("distanceBetween = " + distanceBetween);
      if (distanceBetween > .2f) //move toward it
      {
        move(angleBetween - this.angle, 0f);
        return;
      }
      else // if this is *really* where the plant is, then eat it!
      {
        if (this.size < 1f && this.anchor.distance(closestPlant.anchor) <= .2f)
        {
          //System.out.println("eating...!!!!!!!!!!!!!!!!!");
          
          this.size += .05f;

          if (closestPlant.size - .5f < .1f)
          {
            closestPlant.size = .1f;
          }
          else
          {
            closestPlant.size -= .5f;
          }
        }
      }
    }


    //if we are here then we don't know anything, so move at random...
    move(Utils.randomFloat(-30f, 30f), Utils.randomFloat(0f, 0f));



  //questions the animal is asking...
  //1. are there animals nearby

  //2. are there plants nearby



  }

  public float between180(float ang)
  {
    ang %= 360f;
    if (ang > 180f)
    {
      ang -= 360f;
    }

    if (ang < -180f || ang > 180f)
    {
      //System.out.println("in between180(), how is angle = " + ang + "???");
      System.exit(1);
    }

    //System.out.println("in between180() ang = " + ang + ")");
    return (float) Math.toDegrees(Math.toRadians(ang));
  }

  private void updatePlantKnowledgeBase(List<Plant> sensedPlants, int turn)
  {
    plantKnowledgeBase.specificPlants.clear();

    for (Plant plant : sensedPlants)
    {
      SortedMap<Integer, SpecificPlantKnowledge> specificKnowledge = plantKnowledgeBase.specificPlants.get(plant);

      if (specificKnowledge == null)
      {
        specificKnowledge = new TreeMap<Integer, SpecificPlantKnowledge>();
        plantKnowledgeBase.specificPlants.put(plant, specificKnowledge);

        //create the actual new knowledge - we don't know anything about his speed or angle
        specificKnowledge.put(turn, new SpecificPlantKnowledge(plant.anchor));
      }
      else
      {
        //already seen, so we can determine his angle and speed

        //SpecificPlantKnowledge prevSpecificKnowledge = specificKnowledge.get(turn - 1);

        //if (prevSpecificKnowledge == null)
        {
          //System.err.println("Error : why is prevSpecificKnowledge null? Investigate...");
          //return;
        }

      //float angleBetween = (float) Math.toDegrees(GeomUtils.getAngleBetweenPoints(plant.anchor, prevSpecificKnowledge.location));
      //float distanceBetween = this.anchor.distance(prevSpecificKnowledge.location);

      //specificKnowledge.put(turn, new SpecificPlantKnowledge(plant.anchor));
      }
    //okay, now see if we need to update the general knowledge about the species
    }

    //handle all the plants that we sensed in the past, but now no longer sense
    List<Plant> unsensedPlants = new ArrayList<Plant>(plantKnowledgeBase.specificPlants.keySet());
    unsensedPlants.removeAll(sensedPlants);

    for (Plant plant : unsensedPlants)
    {
      plantKnowledgeBase.specificPlants.remove(plant);

    //SortedMap<Integer, SpecificPlantKnowledge> specificKnowledge = plantKnowledgeBase.specificPlants.get(plant);

    //SpecificPlantKnowledge prevSpecificKnowledge = specificKnowledge.get(turn - 1);
    }

  }

  private void updateAnimalKnowledgeBase(List<Animal> sensedAnimals, int turn)
  {
    for (Animal animal : sensedAnimals)
    {
      SortedMap<Integer, SpecificAnimalKnowledge> specificKnowledge = animalKnowledgeBase.specificAnimals.get(animal);

      if (specificKnowledge == null)
      {
        specificKnowledge = new TreeMap<Integer, SpecificAnimalKnowledge>();
        animalKnowledgeBase.specificAnimals.put(animal, specificKnowledge);

        //create the actual new knowledge - we don't know anything about his speed or angle
        specificKnowledge.put(turn, new SpecificAnimalKnowledge(new Point3f(animal.anchor)));
        //System.out.println("at turn " + turn + " we sense an animal " + animal.id + " at location " + animal.anchor);
      }
      else
      {
        //already seen, so we can determine his angle and speed

        SpecificAnimalKnowledge prevSpecificKnowledge = specificKnowledge.get(turn - 1);

        if (prevSpecificKnowledge == null)
        {
          //System.err.println("Error : why is prevSpecificKnowledge null? Investigate...");
          return;

        }

        //float angleBetween = (float)Math.toDegrees(GeomUtils.getAngleBetweenPoints(animal.anchor, prevSpecificKnowledge.location));

        float angleBetween = (float) Math.toDegrees(GeomUtils.getAngleBetweenPoints(prevSpecificKnowledge.location, animal.anchor));
        float distanceBetween = this.anchor.distance(prevSpecificKnowledge.location);

        specificKnowledge.put(turn, new SpecificAnimalKnowledge(new Point3f(animal.anchor), angleBetween, distanceBetween));
      }

//okay, now see if we need to update the general knowledge about the species
    }

    //handle all the animals that we sensed in the past, but now no longer sense
    List<Animal> unsensedAnimals = new ArrayList<Animal>(animalKnowledgeBase.specificAnimals.keySet());
    unsensedAnimals.removeAll(sensedAnimals);

    for (Animal animal : unsensedAnimals)
    {
      SortedMap<Integer, SpecificAnimalKnowledge> specificKnowledge = animalKnowledgeBase.specificAnimals.get(animal);

      //have we lost him?
      boolean lostHim = true;
      for (int i = turn - 1; i <= turn - 1; i++)
      {
        SpecificAnimalKnowledge checkSpecificKnowledge = specificKnowledge.get(i);

        if (checkSpecificKnowledge == null || checkSpecificKnowledge.locationKnown == true)
        {
          lostHim = false;
          break;

        }


      }
      if (lostHim == true)
      {
        //System.out.println("we lost him!!!");
        //then we lost him, don't assume we know anything about this specific animal anymore....
        animalKnowledgeBase.specificAnimals.remove(animal);
        continue; //continue investigating other unsensed animals...

      }



      SpecificAnimalKnowledge prevSpecificKnowledge = specificKnowledge.get(turn - 1);

      //if we knew his speed/direction from last turn, assume that he continues on that same path
      if (prevSpecificKnowledge.directionKnown == true && prevSpecificKnowledge.speedKnown == true /*&& prevSpecificKnowledge.locationKnown == true*/)
      {
        Point3f newLocation = GeomUtils.getNextPointUsingAngleAndDistance(prevSpecificKnowledge.location,
          prevSpecificKnowledge.direction, prevSpecificKnowledge.speed);

       // System.out.println("I don't see him anymore, but he was at " + prevSpecificKnowledge.location + " " +
       //   " moving in the direction " + prevSpecificKnowledge.direction + " at the speed " +
       //   prevSpecificKnowledge.speed + " and I think he will move to " + newLocation);

        SpecificAnimalKnowledge curSpecificKnowledge = new SpecificAnimalKnowledge(newLocation,
          prevSpecificKnowledge.direction, prevSpecificKnowledge.speed);
        curSpecificKnowledge.locationKnown = false;
        specificKnowledge.put(turn, curSpecificKnowledge);
      //specificKnowledge.remove(turn - 1);
      }
      else
      {

        //if we don't know it, then assume based on our proclivities...

        //if we haven't seen him in X amount of turns, then erase him from our knowledge base

        //for now pretend we don't know anything about him and we are not concerned about it.
        animalKnowledgeBase.specificAnimals.remove(animal);
      }

    }
  }


  //private GeomEcotoneSquare square;

  /*
  public void knowledge()
  {
  //an animal can sense certain things (potentially):
  //  1. does NOT know about elements directly
  //  3. where the sun is (and where it will be in the near future...)
  //  4. what plants are nearby
  //  5. what animals are nearby
  }
   */
  public void move(float d_ang, float d_speed)
  {

    this.speed += d_speed;


    if (d_ang > traitSet.maximumTurnAngle)
    {
      d_ang = traitSet.maximumTurnAngle;
    }
    else if (d_ang < traitSet.minimumTurnAngle)
    {
      d_ang = traitSet.minimumTurnAngle;
    }

    this.angle += d_ang;

    if (this.speed > traitSet.maximumTurnDistance)
    {
      this.speed = traitSet.maximumTurnDistance;
    }
    else if (this.speed < traitSet.minimumTurnDistance)
    {
      this.speed = traitSet.minimumTurnDistance;
    }

    Point3f nextPt = GeomUtils.getNextIncrementUsingAngleAndDistance(this.anchor, this.angle, this.speed);
    BehaviorTranslate.translate(this, System.nanoTime(), GeomEcotone.ANIMAL_ANIMATION, nextPt);

//   nextPt = GeomUtils.getNextPointUsingAngleAndDistance(this.anchor, this.angle, this.speed);

//   if (this.trace != null)
//   {
//    System.out.println("adding " + nextPt + " to GeomTrace?");
//    this.trace.trace.add(nextPt);
//   }
  }

  public void draw(GL gl, GLU glu, float offset)
  {
    if (this.anchor.x > this.ecotone.w)
    {
      if (this.trace != null)
      {
        this.trace.addPoint(this.anchor);
      }

      this.anchor.x -= this.ecotone.w;
    }
    else if (this.anchor.x < 0)
    {
      if (this.trace != null)
      {
        this.trace.addPoint(this.anchor);
      }

      this.anchor.x += this.ecotone.w;
    }

    if (this.anchor.y > this.ecotone.h)
    {
      if (this.trace != null)
      {
        this.trace.addPoint(this.anchor);
      }

      this.anchor.y -= this.ecotone.h;
    }
    else if (this.anchor.y < 0)
    {
      if (this.trace != null)
      {
        this.trace.addPoint(this.anchor);
      }

      this.anchor.y += this.ecotone.h;
    }

    if (this.trace != null)
    {
      this.trace.addPoint(this.anchor);
    }

    float dim = this.size * .2f;

    gl.glColor4f(r, g, b, a);
    gl.glBegin(gl.GL_POLYGON);
    gl.glVertex3f(-dim / 2f, -dim / 2f, offset);
    gl.glVertex3f(dim / 2f, -dim / 2f, offset);
    gl.glVertex3f(dim / 2f, dim / 2f, offset);
    gl.glVertex3f(-dim / 2f, dim / 2f, offset);

    gl.glEnd();
  }

  public static int ID = 1001;
  public static GeomEcotone staticEcotone;
  public static Animal newAnimal ()
  {
    return new Animal (staticEcotone, new Point3f (), ID++);
  }

  public Animal mateWith (Animal babyDaddy)
  {
    Animal child = newAnimal ();
    child.setAnimalTraitSet (AnimalTraitSet.averageOf (traitSet, babyDaddy.traitSet));
    return child;
  }
}
