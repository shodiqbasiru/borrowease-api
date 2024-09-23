package com.msfb.borrowease.service.impl;

import com.msfb.borrowease.entity.Job;
import com.msfb.borrowease.repository.JobRepository;
import com.msfb.borrowease.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class JobServiceImpl implements JobService {

    private final JobRepository repository;

    @Autowired
    public JobServiceImpl(JobRepository repository) {
        this.repository = repository;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Job createJob(Job job) {
        return repository.saveAndFlush(job);
    }

    @Transactional(readOnly = true)
    @Override
    public Job getById(String id) {
        return repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Job not found"));
    }

    @Transactional(readOnly = true)
    @Override
    public List<Job> getAllJobs() {
        return repository.findAll();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Job updateJob(Job job) {
        Job currentJob = getById(job.getId());
        currentJob.setJobName(job.getJobName());
        currentJob.setSalary(job.getSalary());
        return currentJob;
    }
}
