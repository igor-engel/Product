package org.example;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.example.info.Product;
import org.example.info.ProductCategory;
import org.example.repository.ProductRepository;
import org.example.repository.ProductRepositoryImpl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Scanner;
import java.util.UUID;

//{"id":"9e9b5741-e79c-4125-9cfc-2cad71afe166","name":"bread","description":"bread","category":"FOOD","manufactureDateTime":"2022-03-02T14:11:47.629744","manufacturer":"RBK","hasExpiryTime":true,"stock":10}

public class App {
    public static void main(String[] args) {
        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:C:/Users/Игорь/Desktop/Учеба/chinook/chinook.db")) {
            ProductRepository repository = new ProductRepositoryImpl(connection);
            repository.createTable();
            startProgram(repository);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static void startProgram(ProductRepository repository) {
        ObjectMapper mapper = new ObjectMapper()
                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
                .registerModule(new JavaTimeModule());

        System.out.println("Put commands:");

        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.print("> ");
            String line = scanner.nextLine();

            String[] items = line.split(" ");
            String cmd = items[0];

            try {
                switch (cmd) {
                    case "SAVE": {
                        Product info = mapper.readValue(items[1], Product.class);
                        repository.save(info);
                        break;
                    }
                    case "FIND": {
                        UUID uid = UUID.fromString(items[1]);
                        System.out.println(repository.findById(uid));
                        break;
                    }
                    case "DELETE": {
                        UUID uid = UUID.fromString(items[1]);
                        repository.deleteById(uid);
                        break;
                    }
                    case "ALL": {
                        ProductCategory category = ProductCategory.valueOf(items[1]);
                        repository.findAllByCategory(category).forEach(productsList -> {
                            try {
                                System.out.println("- " + mapper.writeValueAsString(productsList));
                            } catch (JsonProcessingException e) {
                                e.printStackTrace();
                            }
                        });
                        break;
                    }
                    case "END": {
                        return;
                    }
                    default: {
                        System.out.println("command id= " + cmd + " not supported.");
                    }
                }
            } catch (Exception ex) {
                System.out.println("Error = " + ex.getMessage());
            }
        }
    }
}

