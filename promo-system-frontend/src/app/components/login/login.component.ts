import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { KeycloakService } from '../../services/keycloak.service';

@Component({
    selector: 'app-login',
    templateUrl: './login.component.html',
    styleUrls: ['./login.component.css'],
    standalone: false
})
export class LoginComponent implements OnInit {
  loggedIn = false;
  username?: string;
  returnUrl: string | null = null;
  btnBusy = false;

  constructor(private kc: KeycloakService, private router: Router, private route: ActivatedRoute) {}

  ngOnInit(): void {
    this.returnUrl = this.route.snapshot.queryParamMap.get('returnUrl');

    this.loggedIn = this.kc.isLoggedIn();
    if (this.loggedIn) {
      this.username = this.kc.getUsername();

      if (this.returnUrl && this.returnUrl !== '/login') {
        this.router.navigateByUrl(this.returnUrl);
        return;
      }

      if (this.kc.isAdmin() || this.kc.isBusiness()) {
        this.router.navigateByUrl('/promos');
      } else {
        this.router.navigateByUrl('/order');
      }
    }
  }

  login(): void {
    this.btnBusy = true;
    this.kc.login(window.location.origin);
  }
}
