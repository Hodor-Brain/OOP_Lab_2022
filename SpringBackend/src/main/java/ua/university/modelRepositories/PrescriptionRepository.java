package ua.university.modelRepositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.university.modelEntities.Prescription;

public interface PrescriptionRepository  extends JpaRepository<Prescription, Integer> {
}
