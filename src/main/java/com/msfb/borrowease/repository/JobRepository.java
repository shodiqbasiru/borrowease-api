package com.msfb.borrowease.repository;

import com.msfb.borrowease.entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobRepository extends JpaRepository<Job, String> {
}
