package gr.aueb.cf.schoolapp.model;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;


@MappedSuperclass                         // Υπάρχει μόνο για να κληρονομηθεί από άλλες οντότητες, δεν θα δημιουργηθεί πίνακας στη βάση δεδομένων
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)  // Ενεργοποιεί το auditing για τα πεδία createdAt και updatedAt
public abstract class AbstractEntity {          // Abstract class for common entity properties

    // auditing fields
    @CreatedDate                                // μας το δίνει ο Listener
    @Column(name = "created_at", nullable = false, updatable = false, columnDefinition = "DATETIME")
    private Instant createdAt;                  // Timestamp for when the entity was created

    @LastModifiedDate                          // μας το δίνει ο Listener
    @Column(name = "updated_at", nullable = false, columnDefinition = "DATETIME")
    private Instant updatedAt;                  // Timestamp for when the entity was last updated

}
