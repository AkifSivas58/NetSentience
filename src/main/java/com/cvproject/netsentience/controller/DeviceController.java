package com.cvproject.netsentience.controller;

import com.cvproject.netsentience.dto.DeviceUptimeDTO;
import com.cvproject.netsentience.model.Device;
import com.cvproject.netsentience.repository.DeviceRepository;
import com.cvproject.netsentience.repository.MonitoringLogRepository;
import com.cvproject.netsentience.repository.NatRuleRepository;
import com.cvproject.netsentience.service.NetworkMonitorService;
import jakarta.validation.Valid;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/devices")
public class DeviceController {

    private final DeviceRepository deviceRepository;
    private final NetworkMonitorService networkService;
    private final MonitoringLogRepository logRepository;
    private final NatRuleRepository natRepository;

    public DeviceController(DeviceRepository deviceRepository, NetworkMonitorService networkService,
                            MonitoringLogRepository logRepository, NatRuleRepository natRepository) {
        this.deviceRepository = deviceRepository;
        this.networkService = networkService;
        this.logRepository = logRepository;
        this.natRepository = natRepository;
    }

    @GetMapping("/{id}/uptime")
    public DeviceUptimeDTO getDeviceUptime(@PathVariable Long id){
        return networkService.calculateUpTime(id);
    }

    // GET /api/devices - List all devices
    @GetMapping
    public List<Device> getAllDevices() {
        return deviceRepository.findAll();
    }

    // POST /api/devices - Create a new device
    @PostMapping
    public Device createDevice(@Valid @RequestBody Device device) {
        return deviceRepository.save(device);
    }

    // DELETE /api/devices/{id}
    @DeleteMapping("/{id}")
    @Transactional
    public void deleteDevice(@PathVariable Long id) {
        if (!deviceRepository.existsById(id)) {
            throw new RuntimeException("Device not found");
        }

        logRepository.deleteByDeviceId(id);

        natRepository.deleteByDeviceId(id);

        deviceRepository.deleteById(id);

        System.out.println("Deleted device and history for ID: " + id);
    }


    // PUT /api/devices/{id} - Update an existing device
    @PutMapping("/{id}")
    public Device updateDevice(@PathVariable Long id, @Valid @RequestBody Device deviceDetails){
        System.out.println("RECEIVED UPDATE REQUEST: " + deviceDetails.getIpAddress());

        Device device = deviceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Device not found"));

        device.setName(deviceDetails.getName());
        device.setIpAddress(deviceDetails.getIpAddress());
        device.setType(deviceDetails.getType());

        // Note: We do NOT update 'status' here. The monitoring service does that.
        return deviceRepository.save(device);
    }

}