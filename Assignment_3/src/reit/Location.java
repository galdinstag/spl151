package reit;

/**
 * Location:
 * A class representing the x,y coordinate of different objects
 * in the simulator (assets and clerks in particular)
 * Created by airbag on 12/9/14.
 */
public class Location {

    private final double x;
    private final double y;

    /**
     * Constructor
     *
     * @param x
     * @param y
     */
    public Location(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Calculates the euclidean distance between this point and another given point
     * @param other- the given point
     * @return - the euclidean distance
     */
    double calculateDistance(Location other) {
        return Math.sqrt(Math.abs(other.x - this.x) + Math.abs(other.y - this.y));
    }

    /**
     *
     * @return - The location's x,y coordinates
     */
    public String toString() {
        return "(" + x + "," + y + ")";
    }
}
