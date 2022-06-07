import { Component, OnInit } from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {ActivatedRoute, Router} from "@angular/router";
import {FieldValidatorService} from "../../../services/utils/field-validator.service";
import {KeycloakService} from "keycloak-angular";
import {HttpClient} from "@angular/common/http";
import {Prescription} from "../../../interfaces/Prescription";
import {Patient} from "../../../interfaces/Patient";
import {Doctor} from "../../../interfaces/Doctor";
import {PatientService} from "../../../services/patient/patient.service";
import {DoctorService} from "../../../services/doctor/doctor.service";
import {PrescriptionService} from "../../../services/prescription/prescription.service";
import {faTimes} from '@fortawesome/free-solid-svg-icons';

@Component({
  selector: 'app-prescription-details',
  templateUrl: './prescription-details.component.html',
  styleUrls: ['./prescription-details.component.css']
})
export class PrescriptionDetailsComponent implements OnInit {
  prescription!: Prescription;
  patients!: Patient[];
  doctors!: Doctor[];
  form!: FormGroup;

  patient!: Patient;
  doctor!: Doctor;
  treatment!: number;
  diagnosis!: string;

  faTimes = faTimes;

  roles: string[] = [];

  constructor(private formBuilder: FormBuilder,
              private route: ActivatedRoute,
              private router: Router,
              private fieldValidator: FieldValidatorService,
              private keycloakService: KeycloakService,
              private http: HttpClient,
              private patientService: PatientService,
              private doctorService: DoctorService,
              private prescriptionService: PrescriptionService) {
  }

  ngOnInit(): void {
    this.route.paramMap.subscribe(() => {
      this.showPrescription();
    });

    this.patientService.getPatients().subscribe((patients) => (this.patients = patients));
    this.doctorService.getDoctors().subscribe((doctors) => (this.doctors = doctors));

    this.form = this.formBuilder.group({
      patient: ['', []],
      doctor: ['', []],
      diagnosis: ['', [Validators.required]],
      treatment: ['', [Validators.required, Validators.pattern("[0-9]+")]]
    });

    this.fieldValidator.form = this.form;

    this.showPrescription();

    this.roles = this.keycloakService.getUserRoles();
  }

  get hasPatientRole(): boolean {
    let requiredRoles = ["ROLE_PATIENT"]
    return requiredRoles.some((role) => this.roles.includes(role));
    // return true;
  }

  get hasDoctorRole(): boolean {
    let requiredRoles = ["ROLE_DOCTOR"]
    return requiredRoles.some((role) => this.roles.includes(role));
    // return true;
  }

  onDelete(prescriptionId: number | undefined) {
    if (prescriptionId != null) {
      this.prescriptionService.deletePrescription(prescriptionId).subscribe({
          next: response => {
            console.log(`Response from deleting: ${response}`);
          },
          error: err => {
            console.log(`There was an error: ${err.message}`);
          },
          complete: () => {
            console.log('Done delete the prescription relation');
            this.router.navigate(['prescription']);
          }

        }
      );
    }
  }

  showPrescription() {
    // @ts-ignore
    const prescriptionId: number = +this.route.snapshot.paramMap.get('id');
    console.log(prescriptionId);
    this.prescriptionService.getPrescription(prescriptionId).subscribe(data => {
      this.prescription = data;
      // try {
      //   // @ts-ignore
      //   let student_link: string = this.studentCourse?._links.student.href;
      //   this.http.get<Student>(student_link).subscribe((student) => (this.studentCourse.student = student));
      //
      //   // @ts-ignore
      //   let course_link: string = this.studentCourse?._links.course.href;
      //   this.http.get<Course>(course_link).subscribe((course) => (this.studentCourse.course = course));
      //
      // } catch (Error) {
      //   console.log(Error);
      // }

      // @ts-ignore
      // console.log("Prescription: " + (data.id))
    });
  }

  onUpdate() {
    if (this.form.valid) {
      let newPrescription = {
        patient: this.patient,
        doctor: this.doctor,
        diagnosis: this.diagnosis,
        treatment: this.treatment
      }

      // if (this.hasTeacherRole && !this.hasAdminRole) {
      //   newStudentCourse.student = this.studentCourse.student;
      //   newStudentCourse.course = this.studentCourse.course;
      // }

      // try {
      //   // @ts-ignore
      //   newStudentCourse.student = this.student?._links.student.href;
      //   // @ts-ignore
      //   newStudentCourse.course = this.course?._links.course.href;
      // } catch (Error) {
      //   newStudentCourse.student = this.student;
      //   newStudentCourse.course = this.course;
      //   console.log("Student-course relation: " + JSON.stringify(newStudentCourse.student) + JSON.stringify(newStudentCourse.course));
      // }


      this.prescriptionService.updatePrescription(this.prescription.id, newPrescription).subscribe({
          next: response => {
            console.log(`Response from updating: ${response}`);
            this.prescription = response;
          },
          error: err => {
            console.log(`There was an error: ${err.message}`);
          },
          complete: () => {
            console.log('Done update the prescription');
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
