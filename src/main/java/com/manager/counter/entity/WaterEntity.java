package com.manager.counter.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "water_counter")
@Data
public class WaterEntity extends Counter{
}
