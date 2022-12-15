package org.example;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.example.info.Product;
import org.example.info.ProductCategory;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
    @Test
    public void shouldAnswerWithTrue() throws JsonProcessingException {
        Product product = new Product(UUID.randomUUID(), "bread", "bread", ProductCategory.FOOD, LocalDateTime.now(), "RBK", true, 10);

        ObjectMapper mapper = new ObjectMapper().configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false).registerModule(new JavaTimeModule());
        String serializedMessage = mapper.writeValueAsString(product);

        System.out.println(serializedMessage);
    }
}
