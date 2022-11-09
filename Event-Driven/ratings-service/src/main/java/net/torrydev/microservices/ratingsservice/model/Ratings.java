package net.torrydev.microservices.ratingsservice.model;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Objects;


@AllArgsConstructor @NoArgsConstructor @Builder
@Getter
@Setter
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "ratings", uniqueConstraints = @UniqueConstraint(columnNames = {"productid", "userid"}))
public class Ratings {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long Id;  // Rate_Id
    @Column(name = "userid")
    Long userid; // i.e. the rater
    @Column(name = "rating")
    Integer rating;
    @Column(name = "productid")
    Long productid;  // ProductId - id of the product rated

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Ratings ratings = (Ratings) o;
        return Id != null && Objects.equals(Id, ratings.Id);
    }

    @Override
    public int hashCode() {
        return 13 * Id.hashCode() + 7;
    }
}
