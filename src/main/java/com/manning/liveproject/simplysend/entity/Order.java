package com.manning.liveproject.simplysend.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
@Table(name = "ORDERS")
@ToString(callSuper = true)
public class Order extends BaseEntity {

    @Id
    @GeneratedValue(generator = "order_seq", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "order_seq", sequenceName = "orders_id_seq", initialValue = 1, allocationSize = 1)
    protected Long id;

    private String status;
    private String reason;
    private String comment;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "ORDERS_ITEM",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "item_id")
    )
    private Set<Item> items;

    @ToString.Exclude
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", referencedColumnName = "id")
    private User owner;
}
