package ua.university.modelRepositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;
import ua.university.modelEntities.Patient;

public interface PatientRepository extends JpaRepository<Patient, Integer> {
}
