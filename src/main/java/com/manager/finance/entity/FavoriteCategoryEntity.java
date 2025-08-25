package com.manager.finance.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "favorite_category")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FavoriteCategoryEntity implements Serializable {
    @Id
    private UUID guid;
    @ManyToOne
    private CategoryEntity category;
    @ManyToOne
    private UserEntity user;

}
