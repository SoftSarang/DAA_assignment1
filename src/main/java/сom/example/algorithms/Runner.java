package сom.example.algorithms;

import сom.example.algorithms.Main;


public class Runner {
    public static void main(String[] args) {
        int[] sizes = {100, 1000, 5000, 10000};
        for (int size : sizes) {
            System.out.println("Running with size: " + size);
            Main.runAlgorithms(size, "output.csv");
        }
        System.out.println("Algorithms executed programmatically!");
    }
}