import {Component, EventEmitter, OnInit, Output} from '@angular/core';
import {Doctor} from "../../../interfaces/Doctor";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {FieldValidatorService} from "../../../services/utils/field-validator.service";
import {DoctorService} from "../../../services/doctor/doctor.service";

@Component({
  selector: 'app-add-doctor',
  templateUrl: './add-doctor.component.html',
  styleUrls: ['./add-doctor.component.css']
})
export class AddDoctorComponent implements OnInit {
  // @ts-ignore
  form: FormGroup;

  // @ts-ignore
  name: string;
  // @ts-ignore
  type: number;

  @Output() onAddDoctor: EventEmitter<Doctor> = new EventEmitter();

  constructor(private formBuilder: FormBuilder,
              private fieldValidator: FieldValidatorService) {

  }

  ngOnInit(): void {
    this.form = this.formBuilder.group({
      type: ['', [Validators.required, Validators.pattern("[0-9]+"), Validators.min(0), Validators.max(1)]],
      name: ['', [Validators.required]]
    });
    this.fieldValidator.form = this.form;
  }

  onSubmit() {
    if (this.form.valid) {
      const newDoctor = {
        type: this.type,
        name: this.name
      }

      this.onAddDoctor.emit(newDoctor);

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
