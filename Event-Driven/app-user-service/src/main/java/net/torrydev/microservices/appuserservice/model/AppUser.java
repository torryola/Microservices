package net.torrydev.microservices.appuserservice.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;

@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor @NoArgsConstructor @Builder @Data
@Entity
@Table(name = "appuser")
public class AppUser {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long Id;
    String name;
    @Column(unique = true)
    String email;
    @Column(unique = true)
    String username;
}
