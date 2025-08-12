import { Injectable } from '@angular/core';
import Keycloak, { KeycloakInstance } from 'keycloak-js';
import { environment } from '../../environments/environment';

@Injectable()
export class KeycloakService {
  private keycloak!: KeycloakInstance;

  init(): Promise<boolean> {
    this.keycloak = Keycloak({
      url: environment.keycloak.url,
      realm: environment.keycloak.realm,
      clientId: environment.keycloak.clientId
    });

    // do not force login here; use check-sso so the app can show the login page
    return this.keycloak
      .init({ onLoad: 'check-sso', checkLoginIframe: false, pkceMethod: "S256" })
      .then((authenticated) => {
        // Schedule token refresh
        if (authenticated) {
          this.scheduleTokenRefresh();
        }
        return authenticated;
      })
      .catch((err) => {
        console.error('Keycloak init failed', err);
        return false;
      });
  }

  login(redirectUri?: string) {
    const opts: any = {};
    if (redirectUri) opts.redirectUri = redirectUri;
    this.keycloak.login(opts);
  }

  logout() {
    this.keycloak.logout({ redirectUri: window.location.origin });
  }

  isLoggedIn(): boolean {
    return !!(this.keycloak && this.keycloak.token);
  }

  getToken(): string | undefined {
    return this.keycloak?.token;
  }

  getUsername(): string | undefined {
    return this.keycloak && (this.keycloak.tokenParsed as any)?.preferred_username;
  }

  hasRole(role: string): boolean {
    const roles = (this.keycloak?.tokenParsed as any)?.roles ?? [];
    return roles.includes(role);
  }

  isAdmin(): boolean { return this.hasRole('ADMIN'); }
  isBusiness(): boolean { return this.hasRole('BUSINESS'); }
  isUser(): boolean { return this.hasRole('USER'); }

  private scheduleTokenRefresh() {
    this.keycloak.onTokenExpired = () => this.updateTokenIfNeeded();
  }

  async updateTokenIfNeeded(minValiditySeconds = 60): Promise<void> {
    if (!this.keycloak) return;
    try {
      await this.keycloak.updateToken(minValiditySeconds);
    } catch (e) {
      console.warn('Token refresh failed; redirecting to login');
      this.login();
    }
  }

}
