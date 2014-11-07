package jp.co.worksap.global;

import java.util.ArrayList;
import java.util.PriorityQueue;

/**
 * Created by Emperor on 30/10/14.
 */

/**
 * @author Simon X. M. LIU
 * @version 1.0
 *
 * This class implements a shortest path finding tool using the A* algorithm.
 * Traditional shortest path finding algorithms include Breadth First Search, Dijkstra algorithm, etc.
 * Those algorithms are suitable for searching paths in an abstract graph consisting of nodes and edges.
 * In the Orienteering problem, the graph is a grid. The abstract form should contain W*H nodes and
 * there might be an edge between every two adjacent nodes. So when W*H is large, for example in this
 * problem the W*H could be 10,000, then those algorithms would be very low-efficient. For example,
 * if the start point is on the top-left corner and the target locates at the bottom right corner, then
 * the BFS might need to search the whole map. But with A* algorithm, it might just need to search
 * along the diagonal with the help of the heuristic function.
 *
 * The A* algorithm shares essential ideas with Dijkstra algorithm, but using a heuristic function
 * for acceleration. Dijkstra algorithm selects an edge with minimum weight from the open set in every
 * iteration until the target is reached. A* algorithm selects the node with minimum f_score instead.
 * f_score is defined as f_score = g_score + heuristic, where g_score means the cost from start point to
 * current point and heuristic means the heuristic value for the current node. The heuristic value indeed
 * is an estimation about the cost from the current node to the target. If the estimation is always less than
 * the actual cost, then A* algorithm is guaranteed to be able to find the shortest path. Otherwise it might
 * find a short path very quickly but the path might not be the shortest. In my implementation I use diagonal
 * distance for heuristic value which surely guarantees a shortest path.
 */


/**
 * The Node class is for assistant use. It represents an cell on the grid map.
 * It contains the coordinate represented by Point, and the cell property(wall, checkpoint).
 */
class Node implements Comparable<Node> {
    Point point;
    char property;


    /**
     * Default constructor
     */
    public Node() {
        point = new Point(0, 0);
        property = 0;
    }

    /**
     * Construct a node with a Point and property
     *
     * @param p        the point of the node.
     * @param property '#' for wall and '@' for checkpoint.
     */
    public Node(Point p, char property) {
        point = new Point(p);
        this.property = property;
    }

    /**
     * Copy from another node.
     * After copying, each node has their own members, no common references.
     *
     * @param node the node to be copied from.
     */
    public Node(Node node) {
        this.point = new Point(node.point);
        this.property = node.property;
    }

    @Override
    public int compareTo(Node o) {
        return this.point.compareTo(o.point);
    }

    @Override
    public boolean equals(Object obj) {
        return this.point.equals(((Node) obj).point);
    }

}


/**
 * AStarNode class is used for calculation. Compared with Node, the AStarNode contains
 * extra members like g_score and h_estimate which are used for generation shortest paths.
 * It is comparable since later on it will be used in the PriorityQueue container.
 */
class AStarNode implements Comparable<AStarNode> {
    Node node;
    int g_score;
    int h_estimate;
    AStarNode parent;

    // Block initialization
    {
        node = null;
        g_score = 0;
        h_estimate = 0;
        parent = null;
    }

    /**
     * Construct from a Node
     *
     * @param node
     */
    public AStarNode(Node node) {
        this.node = new Node(node);
    }

    /**
     * Construct with the current Node and the target Node.
     * With the target Node, the heuristic value could be calculated at construction.
     *
     * @param cur
     * @param to
     */
    public AStarNode(Node cur, Node to) {
        this(cur);
        int d1 = Math.abs(this.node.point.row - to.point.row);
        int d2 = Math.abs(this.node.point.col - to.point.col);
        int d = (int) Math.sqrt(d1 * d1 + d2 * d2 + 0.0);
        h_estimate = d;
    }

    /**
     * The node with small total score is preferred.
     * If the total score is equal, then small heuristic value is preferred.
     *
     * @param o
     * @return
     */
    @Override
    public int compareTo(AStarNode o) {
        if (this.getF_score() < o.getF_score())
            return -1;
        else if (this.getF_score() > o.getF_score())
            return 1;
        else {
            if (h_estimate < o.h_estimate) return -1;
            if (h_estimate > o.h_estimate) return 1;
            else return 0;
        }
    }

    /**
     * @return the total score of the node.
     */
    public int getF_score() {
        return g_score + h_estimate;
    }

}


/**
 * The AStarPathFinder is used to get the shortest paths between any two points on the graph.
 */

public class AStarPathFinder {
    // The graph representing the grid map.
    private Node[][] graph;
    // The corresponding AStarNode array for calculation.
    private AStarNode[][] calc_graph;

    // Auxiliary arrays indicating whether the node has been visited or closed.
    private boolean[][] visited;
    private boolean[][] closed;

    private int height, width;

    /**
     * Constructor from raw input.
     *
     * @param g      the String array containing the grid map.
     * @param width  the width of the map.
     * @param height the height of the map.
     */
    public AStarPathFinder(String[] g, int width, int height) {
        graph = new Node[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                // Construct a Node for every character in the raw grid map.
                graph[i][j] = new Node(new Point(i, j), g[i].charAt(j));
            }
        }
        this.width = width;
        this.height = height;
    }

    /**
     * Get the up, down, left and right valid neighbors.
     *
     * @param node the current node.
     * @return the array of the neighbors
     */
    private ArrayList<AStarNode> getNeighbors(AStarNode node) {
        ArrayList<AStarNode> ret = new ArrayList<AStarNode>();
        Point point = node.node.point;
        if (point.row - 1 >= 0 && graph[point.row - 1][point.col].property != '#') {
            ret.add(calc_graph[point.row - 1][point.col]);
        }
        if (point.row + 1 < height && graph[point.row + 1][point.col].property != '#') {
            ret.add(calc_graph[point.row + 1][point.col]);
        }
        if (point.col - 1 >= 0 && graph[point.row][point.col - 1].property != '#') {
            ret.add(calc_graph[point.row][point.col - 1]);
        }
        if (point.col + 1 < width && graph[point.row][point.col + 1].property != '#') {
            ret.add(calc_graph[point.row][point.col + 1]);
        }
        return ret;
    }

    /**
     * A wrapper function for calling from outside.
     *
     * @param start  the Point of start.
     * @param goal   the Point of the goal.
     * @param width  the width of the map.
     * @param height the height of the map.
     * @return the shortest path.
     */
    public int getShortestPath(Point start, Point goal, int width, int height) {
        return getShortestPath(graph[start.row][start.col], graph[goal.row][goal.col], width, height);
    }

    /**
     * Shortest path generating function.
     *
     * @param start  the Node of the start on the graph.
     * @param target the Node of the goal on the graph.
     * @param width  the width of the map.
     * @param height the height of the map.
     * @return the shortest path from start to goal or 0 if no path exists.
     */
    public int getShortestPath(Node start, Node target, int width, int height) {
        visited = new boolean[height][width];
        closed = new boolean[height][width];
        calc_graph = new AStarNode[height][width];
        // Initialization for all calculating variables.
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                calc_graph[i][j] = new AStarNode(graph[i][j], target);
                visited[i][j] = false;
                closed[i][j] = false;
            }
        }

        // Construct and push the start node to queue.
        AStarNode node = new AStarNode(start, target);
        PriorityQueue<AStarNode> queue;
        queue = new PriorityQueue<AStarNode>(1000);
        queue.add(node);

        // Main iteration. Each time we loose one node with the minimum total cost.
        // Then add its neighbors into the queue.
        while (queue.size() > 0) {
            AStarNode cur = queue.poll();
            if (cur.node.equals(target)) {
                return cur.g_score;
            }
            closed[cur.node.point.row][cur.node.point.col] = true;
            for (AStarNode neighbor : getNeighbors(cur)) {
                Point p = neighbor.node.point;
                if (closed[p.row][p.col])
                    continue;
                int g_score = cur.g_score + 1;
                boolean v = visited[p.row][p.col];
                if (!v || g_score < neighbor.g_score) {
                    visited[p.row][p.col] = true;
                    neighbor.parent = cur;
                    neighbor.g_score = g_score;
                    queue.add(neighbor);
                }
            }
        }
        return 0;
    }
}
