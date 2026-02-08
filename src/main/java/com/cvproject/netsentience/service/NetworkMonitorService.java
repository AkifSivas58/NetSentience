package com.cvproject.netsentience.service;

import com.cvproject.netsentience.dto.DeviceUptimeDTO;
import com.cvproject.netsentience.model.Device;
import com.cvproject.netsentience.model.MonitoringLog;
import com.cvproject.netsentience.repository.DeviceRepository;
import com.cvproject.netsentience.repository.MonitoringLogRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.time.LocalDateTime;

@Service
public class NetworkMonitorService {
    private final DeviceRepository deviceRepository;
    private final MonitoringLogRepository logRepository;
    private final DeviceCheckRunner checkRunner;

    public NetworkMonitorService(DeviceRepository deviceRepository, MonitoringLogRepository logRepository, DeviceCheckRunner checkRunner) {
        this.deviceRepository = deviceRepository;
        this.logRepository = logRepository;
        this.checkRunner = checkRunner;
    }

    @Scheduled(fixedRate = 10000)
    public void checkDevices() {
        List<Device> allDevices = deviceRepository.findAll();

        System.out.println("--- Starting Monitoring Cycle for " + allDevices.size() + " devices ---");

        for (Device device : allDevices){
            // Fire and Forget
            checkRunner.checkDevice(device.getId());
        }
    }

    public DeviceUptimeDTO calculateUpTime(Long deviceId){
        List<MonitoringLog> logs = logRepository.findByDeviceId(deviceId);

        if (logs.isEmpty()){
            return new DeviceUptimeDTO(deviceId, 0, 0, 0.0);
        }

        long upCount = logs.stream().filter(MonitoringLog::isUp).count();
        double percentage = ((double) upCount / logs.size()) * 100.0;
        return new DeviceUptimeDTO(deviceId, logs.size(), upCount, percentage);
    }
}
