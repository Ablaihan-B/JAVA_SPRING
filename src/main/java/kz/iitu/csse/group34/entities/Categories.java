package kz.iitu.csse.group34.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Categories")
public class Categories extends BaseEntity{

    @Column(name = "name")
    private String name;
}
