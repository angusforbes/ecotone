/* GeomTrace.java (created on August 28, 2007, 1:35 PM) */
package ecotone;

import geometry.GeomTrace;
import java.util.LinkedList;
import java.util.List;
import javax.media.opengl.GL;
import javax.media.opengl.glu.GLU;
import javax.vecmath.Point3f;

/*
 * GeomTrace keeps a list of the history of points and draws a line between all of these.
 */
public class GeomTraceWrap extends GeomTrace
{
  float width = 4f;
  float height = 4f;

  /**
   * Creates a new GeomTrace anchored at a specified point with the default number of trace points.
   * @param p3f The achor point.
   */
  public GeomTraceWrap(Point3f p3f)
  {
    super(p3f);
  }

  /**
   * Creates a new GeomTrace anchored at a specified point with a maximum number of trace points.
   * @param p3f The achor point.
   * @param maxPoints The maximum number of points in the trace.
   */
  public GeomTraceWrap(Point3f p3f, int maxPoints)
  {
    super(p3f, maxPoints);
  }

  /**
   * Creates a new GeomTrace anchored at a specified point with a maximum number of trace points.
   * @param p3f The achor point.
   * @param maxPoints The maximum number of points in the trace.
   */
  public GeomTraceWrap(Point3f p3f, List<Point3f> trace, int maxPoints)
  {
    super(p3f, trace, maxPoints);
  }

  @Override
  public void draw(GL gl, GLU glu, float offset)
  {
    //System.out.println("in GeomTraceWrap() : trace size = " + trace.size());
    gl.glColor4f(r, g, b, a);

    gl.glLineWidth(this.lineWidth);

    gl.glBegin(gl.GL_LINE_STRIP);

    Point3f prev_p3f1 = new Point3f(this.anchor);
    Point3f p3f1 = new Point3f(this.anchor);
    //synchronized(trace)
    {
      for (int i = 0; i < trace.size(); i++)
      {
        p3f1 = trace.get(i);

        //System.out.println("currentPt = " + p3f1 + ", prevPt = " + prev_p3f1);

        //if (Math.abs(p3f1.y - prev_p3f1.y) > 2f)
        if (p3f1.y > height)
        {
          //System.out.println("ending...");
          //, ok draw to intersection with boundary we are leaving
          float full_y = p3f1.y - prev_p3f1.y;
          float y_to_intersection = height - prev_p3f1.y;
          float full_x = p3f1.x - prev_p3f1.x;
          float percent = y_to_intersection / full_y;
          float int_x = prev_p3f1.x + (full_x * percent);
          //System.out.println("adding last point : " + (new Point3f(int_x, 4.0f, 0f)));
          gl.glVertex3f(int_x, height, 0f);

          gl.glEnd();
          gl.glBegin(gl.GL_LINE_STRIP);
          //System.out.println("adding first point : " + (new Point3f(int_x, 0.0f, 0f)));
          gl.glVertex3f(int_x, 0.0f, 0f);

          //ok, a draw from intersection we have entered
          prev_p3f1 = p3f1;
          continue;
        }
        else if (p3f1.y < 0f)
        {
          float full_y = prev_p3f1.y - p3f1.y;
          float y_to_intersection = prev_p3f1.y - 0f;
          float full_x = p3f1.x - prev_p3f1.x;
          float percent = y_to_intersection / full_y;
          float int_x = prev_p3f1.x + (full_x * percent);
          //System.out.println("adding last point : " + (new Point3f(int_x, 4.0f, 0f)));
          gl.glVertex3f(int_x, 0.0f, 0f);

          gl.glEnd();

          gl.glBegin(gl.GL_LINE_STRIP);
          //System.out.println("adding first point : " + (new Point3f(int_x, 0.0f, 0f)));
          gl.glVertex3f(int_x, height, 0f);

          //ok, a draw from intersection we have entered
          prev_p3f1 = p3f1;
          continue;
        }

        if (p3f1.x > width)
        {
          //System.out.println("ending...");
          //, ok draw to intersection with boundary we are leaving
          float full_x = p3f1.x - prev_p3f1.x;
          float x_to_intersection = width - prev_p3f1.x;
          float full_y = p3f1.y - prev_p3f1.y;
          float percent = x_to_intersection / full_x;
          float int_y = prev_p3f1.y + (full_y * percent);
          //System.out.println("adding last point : " + (new Point3f(int_x, 4.0f, 0f)));
          gl.glVertex3f(width, int_y, 0f);

          gl.glEnd();
          gl.glBegin(gl.GL_LINE_STRIP);
          //System.out.println("adding first point : " + (new Point3f(int_x, 0.0f, 0f)));
          gl.glVertex3f(0f, int_y, 0f);

          //ok, a draw from intersection we have entered
          prev_p3f1 = p3f1;
          continue;
        }
        else if (p3f1.x < 0f)
        {
          float full_x = prev_p3f1.x - p3f1.x;
          float x_to_intersection = prev_p3f1.x - 0f;
          float full_y = p3f1.y - prev_p3f1.y;
          float percent = x_to_intersection / full_x;
          float int_y = prev_p3f1.y + (full_y * percent);
          //System.out.println("adding last point : " + (new Point3f(int_x, 4.0f, 0f)));
          gl.glVertex3f(0.0f, int_y, 0f);

          gl.glEnd();

          gl.glBegin(gl.GL_LINE_STRIP);
          //System.out.println("adding first point : " + (new Point3f(int_x, 0.0f, 0f)));
          gl.glVertex3f(height, int_y, 0f);

          //ok, a draw from intersection we have entered
          prev_p3f1 = p3f1;
          continue;
        }

        gl.glVertex3f(p3f1.x, p3f1.y, p3f1.z);

        prev_p3f1 = p3f1;

      
      }
      gl.glEnd();
    }
  //trace.add(new Point3f(0f - anchor.x, 0f - anchor.y, 0f - anchor.z));
    /*
  gl.glColor4f(r, g, b, a);
  gl.glPointSize(this.pointSize);

  gl.glBegin(gl.GL_POINTS);
  gl.glVertex3f(anchor.x, anchor.y , anchor.z);  //draws the point, it should be the point plus anchor
  gl.glEnd();
   */
  }
}
