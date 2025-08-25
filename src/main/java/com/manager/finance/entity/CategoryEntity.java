package com.manager.finance.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "Category")
@Data
public class CategoryEntity extends CrudEntity {
    private String name;
    @ManyToOne
    private CategoryEntity parentCategory;


}
