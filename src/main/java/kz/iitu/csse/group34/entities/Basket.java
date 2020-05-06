package kz.iitu.csse.group34.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Basket")
public class Basket extends BaseEntity {
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="author")
    private Users author;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="item")
    private Items items;
}
