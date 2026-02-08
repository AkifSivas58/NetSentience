package com.cvproject.netsentience.service;

import com.cvproject.netsentience.dto.DeviceUptimeDTO;
import com.cvproject.netsentience.model.Device;
import com.cvproject.netsentience.model.MonitoringLog;
import com.cvproject.netsentience.repository.DeviceRepository;
import com.cvproject.netsentience.repository.MonitoringLogRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class NetworkMonitorServiceTest {
    @Mock
    private MonitoringLogRepository logRepository;

    @Mock
    private DeviceRepository deviceRepository;

    @InjectMocks
    private NetworkMonitorService networkService;

    @Test
    public void testCalculateUptime(){
        Long deviceId = 1L;
        Device mockDevice = new Device();
        mockDevice.setId(deviceId);

        List<MonitoringLog> fakeLogs = Arrays.asList(
                new MonitoringLog(mockDevice, true, null),
                new MonitoringLog(mockDevice, true, null),
                new MonitoringLog(mockDevice, true, null),
                new MonitoringLog(mockDevice, true, null),
                new MonitoringLog(mockDevice, false, null)
        );

        // "When someone asks for logs for Device 1, return my fake list."
        when(logRepository.findByDeviceId(deviceId)).thenReturn(fakeLogs);

        DeviceUptimeDTO result = networkService.calculateUpTime(deviceId);

        // 4 successes / 5 total = 80.0%
        assertEquals(80.0, result.getUpTimePercentage(), "Uptime calculation should be 80%");
        assertEquals(5, result.getTotalChecks(), "Total checks should be 5");
        assertEquals(4, result.getUpChecks(), "Up checks should be 4");
    }
}
