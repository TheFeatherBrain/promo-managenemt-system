import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './components/login/login.component';
import { PromoComponent } from './components/promo/promo.component';
import { AuthGuard } from './guards/auth.guard';
import {OrderComponent} from "./components/order/order.component";
import {RoleGuard} from "./guards/role.guard";

const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: 'promos', component: PromoComponent, canActivate: [AuthGuard, RoleGuard], data: { roles: ['ADMIN', 'BUSINESS'] } },
  { path: 'order', component: OrderComponent, canActivate: [AuthGuard, RoleGuard], data: { roles: ['USER'] } },
  { path: '', redirectTo: 'login', pathMatch: 'full' },
  { path: '**', redirectTo: 'login' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {}
