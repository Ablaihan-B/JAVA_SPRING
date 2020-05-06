package kz.iitu.csse.group34.entities;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Items")
public class Items extends BaseEntity{

    @Column(name = "name")
    private String name;

    @Column(name = "price")
    private double price;

    @Column(name = "characteristics")
    private String characteristics;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "categories")
    private Categories categories;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="author")
    private Users author;

    @Lob
    @Column(name="image")
    private byte[] image;

    @Column(name="file")
    private String file;
}
