package com.manning.liveproject.simplysend.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@NoArgsConstructor
@Entity
@EqualsAndHashCode(callSuper = true)
@Table(name = "ITEM")
@ToString(callSuper = true)
public class Item extends BaseEntity {

    private String type;
    private String name;
    private Integer price;
    private String description;
}
