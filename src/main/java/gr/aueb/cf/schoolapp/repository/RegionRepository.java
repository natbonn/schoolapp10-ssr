package gr.aueb.cf.schoolapp.repository;

import gr.aueb.cf.schoolapp.model.Region;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

//@Repository     -- δεν χρειάζεται γιατί κάνουμε extends to JpaRepository
public interface RegionRepository extends JpaRepository<Region, Long> {

    List<Region> findAllByOrderByNameAsc();         // κανονικά είναι findBy, το all το αγνοεί είναι για εμάς περιγραφικό

}
