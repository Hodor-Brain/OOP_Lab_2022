package ua.university.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Prescription implements IModel {
    private int id;
    private Patient patient;
    private Doctor doctor;
    private String diagnosis;
    private int treatment;

    @Override
    public String modelURLPattern() {
        return "/prescriptions";
    }

    @Override
    public String toString() {
        return "Patient: " +
                this.patient +
                ", Doctor: " +
                this.doctor +
                ", Diagnosis: " +
                this.diagnosis +
                ", Treatment: " +
                this.treatment;
    }

}
