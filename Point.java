package jp.co.worksap.global;

/**
 * Created by Emperor on 30/10/14.
 */

/**
 * @author Simon X. M. LIU
 * @version 1.0
 *          This class implements the point on a 2D grid.
 *          The two properties row and col represent the coordinates.
 */
class Point implements Comparable<Point> {
    public int row;
    public int col;

    /**
     * Default constructor setting coordinates to zeros.
     */
    public Point() {
        row = 0;
        col = 0;
    }

    /**
     * Constructor with properties' initial values.
     *
     * @param r the row
     * @param c the column
     */
    public Point(int r, int c) {
        row = r;
        col = c;
    }

    /**
     * Copy constructor copying from an existing object.
     *
     * @param point the point to be copied.
     */
    public Point(Point point) {
        this.row = point.row;
        this.col = point.col;
    }

    /**
     * Test function.
     *
     * @param args
     */
    public static void main(String[] args) {
        Point p1 = new Point(1, 5);
        Point p2 = new Point(1, 3);
        System.out.println(p1.equals(p2));
    }


    /**
     * @return Description of the point.
     */
    @Override
    public String toString() {
        String string = "P(";
        string += this.row;
        string += ", ";
        string += this.col;
        string += ")";
        return string;
    }


    /**
     * Compare two points using lexical order.
     *
     * @param o the object to be compared with.
     * @return 0 for equal, 1 for bigger than and -1 for less than.
     */
    @Override
    public int compareTo(Point o) {
        if (this.row == o.row && this.col == o.col)
            return 0;
        else if (this.row > o.row || this.row == o.row && this.col > o.col)
            return 1;
        else
            return -1;
    }


    @Override
    public boolean equals(Object obj) {
        if (this.row == ((Point) obj).row && this.col == ((Point) obj).col)
            return true;
        return false;
    }

}
