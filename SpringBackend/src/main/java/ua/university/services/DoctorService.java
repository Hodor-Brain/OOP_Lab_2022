package ua.university.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.university.exceptions.BadRequestException;
import ua.university.modelEntities.Doctor;
import ua.university.modelRepositories.DoctorRepository;
import ua.university.utils.ServerUtils;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class DoctorService {
    private final DoctorRepository doctorRepository;
    private static final String BAD_DOCTOR = "Bad doctor id parameter!";

    public List<Doctor> getAllDoctors() {
        return doctorRepository.findAll();
    }

    public Doctor save(Doctor doctor) {
        return doctorRepository.save(doctor);
    }

    public Doctor getDoctor(String id) {
        int intId = ServerUtils.parseParameterId(id);
        Optional<Doctor> value = doctorRepository.findById(intId);
        if (value.isPresent()) {
            return value.get();
        } else {
            throw new BadRequestException(BAD_DOCTOR);
        }
    }

    @Transactional
    public Doctor updateDoctor(String id, Doctor doctor) {
        int intId = ServerUtils.parseParameterId(id);
        if (doctorRepository.findById(intId).isPresent()) {
            return doctorRepository.findById(intId)
                    .map(doctorNew -> {
                        doctorNew.setName(doctor.getName());
                        doctorNew.setType(doctor.getType());
                        return doctorRepository.save(doctorNew);
                    })
                    .orElseGet(() -> {
                        doctor.setId(intId);
                        return doctorRepository.save(doctor);
                    });
        } else {
            throw new BadRequestException(BAD_DOCTOR);
        }
    }

    @Transactional
    public void deleteDoctor(String id) {
        int intId = ServerUtils.parseParameterId(id);
        if (doctorRepository.findById(intId).isPresent()) {
            doctorRepository.deleteById(intId);
        } else {
            throw new BadRequestException(BAD_DOCTOR);
        }
    }
}
