import {APP_INITIALIZER, NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';
import {HTTP_INTERCEPTORS, HttpClientModule} from '@angular/common/http';
import {FontAwesomeModule} from '@fortawesome/angular-fontawesome';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';

import {AppComponent} from './app.component';
import {AppHeaderComponent} from './components/utils/app-header/app-header.component';
import {ButtonComponent} from './components/utils/button/button.component';
import { FieldErrorDisplayComponent } from './components/utils/field-error-display/field-error-display.component';
import {RouterModule, Routes} from "@angular/router";
import { MainPageComponent } from './components/main-page/main-page.component';
import { PageAccessDeniedComponent } from './components/errorPages/page-access-denied/page-access-denied.component';
import { PageNotFoundComponent } from './components/errorPages/page-not-found/page-not-found.component';
import {environment} from "../environments/environment";
import {KeycloakAngularModule, KeycloakBearerInterceptor, KeycloakService} from "keycloak-angular";
import {AuthGuard} from "./auth/auth.guard";
import {AuthService} from "./auth/auth.service";
import { PatientsComponent } from './components/patient/patients/patients.component';
import { AddPatientComponent } from './components/patient/add-patient/add-patient.component';
import { PatientDetailsComponent } from './components/patient/patient-details/patient-details.component';
import { PatientItemComponent } from './components/patient/patient-item/patient-item.component';
import { DoctorsComponent } from './components/doctor/doctors/doctors.component';
import { DoctorItemComponent } from './components/doctor/doctor-item/doctor-item.component';
import { DoctorDetailsComponent } from './components/doctor/doctor-details/doctor-details.component';
import { AddDoctorComponent } from './components/doctor/add-doctor/add-doctor.component';
import { PrescriptionsComponent } from './components/prescription/prescriptions/prescriptions.component';
import { PrescriptionItemComponent } from './components/prescription/prescription-item/prescription-item.component';
import { PrescriptionDetailsComponent } from './components/prescription/prescription-details/prescription-details.component';
import { AddPrescriptionComponent } from './components/prescription/add-prescription/add-prescription.component';

function initializeKeycloak(keycloak: KeycloakService) {
  return () =>
    keycloak.init({
      config: {
        url: environment.keycloak.issuer,
        realm: environment.keycloak.realm,
        clientId: environment.keycloak.clientId
      },
      initOptions: {
        onLoad: 'login-required',
        checkLoginIframe: false,
        // silentCheckSsoRedirectUri:
        //   window.location.origin + `silent-check-sso.html`
      },
      enableBearerInterceptor: true,
      bearerPrefix: 'Bearer',
      bearerExcludedUrls: ['/mainPage'],
      loadUserProfileAtStartUp: true,
    });
}

const roles = ['ROLE_ADMIN', 'ROLE_PATIENT', 'ROLE_DOCTOR']

const routes: Routes = [
  {path: 'mainPage',
    component: MainPageComponent
  },
  {
    path: 'patient/:id',
    component: PatientDetailsComponent,
    canActivate: [AuthGuard],
    data: { roles: roles },
  },
  {
    path: 'patients',
    component: PatientsComponent,
    canActivate: [AuthGuard],
    data: { roles: roles },
  },
  {
    path: 'doctor/:id',
    component: DoctorDetailsComponent,
    canActivate: [AuthGuard],
    data: { roles: roles },
  },
  {
    path: 'doctors',
    component: DoctorsComponent,
    canActivate: [AuthGuard],
    data: { roles: roles },
  },
  {
    path: 'prescription/:id',
    component: PrescriptionDetailsComponent,
    canActivate: [AuthGuard],
    data: { roles: roles },
  },
  {
    path: 'prescriptions',
    component: PrescriptionsComponent,
    canActivate: [AuthGuard],
    data: { roles: roles },
  },
  {
    path: 'rocket',
    component: PageNotFoundComponent
  },
  {
    path: 'access-denied',
    component: PageAccessDeniedComponent,
    // canActivate: [AuthGuard],
  },
  {path: '', redirectTo: '/mainPage', pathMatch: 'full'},
  {path: '**', redirectTo: '/mainPage', pathMatch: 'full'}
];


@NgModule({
  declarations: [
    AppComponent,
    AppHeaderComponent,
    ButtonComponent,
    FieldErrorDisplayComponent,
    MainPageComponent,
    PageAccessDeniedComponent,
    PageNotFoundComponent,
    PatientsComponent,
    AddPatientComponent,
    PatientDetailsComponent,
    PatientItemComponent,
    DoctorsComponent,
    DoctorItemComponent,
    DoctorDetailsComponent,
    AddDoctorComponent,
    PrescriptionsComponent,
    PrescriptionItemComponent,
    PrescriptionDetailsComponent,
    AddPrescriptionComponent
  ],
  imports: [
    RouterModule.forRoot(routes),
    BrowserModule,
    HttpClientModule,
    FontAwesomeModule,
    BrowserAnimationsModule,
    FormsModule,
    ReactiveFormsModule,
    RouterModule,
    KeycloakAngularModule
  ],
  providers: [
    AuthService,
    {
      provide: APP_INITIALIZER,
      useFactory: initializeKeycloak,
      multi: true,
      deps: [KeycloakService]
    },
    {
      provide: HTTP_INTERCEPTORS,
      useClass: KeycloakBearerInterceptor,
      multi: true,
    }
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
