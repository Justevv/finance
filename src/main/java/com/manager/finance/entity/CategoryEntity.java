package com.manager.finance.entity;

import lombok.Data;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "Category")
@Data
public class CategoryEntity extends CrudEntity {
    private String name;
    @ManyToOne
    private CategoryEntity parentCategory;


}
