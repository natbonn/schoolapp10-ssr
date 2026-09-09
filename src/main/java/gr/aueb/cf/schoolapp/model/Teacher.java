package gr.aueb.cf.schoolapp.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.catalina.User;

import java.util.Objects;
import java.util.UUID;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "teachers")
public class Teacher extends AbstractEntity {              // για να κληρονομήσει τα πεδία createdAt και updatedAt

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, updatable = false)
    private UUID uuid;

    @Column(unique = true, nullable = false)
    private String vat;                                    // European VAT number

    private String firstname;
    private String lastname;

    @Setter(AccessLevel.PACKAGE)                           // package-private setter για να μην μπορεί να αλλάξει το Region από έξω μόνο να προσθέτουμε ή να αφαιρούμε αντικείμενα με addTeacher και removeTeacher
    @JoinColumn(name = "region_id")                        // foreign key column name
    @ManyToOne(fetch = FetchType.LAZY)                     // εδώ το default είναι EAGER, αλλά το αλλάζουμε σε LAZY για να μην επιβαρύνεται το app
    private Region region;

    @PrePersist
    public void initializeUUID() {
        if (uuid == null) this.uuid = UUID.randomUUID();   // if null new UUID is generated

    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Teacher teacher)) return false;
        return Objects.equals(getVat(), teacher.getVat());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getVat());
    }
}
