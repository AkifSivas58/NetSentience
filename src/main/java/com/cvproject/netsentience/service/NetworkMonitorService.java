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
    public void checkDevices(){
        List<Device> devices = deviceRepository.findAll();

        for (Device device : devices){
            boolean isReachable = ping(device.getIpAddress());

            // Update device status
            String newStatus = isReachable ? "UP" : "DOWN";
            device.setStatus(newStatus);
            device.setLastChecked(LocalDateTime.now());

            // Save to history
            MonitoringLog log = new MonitoringLog(device, isReachable, LocalDateTime.now());
            logRepository.save(log);

            System.out.println("Logged status for " + device.getName() + ": " + newStatus);
        }

        deviceRepository.saveAll(devices);
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

    private boolean ping(String ipAddress){
        try{
            InetAddress address = InetAddress.getByName(ipAddress);

            // Timeout is set to 2 seconds
            return address.isReachable(2000);
        }
        catch (IOException e){
            return false;
        }
    }
}
