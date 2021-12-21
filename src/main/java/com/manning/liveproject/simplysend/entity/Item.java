package com.manning.liveproject.simplysend.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
