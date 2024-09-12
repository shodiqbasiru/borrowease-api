package com.msfb.borrowease.service;

import com.msfb.borrowease.entity.Job;

import java.util.List;

public interface JobService {
    Job createJob(Job job);
    Job getById(String id);
    List<Job> getAllJobs();
    Job updateJob(Job job);
}
