package com.iftm.client.repositories;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import com.iftm.client.entities.Client;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

    // Fernanda: implementacao do método que busca um cliente pelo nome ignorando
    // maiúsculas e minúsculas
    @Query("SELECT c FROM Client c WHERE LOWER(c.name) = LOWER(:name)")
    Optional<Client> findByNameIgnoreCase(@Param("name") String name);

    // Fernanda: implementacao do método que busca clientes por parte do nome
    // ignorando maiúsculas e minúsculas
    @Query("SELECT c FROM Client c WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Client> findByNameContainingIgnoreCase(@Param("name") String name);

    // Isabela: implementacao do método que busca clientes com salário maior que o
    // valor informado
    @Query("SELECT c FROM Client c WHERE c.income > :income")
    List<Client> findByIncomeGreaterThan(Double income);

    // Isabela: implementacao do método que busca clientes com salário menor que o
    // valor informado
    @Query("SELECT c FROM Client c WHERE c.income < :income")
    List<Client> findByIncomeLessThan(Double income);

    // Isabela: implementacao do método que busca clientes com salário entre os
    // valores informados
    // @Param quando contém múltiplos parâmetros ou quando o nome do parâmetro no
    // método não corresponde exatamente ao nome do parâmetro na consulta. BETWEEN -
    // Os limites pertencem ao intervalo
    @Query("SELECT c FROM Client c WHERE c.income BETWEEN :incomeInit AND :incomeEnd")
    List<Client> findByIncomeBetween(@Param("incomeInit") Double incomeInit, @Param("incomeEnd") Double incomeEnd);

    // Isabela: implementacao do método que busca clientes com salário igual ao valor fornecido
    @Query("SELECT c FROM Client c WHERE c.income = :income")
    List<Client> findByIncomeEqual(@Param("income") Double income);

    // Ana: implementando método que busca clientes cuja data de nascimento está
    // entre os valores informados
    List<Client> findByBirthDateBetween(Instant dataInicio, Instant dataTermino);

    // Ana: implementando método que deleta o registro de um cliente
    @Modifying
    @Query("DELETE FROM Client c WHERE c.id = :id")
    public void deleteById(@NonNull Integer id);    
}
