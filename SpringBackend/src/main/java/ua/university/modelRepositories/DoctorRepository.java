package ua.university.modelRepositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.university.modelEntities.Doctor;

public interface DoctorRepository extends JpaRepository<Doctor, Integer> {
}
