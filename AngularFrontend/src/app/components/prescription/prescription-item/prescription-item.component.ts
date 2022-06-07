import {Component, Input, OnInit} from '@angular/core';
import {Prescription} from "../../../interfaces/Prescription";
import {HttpClient} from "@angular/common/http";
import {Patient} from "../../../interfaces/Patient";
import {Doctor} from "../../../interfaces/Doctor";

@Component({
  selector: 'app-prescription-item',
  templateUrl: './prescription-item.component.html',
  styleUrls: ['./prescription-item.component.css']
})
export class PrescriptionItemComponent implements OnInit {
  @Input() prescription!: Prescription;

  constructor(private http: HttpClient) { }

  ngOnInit(): void {
    try{
      // @ts-ignore
      let patient_link: string = this.prescription?._links.patient.href;
      this.http.get<Patient>(patient_link).subscribe((patient) => (this.prescription.patient = patient));

      // @ts-ignore
      let doctor_link: string = this.prescription?._links.doctor.href;
      this.http.get<Doctor>(doctor_link).subscribe((doctor) => (this.prescription.doctor = doctor));
    }
    catch (Error){
      console.log(Error)
    }
  }

}
