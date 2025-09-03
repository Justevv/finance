package com.manager.finance.entity;

import com.manager.user.entity.UserEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "favorite_category")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class FavoriteCategoryEntity implements Serializable {
    @Id
    private UUID id;
    @ManyToOne
    private CategoryEntity category;
    @ManyToOne
    private UserEntity user;

}
