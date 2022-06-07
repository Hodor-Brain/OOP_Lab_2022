import {Component, OnInit} from '@angular/core';
import {Doctor} from "../../../interfaces/Doctor";
import {DoctorService} from "../../../services/doctor/doctor.service";
import {KeycloakService} from "keycloak-angular";

@Component({
  selector: 'app-doctors',
  templateUrl: './doctors.component.html',
  styleUrls: ['./doctors.component.css']
})
export class DoctorsComponent implements OnInit {
  doctors: Doctor[] = [];

  roles: string[] = [];

  constructor(private doctorService: DoctorService,
              private keycloakService: KeycloakService) {

  }

  get hasRole(): boolean {
    let requiredRoles = ["ROLE_ADMIN"]
    return requiredRoles.some((role) => this.roles.includes(role));
    // return true;
  }


  ngOnInit(): void {
    this.doctorService.getDoctors().subscribe((doctors) => (this.doctors = doctors));

    this.roles = this.keycloakService.getUserRoles();
  }

  deleteDoctor(doctorId: number | undefined){
    if (doctorId != null) {
      this.doctorService.deleteDoctor(doctorId).subscribe(() => (this.doctors = this.doctors.filter((item) => item.id !== doctorId)));
    }
  }

  addDoctor(doctor: Doctor) {
    this.doctorService.addDoctor(doctor).subscribe((doctor) =>(this.doctors.push(doctor)));
  }
}
