import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import {environment} from "../../environments/environment";

export type PromoStatus = 'ACTIVE' | 'EXPIRED' | 'DISABLED';
export type SortField = 'CODE' | 'EXPIRATION_DATE';
export type SortDir = 'ASC' | 'DESC';

export interface PromoDto {
    id: string;
    code: string;
    discountValue: number | null;
    discountPercentage: number | null;
    expiryDate: string | null;
    usageLimit: number | null;
    usages: number | null;
    status: PromoStatus | null;
    createdAt?: string;
    createdBy?: string;
    updatedAt?: string | null;
    updatedBy?: string | null;
}

export interface PromoPage {
    promos: PromoDto[];
    pageNumber: number;
    pageElements: number;
    totalPages: number;
    totalElements: number;
}

@Injectable({ providedIn: 'root' })
export class PromoService {
    private readonly base = environment.apiBaseUrl;

    constructor(private http: HttpClient) {}

    getPromos(filters: {
        code?: string;
        start?: string;
        end?: string;
        status?: PromoStatus;
        pageNumber?: number;
        pageSize?: number;
        sort?: SortField;
        direction?: SortDir;
    }): Observable<PromoPage> {
        let params = new HttpParams();
        Object.entries(filters).forEach(([k, v]) => {
            if (v !== undefined && v !== null && v !== '') params = params.set(k, String(v));
        });
        return this.http.get<PromoPage>(`${this.base}/promo`, { params });
    }

    createPromo(payload: Partial<PromoDto>): Observable<void> {
        return this.http.post<void>(`${this.base}/admin/promo`, payload);
    }

    updatePromo(id: string, payload: Partial<PromoDto>): Observable<void> {
        return this.http.put<void>(`${this.base}/admin/promo/${id}`, payload);
    }

    deletePromo(id: string): Observable<void> {
        return this.http.delete<void>(`${this.base}/admin/promo/${id}`);
    }
}
