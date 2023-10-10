package com.moni.challenge.repositories;

import com.moni.challenge.models.RequestFund;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RequestFundRepository extends JpaRepository<RequestFund, Long> {
}
