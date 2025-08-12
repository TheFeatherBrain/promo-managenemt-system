import { Component } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { OrderService, OrderPromoResponse } from '../../services/order.service';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
    selector: 'app-order',
    templateUrl: './order.component.html',
    styleUrls: ['./order.component.css']
})
export class OrderComponent {
    constructor(
        private fb: FormBuilder,
        private orderApi: OrderService,
        private snack: MatSnackBar
    ) {}

    applying = false;
    lastResult: OrderPromoResponse | null = null;

    form = this.fb.group({
        code: ['', [Validators.maxLength(128)]]
    });

    get codeCtrl() { return this.form.get('code'); }

    apply() {
        if (this.form.invalid) return;
        this.applying = true;
        this.lastResult = null;

        this.orderApi.validatePromo(this.codeCtrl?.value!.trim()).subscribe({
            next: (res) => {
                this.lastResult = res;
                this.applying = false;
            },
            error: (err) => {
                console.error(err);
                const msg = err?.error?.message?.trim() || 'Failed to validate promo code';
                this.lastResult = { valid: false, message: msg };
                this.applying = false;
            }
        });
    }

    completeOrder() {
        const code = (this.codeCtrl?.value ?? '').toString().trim();

        this.orderApi.completeOrder(code).subscribe({
            next: () => {
                this.snack.open('Order completed!', 'Close', { duration: 2500 });
            },
            error: (err) => {
                const msg = err?.error?.message?.trim() || 'Order completion failed';
                this.snack.open(msg, 'Close', { duration: 3000 });
            }
        });
    }
}
