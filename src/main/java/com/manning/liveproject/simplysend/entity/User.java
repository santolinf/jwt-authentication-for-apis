package com.manning.liveproject.simplysend.entity;

import com.manning.liveproject.simplysend.api.enums.Role;
import lombok.*;

import javax.persistence.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EqualsAndHashCode(callSuper = true)
@Table(name = "USER_PROFILE")
@ToString(callSuper = true)
public class User extends BaseEntity {

    @Id
    @GeneratedValue(generator = "user_seq", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "user_seq", sequenceName = "user_profile_id_seq", initialValue = 1, allocationSize = 1)
    protected Long id;

    private String firstName;
    private String lastName;
    @Column(unique = true)
    private String email;
    private Integer age;
    private String phone;
    private String address;
    private String tag;

    @Enumerated(EnumType.STRING)
    private Role role;

    @ManyToOne(fetch = FetchType.LAZY)
    private User manager;
}
