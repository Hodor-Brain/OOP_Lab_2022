import { Component, OnInit } from '@angular/core';
import {Patient} from "../../../interfaces/Patient";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {PatientService} from "../../../services/patient/patient.service";
import {ActivatedRoute, Router} from "@angular/router";
import {faTimes} from '@fortawesome/free-solid-svg-icons'
import {FieldValidatorService} from "../../../services/utils/field-validator.service";
import {KeycloakService} from "keycloak-angular";

@Component({
  selector: 'app-patient-details',
  templateUrl: './patient-details.component.html',
  styleUrls: ['./patient-details.component.css']
})
export class PatientDetailsComponent implements OnInit {
  patient!: Patient;
  form!: FormGroup;

  name!: string;

  roles: string[] = [];

  faTimes = faTimes;

  constructor(private patientService: PatientService,
              private formBuilder: FormBuilder,
              private route: ActivatedRoute,
              private router: Router,
              private fieldValidator: FieldValidatorService,
              private keycloakService: KeycloakService) {
  }

  ngOnInit(): void {
    this.route.paramMap.subscribe(()=>{
      this.showPatient();
    });

    this.form = this.formBuilder.group({
      name: ['', [Validators.required]]
    });
    this.fieldValidator.form = this.form;

    this.showPatient();
    this.roles = this.keycloakService.getUserRoles();
  }

  get hasRole(): boolean {
    let requiredRoles = ["ROLE_ADMIN"]
    return requiredRoles.some((role) => this.roles.includes(role));
    // return true;
  }

  onDelete(patientId: number | undefined){
    if (patientId != null) {
      this.patientService.deletePatient(patientId).subscribe({
          next: response => {
            console.log(`Response from deleting: ${response}`);
          },
          error: err => {
            console.log(`There was an error: ${err.message}`);
          },
          complete: () => {
            console.log('Done delete the patient');
            this.router.navigate(['patients']);
          }
        }
      );
    }
  }

  showPatient() {
    // @ts-ignore
    const patientId: number = +this.route.snapshot.paramMap.get('id');
    this.patientService.getPatient(patientId).subscribe( data => {
      this.patient = data;
      // @ts-ignore
      console.log("Patient: " + (data.id))
    });
  }

  onUpdate() {
    if (this.form.valid) {
      const newPatient = {
        name: this.name
      }

      this.patientService.updatePatient(this.patient.id, newPatient).subscribe({
          next: response => {
            console.log(`Response from updating: ${response}`);
            this.patient = response;
          },
          error: err => {
            console.log(`There was an error: ${err.message}`);
          },
          complete: () => {
            console.log('Done update the patient');
          }
        }
      );

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
