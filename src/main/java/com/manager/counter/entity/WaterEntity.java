package com.manager.counter.entity;

import lombok.Data;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "water_counter")
@Data
public class WaterEntity extends Counter{
}
