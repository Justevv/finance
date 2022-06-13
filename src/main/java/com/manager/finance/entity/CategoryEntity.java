package com.manager.finance.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "Category")
@Data
public class CategoryEntity implements CrudEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String name;
    @ManyToOne
    private CategoryEntity parentCategory;
    @ManyToOne
    private UserEntity user;

}
