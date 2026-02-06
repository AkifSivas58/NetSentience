package com.cvproject.netsentience.dto;

public class DeviceUptimeDTO {
    private Long deviceId;
    private long totalChecks;
    private long upChecks;
    private double upTimePercentage;

    public DeviceUptimeDTO(Long deviceId, long totalChecks, long upChecks, double upTimePercentage) {
        this.deviceId = deviceId;
        this.totalChecks = totalChecks;
        this.upChecks = upChecks;
        this.upTimePercentage = upTimePercentage;
    }

    public Long getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(Long deviceId) {
        this.deviceId = deviceId;
    }

    public long getTotalChecks() {
        return totalChecks;
    }

    public void setTotalChecks(long totalChecks) {
        this.totalChecks = totalChecks;
    }

    public long getUpChecks() {
        return upChecks;
    }

    public void setUpChecks(long upChecks) {
        this.upChecks = upChecks;
    }

    public double getUpTimePercentage() {
        return upTimePercentage;
    }

    public void setUpTimePercentage(double upTimePercentage) {
        this.upTimePercentage = upTimePercentage;
    }
}
