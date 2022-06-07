import { Injectable } from '@angular/core';
import {FormGroup} from "@angular/forms";

@Injectable({
  providedIn: 'root'
})
export class FieldValidatorService {
  // @ts-ignore
  form: FormGroup;

  constructor() { }

  ngOnInit(): void {
  }

  isFieldValid(field: string) {
    // @ts-ignore
    return !this.form.controls[field].valid && this.form.controls[field].touched;
  }

  reset() {
    this.form.reset();
  }
}
