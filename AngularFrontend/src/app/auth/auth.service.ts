import { Injectable } from '@angular/core';
import {KeycloakService} from "keycloak-angular";
import {HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from "@angular/common/http";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  constructor(private keycloakService: KeycloakService) {
  }

  getUserName(): string {
    return this.keycloakService.getUsername();
  }

  logout(): void {
    this.keycloakService.logout();
  }

  getLoggedUser(): any {
    return this.keycloakService.getKeycloakInstance().idTokenParsed;
  }
}

// @Injectable()
// export class AuthInterceptor implements HttpInterceptor {
//   constructor(private keycloak: KeycloakService) {
//   }
//
//   intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
//     request = request.clone({
//       setHeaders: {
//         Authorization: `${this.keycloak.getToken()}`
//       }
//     });
//
//     return next.handle(request);
//   }
// }
