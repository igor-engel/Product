package org.example.repository;

import org.example.info.Product;
import org.example.info.ProductCategory;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductRepository {

    /**
     * Find product by id
     *
     * @param id - product id
     * @return null if product doesn't exist
     */
    Optional<Object> findById(UUID id);

    /**
     * Delete product by id
     *
     * @param id - product id
     * @return
     */
    void deleteById(UUID id);

    /**
     * Save or update product.
     * If product id == null or id doesn't already exist in db then create new product
     * or update existing product else
     *
     * @param product - product
     * @return created or updated product
     */
    Product save(Product product);


    /**
     * Find all products by current category.
     * ProductCategory is enum.
     *
     * @param category - product category
     * @return list of products
     */
    List<Product> findAllByCategory(ProductCategory category);

    void createTable();

    //Product update(Product info);
}


