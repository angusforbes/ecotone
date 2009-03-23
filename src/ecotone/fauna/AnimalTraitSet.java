/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ecotone.fauna;

import java.util.Random;

/**
 *
 * @author ky
 */

public class AnimalTraitSet {
  public float maximumViewAngle;
  public float minimumViewAngle;

  public float maximumViewDistance;

  public float maximumTurnAngle;
  public float minimumTurnAngle;

  public float maximumTurnDistance;
  public float minimumTurnDistance;

  public float hate;
  public float fear;
  private Random r;

  // uniquely identifies the trait set
  public int id;

  public static int ID = 0;


  private AnimalTraitSet ()
  {
    // uniquely identifies the trait set
    id = ID++;
    r = new Random (id);

    maximumViewAngle = 45.0f;
    minimumViewAngle = -45.0f;

    maximumViewDistance = .5f;

    maximumTurnAngle = 26f;
    minimumTurnAngle = -26f;

    maximumTurnDistance = .3f;
    minimumTurnDistance = .05f;

    hate = .5f;
    fear = .5f;
  }

  public static AnimalTraitSet newAnimalTraitSet ()
  {
    AnimalTraitSet a = new AnimalTraitSet ();
    a.initialize ();
//    System.out.println ("creating new AnimalTraitSet\n" + a);
    return a;
  }

  private void initialize ()
  {
    r.nextFloat (); // burn one

    maximumViewAngle    =     180.0f * r.nextFloat ();
    minimumViewAngle    =    -180.0f * r.nextFloat ();

    maximumViewDistance =  .2f + 1.8f * r.nextFloat ();  //between .2 and 1.0

    maximumTurnAngle    =     180.0f * r.nextFloat ();
    minimumTurnAngle    =    -180.0f * r.nextFloat ();
    maximumTurnDistance = .1f  + .1f * r.nextFloat ();
    minimumTurnDistance = .01f + .1f * r.nextFloat ();
    hate           =              r.nextFloat ();
    fear                =              r.nextFloat ();
  }
  
  public static AnimalTraitSet averageOf (AnimalTraitSet a, AnimalTraitSet b)
  {
    AnimalTraitSet c = new AnimalTraitSet ();
    c.maximumViewAngle    = (a.maximumViewAngle    + b.maximumViewAngle   )/2;
    c.minimumViewAngle    = (a.minimumViewAngle    + b.minimumViewAngle   )/2;

    c.maximumViewDistance = (a.maximumViewDistance + b.maximumViewDistance)/2;

    c.maximumTurnAngle    = (a.maximumTurnAngle    + b.maximumTurnAngle   )/2;
    c.minimumTurnAngle    = (a.minimumTurnAngle    + b.minimumTurnAngle   )/2;

    c.maximumTurnDistance = (a.maximumTurnDistance + b.maximumTurnDistance)/2;
    c.minimumTurnDistance = (a.minimumTurnDistance + b.minimumTurnDistance)/2;

    c.hate           = (a.hate           + b.hate          )/2;
    c.fear                = (a.fear                + b.fear          )/2;

    return c;
  }

  public void perturb ()
  {
    AnimalTraitSet x = new AnimalTraitSet ();
    x.initialize ();

    maximumViewAngle    = (2*maximumViewAngle    + x.maximumViewAngle   )/3;
    minimumViewAngle    = (2*minimumViewAngle    + x.minimumViewAngle   )/3;

    maximumViewDistance = (2*maximumViewDistance + x.maximumViewDistance)/3;

    maximumTurnAngle    = (2*maximumTurnAngle    + x.maximumTurnAngle   )/3;
    minimumTurnAngle    = (2*minimumTurnAngle    + x.minimumTurnAngle   )/3;

    maximumTurnDistance = (2*maximumTurnDistance + x.maximumTurnDistance)/3;
    minimumTurnDistance = (2*minimumTurnDistance + x.minimumTurnDistance)/3;

    hate           = (2*hate           + x.hate          )/3;
    fear                = (2*fear                + x.fear               )/3;
  }

    @Override
  public String toString ()
  {
    return "AnimalTraitSet (" + id + ")\n" +

      "\n  maximumViewAngle    = " + maximumViewAngle    +
      "\n  minimumViewAngle    = " + minimumViewAngle    +

      "\n  maximumViewDistance = " + maximumViewDistance +

      "\n  maximumTurnAngle    = " + maximumTurnAngle    +
      "\n  minimumTurnAngle    = " + minimumTurnAngle    +

      "\n  maximumTurnDistance = " + maximumTurnDistance +
      "\n  minimumTurnDistance = " + minimumTurnDistance +

      "\n  curiosity           = " + hate           +
      "\n  fear                = " + fear                +
      "\n  ";
  }
}
