package com.manning.liveproject.simplysend.entity;

import lombok.*;

import javax.persistence.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EqualsAndHashCode(callSuper = true)
@Table(name = "ITEM")
@ToString(callSuper = true)
public class Item extends BaseEntity {

    @Id
    @GeneratedValue(generator = "item_seq", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "item_seq", sequenceName = "item_id_seq", initialValue = 1, allocationSize = 1)
    protected Long id;

    private String type;
    private String name;
    private Integer price;
    private String description;
}
