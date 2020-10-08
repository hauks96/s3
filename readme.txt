/**********************************************************************
 *  readme.txt template                                                   
 *  Kd-tree
**********************************************************************/

Name: Ægir Máni Hauksson
Login: aegir19@ru.is
Section instructor:

Partner name: Hákon Hákonarson
Partner login: hakon19@ru.is
Partner section instructor:

/**********************************************************************
 *  Describe the Node data type you used to implement the
 *  2d-tree data structure.
 **********************************************************************/
    The node contains various attributes. One of the fundamental ones
    being the is_x boolean attribute. This attribute decides which
    dimension we are comparing at the current level in the tree. The
    difference can be seen in the compareTo method. On an X level we
    compare by X, on a Y level we compare by Y.

    Another feature are the left and right rectangles. After a node
    gets created the node gets assigned two rectangles. These two
    rectangles describe mainly what area the left or the right of the
    node contain. The debate with these two attributes could be that
    they are unnecessary and can be computed when they are needed,
    but then again that would mean performance loss to some extent
    making it a question between performance and space saving.

    Apart from these two attributes, and the compareTo method the node
    is a very normal binary search tree node with a parent and one child
    on either side.

/**********************************************************************
 *  Describe your method for range search in a kd-tree.
 **********************************************************************/
    The range search is done by recursively checking the left and right
    sub tree whether they intersect the rectangle given in the argument.
    If they intersect, we check whether the point is contained within
    the rectangle and keep the recursion going, otherwise we know we are
    no longer in range of the rectangle so we return. If we reach a
    leaf we also return.

    We use a Queue object defined in an outer scope of the recursive call
    so that we do not need to return anything from the actual recursion.
    We only need to add(point) to get our new point into the queue.

/**********************************************************************
 *  Describe your method for nearest neighbor search in a kd-tree.
 **********************************************************************/
    1. check distance from current node to query point
    2. if distance is shorter than current shortest distance then overwrite shortest distance.
    3. check if it is more likely to be on the left/bottom or right/top
      3.1 check by comparing the query point and current node x-coordinates if its a
      vertical node, y-coordinates if its a horizontal node.
      3.2 if the query point is less than node then go left/down, else go right/top.
    4. go to the more likely node.
    5. check if shortest_distance from query point is longer than distance to opposite rectangle.
    6. if distance is longer, then go to the other direction. If not, go straight to return.
    7. return shortest_node


/**********************************************************************
 *  Give the total memory usage in bytes (using tilde notation and 
 *  the standard 64-bit memory cost model) of your 2d-tree data
 *  structure as a function of the number of points N. Justify your
 *  answer below.
 *
 *  Include the memory for all referenced objects (deep memory),
 *  including memory for the nodes, points, and rectangles.
 **********************************************************************/

bytes per Point2D: 32 bytes
bytes per RectHV:

bytes per KdTree of N points (using tilde notation):   ~
[include the memory for any referenced Node, Point2D and RectHV objects]


/**********************************************************************
 *  Give the expected running time in seconds (using tilde notation)
 *  to build a 2d-tree on N random points in the unit square.
 *  Use empirical evidence by creating a table of different values of N
 *  and the timing results. (Do not count the time to generate the N 
 *  points or to read them in from standard input.)
 **********************************************************************/


/**********************************************************************
 *  How many nearest neighbor calculations can your brute-force
 *  implementation perform per second for input100K.txt (100,000 points)
 *  and input1M.txt (1 million points), where the query points are
 *  random points in the unit square? Explain how you determined the
 *  operations per second. (Do not count the time to read in the points
 *  or to build the 2d-tree.)
 *
 *  Repeat the question but with the 2d-tree implementation.
 **********************************************************************/

                     calls to nearest() per second
                     brute force           2d-tree
input100K.txt
input1M.txt



/**********************************************************************
 *  Have you taken (part of) this course before:
 **********************************************************************/


/**********************************************************************
 *  Known bugs / limitations.
 **********************************************************************/


/**********************************************************************
 *  Describe whatever help (if any) that you received.
 *  Don't include readings, lectures, and d�mat�mar, but do
 *  include any help from people (including course staff, 
 *  classmates, and friends) and attribute them by name.
 **********************************************************************/


/**********************************************************************
 *  Describe any serious problems you encountered.                    
 **********************************************************************/


/**********************************************************************
 *  If you worked with partners, assert below that you followed
 *  the protocol as described on the assignment page. Give one
 *  sentence explaining what each of you contributed.
 **********************************************************************/






/**********************************************************************
 *  List any other comments here. Feel free to provide any feedback   
 *  on how much you learned from doing the assignment, and whether    
 *  you enjoyed doing it.                                             
 **********************************************************************/
