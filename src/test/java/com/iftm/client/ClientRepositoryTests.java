package com.iftm.client;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
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
        // O banco possui 10 registros com clientes com salário > 2000
        int tamanhoEsperado = 10;

        double incomeLimiteInferior = 2000.00;
        List<Client> result = clientRepository.findByIncomeGreaterThan(incomeLimiteInferior);
        if (result.isEmpty()) {
            throw new NoSuchElementException("Nenhum cliente encontrado com salário maior que " + incomeLimiteInferior);
        }
        assertTrue(result.stream().allMatch(client -> client.getIncome() > incomeLimiteInferior));

        int tamanhoReal = result.size();
        assertEquals(tamanhoEsperado, tamanhoReal, "O tamanho da lista retornada não corresponde ao esperado.");
    }

    // Isabela
    @DisplayName("Testar busca de clientes com salário menor que o valor informado.")
    @Test
    public void testarSalarioMenorQueLimiteSuperior() {
        // O banco possui 3 registros com clientes com salário < 2000
        int tamanhoEsperado = 3;

        Double incomeLimiteSuperior = 2000.00;
        List<Client> result = clientRepository.findByIncomeLessThan(incomeLimiteSuperior);
        if (result.isEmpty()) {
            throw new NoSuchElementException("Nenhum cliente encontrado com salário menor que " + incomeLimiteSuperior);
        }
        // Verifica se todos os clientes possuem salário menor que o informado
        assertTrue(result.stream().allMatch(client -> client.getIncome() < incomeLimiteSuperior));

        int tamanhoReal = result.size();
        assertEquals(tamanhoEsperado, tamanhoReal, "O tamanho da lista retornada não corresponde ao esperado.");
    }

    // Isabela
    @DisplayName("Testar busca de clientes com salário entre os valores informados.")
    @Test
    public void testarSalarioEntreValores() {
        // O banco possui 6 registros com clientes com salário entre 1000 e 3000
        int tamanhoEsperado = 6;

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

        int tamanhoReal = result.size();
        assertEquals(tamanhoEsperado, tamanhoReal, "O tamanho da lista retornada não corresponde ao esperado.");
    }

    // Isabela
    @DisplayName("Testar busca de clientes com salário igual ao valor fornecido para um salário existente")
    @Test
    public void testarSalarioIgualExistente() {
        // O banco de dados possui 3 registros de clientes com salário = 1500
        int tamanhoEsperado = 3; 

        double income = 1500.00;
        List<Client> result = clientRepository.findByIncomeEqual(income);
        if (result.isEmpty()) {
            throw new NoSuchElementException("Nenhum cliente encontrado com salário igual a " + income);
        }
        // Verifica se todos os clientes possuem salário igual ao informado
        assertTrue(result.stream().allMatch(client -> client.getIncome().equals(income)));

        int tamanhoReal = result.size();
        assertEquals(tamanhoEsperado, tamanhoReal, "O tamanho da lista retornada não corresponde ao esperado.");
    }

    // Ana
    @DisplayName("Testar a busca de clientes cujas datas de nascimento estão dentro do intervalo especificado")
    @Test
    public void testarFindByBirthDateBetweenClientesValidos() {
        Instant dataInicio = Instant.parse("1970-01-01T00:00:00Z");
        Instant dataTermino = Instant.parse("2000-01-01T00:00:00Z");

        List<Client> clients = clientRepository.findByBirthDateBetween(dataInicio, dataTermino);

        assertNotNull(clients);
        assertThat(clients).hasSize(5);  // espera-se que retorne 6 clientes
        assertThat(clients).extracting("name").containsExactlyInAnyOrder("Lázaro Ramos", "Carolina Maria de Jesus", "Djamila Ribeiro", "Jose Saramago", "Silvio Almeida"); // nomes dos clientes para confirmar o retorno correto
    }

    // Ana
    @DisplayName("Testar a busca de clientes cujas datas de nascimento não estão dentro do intervalo especificado")
    @Test
    public void testarFindByBirthDateBetweenClientesInvalidos() {
        Instant dataInicio = Instant.parse("1900-01-01T00:00:00Z");
        Instant dataTermino = Instant.parse("1910-01-01T00:00:00Z");

        List<Client> clients = clientRepository.findByBirthDateBetween(dataInicio, dataTermino);

        // assertNotNull(clients);
        assertThat(clients).isEmpty();  // espera-se que não retorne nenhum cliente
    }

    // Ana
    @DisplayName("Testar a busca que retorna somente um cliente cuja data de nascimento está dentro do invervalo especificado")
    @Test
    public void testarFindByBirthDateBetweenUmCliente() {
        Instant dataInicio = Instant.parse("1956-01-01T00:00:00Z");
        Instant dataTermino = Instant.parse("1956-12-31T23:59:59Z");

        List<Client> clients = clientRepository.findByBirthDateBetween(dataInicio, dataTermino);

        // assertNotNull(clients);
        assertThat(clients).hasSize(2);  // espera-se que retorne 2 cliente
        assertThat(clients).extracting("name").containsExactly("Yuval Noah Harari", "Chimamanda Adichie"); // nomes dos clientes para confirmar o retorno correto
    }

    // Ana
    @DisplayName("Testar se a exclusão por id realmente apaga um resgistro existente")
    @Test
    public void testarExcluirPorId() {
        Optional<Client> client = clientRepository.findByNameIgnoreCase("Conceição Evaristo"); // encontra o cliente pelo nome utilizando o método
        assertThat(client).isPresent(); // verifica se o cliente está presente no banco
        Client clientExistente = client.get(); // pega o objeto cliente
        Long clienteId = clientExistente.getId(); // pega o id do cliente pelo objeto

        clientRepository.delete(clientExistente);

        assertThat(clientRepository.existsById(clienteId)).isFalse(); // verifica se o cliente foi deletado
        assertThat(clientRepository.count()).isEqualTo(12); // verifica se o número de registros diminuiu 1
    }

}
