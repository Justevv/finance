package com.manager.finance.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "Category")
@Data
public class CategoryEntity extends CrudEntity {
    private String name;
    @ManyToOne
    private CategoryEntity parentCategory;


}
