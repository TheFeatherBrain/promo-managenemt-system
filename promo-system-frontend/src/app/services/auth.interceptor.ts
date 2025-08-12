// src/app/services/auth.interceptor.ts
import { Injectable } from '@angular/core';
import {
  HttpInterceptor,
  HttpRequest,
  HttpHandler,
  HttpEvent,
  HttpErrorResponse
} from '@angular/common/http';
import { Observable, from, throwError } from 'rxjs';
import { catchError, switchMap } from 'rxjs/operators';
import { KeycloakService } from './keycloak.service';
import { environment } from '../../environments/environment';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {
  constructor(private kc: KeycloakService) {}

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    const isApi = req.url.startsWith(environment.apiBaseUrl);
    if (!isApi) {
      return next.handle(req);
    }

    return from(this.kc.updateTokenIfNeeded()).pipe(
        switchMap(() => {
          const token = this.kc.getToken();
          const authReq = token
              ? req.clone({
                setHeaders: {
                  Authorization: `Bearer ${token}`,
                  Accept: 'application/json'
                }
              })
              : req;

          return next.handle(authReq);
        }),
        catchError((err: HttpErrorResponse) => {
          // If we ever get unauthorized/forbidden, bounce to login
          if (err.status === 401 || err.status === 403) {
            this.kc.login(window.location.href);
          }
          return throwError(() => err);
        })
    );
  }
}
