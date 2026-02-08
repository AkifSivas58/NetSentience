package com.cvproject.netsentience.service;

import com.cvproject.netsentience.model.Device;
import com.cvproject.netsentience.model.MonitoringLog;
import com.cvproject.netsentience.repository.DeviceRepository;
import com.cvproject.netsentience.repository.MonitoringLogRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class DeviceCheckRunner {
    private final DeviceRepository deviceRepository;
    private final MonitoringLogRepository logRepository;

    public DeviceCheckRunner(DeviceRepository deviceRepository, MonitoringLogRepository logRepository){
        this.deviceRepository = deviceRepository;
        this.logRepository = logRepository;
    }

    @Async
    public void checkDevice(Long deviceId){
        Device device = deviceRepository.findById(deviceId).orElse(null);
        if (device == null) return;

        boolean isReachable = ping(device.getIpAddress());
        String newStatus = isReachable ? "UP" : "DOWN";

        deviceRepository.updateStatus(newStatus, LocalDateTime.now(), device.getId());

        MonitoringLog log = new MonitoringLog();
        log.setDevice(device);
        log.setUp(isReachable);
        log.setCheckTime(LocalDateTime.now());
        logRepository.save(log);

        System.out.println("Async Check: " + device.getName() + " -> " + newStatus + " [Thread: " + Thread.currentThread().getName() + "]");
    }

    private boolean ping(String ipAddress) {
        try {
            boolean isWindows = System.getProperty("os.name").toLowerCase().contains("win");
            String command = isWindows
                    ? "ping -n 1 -w 1000 " + ipAddress  // Windows: 1 packet, 1000ms timeout
                    : "ping -c 1 -W 1 " + ipAddress;    // Linux: 1 packet, 1 second timeout

            // Run the command in the terminal
            Process process = Runtime.getRuntime().exec(command);

            // Wait for it to finish
            int returnCode = process.waitFor();

            // Return true if exit code is 0 (Success)
            return (returnCode == 0);

        } catch (Exception e) {
            System.out.println("Ping error: " + e.getMessage());
            return false;
        }
    }
}
