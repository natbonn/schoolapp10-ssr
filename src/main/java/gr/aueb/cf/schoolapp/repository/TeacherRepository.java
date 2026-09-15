package gr.aueb.cf.schoolapp.repository;

import gr.aueb.cf.schoolapp.model.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

//@Repository      δεν χρειάζεται γιατί κάνουμε extends JpaRepository
public interface TeacherRepository extends JpaRepository<Teacher, Long> {

    Optional<Teacher> findByVat(String vat);            // υπονοείται equals -- Optional για να αποφύγουμε το Null- NullPointerException
    Optional<Teacher> findByUuid(UUID uuid);            // Όχι String είναι UUID Class

}
