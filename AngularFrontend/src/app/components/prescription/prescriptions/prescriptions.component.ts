import { Component, OnInit } from '@angular/core';
import {PrescriptionService} from "../../../services/prescription/prescription.service";
import {Prescription} from "../../../interfaces/Prescription";
import {KeycloakService} from "keycloak-angular";

@Component({
  selector: 'app-prescriptions',
  templateUrl: './prescriptions.component.html',
  styleUrls: ['./prescriptions.component.css']
})
export class PrescriptionsComponent implements OnInit {
  prescriptions: Prescription[] = [];

  roles: string[] = [];

  constructor(private prescriptionService: PrescriptionService,
              private keycloakService: KeycloakService) {

  }

  get hasRole(): boolean {
    let requiredRoles = ["ROLE_ADMIN", "ROLE_PATIENT", "ROLE_DOCTOR"]
    return requiredRoles.some((role) => this.roles.includes(role));
    // return true;
  }

  ngOnInit(): void {
    this.roles = this.keycloakService.getUserRoles();
    // @ts-ignore
    let name: string = this.keycloakService.getKeycloakInstance().profile.firstName;

    if(this.roles.includes("ROLE_ADMIN")){
      this.prescriptionService.getPrescriptions().subscribe((prescriptions) => (this.prescriptions = prescriptions));
    }
    else if(this.roles.includes("ROLE_PATIENT")){
      this.prescriptionService.getPrescriptionsForPatient(name).subscribe((prescriptions) => (this.prescriptions = prescriptions));
    }
    else if(this.roles.includes("ROLE_DOCTOR")) {
      this.prescriptionService.getPrescriptionsForDoctor(name).subscribe((prescriptions) => (this.prescriptions = prescriptions));
    }
  }

  deletePrescription(prescriptionId: number | undefined){
    if (prescriptionId != null) {
      this.prescriptionService.deletePrescription(prescriptionId).subscribe(() => (this.prescriptions = this.prescriptions.filter((item) => item.id !== prescriptionId)));
    }
  }

  addPrescription(prescription: Prescription) {
    this.prescriptionService.addPrescription(prescription).subscribe((prescription) =>(this.prescriptions.push(prescription)));
  }
}
