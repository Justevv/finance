package com.manager.finance.infrastructure.adapter.out.persistence.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "favorite_category")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
public class FavoriteCategoryEntity implements Serializable {
    @Id
    private UUID id;
    @ManyToOne
    private CategoryEntity category;
    private UUID userId;

}
