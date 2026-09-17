package gr.aueb.cf.schoolapp.service;


import gr.aueb.cf.schoolapp.core.exceptions.EntityAlreadyExistsException;
import gr.aueb.cf.schoolapp.core.exceptions.EntityInvalidArgumentException;
import gr.aueb.cf.schoolapp.dto.TeacherInsertDTO;
import gr.aueb.cf.schoolapp.dto.TeacherReadOnlyDTO;
import gr.aueb.cf.schoolapp.mapper.Mapper;
import gr.aueb.cf.schoolapp.model.Region;
import gr.aueb.cf.schoolapp.model.Teacher;
import gr.aueb.cf.schoolapp.repository.RegionRepository;
import gr.aueb.cf.schoolapp.repository.TeacherRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class TeacherService implements ITeacherService {

    private final TeacherRepository teacherRepository;
    private final RegionRepository regionRepository;
    private final Mapper mapper;                // είναι μέσα στον IoC container λόγω annotation @Component

    // Με τον constructor γίνεται inject των πάνω instances
    // Παλιότερα κάναμε @Autowired
    // Τελικά μπορούμε να χρησιμοποιήσουμε @RequiredArgsConstructor
    // όταν είναι final τα πεδία πάνω (καλό είναι να είναι final)
//    public TeacherService(TeacherRepository teacherRepository, RegionRepository regionRepository, Mapper mapper) {
//        this.teacherRepository = teacherRepository;
//        this.regionRepository = regionRepository;
//        this.mapper = mapper;
//    }


    @Override
    // προστατεύει από RuntimeExceptions & θέλουμε και στα δικά μας
    // το rollback μας νοιάζει σε πράξεις που αλλάζουν τη βάση (όχι αν κάνουμε Select)
    @Transactional(rollbackFor = {EntityAlreadyExistsException.class, EntityInvalidArgumentException.class})
    public TeacherReadOnlyDTO saveTeacher(TeacherInsertDTO dto)
            throws EntityAlreadyExistsException, EntityInvalidArgumentException {

        try {
//            if (dto.vat() != null && teacherRepository.findByVat(dto.vat()).isPresent()) {
            if (dto.vat() != null && isTeacherExistsByVat(dto.vat())) {
                throw new EntityAlreadyExistsException("Teacher with VAT= " + dto.vat() + " already exists");
            }

            // Χρειαζόμαστε συσχέτιση του region με τον teacher που ανήκει
            Region region = regionRepository.findById(dto.regionId())
                    .orElseThrow(() -> new EntityInvalidArgumentException("Region id " + dto.regionId() + " not found"));

            Teacher teacher = mapper.mapToTeacherEntity(dto);
            region.addTeacher(teacher);          // βοηθητική μέθοδος στο region η addTeacher
            teacherRepository.save(teacher);     // pre-persist uuid-id & region από λίστα - ειναι ο saved teacher
            log.info("Teacher with vat={} save successfully.", dto.vat());    // structured logging - parameterized placeholder pattern
            return mapper.mapToTeacherReadOnlyDTO(teacher);
        } catch(EntityAlreadyExistsException e) {        // exception αφού φτάσει στη βάση
            log.warn("Save failed for teacher with VAT={}. Teacher already exists", dto.vat());
            throw e;
        } catch(EntityInvalidArgumentException e) {
            log.warn("Save failed for teacher with VAT={}. Region with id={} invalid", dto.vat(), dto.regionId());
            throw e;
        } catch(DataIntegrityViolationException e) {     // του Springboot - ταυτόχρονη εισαγωγή ίδιου teacher
            log.warn("Save failed for teacher with VAT={}. Teacher exists.", dto.vat());                    // αυτό θα δει το log
            throw new EntityAlreadyExistsException("Teacher with VAT= " + dto.vat() + " already exists");   // για να δει αυτό ο Controller
        }
    }

    public TeacherReadOnlyDTO updateTeacher(TeacherEditDTO dto) {

    }

    @Override
    @Transactional(readOnly = true)            // query read only δεν κάνει αλλαγές
    public Page<TeacherReadOnlyDTO> getPaginatedTeachersDeletedFalse(Pageable pageable) {
        Page<Teacher> teachersPage = teacherRepository.findAllByDeletedFalse(pageable);
        log.debug("Get paginated teachers not deleted, returned successfully page={}, size={}",
                teachersPage.getNumber(), teachersPage.getSize());
        return teachersPage.map(mapper::mapToTeacherReadOnlyDTO);       // .map από API του Page - περιμένει ένα λαμδα μετά ::
    }

    @Override
    @Transactional(readOnly = true)            // query read only δεν κάνει αλλαγές
    public Page<TeacherReadOnlyDTO> getPaginatedTeachers(Pageable pageable) {
        Page<Teacher> teachersPage = teacherRepository.findAll(pageable);          // .findAll λόγω extending JPΑ που κανει extends τη PagingAndSortingRepository
        log.debug("Get paginated teachers returned successfully page={}, size={}",
                teachersPage.getNumber(), teachersPage.getSize());
        return teachersPage.map(mapper::mapToTeacherReadOnlyDTO);       // .map από API του Page - περιμένει ένα λαμδα μετά ::
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isTeacherExistsByVat(String vat) {
        return teacherRepository.findByVat(vat).isPresent();
    }
}
