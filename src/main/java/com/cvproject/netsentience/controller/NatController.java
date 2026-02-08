package com.cvproject.netsentience.controller;

import com.cvproject.netsentience.model.Device;
import com.cvproject.netsentience.model.NatRule;
import com.cvproject.netsentience.repository.DeviceRepository;
import com.cvproject.netsentience.repository.NatRuleRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/nat")
public class NatController {
    private final NatRuleRepository natRepository;
    private final DeviceRepository deviceRepository;

    public NatController(NatRuleRepository natRepository, DeviceRepository deviceRepository) {
        this.natRepository = natRepository;
        this.deviceRepository = deviceRepository;
    }

    // GET /api/nat/device/1  -> Show all rules for Router 1
    @GetMapping("/deviceId/{deviceId}")
    public List<NatRule> getRules(@PathVariable Long deviceId){
        return natRepository.findByDeviceId(deviceId);
    }

    // POST /api/nat/device/1 -> Add a new Rule
    @PostMapping("/device/{deviceId}")
    public NatRule addRule(@PathVariable Long deviceId, @RequestBody NatRule rule){
        Device router = deviceRepository.findById(deviceId)
                .orElseThrow(() -> new RuntimeException("Router not found"));

        rule.setDevice(router);
        return natRepository.save(rule);
    }

    // DELETE /api/nat/{ruleId} -> Delete a rule
    @DeleteMapping("/{ruleId}")
    public void deleteRule(@PathVariable Long ruleId){
        natRepository.deleteById(ruleId);
    }
}
