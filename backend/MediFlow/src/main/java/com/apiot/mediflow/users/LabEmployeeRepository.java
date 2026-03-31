package com.apiot.mediflow.users;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LabEmployeeRepository extends JpaRepository<LabEmployee, Long> {
}
