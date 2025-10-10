package com.apiot.mediflow.test;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MedicalTestRepository extends JpaRepository<MedicalTest, Long> {

    @Query("SELECT mt FROM MedicalTest mt JOIN FETCH mt.category")
    List<MedicalTest> findAllWithCategory();
}
