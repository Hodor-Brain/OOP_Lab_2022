import {Component, Input, OnInit} from '@angular/core';
import {Patient} from "../../../interfaces/Patient";

@Component({
  selector: 'app-patient-item',
  templateUrl: './patient-item.component.html',
  styleUrls: ['./patient-item.component.css']
})
export class PatientItemComponent implements OnInit {
  @Input() patient!: Patient

  constructor() { }

  ngOnInit(): void {
  }

}
