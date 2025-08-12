import { NgModule, inject, provideAppInitializer } from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';
import { HTTP_INTERCEPTORS, provideHttpClient, withInterceptorsFromDi } from '@angular/common/http';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {LoginComponent} from './components/login/login.component';
import {PromoComponent} from './components/promo/promo.component';
import {KeycloakService} from './services/keycloak.service';
import {AuthInterceptor} from './services/auth.interceptor';
import {AuthGuard} from './guards/auth.guard';
import {provideAnimationsAsync} from '@angular/platform-browser/animations/async';
import {MatInputModule} from "@angular/material/input";
import {MatSelectModule} from "@angular/material/select";
import {MatFormFieldModule} from "@angular/material/form-field";
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {MatTableModule} from "@angular/material/table";
import {MatPaginatorModule} from "@angular/material/paginator";
import {MatSortModule} from "@angular/material/sort";
import {MatButtonModule} from "@angular/material/button";
import {MatCardModule} from "@angular/material/card";
import {MatToolbarModule} from "@angular/material/toolbar";
import {MatNativeDateModule} from "@angular/material/core";
import {MatDatepickerModule} from "@angular/material/datepicker";
import {MatSnackBarModule} from "@angular/material/snack-bar";
import {MatIconModule} from "@angular/material/icon";
import {MatChipsModule} from "@angular/material/chips";
import {MatProgressBarModule} from "@angular/material/progress-bar";
import {MatDialogModule} from "@angular/material/dialog";
import {ConfirmDialogComponent} from "./components/shared/confirm-dialog/confirm-dialog.component";
import {OrderComponent} from "./components/order/order.component";

export function initializeKeycloak(kc: KeycloakService) {
  return () => kc.init();
}

@NgModule({ declarations: [
        AppComponent,
        LoginComponent,
        PromoComponent,
        ConfirmDialogComponent,
        OrderComponent
    ],
    bootstrap: [AppComponent], imports: [BrowserModule,
        BrowserAnimationsModule,
        FormsModule,
        ReactiveFormsModule,
        AppRoutingModule,
        MatTableModule,
        MatPaginatorModule,
        MatSortModule,
        MatFormFieldModule,
        MatInputModule,
        MatSelectModule,
        MatButtonModule,
        MatCardModule,
        MatToolbarModule,
        MatDatepickerModule,
        MatNativeDateModule,
        MatSnackBarModule,
        MatDialogModule,
        MatProgressBarModule,
        MatChipsModule,
        MatIconModule], providers: [
        KeycloakService,
        AuthGuard,
        provideAppInitializer(() => {
        const initializerFn = (initializeKeycloak)(inject(KeycloakService));
        return initializerFn();
      }),
        { provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true },
        provideAnimationsAsync(),
        provideHttpClient(withInterceptorsFromDi())
    ] })
export class AppModule {}
