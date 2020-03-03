/**
 * The problem statement can be found here - https://coursera.cs.princeton.edu/algs4/assignments/percolation/specification.php
 */

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private final WeightedQuickUnionUF sites;
    private final WeightedQuickUnionUF trackFullSites;
    private final int rowSize;
    private final boolean[][] trackOpenSites;
    private int noOfOpenSites;
    private final int topSite;
    private final int bottomSite;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("Grid Size can be greater or equal to 1");
        }
        rowSize = n;
        trackOpenSites = new boolean[n][n];
        noOfOpenSites = 0;
        sites = new WeightedQuickUnionUF(n * n + 2);
        trackFullSites = new WeightedQuickUnionUF(n * n + 1);
        topSite = 0;
        bottomSite = n * n + 1;
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        validateIndices(row, col);
        if (!isOpen(row, col)) {
            int source = xyTo1D(row, col);
            trackOpenSites[row - 1][col - 1] = true;
            noOfOpenSites++;
            connectNeighbor(source, row, col-1);
            connectNeighbor(source, row, col+1);
            connectNeighbor(source, row-1, col);
            connectNeighbor(source, row+1, col);

            if (row == 1) {
                sites.union(topSite, source);
                trackFullSites.union(topSite, source);
            }
            if (row == rowSize) {
                sites.union(bottomSite, source);
            }
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        validateIndices(row, col);
        return trackOpenSites[row - 1][col - 1];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        validateIndices(row, col);
        if (isOpen(row, col)) {
            int source = xyTo1D(row, col);
            return trackFullSites.find(source) == trackFullSites.find(topSite);
        }
        return false;
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return noOfOpenSites;
    }

    // does the system percolate?
    public boolean percolates() {
        return sites.find(topSite) == sites.find(bottomSite);
    }

    private void connectNeighbor(int source, int row, int col) {
        try {
            validateIndices(row, col);
            if (isOpen(row, col)) {
                int neighbor = xyTo1D(row, col);
                sites.union(neighbor, source);
                trackFullSites.union(neighbor, source);
            }
        } catch (IllegalArgumentException e) {
            return;
        }
    }

    private int xyTo1D(int row, int col) {
        return (row - 1) * rowSize + col;
    }

    private void validateIndices(int row, int col) {
        if (row < 1 || row > rowSize) {
            throw new IllegalArgumentException("row index is out of bounds");
        }
        if (col < 1 || col > rowSize) {
            throw new IllegalArgumentException("column index is out of bounds");
        }
    }

}
