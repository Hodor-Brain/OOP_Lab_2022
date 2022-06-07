import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {map, Observable} from "rxjs";
import {Doctor} from "../../interfaces/Doctor";
import {environment} from "../../../environments/environment";

const httpOptions = {
  headers: new HttpHeaders({
    'Content-type': 'application/json',
  })
}

@Injectable({
  providedIn: 'root'
})
export class DoctorService {
  private apiUrl = `${environment.apiUrl}doctors`

  constructor(private http: HttpClient) {
  }

  getDoctors(): Observable<Doctor[]> {
    return this.http.get<Doctor[]>(this.apiUrl);
  }

  getDoctorByName(name: string): Observable<Doctor> {
    // return this.http.get<Patient>(`${this.apiUrl}?name=${name}`);
    return this.http.get<Doctor>(`${this.apiUrl}/search/findByNameContaining?name=${name}`);
  }

  getDoctor(id: number): Observable<Doctor> {
    return this.http.get<Doctor>(`${this.apiUrl}/${id}`);
  }

  deleteDoctor(id: number): Observable<unknown> {
    return this.http.delete<unknown>(`${this.apiUrl}/${id}`);
  }

  addDoctor(doctor: Doctor): Observable<Doctor> {
    return this.http.post<Doctor>(this.apiUrl, doctor, httpOptions);
  }

  updateDoctor(id: number | undefined, doctor: Doctor): Observable<Doctor> {
    console.log(id, doctor);
    return this.http.put<Doctor>(`${this.apiUrl}/${id}`, doctor, httpOptions);
  }
}
