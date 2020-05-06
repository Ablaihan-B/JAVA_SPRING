package kz.iitu.csse.group34.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Users")
public class Users extends BaseEntity{

    @Column(name = "email")
    private String email;

    @Column(name = "name")
    private String name;

    @Column(name = "surname")
    private String surname;

    @Column(name = "isActive")
    private Boolean isActive;

    @Column(name = "password")
    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Roles> roles;
}
