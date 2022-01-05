import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

/**
 * OrbitalPathfinder takes a .txt file of orbits and calculates the total orbits and the distance between two planets.
 * Inspiration for solution found on https://github.com/TheTurkeyDev/Advent-of-Code-2019/blob/master/src/com/theprogrammingturkey/aoc2019/Day6.java
 * and https://github.com/neiomi1/AdventOfCode2019/blob/master/src/day_06/Orbits.java.
 *
 * @author Scott Charles
 *
 */
public class OrbitalPathfinder {

    private static HashMap<String, String> orbitGraph;
    private static String center;

    /**
     * OrbitalPathfinder main.
     * @param args command line input
     */
    public static void main(String[] args) {

        if (args.length == 0) {
            System.err.println("Error: Please indicate the part to run and the input file.");
            System.exit(0);
        } else if (args[0].equals("1")) {
            createGraph(args[1]);
            System.out.println(getOrbits());
        } else if (args[0].equals("2")) {
            createGraph(args[1]);
            System.out.println(getDistance(args[2], args[3]));
        } else {
            System.err.println("Error: Please indicate either part 1 or part 2.");
            System.exit(0);
        }
    }

    /**
     * Calculates distance between two planets.
     * @param from source planet
     * @param to destination planet
     * @return int distance
     */
    private static int getDistance(String from, String to) {

        checkForErrors(from, to);

        List<String> fromPlanet = getPathToCenter(from, null);
        List<String> toPlanet = getPathToCenter(to, null);

        for (int j = 0; j < fromPlanet.size(); j++) {
            for (int i = 0; i < toPlanet.size(); i++) {
                if (fromPlanet.get(j).equals(toPlanet.get(i))) {
                    fromPlanet.remove(j);
                    toPlanet.remove(i);
                }
            }
        }

        for (int j = 0; j < toPlanet.size(); j++) {
            for (int i = 0; i < fromPlanet.size(); i++) {
                if (toPlanet.get(j).equals(fromPlanet.get(i))) {
                    toPlanet.remove(j);
                    fromPlanet.remove(i);
                }
            }
        }

        if (fromPlanet.size() > 0 && toPlanet.size() > 0) {
            fromPlanet.remove(fromPlanet.size() - 1);
            toPlanet.remove(toPlanet.size() - 1);
        }
        return (fromPlanet.size() + toPlanet.size());
    }

    /**
     * Validates input and prints error messages if necessary.
     *
     * @param from source input
     * @param to destination input
     */
    private static void checkForErrors(String from, String to) {
        if (from.equals(center) || to.equals(center)) {
            System.err.println("Error: The object " + center + " can not be the source or destination.");
            System.exit(0);
        } else if (!orbitGraph.containsKey(from)) {
            System.err.println("Error: The object " + "'" + from + "'" + " could not be found.");
            System.exit(0);
        } else if (!orbitGraph.containsKey(to)) {
            System.err.println("Error: The object " + "'" + to + "'" + " could not be found.");
            System.exit(0);
        }
    }

    /**
     * counts orbits from orbitGraph.
     */
    public static int getOrbits()
    {
        int total = 0;
        for (String plt : orbitGraph.keySet())
        {
            int count = countOrbits(plt, 0);
            total += count;
        }
        return total;
    }

    /**
     * recursively counts the total number of orbits.
     * @param planet current planet path to count
     * @param result paths
     * @return number of orbits
     */
    public static int countOrbits(String planet, int result)
    {
        if (orbitGraph.containsKey(planet)) {
            return countOrbits(orbitGraph.get(planet), result + 1);
        }
        return result;
    }

    /**
     *
     * recursively finds path to center of mass.
     * @param from planet to center of mass
     * @param list list of planets on path
     * @return list
     */
    public static List<String> getPathToCenter(String from, List<String> list)
    {
        if (list == null) {
            list = new ArrayList<String>();
        }
        if (orbitGraph.containsKey(from)) {
            list.add(orbitGraph.get(from));
        }
        else {
            return list;
        }
        return getPathToCenter(orbitGraph.get(from), list);
    }

    /**
     * creates graph from text file.
     * @param input .txt file
     */
    public static void createGraph(String input) {
        String[] splitList = null;
        String currentLine;
        Scanner fileReader;
        orbitGraph = new HashMap<String, String>();

        File inFile = new File(input);
        try {
            fileReader = new Scanner(inFile);
            boolean firstLine = true;
            while (fileReader.hasNextLine()) {
                currentLine = fileReader.nextLine();
                splitList = currentLine.split("\\)");
                if (firstLine) {
                    center = splitList[0];
                }
                if (!splitList[0].equals("")) {
                    orbitGraph.put(splitList[1], splitList[0]);
                }
                firstLine = false;
            }
        } catch (FileNotFoundException error) {
            System.err.println("Error: The file " + input + " could not be read.");
            System.exit(0);
        }
    }
}
