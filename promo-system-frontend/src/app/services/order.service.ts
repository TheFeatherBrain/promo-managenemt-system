import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

export interface OrderPromoResponse {
    valid: boolean;
    message: string;
}

@Injectable({ providedIn: 'root' })
export class OrderService {
    private readonly base = environment.apiBaseUrl;

    constructor(private http: HttpClient) {}

    validatePromo(code: string): Observable<OrderPromoResponse> {
        return this.http.post<OrderPromoResponse>(`${this.base}/order/promo`, { code });
    }

    completeOrder(code?: string): Observable<any> {
        const body = code && code.trim() ? { code: code.trim() } : {};
        return this.http.post(`${this.base}/order`, body);
    }
}
