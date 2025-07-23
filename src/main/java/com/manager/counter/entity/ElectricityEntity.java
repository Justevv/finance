package com.manager.counter.entity;

import lombok.Data;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "electricity_counter")
@Data
public class ElectricityEntity extends Counter {
}
