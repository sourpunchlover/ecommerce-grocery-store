package org.yearup.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;
import org.yearup.models.Product;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@Sql(scripts = "classpath:test-insert-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class ProductRepositoryTest
{
    @Autowired
    private ProductRepository productRepository;

    @Test
    public void getById_shouldReturn_theCorrectProduct()
    {
        // arrange
        int productId = 1;

        // act
        Product actual = productRepository.findById(productId).orElse(null);

        // assert
        assertNotNull(actual, "Because product 1 should exist in the test database.");
        assertEquals(499.99, actual.getPrice(), 0.001, "Because I tried to get product 1 from the database.");
    }

    @Test
    public void search_withNoFilters_shouldReturn_allProducts()
    {
        // arrange
        int expectedCount = 12;

        // act
        List<Product> actual = productRepository.findAll();

        // assert
        assertEquals(expectedCount, actual.size(), "Because search with no filters should return all products, not just featured ones.");
    }

    @Test
    public void update_shouldSave_stockCorrectly()
    {
        // arrange
        Product product = productRepository.findAll().get(0);
        int productId = product.getProductId();
        product.setStock(999);

        // act
        productRepository.save(product);
        Product updated = productRepository.findById(productId).orElse(null);

        // assert
        assertEquals(999, updated.getStock(), "Because updating stock should persist to the database.");
    }
}
