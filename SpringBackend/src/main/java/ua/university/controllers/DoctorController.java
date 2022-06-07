package ua.university.controllers;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.university.exceptions.BadRequestException;
import ua.university.modelEntities.Doctor;
import ua.university.modelEntities.Response;
import ua.university.services.DoctorService;

import java.util.List;

@AllArgsConstructor
@RestController
@CrossOrigin
@RequestMapping("/api/doctors")
public class DoctorController {
    private DoctorService doctorService;

    @GetMapping
    public List<Doctor> getAllDoctors() {
        return doctorService.getAllDoctors();
    }

    @GetMapping("/{id}")
    public Doctor getDoctor(@PathVariable String id) {
        return doctorService.getDoctor(id);
    }

    @PostMapping
    public Doctor addDoctors(@RequestBody Doctor doctor) {
        return doctorService.save(doctor);
    }

    @PutMapping("/{id}")
    public Doctor updateDoctor(@RequestBody Doctor doctor, @PathVariable String id) {
        return doctorService.updateDoctor(id, doctor);
    }

    @DeleteMapping("/{id}")
    public void deleteDoctor(@PathVariable String id) {
        doctorService.deleteDoctor(id);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Response> handleException(BadRequestException e) {
        return new ResponseEntity<>(new Response(e.getMessage()), HttpStatus.BAD_REQUEST);
    }
}
