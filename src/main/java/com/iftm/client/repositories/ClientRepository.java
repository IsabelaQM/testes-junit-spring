package com.iftm.client.repositories;

import java.time.Instant;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.iftm.client.entities.Client;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

    Client findByNameIgnoreCase(String name);

    List<Client> findByNameContainingIgnoreCase(String name);

    List<Client> findByIncomeGreaterThan(Double income);

    List<Client> findByIncomeLessThan(Double income);

    List<Client> findByIncomeBetween(Double incomeInit, Double incomeEnd);

    List<Client> findByBirthDateBetween(Instant dateInit, Instant dateEnd);
}
