package com.manning.liveproject.simplysend.entity;

import lombok.*;

import javax.persistence.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EqualsAndHashCode(callSuper = true)
@Table(name = "USER_ACCOUNT")
@ToString(callSuper = true)
public class UserAccount extends BaseEntity {

    @Id
    @GeneratedValue(generator = "user_account_seq", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "user_account_seq", sequenceName = "user_account_id_seq", initialValue = 1, allocationSize = 1)
    protected Long id;

    @Column(unique = true)
    private String username;
    @ToString.Exclude
    private String password;
    private Boolean enabled;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "username", referencedColumnName = "email", insertable = false, updatable = false)
    private User user;
}
