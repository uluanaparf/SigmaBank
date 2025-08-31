package com.sigmabank.model;

public class Statistics {
    private String departmentName;
    private double min;
    private double max;
    private double mid;

    public Statistics(String departmentName, double min, double max, double mid) {
        this.departmentName = departmentName;
        this.min = min;
        this.max = max;
        this.mid = mid;
    }

    public String getDepartmentName() { return departmentName; }

    public double getMin() { return min; }

    public double getMax() { return max; }

    public double getMid() { return mid; }

    @Override
    public String toString() {
        return String.format("%s,%.2f,%.2f,%.2f", departmentName, min, max, mid);
    }
}
