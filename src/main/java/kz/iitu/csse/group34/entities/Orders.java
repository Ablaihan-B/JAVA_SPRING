package kz.iitu.csse.group34.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Orders")
public class Orders extends BaseEntity{

    @Column(name = "city")
    private String city;

    @Column(name = "address")
    private String address;

    @Column(name = "delivery")
    private String delivery;

    @Column(name = "payment")
    private String payment;

    @Column(name = "price")
    private double price;
/*
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="item")
    private List<Items> items;*/

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="author")
    private Users author;
}
