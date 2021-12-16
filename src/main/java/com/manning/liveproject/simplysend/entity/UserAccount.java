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

    @Column(unique = true)
    private String username;
    @ToString.Exclude
    private String password;
    private Boolean enabled;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "username", referencedColumnName = "email", insertable = false, updatable = false)
    private UserProfile profile;
}
