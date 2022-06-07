package ua.university.modelEntities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "prescriptions")
@Entity
public class Prescription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;

    @OneToOne
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @OneToOne
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    @Column(name = "diagnosis", nullable = false)
    @JsonProperty
    private String diagnosis;

    @Column(name = "treatment", nullable = false)
    @JsonProperty
    private int treatment;

    public Integer getPatientId() {
        return patient.getId();
    }

    public Integer getDoctorId() {
        return doctor.getId();
    }
}
