package gr.aueb.cf.schoolapp.mapper;

import gr.aueb.cf.schoolapp.dto.*;
import gr.aueb.cf.schoolapp.model.Region;
import gr.aueb.cf.schoolapp.model.Teacher;
import org.springframework.stereotype.Component;

@Component      // για να μπορούμε να την κάνουμε inject όπου χρειάζεται
public class Mapper {

    public Teacher mapToTeacherEntity(TeacherInsertDTO teacherInsertDTO) {
        return new Teacher(teacherInsertDTO.firstname(), teacherInsertDTO.lastname(), teacherInsertDTO.vat());
    }

    public TeacherReadOnlyDTO mapToTeacherReadOnlyDTO(Teacher teacher) {
        return new TeacherReadOnlyDTO(teacher.getUuid().toString(),
                teacher.getFirstname(), teacher.getLastname(),
                teacher.getVat(), teacher.getRegion().getName());
    }

    public RegionReadOnlyDTO mapToRegionReadOnlyDTO(Region region) {
        return new RegionReadOnlyDTO(region.getId(), region.getName());
    }

    public TeacherEditReadOnlyDTO mapToTeacherEditReadOnlyDTO(Teacher teacher) {
        return new TeacherEditReadOnlyDTO(teacher.getUuid(), teacher.getFirstname(),
                teacher.getLastname(), teacher.getVat(), teacher.getRegion().getId());
    }
}
