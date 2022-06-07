import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {map, Observable} from "rxjs";
import {Patient} from "../../interfaces/Patient";
import {environment} from "../../../environments/environment";

const httpOptions = {
  headers: new HttpHeaders({
    'Content-type': 'application/json',
  })
}

@Injectable({
  providedIn: 'root'
})
export class PatientService {
  private apiUrl = `${environment.apiUrl}patients`

  constructor(private http: HttpClient) {
  }

  getPatients(): Observable<Patient[]> {
    return this.http.get<Patient[]>(this.apiUrl);
  }

  getPatientByName(name: string): Observable<Patient> {
    // return this.http.get<Patient>(`${this.apiUrl}?name=${name}`);
    return this.http.get<Patient>(`${this.apiUrl}/search/findByNameContaining?name=${name}`);
  }

  getPatient(id: number): Observable<Patient> {
    return this.http.get<Patient>(`${this.apiUrl}/${id}`);
  }

  deletePatient(id: number): Observable<unknown> {
    return this.http.delete<unknown>(`${this.apiUrl}/${id}`);
  }

  addPatient(patient: Patient): Observable<Patient> {
    return this.http.post<Patient>(this.apiUrl, patient, httpOptions);
  }

  updatePatient(id: number | undefined, patient: Patient): Observable<Patient> {
    console.log(id, patient);
    return this.http.put<Patient>(`${this.apiUrl}/${id}`, patient, httpOptions);
  }
}
