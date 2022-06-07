import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {map, Observable} from "rxjs";
import {Prescription} from "../../interfaces/Prescription";
import {environment} from "../../../environments/environment";

const httpOptions = {
  headers: new HttpHeaders({
    'Content-type': 'application/json',
  })
}

@Injectable({
  providedIn: 'root'
})

export class PrescriptionService {
  private apiUrl = `${environment.apiUrl}prescriptions`

  constructor(private http: HttpClient) {
  }

  getPrescriptions(): Observable<Prescription[]> {
    return this.http.get<Prescription[]>(this.apiUrl);
  }

  getPrescriptionsForDoctor(name: string): Observable<Prescription[]> {
    return this.http.get<Prescription[]>(`${this.apiUrl}/search/findByDoctorNameContaining?name=${name}`);
  }

  getPrescriptionsForPatient(name: string): Observable<Prescription[]> {
    return this.http.get<Prescription[]>(`${this.apiUrl}/search/findByPatientNameContaining?name=${name}`);
  }

  getPrescription(id: number): Observable<Prescription> {
    return this.http.get<Prescription>(`${this.apiUrl}/${id}`);
  }


  deletePrescription(id: number): Observable<unknown> {
    return this.http.delete<unknown>(`${this.apiUrl}/${id}`);
  }

  addPrescription(prescription: Prescription): Observable<Prescription> {
    return this.http.post<Prescription>(this.apiUrl, prescription, httpOptions);
  }

  updatePrescription(id: number | undefined, prescription: Prescription): Observable<Prescription> {
    return this.http.put<Prescription>(`${this.apiUrl}/${id}`, prescription, httpOptions);
  }
}
