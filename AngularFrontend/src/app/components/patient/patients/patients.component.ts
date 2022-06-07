import { Component, OnInit } from '@angular/core';
import {Patient} from "../../../interfaces/Patient";
import {PatientService} from "../../../services/patient/patient.service";
import {KeycloakService} from "keycloak-angular";

@Component({
  selector: 'app-patients',
  templateUrl: './patients.component.html',
  styleUrls: ['./patients.component.css']
})
export class PatientsComponent implements OnInit {
  patients: Patient[] = [];

  roles: string[] = [];

  constructor(private patientService: PatientService,
              private keycloakService: KeycloakService) {

  }

  get hasRole(): boolean {
    let requiredRoles = ["ROLE_ADMIN"]
    return requiredRoles.some((role) => this.roles.includes(role));
    // return true;
  }

  ngOnInit(): void {
    this.patientService.getPatients().subscribe((patients) => (this.patients = patients));

    this.roles = this.keycloakService.getUserRoles();
  }

  deletePatient(patientId: number | undefined){
    if (patientId != null) {
      this.patientService.deletePatient(patientId).subscribe(() => (this.patients = this.patients.filter((item) => item.id !== patientId)));
    }
  }

  addPatient(patient: Patient) {
    this.patientService.addPatient(patient).subscribe((patient) =>(this.patients.push(patient)));
  }
}
