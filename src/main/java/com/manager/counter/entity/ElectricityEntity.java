package com.manager.counter.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "electricity_counter")
@Data
public class ElectricityEntity extends Counter {
}
