package com.iftm.client;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;

import com.iftm.client.entities.Client;
import com.iftm.client.repositories.ClientRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.ANY)
public class ClientRepositoryTests {

    @Autowired
    private ClientRepository clientRepository;

    // Fernanda
    @DisplayName("Testar nome completo existente no banco de dados.")
    @Test
    public void testarNomeExistente() {
        assertThat(clientRepository.findByNameIgnoreCase("Gilberto Gil")).isPresent();

        assertThat(clientRepository.findByNameIgnoreCase("Gilberto Gil").get().getName())
                .isEqualToIgnoringCase("Gilberto Gil");
    }

    // Fernanda
    @DisplayName("Testar nome completo inexistente no banco de dados.")
    @Test
    public void testarNomeInexistente() {
        assertThrows(NoSuchElementException.class, () -> {
            clientRepository.findByNameIgnoreCase("Cricia").get();
        });
    }

    // Fernanda
    @DisplayName("Testar parte do nome existente no banco de dados.")
    @Test
    public void testarNomeComParteSimilarExistente() {
        List<Client> result = clientRepository.findByNameContainingIgnoreCase("Maria");
        assertEquals(2, result.size());

    }

    // Fernanda
    @DisplayName("Testar nome completo inexistente no banco de dados.")
    @Test
    public void testarNomeComParteSimilarInexistente() {
        assertThrows(NoSuchElementException.class, () -> {
            List<Client> result = clientRepository.findByNameContainingIgnoreCase("Medeiros");
            if (result.isEmpty()) {
                throw new NoSuchElementException("Nenhum cliente encontrado com essa parte do nome.");
            }
        });
    }

    // Isabela
    @DisplayName("Testar busca de clientes com salário maior que o valor informado.")
    @Test
    public void testarSalarioMaiorQueLimiteInferior() {
        double incomeLimiteInferior = 2000.00;
        List<Client> result = clientRepository.findByIncomeGreaterThan(incomeLimiteInferior);
        if (result.isEmpty()) {
            throw new NoSuchElementException("Nenhum cliente encontrado com salário maior que " + incomeLimiteInferior);
        }
        // Verifica se todos os clientes possuem salário maior que o informado
        assertTrue(result.stream().allMatch(client -> client.getIncome() > incomeLimiteInferior));
    }

    // Isabela
    @DisplayName("Testar busca de clientes com salário menor que o valor informado.")
    @Test
    public void testarSalarioMenorQueLimiteSuperior() {
        Double incomeLimiteSuperior = 2000.00;
        List<Client> result = clientRepository.findByIncomeLessThan(incomeLimiteSuperior);
        if (result.isEmpty()) {
            throw new NoSuchElementException("Nenhum cliente encontrado com salário menor que " + incomeLimiteSuperior);
        }
        // Verifica se todos os clientes possuem salário menor que o informado
        assertTrue(result.stream().allMatch(client -> client.getIncome() < incomeLimiteSuperior));
    }

    // Isabela
    @DisplayName("Testar busca de clientes com salário entre os valores informados.")
    @Test
    public void testarSalarioEntreValores() {
        double incomeLimiteInferior = 1000.00;
        double incomeLimiteSuperior = 3000.00;
        List<Client> result = clientRepository.findByIncomeBetween(incomeLimiteInferior, incomeLimiteSuperior);
        if (result.isEmpty()) {
            throw new NoSuchElementException("Nenhum cliente encontrado com salário entre " + incomeLimiteInferior
                    + " e " + incomeLimiteSuperior);
        }

        // Log dos resultados
        System.out.println("Número de clientes encontrados: " + result.size());
        result.forEach(
                client -> System.out.println("Cliente: " + client.getName() + ", Salário: " + client.getIncome()));

        // Verifica se todos os clientes na lista têm salário dentro dos limites
        // especificados
        assertTrue(result.stream().allMatch(
                client -> client.getIncome() >= incomeLimiteInferior && client.getIncome() <= incomeLimiteSuperior));
    }

    // Isabela
    @DisplayName("Testar busca de clientes com salário igual ao valor fornecido para um salário existente")
    @Test
    public void testarSalarioIgualExistente() {
        // Alguns registros do banco de dados têm o salário de 1500
        double income = 1500.00;
        List<Client> result = clientRepository.findByIncomeEqual(income);
        if (result.isEmpty()) {
            throw new NoSuchElementException("Nenhum cliente encontrado com salário igual a " + income);
        }
        // Verifica se todos os clientes possuem salário igual ao informado
        assertTrue(result.stream().allMatch(client -> client.getIncome().equals(income)));
    }

}
