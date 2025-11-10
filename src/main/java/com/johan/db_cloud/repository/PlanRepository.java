package com.johan.db_cloud.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.johan.db_cloud.model.Plan;

@Repository
public interface PlanRepository extends JpaRepository<Plan,Long>{
    
}
