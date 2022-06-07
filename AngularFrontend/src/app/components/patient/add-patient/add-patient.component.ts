import {Component, EventEmitter, OnInit, Output} from '@angular/core';
import {Patient} from "../../../interfaces/Patient";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {PatientService} from "../../../services/patient/patient.service";
import {FieldValidatorService} from "../../../services/utils/field-validator.service";

@Component({
  selector: 'app-add-patient',
  templateUrl: './add-patient.component.html',
  styleUrls: ['./add-patient.component.css']
})
export class AddPatientComponent implements OnInit {
  // @ts-ignore
  form: FormGroup;

  // @ts-ignore
  name: string;

  @Output() onAddPatient: EventEmitter<Patient> = new EventEmitter();

  constructor(private formBuilder: FormBuilder,
              private fieldValidator: FieldValidatorService) {

  }

  ngOnInit(): void {
    this.form = this.formBuilder.group({
      name: ['', [Validators.required]]
    });
    this.fieldValidator.form = this.form;
  }

  onSubmit() {
    if (this.form.valid) {
      const newPatient = {
        name: this.name
      }

      this.onAddPatient.emit(newPatient);

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
