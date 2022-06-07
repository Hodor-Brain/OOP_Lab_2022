package ua.university.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.university.exceptions.BadRequestException;
import ua.university.modelEntities.Patient;
import ua.university.modelRepositories.PatientRepository;
import ua.university.utils.ServerUtils;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class PatientService {
    private final PatientRepository patientRepository;
    private static final String BAD_PATIENT = "Bad patient id parameter!";

    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    public Patient save(Patient patient) {
        return patientRepository.save(patient);
    }

    public Patient getPatient(String id) {
        int intId = ServerUtils.parseParameterId(id);
        Optional<Patient> value = patientRepository.findById(intId);
        if (value.isPresent()) {
            return value.get();
        } else {
            throw new BadRequestException(BAD_PATIENT);
        }
    }

    @Transactional
    public Patient updatePatient(String id, Patient patient) {
        int intId = ServerUtils.parseParameterId(id);
        if (patientRepository.findById(intId).isPresent()) {
            return patientRepository.findById(intId)
                    .map(patientNew -> {
                        patientNew.setName(patient.getName());
                        return patientRepository.save(patientNew);
                    })
                    .orElseGet(() -> {
                        patient.setId(intId);
                        return patientRepository.save(patient);
                    });
        } else {
            throw new BadRequestException(BAD_PATIENT);
        }
    }

    @Transactional
    public void deletePatient(String id) {
        int intId = ServerUtils.parseParameterId(id);
        if (patientRepository.findById(intId).isPresent()) {
            patientRepository.deleteById(intId);
        } else {
            throw new BadRequestException(BAD_PATIENT);
        }
    }
}
