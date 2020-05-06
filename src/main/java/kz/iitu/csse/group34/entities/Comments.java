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
@Table(name = "Comments")
public class Comments extends BaseEntity{
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="author")
    private Users author;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="item")
    private Items items;

    @Column(name = "comment")
    private String comment;
}
