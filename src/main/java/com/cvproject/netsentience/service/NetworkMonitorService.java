package com.cvproject.netsentience.service;

import com.cvproject.netsentience.dto.DeviceUptimeDTO;
import com.cvproject.netsentience.model.Device;
import com.cvproject.netsentience.model.MonitoringLog;
import com.cvproject.netsentience.repository.DeviceRepository;
import com.cvproject.netsentience.repository.MonitoringLogRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.io.IOException;
import java.net.InetAddress;
import java.time.LocalDateTime;

@Service
public class NetworkMonitorService {
    private final DeviceRepository deviceRepository;
    private final MonitoringLogRepository logRepository;

    public NetworkMonitorService(DeviceRepository deviceRepository, MonitoringLogRepository logRepository){
        this.deviceRepository = deviceRepository;
        this.logRepository = logRepository;
    }

    @Scheduled(fixedRate = 10000)
    public void checkDevices() {
        // Fetch all devices fresh from the DB
        List<Device> devices = deviceRepository.findAll();

        for (Device device : devices) {
            // 1. Ping
            boolean isReachable = ping(device.getIpAddress());
            String newStatus = isReachable ? "UP" : "DOWN";

            // 2. Update DB
            device.setStatus(newStatus);
            device.setLastChecked(LocalDateTime.now());
            deviceRepository.save(device);

            // 3. Log History
            MonitoringLog log = new MonitoringLog();
            log.setDevice(device);
            log.setUp(isReachable);
            log.setCheckTime(LocalDateTime.now());
            logRepository.save(log);

            System.out.println("Checked " + device.getName() + " (" + device.getIpAddress() + ") -> " + newStatus);
        }
    }


    public DeviceUptimeDTO calculateUpTime(Long deviceId){
        List<MonitoringLog> logs = logRepository.findByDeviceId(deviceId);

        if (logs.isEmpty()){
            return new DeviceUptimeDTO(deviceId, 0, 0, 0.0);
        }

        long totalChecks = logs.size();
        long upCount = 0;
        for (MonitoringLog log : logs){
            if (log.isUp()){
                upCount++;
            }
        }

        double percentage = ((double) upCount / totalChecks) * 100;

        return new DeviceUptimeDTO(deviceId, totalChecks, upCount, percentage);
    }

    private boolean ping(String ipAddress) {
        try {
            // 1. Detect OS (Windows uses "-n", Linux/Mac uses "-c")
            boolean isWindows = System.getProperty("os.name").toLowerCase().contains("win");
            String command = isWindows
                    ? "ping -n 1 -w 1000 " + ipAddress  // Windows: 1 packet, 1000ms timeout
                    : "ping -c 1 -W 1 " + ipAddress;    // Linux: 1 packet, 1 second timeout

            // 2. Run the command in the terminal
            Process process = Runtime.getRuntime().exec(command);

            // 3. Wait for it to finish
            int returnCode = process.waitFor();

            // 4. Return true if exit code is 0 (Success)
            return (returnCode == 0);

        } catch (Exception e) {
            System.out.println("Ping error: " + e.getMessage());
            return false;
        }
    }
}
