package com.cvproject.netsentience.repository;

import com.cvproject.netsentience.model.MonitoringLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface MonitoringLogRepository extends JpaRepository<MonitoringLog, Long> {
    List<MonitoringLog> findByDeviceId(Long deviceId);

    @Transactional
    void deleteByDeviceId(Long deviceId);
}
