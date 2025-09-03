package com.manager.finance.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "Category")
@Getter
@Setter
public class CategoryEntity extends CrudEntity {
    private String name;
    @ManyToOne
    private CategoryEntity parentCategory;


}
