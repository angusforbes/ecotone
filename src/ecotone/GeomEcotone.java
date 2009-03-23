/* GeomEcotone.java ~ Nov 19, 2008 */
package ecotone;

import com.bric.geom.CAGArea;
import com.bric.geom.CAGShape;
import ecotone.fauna.Animal;
import ecotone.fauna.CarnivoreAngus;
import ecotone.fauna.Herbivore;
import ecotone.flora.Plant;
import ecotone.nutrients.Nutrient;
import geometry.Colorf;
import geometry.GeomGrid;
import geometry.GeomTrace;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.vecmath.Point3f;
import utils.Utils;

/**
 *
 * @author angus
 */
public class GeomEcotone extends GeomGrid
{
  //put these in a separate properties file at some point...
  public static long TIME_BETWEEN_TURNS = 100L;
  public static final long ANIMAL_ANIMATION = 7000L;
  public static boolean CARNIVORES_CAN_EAT_PLANTS = true;
  private static final int TRACE_LENGTH = 175; //max number of points in parent trace
  private static final int MAX_PLANTS = 1000;
  private static final int NUM_ANIMALS = 3; //num animals we start with
  private static final int NUM_PLANTS = 5; //num plants we start with

  Sun sun;
  Wind wind;
  List<GeomEcotoneSquare> squares = new ArrayList<GeomEcotoneSquare>();
  DrawModeNutrientEnum drawMode = DrawModeNutrientEnum.DRAW_ALL_BLEND;
  //DrawModeNutrientEnum drawMode = DrawModeNutrientEnum.DRAW_ALL_BAR_CHART;
  //public List<Animal> animals = Collections.synchronizedList(new ArrayList<Animal>());
  //public List<Plant> plants = Collections.synchronizedList(new ArrayList<Plant>());
  public List<Animal> animals = new CopyOnWriteArrayList<Animal>();
  public List<Plant> plants = new CopyOnWriteArrayList<Plant>();

  public GeomEcotone(int numCols, int numRows, Point3f p3f, float w, float h)
  {
    super(numCols, numRows, p3f, w, h);

    initializeSquares();
    initializeSun();
    initializeWind();

    initializePlants();
    initializeAnimals();

    //isVisible = false;
    
    Animal.staticEcotone = this;
  }

  private void initializeAnimals()
  {
    Animal animal;

    
//    parent = new CarnivoreAngus(this, new Point3f(Utils.randomFloat(1f, 2f), Utils.randomFloat(.3f, .3f), 0f), 3);
//      addAnimal(parent);
//
    /*
    parent = new CarnivoreAngus(this, new Point3f(Utils.randomFloat(2f, 2f), Utils.randomFloat(.3f, .3f), 0f), 1);
      addAnimal(parent);

    parent = new Herbivore(this, new Point3f(Utils.randomFloat(2.1f, 2.1f), Utils.randomFloat(1f, 1f), 0f), 2);
      addAnimal(parent);
    parent = new Herbivore(this, new Point3f(Utils.randomFloat(1f, 4f), Utils.randomFloat(1f, 1f), 0f), 4);
      addAnimal(parent);
      */
      
    //hi
    for (int i = 0; i < NUM_ANIMALS; i++)
    {
      if (Utils.random() > 1f)
      {
        animal = new CarnivoreAngus(this, new Point3f(Utils.randomFloat(0f, w), Utils.randomFloat(0f, h), 0f), i);
      }
      else
      {
        animal = new Herbivore(this, new Point3f(Utils.randomFloat(0f, w), Utils.randomFloat(0f, h), 0f), i);
      }
      addAnimal(animal);
    }
  }

  public void addAnimal(Animal animal)
  {
    addGeom(animal);
    this.animals.add(animal);

    if (TRACE_LENGTH > 0)
    {
       GeomTrace trace = new GeomTraceWrap(new Point3f(), TRACE_LENGTH);
    
       addGeom(trace);
       trace.isVisible = Main.traceIsVisible;
       animal.addTrace(trace);
       trace.setColor(new Colorf(animal.getColor()));
    }
  }

  public void addPlant(Plant plant)
  {
    if (plants.size() < MAX_PLANTS)
    {
    addGeom(plant, true);
    this.plants.add(plant);
    }
  }

  private void initializePlants()
  {
    for (int i = 0; i < NUM_PLANTS; i++)
    {
      Plant plant = new Plant(this, new Point3f(Utils.randomFloat(0f, w), Utils.randomFloat(0f, h), 0f), i);
      plant.size = .1f * Utils.randomInt(1, 10);
      plant.randomize ();
      addPlant(plant);
    }
  }

  private void initializeWind()
  {
    this.wind = new Wind(this, Utils.randomFloat(0f, 360f), Utils.randomFloat(.1f, .5f));
  }

  private void initializeSun()
  {
    //put sun on left side of grid in the middle. sun moves to the right one square per turn
    this.sun = new Sun(this, 0, numRows / 2, .3f, 0f);

  //a less ordinary sun
  //this.sun = new Sun(this, 0, numRows / 2, -.13f, -.1f);
  }

  public int getIndexAt(int col, int row)
  {
    if (col >= numCols || row >= numRows)
    {
      throw new IllegalArgumentException(
        "invalid column or row input (col/row input = " + col + "/" + row +
        ", numCols/numRows = " + numCols + "/" + numRows + ")");
    }

    return (numRows * row) + col;
  }

  public GeomEcotoneSquare getSquareAt(int col, int row)
  {
    if (col >= numCols || row >= numRows)
    {
      return null;

//      throw new IllegalArgumentException(
//        "invalid column or row input (col/row input = " + col + "/" + row +
//        ", numCols/numRows = " + numCols + "/" + numRows + ")");
    }

    return squares.get((numCols * row) + col);
  }

  public GeomEcotoneSquare getWrappedSquareAt(int col, int row)
  {
    if (col < 0)
    {
      col = (col % numCols) + numCols;
    }
    else if (col >= numCols)
    {
      col = col % numCols;
    }

    if (row < 0)
    {
      row = (row % numRows) + numRows;
    }
    else if (row >= numRows)
    {
      row = row % numRows;
    }

    return squares.get((numCols * row) + col);
  }

  private List<GeomEcotoneSquare> initializeSquares()
  {
    for (int row = 0; row < numRows; row++)
    {
      for (int col = 0; col < numCols; col++)
      {
        squares.add(initializeSquare(col, row));
      }
    }

    for (int row = 0; row < numRows; row++)
    {
      for (int col = 0; col < numCols; col++)
      {
        GeomEcotoneSquare ges = getSquareAt (col, row);
        ges.addNeighbor (Direction.NORTH,      getWrappedSquareAt (row-1, col  ));
        ges.addNeighbor (Direction.SOUTH,      getWrappedSquareAt (row+1, col  ));
        ges.addNeighbor (Direction.EAST,       getWrappedSquareAt (row  , col+1));
        ges.addNeighbor (Direction.WEST,       getWrappedSquareAt (row  , col-1));
        ges.addNeighbor (Direction.NORTH_WEST, getWrappedSquareAt (row-1, col-1));
        ges.addNeighbor (Direction.NORTH_EAST, getWrappedSquareAt (row-1, col+1));
        ges.addNeighbor (Direction.SOUTH_WEST, getWrappedSquareAt (row+1, col-1));
        ges.addNeighbor (Direction.SOUTH_EAST, getWrappedSquareAt (row+1, col+1));
      }
    }

    return squares;
  }

  private GeomEcotoneSquare initializeSquare(int col, int row)
  {
    Point3f cellPt = getGridPoint(col, row);

    GeomEcotoneSquare ges = new GeomEcotoneSquare(this, new Point3f(cellPt.x, cellPt.y, 0f), xsize, ysize, col, row);
    ges.setColor(0f, 0f, 0f, 0f); //init to no color
    //ges.addGeom(new GeomTextBuilder(col +"/" + row).anchor(new Point3f()).
    //  exactBounds(true).constrainByHeight(.3f).build());
    addGeom(ges);
    return ges;
  }

  public int wrapSquare(int idx, int numIdx)
  {
    if (idx >= numIdx)
    {
      idx -= numIdx;
    }
    else if (idx < 0)
    {
      idx += numIdx;
    }
    return idx;
  }

  public GeomEcotoneSquare getSquareContainingPoint(Point3f p3f)
  {
    //System.out.println("testing point " + p3f);
    int origCol = getColContaining(p3f);

    if (origCol >= numCols)
    {
      origCol -= numCols;
      p3f.x -= this.w;
    }
    else if (origCol < 0)
    {
      origCol += numCols;
      p3f.x += this.w;
    }

    int closestCol = wrapSquare(origCol, numCols);


    int origRow = getRowContaining(p3f);
    if (origRow >= numRows)
    {
      origRow -= numRows;
      p3f.y -= this.h;
    }
    else if (origRow < 0)
    {
      origRow += numRows;
      p3f.y += this.h;
    }

    int closestRow = wrapSquare(origRow, numRows);

    //System.out.println("closestRow/Row = " + closestRow + "/" + closestRow);
    return getSquareAt(closestCol, closestRow);
  }

  /**
   * This code uses the BasicShape library, which should be patched within a few days.
   * For now it is using the *slow* java2D.Area fallback (via CAGArea).
   * @param testRect
   * @param gridRect
   * @return
   */
  private float getIntersectionPercent(Shape testRect, Shape gridRect)
  {
    //System.out.println("testing rect " + testRect);
    //System.out.println("against grid " + gridRect);
    float origArea = (float) (testRect.getBounds2D().getWidth() * testRect.getBounds2D().getHeight());

    CAGShape bs1 = new CAGArea(testRect);
    CAGShape bs2 = new CAGArea(gridRect);

    if (bs1.intersects(bs2))
    {
      bs1.intersect(bs2);
      Rectangle2D bounds = bs1.getBounds2D();
      float intersectArea = (float) (bounds.getWidth() * bounds.getHeight());
      //System.out.println("intersectArea = " + intersectArea);
      //System.out.println("originalArea = " + origArea);
      return intersectArea / origArea;
    }

    return 0f;
  }

  private void updateWind(Rectangle2D testRect, float[] windChange, float amt, int col, int row)
  {
    //System.out.println("col/row = " + col + "/" + row + ", testRect = " + testRect);
    float percent;
    boolean newRect = false;
    float newX = (float) testRect.getX();
    float newY = (float) testRect.getY();

    if (col >= numCols)
    {
      //System.out.println("col was " + col);
      newRect = true;
      newX -= (this.w);
      col -= (numCols);
    //System.out.println("col now is " + col);
    //System.exit(1);
    }
    else if (col < 0)
    {
      //System.out.println("col was " + col);
      newRect = true;
      //newX = (float) (0f + testRect.getX() + ((col-1) * xsize));
      newX += (this.w);
      col += (numCols);
    //System.out.println("col now is " + col);
    }

    if (row >= numRows)
    {
      newRect = true;
      newY -= this.h;
      row -= numRows;
    }
    else if (row < 0)
    {
      newRect = true;
      newY += this.h;
      row += numRows;
    }

    if (newRect == true)
    {
      Rectangle2D.Float wrapRect = new Rectangle2D.Float(newX, newY, xsize, xsize);
      //System.out.println("wrapRect = " + wrapRect);
      //System.out.println("wrapSqar = " + getSquareAt(col, row).makeRectangleFromGeom());
      percent = getIntersectionPercent(wrapRect, getSquareAt(col, row).makeRectangle2DFromRect());
        //makeRectangleFromGeom());
    //System.out.println("percent = " + percent);
    }
    else
    {
      percent = getIntersectionPercent(testRect, getSquareAt(col, row).makeRectangle2DFromRect());
      //makeRectangleFromGeom());
    //System.out.println("percent = " + percent);
    }
    windChange[getIndexAt(col, row)] += percent * amt;
  }

  //TODO: make wind bounce against edges, OR wrap around-- doing wrap around now since it's simpler
  //but I like the idea of creating "drifts" of elements/plants
  public void moveNutrientsWithWind(float[] windChange, GeomEcotoneSquare square, Nutrient nut)
  {
    square.windFunnel += square.windFunnelRate;
    //System.out.println(square.col + "/" + square.row);
    //System.out.println("\nin moveNutrientsWithWind : square = " + square.col + "/" + square.row);
    float pow = wind.power * xsize * nut.weight;
    pow *= square.windResistance;

    float ang = wind.direction + square.windFunnel;
    float amt = square.soil.nutrients.get(nut);
    //System.out.println("\n" + square.printColAndRow() + " pow/ang/amt: "+pow +"/" +ang +"/" + amt );

    square.windVectorGeom.updateVector(ang, (pow * .5f));
    //square.windVectorGeom.updateVector(270f, 1f);
    //System.out.println(" vec lines = " + square.windVectorGeom);

    ang = (float) Math.toRadians(ang);


    //float ang = (float) Math.toRadians(wind.direction);
    //System.out.println("amount uARBON = " + amt);
    //System.out.println("xsize = " + xsize);
    Point3f curPt = getCellCenter(square.col, square.row);
    //System.out.println("curPt = " + curPt);
    Point3f newPt = new Point3f(curPt.x + (float) Math.cos(ang) * pow, curPt.y + (float) Math.sin(ang) * pow, 0f);
    //System.out.println("newPt = " + newPt);

    if (newPt.x > this.w)
    {
      newPt.x -= this.w;
    //System.out.println("so now newPt.x = " + newPt.x);
    }
    else if (newPt.x < 0f)
    {
      //newPt.x = getColCenter(numCols - 1);
      newPt.x += this.w; //getColCenter(numCols - 1);
    }

    if (newPt.y > this.h)
    {
      newPt.y -= this.h;
    //newPt.y = getRowCenter(0);
    }
    else if (newPt.y < 0f)
    {
      newPt.y += this.h;

    //newPt.y = getRowCenter(numRows - 1);
    }
//      GeomEcotoneSquare oldSquare = getSquareContainingPoint(curPt);
    //     System.out.println("oldSquare col/row = " + oldSquare.col + "/" + oldSquare.row);

    GeomEcotoneSquare newSquare = getSquareContainingPoint(newPt);
    //System.out.println("newPt " + newPt + ", newSquare col/row = " + newSquare.col + "/" + newSquare.row);


    float percent = 0f;
    int ucol = -1;
    int urow = -1;

    Rectangle2D.Float testRect = new Rectangle2D.Float(newPt.x - xsize / 2f, newPt.y - xsize / 2f, xsize, xsize);
    //System.out.println("testRect = "  +testRect);

    //CENTER
//    ucol = newSquare.col;
//    urow = newSquare.row;
//
//    percent = getIntersectionPercent(testRect, getSquareAt(ucol, urow).makeRectangleFromGeom());
//    windChange[getIndexAt(ucol, urow)] += percent * amt;

    //System.out.println("C");
    updateWind(testRect, windChange, amt, newSquare.col, newSquare.row);

    //System.out.println("N");
    updateWind(testRect, windChange, amt, newSquare.col, newSquare.row + 1);
    //System.out.println("S");
    updateWind(testRect, windChange, amt, newSquare.col, newSquare.row - 1);
    //System.out.println("E");
    updateWind(testRect, windChange, amt, newSquare.col + 1, newSquare.row);
    //System.out.println("NE");
    updateWind(testRect, windChange, amt, newSquare.col + 1, newSquare.row + 1);
    //System.out.println("SE");
    updateWind(testRect, windChange, amt, newSquare.col + 1, newSquare.row - 1);
    //System.out.println("W");
    updateWind(testRect, windChange, amt, newSquare.col - 1, newSquare.row);
    //System.out.println("NW");
    updateWind(testRect, windChange, amt, newSquare.col - 1, newSquare.row + 1);
    //System.out.println("SW");
    updateWind(testRect, windChange, amt, newSquare.col - 1, newSquare.row - 1);

  /*
  if (newSquare.col + 1 < numCols)
  {
  ucol = newSquare.col + 1;
  urow = newSquare.row;
  percent = getIntersectionPercent(testRect, getSquareAt(ucol, urow).makeRectangleFromGeom());
  windChange[getIndexAt(ucol, urow)] += percent * amt;
  }

  //W
  if (newSquare.col - 1 >= 0)
  {
  ucol = newSquare.col - 1;
  urow = newSquare.row;
  percent = getIntersectionPercent(testRect, getSquareAt(ucol, urow).makeRectangleFromGeom());
  windChange[getIndexAt(ucol, urow)] += percent * amt;
  }

  //N
  if (newSquare.row + 1 < numRows)
  {
  ucol = newSquare.col;
  urow = newSquare.row + 1;
  percent = getIntersectionPercent(testRect, getSquareAt(ucol, urow).makeRectangleFromGeom());
  windChange[getIndexAt(ucol, urow)] += percent * amt;
  }

  //NE
  if (newSquare.col + 1 < numCols && newSquare.row + 1 < numRows)
  {
  ucol = newSquare.col + 1;
  urow = newSquare.row + 1;
  percent = getIntersectionPercent(testRect, getSquareAt(ucol, urow).makeRectangleFromGeom());
  windChange[getIndexAt(ucol, urow)] += percent * amt;
  }


  //SE
  if (newSquare.col + 1 < numCols && newSquare.row - 1 >= 0)
  {
  ucol = newSquare.col + 1;
  urow = newSquare.row - 1;
  percent = getIntersectionPercent(testRect, getSquareAt(ucol, urow).makeRectangleFromGeom());
  windChange[getIndexAt(ucol, urow)] += percent * amt;
  }

  //S
  if (newSquare.row - 1 >= 0)
  {
  ucol = newSquare.col;
  urow = newSquare.row - 1;
  percent = getIntersectionPercent(testRect, getSquareAt(ucol, urow).makeRectangleFromGeom());
  windChange[getIndexAt(ucol, urow)] += percent * amt;
  }

  //SW
  if (newSquare.col - 1 >= 0 && newSquare.row - 1 >= 0)
  {
  ucol = newSquare.col - 1;
  urow = newSquare.row - 1;
  percent = getIntersectionPercent(testRect, getSquareAt(ucol, urow).makeRectangleFromGeom());
  windChange[getIndexAt(ucol, urow)] += percent * amt;
  }

  //NW
  if (newSquare.col - 1 >= 0 && newSquare.row + 1 < numRows)
  {
  ucol = newSquare.col - 1;
  urow = newSquare.row + 1;
  percent = getIntersectionPercent(testRect, getSquareAt(ucol, urow).makeRectangleFromGeom());
  windChange[getIndexAt(ucol, urow)] += percent * amt;
  }
   */
  }

  public float getTotalAmountOfNutrient(Nutrient nutrient)
  {
    float amt = 0f;
    for (GeomEcotoneSquare square : squares)
    {
      amt += square.soil.nutrients.get(nutrient);
    }
    return amt;
  }

  //maybe put this in a behavior intermittent so that we can time it...
  public void takeTurn(int turn)
  {
    if (sun != null)
    {
      sun.move();
    }

    if (wind != null)
    {
    //the wind only changes occasionally
    //if (Utils.random() < .5f)
    {
      wind.changeDirection();
    }
    //if (Utils.random() < .5f)
    {
      wind.changePower();
    }

    // wind.power = 1f;

    //wind.direction = 0f;

    /*

    if (Utils.random() < .5)
    {
    wind.direction = 90f + 45f;
    wind.direction = 90f;
    }
    else
    {
    wind.direction = 270f + 45f;
    wind.direction = 270f;
    }
     */
    //wind.power = Utils.randomFloat(1f, 2.0f);
    //wind.power = (float) Math.sqrt(2);
    }

    //System.out.println("current wind = " + wind);
    float[] windChange = new float[squares.size()];
    float[] windChange2 = new float[squares.size()];
    float[] windChange3 = new float[squares.size()];

    for (GeomEcotoneSquare square : squares)
    {
      if (sun != null)
      {
      //based on sun/clouds, each square receives or loses nutrients
      square.distanceToSun(sun);

      if (square.distanceToSun < sun.power)
      {
        square.soil.nutrientIncrement(Nutrient.OXYGEN, (sun.power - square.distanceToSun) / (sun.power * 20f));
      }
      else
      {
        square.soil.nutrientDecrement(Nutrient.OXYGEN, .1f);
      }
      }

      if (wind != null)
      {
      if (square.soil.nutrients.get(Nutrient.CARBON) > 0f)
      {
        moveNutrientsWithWind(windChange, square, Nutrient.CARBON);
      }
      if (square.soil.nutrients.get(Nutrient.OXYGEN) > 0f)
      {
        //moveNutrientsWithWind(windChange2, square, Nutrient.OXYGEN);
      }
      if (square.soil.nutrients.get(Nutrient.NITROGEN) > 0f)
      {
        moveNutrientsWithWind(windChange3, square, Nutrient.NITROGEN);
      }
      }
    }

    if (wind != null)
    {
    for (int i = 0; i < squares.size(); i++)
    {
      GeomEcotoneSquare square = (GeomEcotoneSquare) squares.get(i);

      square.soil.nutrientAmount(Nutrient.CARBON, windChange[i]);
      //square.soil.nutrientAmount(Nutrient.OXYGEN, windChange2[i]);
      square.soil.nutrientAmount(Nutrient.NITROGEN, windChange3[i]);
    }
    }
    
  //for all squares...
  //redistributeNutrients(); //i.e. from climate

  //plants transmute / eat / redistribute
  //animals transmute / eat / redistribute

  //have any plants died?
  //have any animals died?
  //redistribute from death...

  //for all plants
  //decide what to do...

  //for all animals
  //decide what to do...

  
    for (Plant plant : plants)
    {
      plant.takeTurn(turn);
    }
   

    for (Animal animal : animals)
    {
      animal.takeTurn(turn);

      /*
      if (Utils.random() > 0f)
      {
        parent.move(Utils.randomFloat(1f, 15f), Utils.randomFloat(0f, 0f));
      }
      else
      {
        parent.move(0f, 0f);
      }
      */
    }


    //remove dead and eaten animals...
    for (int i = animals.size() - 1; i >= 0; i--)
    {
      Animal animal = animals.get(i);
      if (animal.isDead == true)
      {
        animals.remove(animal);
        animal.trace.isDone = true;
        animal.isDone = true;

      }
    }
    //remove dead and eaten plants...
    for (int i = plants.size() - 1; i >= 0; i--)
    {
      Plant plant = plants.get(i);
      if (plant.isDead == true)
      {
        plants.remove(plant);
        plant.isDone = true;
      }
    }

    //clone plants
    List<Plant> newPlants = Collections.synchronizedList(new ArrayList<Plant>() );
    for (Plant plant : plants)
    {
      if (plant.isCloning == true)
      {
        plant.size = .7f;

        for (int i = 0; i < 3; i++)
        {
          Point3f npt1 = new Point3f(Utils.randomFloat(-.5f, .5f), Utils.randomFloat(-.5f, .5f), 0f);
          npt1.add(plant.anchor);
          Plant plant1 = new Plant(this, npt1, 101);
          plant1.size = .3f;

          plant1.randomize ();

          fitAnchor(plant1.anchor);

          newPlants.add(plant1);
        }

        plant.isCloning = false;
      }
    }

    for (Plant plant : newPlants)
    {
      addPlant(plant);
    }


    //clone animals
    List<Animal> newAnimals = Collections.synchronizedList(new ArrayList<Animal>());
    for (Animal parent : animals)
    {
      if (parent.isCloning == true)
      {
        parent.size = .7f;

        for (int i = 0; i < 3; i++)
        {
          Point3f npt1 = new Point3f(Utils.randomFloat(-.5f, .5f), Utils.randomFloat(-.5f, .5f), 0f);
          npt1.add(parent.anchor);

          Animal child = null;
          //Animal child = new CarnivoreAngus(this, npt1, i);
          if (parent instanceof CarnivoreAngus)
          {
            child = new CarnivoreAngus (this, npt1, i);
            child.setColor(1f,0f,0f,1f);
          }
          else
          {
              child = new Herbivore(this, npt1, i);
              child.setColor(0f,1f,0f,1f);
          }

          child.averageTraitsWith(parent);

          //Animal child = new Animal(this, npt1, 101);
          child.size = .3f;

          fitAnchor(child.anchor);

          newAnimals.add(child);
        }

        parent.isCloning = false;
      }
    }

    for (Animal animal : newAnimals)
    {
      addAnimal(animal);
    }

  }



  public void fitAnchor(Point3f p3f)
  {
    if (p3f.x <= .001f)
    {
      p3f.x = .001f;
    }
    else if (p3f.x >= this.w - .001f)
    {
      p3f.x = this.w - .001f;
    }

    if (p3f.y <= .001f)
    {
      p3f.y = .001f;
    }
    else if (p3f.y >= this.h - .001f)
    {
      p3f.y = this.h - .001f;
    }

  }
  /*
  turn

  A Turn does the following:


  1. move the sun
  2. change wind
  3. add nutrients to soil from Sun

  plants eat / transmute / die
  animals eat / transmute / die

  {store state and simultaneously do...}
  plants decide where to move / reproduce
  animals decie where to move / reproduce (ie, think)

  move and reproduce

   *
   */
}
