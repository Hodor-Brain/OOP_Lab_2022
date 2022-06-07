package ua.university.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.university.exceptions.BadRequestException;
import ua.university.modelEntities.Prescription;
import ua.university.modelRepositories.PrescriptionRepository;
import ua.university.utils.ServerUtils;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class PrescriptionService {
    private final PrescriptionRepository prescriptionRepository;
    private static final String BAD_PRESCRIPTION = "Bad prescription id parameter!";

    public List<Prescription> getAllPrescriptions() {
        return prescriptionRepository.findAll();
    }

    public Prescription save(Prescription prescription) {
        return prescriptionRepository.save(prescription);
    }

    public Prescription getPrescription(String id) {
        int intId = ServerUtils.parseParameterId(id);
        Optional<Prescription> value = prescriptionRepository.findById(intId);
        if (value.isPresent()) {
            return value.get();
        } else {
            throw new BadRequestException(BAD_PRESCRIPTION);
        }
    }

    @Transactional
    public Prescription updatePrescription(String id, Prescription prescription) {
        int intId = ServerUtils.parseParameterId(id);
        if (prescriptionRepository.findById(intId).isPresent()) {
            return prescriptionRepository.findById(intId)
                    .map(prescriptionNew -> {
                        prescriptionNew.setDoctor(prescription.getDoctor());
                        prescriptionNew.setPatient(prescription.getPatient());
                        prescriptionNew.setDiagnosis(prescription.getDiagnosis());
                        prescriptionNew.setTreatment(prescription.getTreatment());
                        return prescriptionRepository.save(prescriptionNew);
                    })
                    .orElseGet(() -> {
                        prescription.setId(intId);
                        return prescriptionRepository.save(prescription);
                    });
        } else {
            throw new BadRequestException(BAD_PRESCRIPTION);
        }
    }

    @Transactional
    public void deletePrescription(String id) {
        int intId = ServerUtils.parseParameterId(id);
        if (prescriptionRepository.findById(intId).isPresent()) {
            prescriptionRepository.deleteById(intId);
        } else {
            throw new BadRequestException(BAD_PRESCRIPTION);
        }
    }
}
