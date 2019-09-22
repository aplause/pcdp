package edu.coursera.concurrent;

public class SieveData {

    private final int workingNumber;
    private final int maxLocalPrimes;

    public SieveData(int workingNumber, int maxLocalPrimes) {
        this.workingNumber = workingNumber;
        this.maxLocalPrimes = maxLocalPrimes;
    }

    public int getWorkingNumber() {
        return workingNumber;
    }

    public int getMaxLocalPrimes() {
        return maxLocalPrimes;
    }
}
