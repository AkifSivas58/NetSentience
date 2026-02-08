package com.cvproject.netsentience.repository;

import com.cvproject.netsentience.model.NatRule;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface NatRuleRepository extends JpaRepository<NatRule, Long> {
    List<NatRule> findByDeviceId(Long deviceId);
}
