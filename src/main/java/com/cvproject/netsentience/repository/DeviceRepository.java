package com.cvproject.netsentience.repository;

import com.cvproject.netsentience.model.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface DeviceRepository extends JpaRepository<Device, Long> {
    @Modifying
    @Transactional
    @Query("update Device d set d.status = ?1, d.lastChecked = ?2 where d.id = ?3")
    void updateStatus(String status, LocalDateTime lastChecked, Long id);
}
