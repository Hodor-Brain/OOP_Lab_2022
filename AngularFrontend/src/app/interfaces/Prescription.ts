import {Patient} from "./Patient";
import {Doctor} from "./Doctor";

export interface Prescription {
  id?: number;
  patient: Patient;
  doctor: Doctor;
  diagnosis?: string;
  treatment?: number;
}
