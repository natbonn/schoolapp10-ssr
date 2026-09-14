package gr.aueb.cf.schoolapp.dto;

// Επιστρέφει data του Teacher από τον Controller στη σελίδα
public record TeacherReadOnlyDTO(String uuid, String firstname, String lastname, String vat, String region) {
}
