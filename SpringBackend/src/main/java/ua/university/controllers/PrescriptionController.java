package ua.university.controllers;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ua.university.exceptions.BadRequestException;
import org.springframework.http.ResponseEntity;
import ua.university.modelEntities.Prescription;
import ua.university.modelEntities.Response;
import ua.university.services.PrescriptionService;

import java.util.List;

@AllArgsConstructor
@CrossOrigin
@RestController
@RequestMapping("/api/prescriptions")
public class PrescriptionController {
    private PrescriptionService prescriptionService;

    @GetMapping
    public List<Prescription> getAllPrescriptions(){
        return this.prescriptionService.getAllPrescriptions();
    }

    @GetMapping("/{id}")
    public Prescription getPrescription(@PathVariable String id){
        return this.prescriptionService.getPrescription(id);
    }

    @PostMapping
    public Prescription addPrescription(@RequestBody Prescription prescription){
        return this.prescriptionService.save(prescription);
    }

    @PutMapping("/{id}")
    public Prescription updatePrescription(@RequestBody Prescription prescription, @PathVariable String id){
        return this.prescriptionService.updatePrescription(id, prescription);
    }

    @DeleteMapping("/{id}")
    public void deletePrescription(@PathVariable String id){
        this.prescriptionService.deletePrescription(id);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Response> handleException(BadRequestException e){
        return new ResponseEntity<>(new Response(e.getMessage()), HttpStatus.BAD_REQUEST);
    }
}
