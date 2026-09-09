package gr.aueb.cf.schoolapp.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "regions")                   // δεν χρειάζεται timestamp - εδώ - μπορεί σε άλλο app ναι
public class Region {                      // παραμετρικός πίνακας για combo box - όχι extended

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    @Getter(AccessLevel.PACKAGE)
    @Setter(AccessLevel.NONE)                               // δεν θέλουμε να αλλάζει το Set από έξω μόνο να προσθέτουμε ή να αφαιρούμε αντικείμενα με addTeacher και removeTeacher
    @OneToMany(mappedBy = "region", fetch = FetchType.LAZY)
    private Set<Teacher> teachers = new HashSet<>();        // όχι διπλότυπα

    public Set<Teacher> getAllTeachers() {
        return Collections.unmodifiableSet(teachers) ;      // επιστρέφει αντίγραφο του Set για να μην μπορεί να αλλάξει από έξω
    }

    public void addTeacher(Teacher teacher) {
        teachers.add(teacher);
        teacher.setRegion(this);                          // set the region of the teacher to this region
    }

    public void removeTeacher(Teacher teacher) {
        teachers.remove(teacher);
        teacher.setRegion(null);                          // set the region of the teacher to null
    }
}
