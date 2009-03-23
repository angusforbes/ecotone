/* BehaviorTakeTurn.java ~ Nov 30, 2008 */

package ecotone;

import behaviors.BehaviorDiscrete.DiscreteBehaviorBuilder;
import behaviors.geom.discrete.BehaviorGeomDiscrete;
import geometry.Geom;
import java.util.Collections;
import java.util.List;
import utils.Utils;

/**
 * This Behavior simply schedules a turn to happen at certain times.
 * @author angus
 */
public class BehaviorTakeTurn extends BehaviorGeomDiscrete
{
  int turn = 1;
  int numUpdates = 0;
  public BehaviorTakeTurn(DiscreteBehaviorBuilder builder)
  {
    super(builder);
  }

  @Override
  public void updateGeom(Geom geom)
  {
    //System.out.print("in updateGeom... ");
    if (!(geom instanceof GeomEcotone))
    {
      throw new IllegalArgumentException("This behavior can only be attached to an Animal.");
    }
   // System.out.println("about to take turn! + numUpdates = " + numUpdates);
    for (int i = 0; i < numUpdates; i++)
    {
      ((GeomEcotone) geom).takeTurn(turn);
      turn++;
    }
  }

  //testing tick...
  @Override
  public void tick(long currentNano)
  {
   // System.out.println("tick... at " + currentNano);
    isActive = false;

    if (currentNano < startTime)
    {
      return;
    } //not ready yet

    if (isInterrupted == true && interruptNano <= currentNano)
    {
      this.isDone = true;
    //call disposals?
    }


    now = currentNano - startNano;

//    System.out.println("now in ms = " + Utils.nanosToMillis(now));
//    System.out.println("lastCheck in ms = " + Utils.nanosToMillis(lastCheck));
//    System.out.println("lengthNano in ms = " + Utils.nanosToMillis(lengthNano));

    numUpdates = 0;
    long sub = lengthNano;
    //System.out.println("A sub in ms = " + Utils.nanosToMillis(sub));

    //System.out.println("(now - lastCheck) in ms = " + Utils.nanosToMillis(now - lastCheck));

    while (now > sub)
    {
      //System.out.println("sub in ms = " + Utils.nanosToMillis(sub));
      numUpdates += lengthNanos.size() - 1;

      //lengthNanos = loopLengthNanos(lengthNanos, lengthNano, 1, waitTime);
      startNano += lengthNano;
      sub += lengthNano;
    }

    //System.out.println("so now should be... " + Utils.nanosToMillis(currentNano - startNano));

    if (numUpdates > 0)
    {
      isActive = true;
      numUpdates = 1;
    }

    lastCheck = now;

  }
  /*

    //System.out.println("" + isGeomActive + " : now(" + Utils.nanosToMillis(now) + ") ... len(" + Utils.nanosToMillis(lengthNano) + ")");

    //System.out.println("now = " + now + ", lengthNano = " + lengthNano);
    //System.out.println("lengthsNanos are... " + Arrays.toString(lengthNanos.toArray() ));
    int nextIndex = getIndexAtNano2(lengthNanos, now);

    //int numUpdates = getNumUpdates(lengthNanos, lastCheck, now);

    System.out.println("numUpdates = " + numUpdates);




    //if we are at a different step in time, then we need to update the Geom
    //Otherwise, it is the same as before and nothing needs to be done...
    if (nextIndex != curIndex)
    {
      System.out.println("new... nextIndex = " + nextIndex + ", currentIndex = " + curIndex);

      isActive = true;
      curIndex = nextIndex;
    }
    else
    {
      System.out.println("nothing new... nextIndex = " + nextIndex + ", currentIndex = " + curIndex);
      //System.out.println("nothing new...");
      return; //right?
    }

    if (curIndex < 0)
    {
      System.out.println("why are we here???");
      lastCheck = now;
      //before anything...
      //isGeomActive = false;
      isActive = true;
      //should toggle to whatever startOn is equal to
if (curIndex < 0)
    {
      curIndex = -curIndex - 2; //prev
    }
      //System.out.println("before anything... index = " + index);
      //return;
    }

    //remove if this is the last one (and loopBehavior == ONCE)
    if (curIndex == lengthNanos.size() - 1)
    {
      switch (loopBehavior)
      {
        case ONCE:
          //System.out.println("onceing... ");
          this.isDone = true;
          lastCheck = now;
          break;
        case LOOP:
          System.out.println("looping...");
          //startNano += lengthNano; //add length of behavior to starting time
          //so then we also need to push out lastCheck into the past
          //lastCheck = now - lengthNano;
          lastCheck = now;
          lengthNanos = loopLengthNanos(lengthNanos, lengthNano, 1, waitTime);

          curIndex = getIndexAtNano(lengthNanos, lastCheck);
          break;
        case REVERSE:
          //System.out.println("reversing...");
          lastCheck = now;
          lengthNanos = reverseLengthNanos(lengthNanos, lengthNano, 1, waitTime);
          if (lengthNanos.size() % 2 != 0)
          {
            startOn = !startOn;
          }

          curIndex = getIndexAtNano(lengthNanos, lastCheck);
          break;
      }
    }
    else
    {
      lastCheck = now;
    }

    //System.out.println("...end of tick... curIndex = " + curIndex);
  }
   */

  int getNumUpdates(List<Long> lengthNanos, long lastCheck, long now)
  {
    if (now > this.lengthNano)
    {
      return 1;
    }

    return 0;
  }

  final protected int getIndexAtNano2(List<Long> lengthNanos, long now)
  {
    int index = Collections.binarySearch(lengthNanos, now);

    System.out.print("\nindex was " + index);
    if (index < 0)
    {
      index = -index - 2; //prev
    }
    System.out.println(" ...index is " + index);

    return index;
  }

}

