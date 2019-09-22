package edu.coursera.concurrent;

import java.util.Objects;

public class SieveData {
    public static final SieveData FINISH = new SieveData(0, 0);
    private final int numberToCheck;
    private final int maxLocalPrimes;

    public SieveData(int numberToCheck, int maxLocalPrimes) {
        this.numberToCheck = numberToCheck;
        this.maxLocalPrimes = maxLocalPrimes;
    }

    public int getNumberToCheck() {
        return numberToCheck;
    }

    public int getMaxLocalPrimes() {
        return maxLocalPrimes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SieveData sieveData = (SieveData) o;
        return numberToCheck == sieveData.numberToCheck &&
                maxLocalPrimes == sieveData.maxLocalPrimes;
    }

    @Override
    public int hashCode() {
        return Objects.hash(numberToCheck, maxLocalPrimes);
    }

    @Override
    public String toString() {
        return "SieveData{" +
                "numberToCheck=" + numberToCheck +
                ", maxLocalPrimes=" + maxLocalPrimes +
                '}';
    }
}
