import { Component, OnInit } from '@angular/core';
import {Doctor} from "../../../interfaces/Doctor";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {DoctorService} from "../../../services/doctor/doctor.service";
import {ActivatedRoute, Router} from "@angular/router";
import {faTimes} from '@fortawesome/free-solid-svg-icons'
import {FieldValidatorService} from "../../../services/utils/field-validator.service";
import {KeycloakService} from "keycloak-angular";

@Component({
  selector: 'app-doctor-details',
  templateUrl: './doctor-details.component.html',
  styleUrls: ['./doctor-details.component.css']
})
export class DoctorDetailsComponent implements OnInit {
  doctor!: Doctor;
  form!: FormGroup;

  name!: string;
  type!: number;

  roles: string[] = [];

  faTimes = faTimes;

  constructor(private doctorService: DoctorService,
              private formBuilder: FormBuilder,
              private route: ActivatedRoute,
              private router: Router,
              private fieldValidator: FieldValidatorService,
              private keycloakService: KeycloakService) {
  }

  ngOnInit(): void {
    this.route.paramMap.subscribe(()=>{
      this.showDoctor();
    });

    this.form = this.formBuilder.group({
      type: ['', [Validators.pattern("[0-9]+"), Validators.min(0), Validators.max(1)]],
      name: ['', [Validators.required]]
    });
    this.fieldValidator.form = this.form;

    this.showDoctor();
    this.roles = this.keycloakService.getUserRoles();
  }

  get hasRole(): boolean {
    let requiredRoles = ["ROLE_ADMIN"]
    return requiredRoles.some((role) => this.roles.includes(role));
    // return true;
  }

  get isDoctor(): boolean {
    return this.doctor.type == 1;
  }

  get isNurse(): boolean {
    return this.doctor.type == 0;
  }

  onDelete(doctorId: number | undefined){
    if (doctorId != null) {
      this.doctorService.deleteDoctor(doctorId).subscribe({
          next: response => {
            console.log(`Response from deleting: ${response}`);
          },
          error: err => {
            console.log(`There was an error: ${err.message}`);
          },
          complete: () => {
            console.log('Done delete the doctor');
            this.router.navigate(['doctors']);
          }
        }
      );
    }
  }

  showDoctor() {
    // @ts-ignore
    const doctorId: number = +this.route.snapshot.paramMap.get('id');
    this.doctorService.getDoctor(doctorId).subscribe( data => {
      this.doctor = data;
      // @ts-ignore
      console.log("Doctor: " + (data.id))
    });
  }

  onUpdate() {
    if (this.form.valid) {
      const newDoctor = {
        type: this.doctor.type,
        name: this.name
      }

      this.doctorService.updateDoctor(this.doctor.id, newDoctor).subscribe({
          next: response => {
            console.log(`Response from updating: ${response}`);
            this.doctor = response;
          },
          error: err => {
            console.log(`There was an error: ${err.message}`);
          },
          complete: () => {
            console.log('Done update the doctor');
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
