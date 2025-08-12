import { Injectable } from '@angular/core';
import { CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, Router } from '@angular/router';
import { KeycloakService } from '../services/keycloak.service';

@Injectable({ providedIn: 'root' })
export class RoleGuard implements CanActivate {
    constructor(private kc: KeycloakService, private router: Router) {}

    canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean {
        const allowed: string[] = route.data?.['roles'] ?? [];

        if (!allowed.length) return true;

        if (!this.kc.isLoggedIn()) {
            this.router.navigate(['/login'], { queryParams: { returnUrl: state.url } });
            return false;
        }

        const ok = allowed.some(r => this.kc.hasRole(r));
        if (ok) return true;

        if (this.kc.isUser()) {
            this.router.navigate(['/order']);
        } else if (this.kc.isAdmin() || this.kc.isBusiness()) {
            this.router.navigate(['/promos']);
        } else {
            this.router.navigate(['/login']);
        }
        return false;
    }
}
