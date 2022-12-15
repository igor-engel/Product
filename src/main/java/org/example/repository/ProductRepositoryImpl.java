package org.example.repository;

import org.example.info.Product;
import org.example.info.ProductCategory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class ProductRepositoryImpl implements ProductRepository {

    private final Connection connection;

    public ProductRepositoryImpl(Connection connection) {
        this.connection = connection;
    }

    public void createTable() {
        try (Statement statement = connection.createStatement()) {
            String script = "CREATE table 'products' (\n" +
                    "'id' varchar(36) primary key,\n" +
                    "'name' varchar(16) not null unique,\n" +
                    "'description' varchar(36),\n" +
                    "'category' varchar(36) not null,\n" +
                    "'manufactureDateTime' varchar(36) not null,\n" +
                    "'manufacturer' varchar(36) not null,\n" +
                    "'hasExpiryTime' varchar(16) not null,\n" +
                    "'stock' varchar(36) not null\n" +
                    ")";
            int updatedRows = statement.executeUpdate(script);
            System.out.println(".createTable completed updatedRows = " + updatedRows);
        } catch (Exception ex) {
            throw new RuntimeException("Failed to createTable error = " + ex.toString(), ex);
        }
    }

    @Override
    public Optional<Object> findById(UUID id) {
        try (Statement statement = connection.createStatement()) {
            try (ResultSet resultSet = statement.executeQuery("select * from products where id = '" + id.toString() + "'")) {
                if (!resultSet.next()) {
                    return Optional.empty();
                }

                return Optional.of(extractUserFromResultSet(resultSet));
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex.toString());
        }
    }

    @Override
    public void deleteById(UUID id) {
        try (Statement statement = connection.createStatement()) {
            int updatedRows = statement.executeUpdate("delete from products where id = '" + id + "'");
            System.out.println(".delete completed updatedRows = " + updatedRows);
        } catch (Exception ex) {
            throw new RuntimeException("Failed to .delete product id = " + id + " error = " + ex.toString(), ex);
        }
    }

    @Override
    public Product save(Product product) {
        try (Statement statement = connection.createStatement()) {
            String script = "";

            if (!findById(product.getId()).isPresent()) {
                script = String.format("insert into products (id, name, description, category, manufactureDateTime, manufacturer, hasExpiryTime, stock) VALUES ('%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s') ",
                        product.getId().toString(), product.getName(), product.getDescription(), product.getCategory().toString(),
                        product.getManufactureDateTime().toString(), product.getManufacturer(),
                        product.isHasExpiryTime(), product.getStock());
            } else {
                script = String.format("update products " +
                                "SET name = '%s', description = '%s', category = '%s', manufactureDateTime = '%s', manufacturer = '%s', hasExpiryTime = '%s', stock = '%s' WHERE id = '%s'",
                        product.getName(), product.getDescription(), product.getCategory().toString(),
                        product.getManufactureDateTime().toString(), product.getManufacturer(),
                        product.isHasExpiryTime(), product.getStock(), product.getId().toString());
            }

            int updatedRows = statement.executeUpdate(script);
            System.out.println(".save completed updatedRows = " + updatedRows);

            return (Product) findById(product.getId()).orElse(null);
        } catch (Exception ex) {
            throw new RuntimeException("Failed to .save product = " + product + " error = " + ex.toString(), ex);
        }
    }

    @Override
    public List<Product> findAllByCategory(ProductCategory category) {
        try (Statement statement = connection.createStatement()) {
            try (ResultSet resultSet = statement.executeQuery("select * from products where category = '"
                    + category + "'")) {
                List<Product> productsList = new ArrayList<>();

                while (resultSet.next()) {
                    Product product = extractUserFromResultSet(resultSet);
                    productsList.add(product);
                }
                return productsList;
            } catch (Exception ex) {
                throw new RuntimeException(ex.toString());
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex.toString());
        }
    }

    private Product extractUserFromResultSet(ResultSet resultSet) throws SQLException {
        String id = resultSet.getString("id");
        String name = resultSet.getString("name");
        String description = resultSet.getString("description");
        String statementCategory = resultSet.getString("category");
        String manufactureDateTime = resultSet.getString("manufactureDateTime");
        String manufacturer = resultSet.getString("manufacturer");
        String hasExpiryTime = resultSet.getString("hasExpiryTime");
        String stock = resultSet.getString("stock");

        Product product = new Product();
        product.setId(UUID.fromString(id));
        product.setName(name);
        product.setDescription(description);
        product.setCategory(ProductCategory.valueOf(statementCategory));
        product.setManufactureDateTime(LocalDateTime.parse(manufactureDateTime));
        product.setManufacturer(manufacturer);
        product.setHasExpiryTime(Boolean.parseBoolean(hasExpiryTime));
        product.setStock(Integer.parseInt(stock));

        return product;
    }
}
