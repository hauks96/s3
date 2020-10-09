package s3;
/*************************************************************************
 *************************************************************************/

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;

import edu.princeton.cs.algs4.*;

class Node {
    public boolean is_x = true;
    public Node parent;
    public Node left_child = null;
    public Node right_child = null;
    public Point2D point;
    public RectHV rect_left = null;
    public RectHV rect_right = null;

    public Node(boolean isY, Point2D point, Node parent){
        if(isY) this.is_x = false;
        this.point = point;
        this.parent = parent;
    }

    public int compareTo(Point2D that) {
        /*What should I do if a point has the same x/y-coordinate as the point in a node when
        inserting / searching in a 2d-tree? Go to the right subtree as specified on the assignment
        page under Search and insert.*/
        if (this.is_x) {
            if (this.point.x() > that.x()) return 1;
            else return -1;
        } else {
            if (this.point.y() > that.y()) return 1;
            else return -1;
        }
    }
}

public class KdTree {
    // construct an empty set of points
    public Node root;
    public int size;
    public Point2D max;
    public Point2D min;

    public KdTree() {
        this.size = 0;
        this.root = null;
        this.max = null;
        this.min = null;
    }

    // is the set empty?
    public boolean isEmpty() {
        return this.size == 0;
    }

    // number of points in the set
    public int size() {
        return this.size;
    }

    // add the point p to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if(this.root == null){
            this.root = new Node(false, p, null);
            this.size +=1;
            this.max = p;
            this.min = p;
            createRootRect(1,1,0,0);
            return;
        }
        recursiveInsert(p);
    };

    // Creating the two rectangles for the root
    private void createRootRect(double max_x, double max_y, double min_x, double min_y){
        /* NOTE: This method is specific to the root only. This method is creating the first two squares, that the
         root point divides the plane into. We need pre determined values (0 and 1 in our case). If we would get a
         dataset up front with the constructor we would be able to determine what the min and max values are of the
         first square. However we cannot do that when the data is streamed in through insert statements.*/
        this.root.rect_left = new RectHV(min_x, min_y, this.root.point.x(), max_y);
        this.root.rect_right = new RectHV(this.root.point.x(), min_y, max_x, max_y);
    }

    // Insert using the recursive search
    private void recursiveInsert(Point2D point){
        Node node = recursiveSearch(this.root, point);
        // Node with this point already exist so we don't do anything and return
        if (node.point.compareTo(point)==0) return;

        // New point is larger compared to current node, so we insert on the right
        if (node.compareTo(point)<0){
            setPointRight(node, point);
            this.size +=1;
            checkMax(point);
            checkMin(point);
            return;
        }
        // New point is smaller compared to current node, so we insert on the left
        if (node.compareTo(point)>0){
            setPointLeft(node, point);
            this.size +=1;
            checkMax(point);
            checkMin(point);
        }
    }

    // Setting a node to the left of current node
    private void setPointLeft(Node node, Point2D point){
        /* NOTE: This method assumes that the input node is a leaf that you want to add the point to.
         The compareTo method compares the x or y of the point to the x/y of the point in the Node
         depending on the is_x variable in the node*/

        // If current node is an X node we want the new node to be a Y node
        if(node.is_x){
            node.left_child = new Node(true, point, node);
            double max_x = node.point.x();
            double min_x = node.rect_left.xmin();

            node = node.left_child;
            node.rect_right = new RectHV(min_x,node.point.y(),
                                         max_x, node.parent.rect_left.ymax());

            node.rect_left = new RectHV(min_x, node.parent.rect_left.ymin(),
                                        max_x, node.point.y());

        }else{
            double max_y = node.point.y();
            double min_y = node.rect_left.ymin();
            node.left_child = new Node(false, point, node);
            node = node.left_child;
            node.rect_right = new RectHV(node.point.x(), min_y,node.parent.rect_left.xmax(), max_y);

            node.rect_left = new RectHV(node.parent.rect_left.xmin(), min_y,node.point.x(), max_y);
        }
    }

    // Setting a node to the right of current node
    private void setPointRight(Node node, Point2D point){
        /* NOTE: This method assumes that the input node is a leaf that you want to add the point to.
                 The compareTo method compares the x or y of the point to the x/y of the point in the Node
                 depending on the is_x variable in the node*/

        // If current node is an X node we want the new node to be a Y node
        if(node.is_x){
            double max_x = node.rect_right.xmax();
            double min_x = node.point.x();
            node.right_child = new Node(true, point, node);
            node = node.right_child;
            node.rect_left = new RectHV(min_x, node.parent.rect_right.ymin(), max_x, node.point.y());

            node.rect_right = new RectHV(min_x, node.point.y(), max_x, node.parent.rect_right.ymax());
        }

        else{
            double max_y = node.rect_right.ymax();
            double min_y = node.point.y();
            node.right_child = new Node(false, point, node);
            node = node.right_child;

            node.rect_left = new RectHV(node.parent.rect_right.xmin(), min_y, node.point.x(), max_y);

            node.rect_right = new RectHV(node.point.x(), min_y, node.parent.rect_right.xmax(), max_y);
        }
    }

    // Checking if new point is greater than current max
    private void checkMax(Point2D point){
        if (point.compareTo(this.max)>0){
            this.max = point;
        }
    }

    // Checking if new point is lesser than current min
    private void checkMin(Point2D point){
        if (point.compareTo(this.max)<0){
            this.min = point;
        }
    }

    // does the set contain the point p?
    public boolean contains(Point2D p) {
        Node node = recursiveSearch(this.root, p);
        return node.point.compareTo(p) == 0;
    }

    // Typical binary search with a small twist (compareTo in Node is special)
    private Node recursiveSearch(Node current_node, Point2D point){
        /* NOTE: This method returns a node that has point = arg point, or a leaf at which
                argument point would be inserted on either the left or the right. */

        if (current_node.point.compareTo(point)==0){
            return current_node;
        }

        // If the current point compares as larger than current node
        if (current_node.compareTo(point)<0){
            // If we reached a leaf we know the point doesn't exist
            if (current_node.right_child == null){
                return current_node;
            }
            current_node = current_node.right_child;
        }
        // If the current point compares as smaller than current node
        else { //(current_node.compareTo(point)>0)
            // If we reached a leaf we know the point doesn't exist
            if (current_node.left_child == null){
                return current_node;
            }
            // Continue recursion from left child
            current_node = current_node.left_child;
        }
        return recursiveSearch(current_node, point);
    }

    // draw all of the points to standard draw
    public void draw() {
        StdDraw.setYscale(-0.2,1.2);
        StdDraw.setXscale(-0.2, 1.2);
        RectHV square = new RectHV(0, 0, 1, 1);
        square.draw();
        drawRecursive(this.root);
    }

    // Draw points recursively
    private void drawRecursive(Node current_node){
        if (current_node==null) {
            return;
        }
        StdDraw.setPenRadius(0.01);
        StdDraw.setPenColor(Color.black);
        StdDraw.point(current_node.point.x(), current_node.point.y());
        StdDraw.setPenRadius();
        if (current_node.is_x){
            StdDraw.setPenColor(Color.red);
            double max_y = current_node.rect_right.ymax();
            double min_y = current_node.rect_right.ymin();
            double x = current_node.point.x();
            StdDraw.line(x, min_y, x, max_y);

        }
        else {
            StdDraw.setPenColor(Color.blue);
            double max_x = current_node.rect_right.xmax();
            double min_x = current_node.rect_right.xmin();
            double y = current_node.point.y();
            StdDraw.line(min_x, y, max_x, y);
        }
        drawRecursive(current_node.right_child);
        drawRecursive(current_node.left_child);
    }

    // all points in the set that are inside the rectangle
    public Iterable<Point2D> range(RectHV rect) {
        Queue<Point2D> points = new Queue<Point2D>();
        recursiveRangeSearch(this.root, rect, points);
        return points;
    }

    // Recursively go through the tree and find all nodes that are within the range of a given rectangle
    private void recursiveRangeSearch(Node current_node, RectHV rect, Queue<Point2D> points){
        // When leaf is reached stop recursion
        if (current_node==null) return;

        // If the current node's point is within the rectangle
        if (rect.contains(current_node.point)) {
            points.enqueue(current_node.point); // Add to queue
        }
        // Recursively go to left and right sub tree until they are no longer intersecting the rectangle
        if (current_node.rect_left.intersects(rect)){
            // Repeat for left tree
            recursiveRangeSearch(current_node.left_child, rect, points);
        }
        if (current_node.rect_right.intersects(rect)){
            // Repeat for right tree
            recursiveRangeSearch(current_node.right_child, rect, points);
        }
    }

    // a nearest neighbor in the set to p; null if set is empty
    public Point2D nearest(Point2D p) {
        Node nearest_node = recursiveNearestSearch(this.root, p, this.root.point.distanceTo(p), this.root);
        return nearest_node.point;
    }
    private Boolean isShorterDistance(Node current_node, Node ret_node, Point2D point){
        return ret_node.point.distanceSquaredTo(point) < current_node.point.distanceSquaredTo(point);
    }

    private Node recursiveNearestSearch(Node current_node, Point2D point, double shortest_distance, Node shortest_node){
        // Check distance from current node to query point
        double distance_to_point = current_node.point.distanceSquaredTo(point);

        // If distance is shorter than current shortest distance then overwrite shortest distance.
        if (distance_to_point<shortest_distance){
            shortest_distance = distance_to_point;
            shortest_node = current_node;
        }
        // Check if it is more likely to be on the left/bottom or right/top
        int direction = current_node.compareTo(point); // If -1 then left, if 1 then right

        // Go to the more likely node.
        if (direction>0){
            if (current_node.left_child!=null){
                Node ret_node = recursiveNearestSearch(current_node.left_child, point, shortest_distance, shortest_node);
                if (isShorterDistance(current_node, ret_node, point)){
                    shortest_node = ret_node;
                    shortest_distance = shortest_node.point.distanceSquaredTo(point);
                }
            }
        }
        else{
            if (current_node.right_child!=null){
                Node ret_node = recursiveNearestSearch(current_node.right_child, point, shortest_distance, shortest_node);
                if (isShorterDistance(current_node, ret_node, point)){
                    shortest_node = ret_node;
                    shortest_distance = shortest_node.point.distanceSquaredTo(point);
                }
            }
        }

        // Check if shortest_distance from query point is longer than distance to opposite rectangle.
        double dist_rect;
        if (direction>0){
            dist_rect = current_node.rect_right.distanceSquaredTo(point);

        }else {
            dist_rect = current_node.rect_left.distanceSquaredTo(point);
        }

        // If distance is longer, then go to the other direction. If not, go straight to return.
        if (dist_rect<shortest_distance){
            if(direction>0){
                if (current_node.right_child!=null){
                    Node ret_node = recursiveNearestSearch(current_node.right_child, point, shortest_distance, shortest_node);
                    if (isShorterDistance(current_node, ret_node, point)){
                        shortest_node = ret_node;
                    }
                }
            }else {
                if (current_node.left_child!=null){
                    Node ret_node = recursiveNearestSearch(current_node.left_child, point, shortest_distance, shortest_node);
                    if (isShorterDistance(current_node, ret_node, point)){
                        shortest_node = ret_node;
                    }
                }
            }
        }
        // Distance is shorter -> return
        return shortest_node;
    }

    /*******************************************************************************
     * Test client
     ******************************************************************************/
    public static void main(String[] args) {
        // Custom test
        //StdDraw.setCanvasSize(1000, 1000);
        KdTree test = new KdTree();
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
        System.out.println("Total time for "+k+" nearest runs: " + total_time);
        System.out.println(test.size());
        /*
        // Drawing all squares and points in tree
        test.draw();

        // Checking if point exists
        Point2D test_point = new Point2D(0.656218, 0.739517); // Point 281
        System.out.println(test.contains(test_point));

        // Checking amount of nodes in tree
        System.out.println(test.size());

        // Testing range with this test point
        RectHV test_rect = new RectHV(0.15, 0.25, 0.35, 0.6);
        StdDraw.setPenColor(Color.green);
        StdDraw.setPenRadius(0.01);
        for (Point2D point: test.range(test_rect)){
            point.draw();
        }
        StdDraw.setPenRadius();
        test_rect.draw();

        // Testing nearest neighbor
        Point2D neighbor_point = new Point2D(0.2, 0.23);
        StdDraw.setPenColor(Color.MAGENTA);
        StdDraw.setPenRadius(0.02);
        neighbor_point.draw();
        StdDraw.setPenColor(Color.orange);
        Point2D nearest = test.nearest(neighbor_point);
        nearest.draw();
         */
    }
}
