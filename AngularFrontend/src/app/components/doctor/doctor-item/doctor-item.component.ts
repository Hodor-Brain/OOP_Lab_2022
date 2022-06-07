import {Component, Input, OnInit} from '@angular/core';
import {Doctor} from "../../../interfaces/Doctor";

@Component({
  selector: 'app-doctor-item',
  templateUrl: './doctor-item.component.html',
  styleUrls: ['./doctor-item.component.css']
})
export class DoctorItemComponent implements OnInit {
  @Input() doctor!: Doctor

  constructor() { }

  ngOnInit(): void {
  }

  get isDoctor(): boolean {
    return this.doctor.type == 1;
    // return true;
  }

  get isNurse(): boolean {
    return this.doctor.type == 0;
    // return true;
  }
}
