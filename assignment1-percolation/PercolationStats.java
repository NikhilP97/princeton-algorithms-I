/**
 * The problem statement can be found here - https://coursera.cs.princeton.edu/algs4/assignments/percolation/specification.php
 */

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {

    private final double mean;
    private final double stddev;
    private final double confidenceLo;
    private final double confidenceHi;

    // perform trials independent experiments on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException("value has to be greater than 0");
        }

        double[] thresholds = new double[trials];
        for (int i = 0; i < trials; i++) {
            Percolation system = new Percolation(n);
            while (!system.percolates()) {
                int column = 1 + StdRandom.uniform(n);
                int row = 1 + StdRandom.uniform(n);
                system.open(row, column);
            }
            thresholds[i] = system.numberOfOpenSites() / (double) (n * n);
        }
        mean = StdStats.mean(thresholds);
        stddev = StdStats.stddev(thresholds);
        double openSiteFractions = (1.96 * stddev()) / Math.sqrt(trials);
        confidenceLo = mean - openSiteFractions;
        confidenceHi = mean + openSiteFractions;
    }

    // sample mean of percolation threshold
    public double mean() {
        return mean;
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return stddev;
    }

    // low  endpoint of 95% confidence interval
    public double confidenceLo() {
        return confidenceLo;
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return confidenceHi;
    }

    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int trials = Integer.parseInt(args[1]);
        PercolationStats stats = new PercolationStats(n, trials);
        System.out.println("mean                    = "+stats.mean());
        System.out.println("stddev                  = "+stats.stddev());
        System.out.printf("95%% confidence interval = [%f, %f] \n", stats.confidenceLo(), stats.confidenceHi());
    }

}