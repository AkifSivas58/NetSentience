package com.cvproject.netsentience.service;

import com.cvproject.netsentience.model.Device;
import com.cvproject.netsentience.repository.DeviceRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.io.IOException;
import java.net.InetAddress;
import java.time.LocalDateTime;

@Service
public class NetworkMonitorService {
    private final DeviceRepository deviceRepository;

    public NetworkMonitorService(DeviceRepository deviceRepository){
        this.deviceRepository = deviceRepository;
    }

    @Scheduled(fixedRate = 10000)
    public void checkDevices(){
        List<Device> devices = deviceRepository.findAll();

        for (Device device : devices){
            boolean isReachable = ping(device.getIpAddress());

            String newStatus = isReachable ? "UP" : "DOWN";
            device.setStatus(newStatus);
            device.setLastChecked(LocalDateTime.now());

            System.out.println("Pinging " + device.getName() + " (" + device.getIpAddress() + ") -> " + newStatus);
        }

        deviceRepository.saveAll(devices);
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
