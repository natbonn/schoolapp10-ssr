package gr.aueb.cf.schoolapp.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "regions")             // δεν χρειάζεται timestamp - εδώ - μπορεί σε άλλο app ναι
public class Region {                // παραμετρικός πίνακας για combo box - όχι extended

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;
}
