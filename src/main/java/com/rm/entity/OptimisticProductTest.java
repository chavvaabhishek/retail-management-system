package com.rm.entity;


import jakarta.persistence.*;
import org.springframework.data.jpa.repository.Lock;

@Entity
@Table(name = "optimistic_product_test")
public class OptimisticProductTest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Integer stock;

   // @Version

    private Long version;

    public OptimisticProductTest() {
    }

    public OptimisticProductTest(String name, Integer stock) {
        this.name = name;
        this.stock = stock;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getStock() {
        return stock;
    }

    public Long getVersion() {
        return version;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }
}