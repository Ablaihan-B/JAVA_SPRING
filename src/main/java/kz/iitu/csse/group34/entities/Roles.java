package kz.iitu.csse.group34.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Roles")
public class Roles extends BaseEntity implements GrantedAuthority {

    @Column(name = "role")
    private String role;

    @Override
    public String getAuthority() {
        return this.role;
    }
}
