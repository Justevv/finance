package com.manager.finance.entity;

import com.manager.finance.dto.CategoryDTO;
import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "Category")
@Data
public class CategoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String name;
    @OneToOne(fetch = FetchType.EAGER)
    private CategoryEntity parentCategory;

    public CategoryEntity() {
    }

    public CategoryEntity(CategoryDTO categoryDTO) {
        this.name = categoryDTO.getName();
        this.parentCategory = categoryDTO.getParentCategory();
    }
}
