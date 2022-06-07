import {Component, EventEmitter, OnInit, Output} from '@angular/core';
import {Patient} from "../../../interfaces/Patient";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {PrescriptionService} from "../../../services/prescription/prescription.service";
import {FieldValidatorService} from "../../../services/utils/field-validator.service";
import {DoctorService} from "../../../services/doctor/doctor.service";
import {PatientService} from "../../../services/patient/patient.service";
import {Doctor} from "../../../interfaces/Doctor";
import {Prescription} from "../../../interfaces/Prescription";

@Component({
  selector: 'app-add-prescription',
  templateUrl: './add-prescription.component.html',
  styleUrls: ['./add-prescription.component.css']
})
export class AddPrescriptionComponent implements OnInit {
  patients: Patient[] = [];
  doctors: Doctor[] = [];
  // @ts-ignore
  form: FormGroup;

  // @ts-ignore
  patient: Patient;
  // @ts-ignore
  doctor: Doctor;
  // @ts-ignore
  diagnosis: string;
  // @ts-ignore
  treatment: number;

  @Output() onAddPrescription: EventEmitter<Prescription> = new EventEmitter();

  constructor(private prescriptionService: PrescriptionService,
              private formBuilder: FormBuilder,
              private fieldValidator: FieldValidatorService,
              private doctorService: DoctorService,
              private patientService: PatientService) {

  }

  ngOnInit(): void {

    this.patientService.getPatients().subscribe((patients) => (this.patients = patients));
    this.doctorService.getDoctors().subscribe((doctors) => (this.doctors = doctors));
    this.form = this.formBuilder.group({
      patient: ['', [Validators.required]],
      doctor: ['', [Validators.required]],
      diagnosis: ['', [Validators.required]],
      treatment: ['', [Validators.required, Validators.pattern("[0-9]+")]],
    });
    this.fieldValidator.form = this.form;
  }

  onSubmit() {
    if (this.form.valid) {
      const newPrescription = {
        patient: this.patient,
        doctor: this.doctor,
        diagnosis: this.diagnosis,
        treatment: this.treatment
      }

      this.onAddPrescription.emit(newPrescription);

      this.fieldValidator.reset();
    }
  }

  isFieldValid(field: string) {
    // @ts-ignore
    return this.fieldValidator.isFieldValid(field);
  }

  reset() {
    this.fieldValidator.reset();
  }
}
