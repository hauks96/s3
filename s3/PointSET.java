package s3;
/****************************************************************************
 *  Compilation:  javac PointSET.java
 *  Execution:    
 *  Dependencies:
 *  Author:
 *  Date:
 *
 *  Data structure for maintaining a set of 2-D points, 
 *    including rectangle and nearest-neighbor queries
 *
 *************************************************************************/

import edu.princeton.cs.algs4.*;

import java.awt.*;

public class PointSET {
    // construct an empty set of points
    private SET<Point2D> point_set;

    public PointSET() {
        this.point_set = new SET<Point2D>();
    }

    // is the set empty?
    public boolean isEmpty() {
        return this.point_set.isEmpty();
    }

    // number of points in the set
    public int size() {
        return this.point_set.size();
    }

    // add the point p to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (!contains(p)){
            this.point_set.add(p);
        }
    }

    // does the set contain the point p?
    public boolean contains(Point2D p) {
        return this.point_set.contains(p);
    }

    // draw all of the points to standard draw
    public void draw() {
        for (Point2D point: this.point_set){
            point.draw();
        }
    }

    // all points in the set that are inside the rectangle
    public Iterable<Point2D> range(RectHV rect) {
        /* NOTE: Not sure if this is a good solution. This method uses
                 O(N)+contains time in a Set that implements a balanced binary tree
                 (Maybe possible in log(N)+contains time?)*/

        // Iterating over all points in our set and checking if they are within the rectangle
        SET<Point2D> sub_set = new SET<Point2D>();
        for (Point2D point: this.point_set){
            if (rect.contains(point)){
                sub_set.add(point);
            }
        }
        return sub_set;
    }

    // a nearest neighbor in the set to p; null if set is empty
    public Point2D nearest(Point2D p) {
        /* NOTE: Not sure if this is a good solution. This method uses
                 O(N) time in a Set that implements a balanced binary tree
                 (Should be possible in log(N) time?)*/
        double min_dist = Double.POSITIVE_INFINITY;
        Point2D ret_point = null;
        for(Point2D point: this.point_set){
            double this_dist = p.distanceTo(point);
            if(this_dist<min_dist){
                min_dist = this_dist;
                ret_point = point;
            }
        }
        return ret_point;
    }

    public static void main(String[] args) {
        // Custom test
        //StdDraw.setCanvasSize(1000, 1000);
        PointSET test = new PointSET();
        In in = new In();
        int n = in.readInt();
        int k = in.readInt(); // Nearest neighbour points
        Point2D [] points = new Point2D[n];
        for(int i=0; i<n; i++){
            double x_coord = in.readDouble();
            double y_coord = in.readDouble();
            Point2D new_point = new Point2D(x_coord, y_coord);
            points[i]=new_point;
        }
        Stopwatch time = new Stopwatch();
        for (Point2D point: points){
            test.insert(point);
        }
        double total_time = time.elapsedTime();
        System.out.println("Total insert time for "+n+" points: " + total_time);
        System.out.println(test.size());

        Point2D [] nearest_points = new Point2D[k];
        for (int i=0; i<k; i++){
            double x_coord = in.readDouble();
            double y_coord = in.readDouble();
            Point2D new_point = new Point2D(x_coord, y_coord);
            nearest_points[i]=new_point;
        }
        Stopwatch time_nearest = new Stopwatch();
        for (Point2D point: nearest_points){
            test.nearest(point);
        }
        total_time = time_nearest.elapsedTime();
        System.out.println("Total insert time for "+n+" points: " + total_time);
        System.out.println(test.size());

        /*
        PlotXY plot = new PlotXY("x",  "y", "Point Plot");
        plot.graphTitle("Circle 10.txt");
        PointSET test = new PointSET();
        In in = new In();
        int n = in.readInt();
        Point2D [] draw_points = new Point2D[n];
        for(int i=0; i<n; i++){
            double x_coord = in.readDouble();
            double y_coord = in.readDouble();
            Point2D new_point = new Point2D(x_coord, y_coord);
            test.insert(new_point);
            draw_points[i]=new_point;
        }
        plot.setPoints2D(draw_points); // add points to graph before drawing
        plot.enumPoints(); //enumerate with P
        plot.draw(); // draw

        // Might have to edit this test_point values if you use another test file
        Point2D [] test_points = new Point2D[4];
        test_points[0] = new Point2D(0.2, 0.8);
        test_points[1] = new Point2D(0.0, 0.0);
        test_points[2] = new Point2D(0.7, 0.9);
        test_points[3] = new Point2D(0.4, 1.0);

        for (Point2D point: test_points){
            plot.drawGreenPoint(point.x(), point.y());
            Point2D nearest_point = test.nearest(point);
            if (nearest_point!=null) {
                System.out.println("Nearest point to "+point.toString()+": " +
                        nearest_point.toString());
                plot.drawRedPoint(nearest_point.x(), nearest_point.y());
            }
            else System.out.println("No point near " +point.toString());
        }

         */
    }
}
