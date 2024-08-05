package com.iftm.client.repositories;

import java.time.Instant;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.iftm.client.entities.Client;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

    @Query("SELECT c FROM Client c WHERE LOWER(c.name) = LOWER(:name)")
    Client findByNameIgnoreCase(String name);

    @Query("SELECT c FROM Client c WHERE LOWER(c.name) LIKE %:name%")
    List<Client> findByNameContainingIgnoreCase(String name);

    @Query("SELECT c FROM Client c WHERE c.income > :income")
    List<Client> findByIncomeGreaterThan(Double income);

    @Query("SELECT c FROM Client c WHERE c.income < :income")
    List<Client> findByIncomeLessThan(Double income);

    @Query("SELECT c FROM Client c WHERE c.salary BETWEEN :incomeInit AND :incomeEnd")
    List<Client> findByIncomeBetween(Double incomeInit, Double incomeEnd);

    List<Client> findByBirthDateBetween(Instant dateInit, Instant dateEnd);
}
