package com.iftm.client;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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

    //Fernanda
    @DisplayName("Testar nome completo existente no banco de dados.")
    @Test
    public void testarNomeExistente() {
        assertThat(clientRepository.findByNameIgnoreCase("Gilberto Gil")).isPresent();

        assertThat(clientRepository.findByNameIgnoreCase("Gilberto Gil").get().getName())
                .isEqualToIgnoringCase("Gilberto Gil");
    }
    
    //Fernanda
    @DisplayName("Testar nome completo inexistente no banco de dados.")
    @Test
    public void testarNomeInexistente() {
        assertThrows(NoSuchElementException.class, () -> {
            clientRepository.findByNameIgnoreCase("Cricia").get();
        });
    }

    //Fernanda
    @DisplayName("Testar parte do nome existente no banco de dados.")
    @Test
    public void testarNomeComParteSimilarExistente() {
        List<Client> result = clientRepository.findByNameContainingIgnoreCase("Maria");
        assertEquals(2, result.size());

    }

    //Fernanda
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
}