package de.neoventus.persistence.repository;

import de.neoventus.persistence.entity.Workingplan;
import de.neoventus.persistence.repository.advanced.NVWorkingPlanRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkingPlanRepository extends CrudRepository<Workingplan, String>, NVWorkingPlanRepository{



}
