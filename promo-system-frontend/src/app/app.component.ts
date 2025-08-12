import { Component } from '@angular/core';
import { KeycloakService } from './services/keycloak.service';
import { Router } from '@angular/router';

@Component({
    selector: 'app-root',
    templateUrl: './app.component.html',
    standalone: false
})
export class AppComponent {
  constructor(public kc: KeycloakService, private router: Router) {}

  logout() {
    this.kc.logout();
  }
}
