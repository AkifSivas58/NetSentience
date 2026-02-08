package com.cvproject.netsentience.controller;

import com.cvproject.netsentience.dto.DeviceUptimeDTO;
import com.cvproject.netsentience.model.Device;
import com.cvproject.netsentience.repository.DeviceRepository;
import com.cvproject.netsentience.repository.MonitoringLogRepository;
import com.cvproject.netsentience.service.NetworkMonitorService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/devices")
public class DeviceController {

    private final DeviceRepository deviceRepository;
    private final NetworkMonitorService networkService;

    public DeviceController(DeviceRepository deviceRepository, NetworkMonitorService networkService) {
        this.deviceRepository = deviceRepository;
        this.networkService = networkService;
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
    public void deleteDevice(@PathVariable Long id) {
        deviceRepository.deleteById(id);
    }


    // PUT /api/devices/{id} - Update an existing device
    @PutMapping("/{id}")
    public Device updateDevice(@PathVariable Long id, @Valid @RequestBody Device deviceDetails){
        System.out.println("RECEIVED UPDATE REQUEST: " + deviceDetails.getIpAddress());

        Device device = deviceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Device not found"));

        device.setName(deviceDetails.getName());
        device.setIpAddress(device.getIpAddress());
        device.setType(deviceDetails.getType());

        // Note: We do NOT update 'status' here. The monitoring service does that.
        return deviceRepository.save(device);
    }

}