import { Injectable } from '@angular/core';
import { CanActivate, Router, ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';
import { KeycloakService } from '../services/keycloak.service';

@Injectable()
export class AuthGuard implements CanActivate {
  constructor(private kc: KeycloakService, private router: Router) {}

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean {
    if (this.kc.isLoggedIn()) {
      return true;
    }
    // Not authenticated → go to login page and remember return url
    this.router.navigate(['/login'], { queryParams: { returnUrl: state.url } });
    return false;
  }
}
