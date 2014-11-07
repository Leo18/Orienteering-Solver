package jp.co.worksap.global;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by Simon X. M. LIU on 30/10/14.
 */

/**
 * For small input such as the number of checkpoints <= 8, a brute force search should work.
 * But for large scale input, say the number of checkpoints to be 18, then the number of iteration
 * would exceed 10^15, it is impossible for a standard PC to finish running in one hour.
 * The problem is quite like the famous Hamilton path problem and the Traveling Salesperson Problem,
 * they are NP-hard problems so we cannot expect a polynomial solution. There have already some researches
 * like genetic algorithm and Ant Colony Algorithm to give out an approximated solution within acceptable
 * time complexity. In this case, the requirement is to get the EXACT answer. Luckily the extreme case includes
 * 18 checkpoints and we can solve it using dynamic programming.
 * By the A* path finding algorithm, we can get the shortest path between every two node on the grid map.
 * Suppose we are now standing on some checkpoint, for each of other checkpoints, the state would be "passed" or
 * "not passed", hence there are totally 2^n different cases. Then for each case, we can easily get a checkpoint
 * which has not been passed, and dynamically calculate the shortest path to that checkpoints using information
 * in the distance matrix and other states. So the time complexity becomes O(n*2^n), for n = 18, it is still
 * acceptable.
 */
public class Orienteering {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int width = scanner.nextInt();
        int height = scanner.nextInt();
        String[] map = new String[height];
        for (int i = 0; i < height; i++) {
            map[i] = scanner.next();
        }

        AStarPathFinder pathFinder = new AStarPathFinder(map, width, height);
        ArrayList<Point> checkPoints = new ArrayList<Point>(40);
        Point start = null, goal = null;

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (map[i].charAt(j) == '@') {
                    checkPoints.add(new Point(i, j));
                }
                if (map[i].charAt(j) == 'S') {
                    start = new Point(i, j);
                }
                if (map[i].charAt(j) == 'G') {
                    goal = new Point(i, j);
                }
            }
        }
        if (start == null || goal == null) {
            System.out.println(-1);
            return;
        }
        int numCheckPoints = checkPoints.size();
        int[][] distances = new int[numCheckPoints + 2][numCheckPoints + 2];
        checkPoints.add(start);
        checkPoints.add(goal);

        for (int i = 0; i < checkPoints.size() - 1; i++) {
            for (int j = i + 1; j < checkPoints.size(); j++) {
                distances[i][j] = pathFinder.getShortestPath(checkPoints.get(i), checkPoints.get(j), width, height);
                if (distances[i][j] == 0) {
                    System.out.println(-1);
                    return;
                }
                distances[j][i] = distances[i][j];
            }
        }

        int startIndex = numCheckPoints;
        int goalIndex = numCheckPoints + 1;
        int answer = 1 << 30;
        if (numCheckPoints == 0) {
            System.out.println(distances[numCheckPoints][numCheckPoints + 1]);
            return;
        }
        int numSelections = 1 << numCheckPoints;
        // Store all the state information.
        // For every node, there should be 2^n different states.
        // The paths means for every possible state, the shortest path for stopping to
        // stand on the checkpoint.
        // For example, paths[3][0] means I just made two steps(because 3 is 0b11)
        // from the start to the checkpoint with index 0,
        // its value is the shortest path from start to the index 1 checkpoint plus
        // the shortest path from index 1 to index 0 checkpoint.
        int[][] paths = new int[numSelections][numCheckPoints];
        for (int i = 0; i < numSelections; i++) {
            for (int j = 0; j < numCheckPoints; j++) {
                paths[i][j] = 1 << 28;
            }
        }
        // For every possible state, search from the first checkpoint to the last.
        // Then we can get all the Node<->State pairs.
        for (int selection = 1; selection < numSelections; selection++) {
            for (int i = 0; i < numCheckPoints; i++) {
                // Not standing on this checkpoint, so just ignore and continue.
                if ((selection & (1 << i)) == 0) continue;
                // Get the checkpoints which have already been passed.
                int subset = selection - (1 << i);
                // Case one: just made one step.
                if (subset == 0) {
                    paths[selection][i] = distances[startIndex][i];
                    continue;
                }
                // Case two: more than one steps made.
                // For each of the checkpoint passed, if the path value of
                // that point plus the distance from that point to the current one
                // is less than the current value, then update it.
                // The equation is: paths[state][i] = min(paths[state_i][i], paths[state_j][j] + dist[j][i]).
                // state_j means the state that we are standing on j and have passed all the checkpoints
                // stated in state_i except i itself, dist[j][i] is the shortest path from j to i.
                for (int j = 0; j < numCheckPoints; j++) {
                    if ((selection & (1 << j)) != 0
                            && i != j) {
                        paths[selection][i] = Math.min(
                                paths[selection][i],
                                paths[subset][j] + distances[j][i]
                        );
                    }
                }
            }
        }

        // Then we check the state of all 1s and decide which is the best path according to
        // the addition of the path value and distance from the finally standing point to the goal.
        for (int i = 0; i < numCheckPoints; i++) {
            answer = Math.min(answer,
                    paths[numSelections - 1][i] + distances[i][goalIndex]);
        }
        System.out.println(answer);
    }
}
