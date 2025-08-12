import { Component, OnInit } from '@angular/core';
import { FormBuilder, AbstractControl, FormGroup } from '@angular/forms';
import { PromoService, PromoDto, PromoStatus } from '../../services/promo.service';
import { KeycloakService } from '../../services/keycloak.service';
import { PageEvent } from '@angular/material/paginator';
import { MatSnackBar } from '@angular/material/snack-bar';
import {MatDialog} from "@angular/material/dialog";
import {ConfirmDialogComponent} from "../shared/confirm-dialog/confirm-dialog.component";
import {Sort} from "@angular/material/sort";

@Component({
    selector: 'app-promo',
    templateUrl: './promo.component.html',
    styleUrls: ['./promo.component.css'],
    standalone: false
})
export class PromoComponent implements OnInit {

  // TABLE / FILTERS
  promos: PromoDto[] = [];
  loading = false;
  error: string | null = null;

  // filters bound to inputs
  fCode = '';
  fStatus: '' | PromoStatus = '';
  fStartISO = '';
  fEndISO = '';

  fStart: Date | null = null;
  fEnd: Date | null = null;
  fStartTime: string = '';
  fEndTime: string = '';

  // paging
  pageNumber = 0;
  pageSize = 10;
  pageSizeOptions = [5, 10, 20, 50, 100];
  totalPages = 0;
  totalElements = 0;

  // sorting
  sortActive: 'code' | 'expiryDate' | '' = '';
  sortDirection: 'asc' | 'desc' | '' = '';

  // FORM (create/update)
  form: FormGroup = this.fb.group({
    id: [''],
    code: [''],
    discountPercentage: [null],
    discountValue: [null],
    expiryDate: [''],
    expiryTime: [''],
    usageLimit: [null],
    status: 'ACTIVE'
  }, { validators: this.discountRule });

  isEditing = false;

  constructor(
      private fb: FormBuilder,
      public auth: KeycloakService,
      private promoApi: PromoService,
      private snack: MatSnackBar,
      private dialog: MatDialog
  ) {}

  ngOnInit(): void {
    this.load(0);
    if (!this.auth.isAdmin()) {
      this.form.disable();
    }
  }

  onPage(e: PageEvent) {
    this.pageSize = e.pageSize;
    this.load(e.pageIndex);
  }

  onSort(ev: Sort) {
    this.sortActive = (ev.active as any) || '';
    this.sortDirection = (ev.direction as any) || '';
    this.load(0);
  }


  private toastOk(msg: string) {
    this.snack.open(msg, 'Close', { duration: 2500 });
  }
  private toastErr(msg: string) {
    this.snack.open(msg, 'Close', { duration: 4000 });
  }

  // ---- Validation rules ----
  private discountRule(group: AbstractControl) {
    const dv = group.get('discountValue')?.value;
    const dp = group.get('discountPercentage')?.value;
    if (dv && dp) return { bothDiscounts: true };
    return null;
  }

  private validateCreate(): string | null {
    const v = this.form.value;
    const dv = v.discountValue;
    const dp = v.discountPercentage;
    if (!v.code || v.code.trim().length === 0) return 'Code is required.';
    if (((dv == null || dv === '') && (dp == null || dp === '')) || (dv && dp)) {
      return 'Provide only one of discount value or percentage.';
    }
    if (v.expiryDate) {
      const dateTime = this.combineDateAndTime(v.expiryDate, v.expiryTime);
      const now = new Date()
      if (dateTime.getTime() < now.getTime()) return 'Expiration date must be in the future.';
    }
  }

  private validateUpdate(): string | null {
    const v = this.form.value;
    const dv = v.discountValue;
    const dp = v.discountPercentage;
    if (!v.code || v.code.trim().length === 0) return 'Code is required.';
    const provided = Object.entries(v)
        .filter(([k, val]) => k !== 'id' && (val !== null && val !== ''))
        .map(([k]) => k);

    if (provided.length === 0) return 'At least one field must be populated.';
    if (dv && dp) return 'Provide only one of discount value or percentage.';

    if (v.expiryDate) {
      const dateTime = this.combineDateAndTime(v.expiryDate, v.expiryTime);
      const now = new Date()
      const past = dateTime.getTime() < now.getTime();
      if ((v.status == 'ACTIVE' || v.status == 'DISABLED') && past) return 'Expiration date must be in the future for non expired promos.'
      if (v.status == 'EXPIRED' && !past) return 'Expiration date must be in the past for expired promos.'
    }

    return null;
  }

  load(page = 0) {
    this.loading = true;
    this.error = null;

    const start = this.fStart ? this.toCustomDate(this.combineDateAndTime(this.fStart, this.fStartTime)) : undefined;
    const end   = this.fEnd   ? this.toCustomDate(this.combineDateAndTime(this.fEnd,   this.fEndTime))   : undefined;

    const sortParams = this.mapSortForApi();

    this.promoApi.getPromos({
      code: this.fCode || undefined,
      status: this.fStatus || undefined,
      start, end,
      pageNumber: page,
      pageSize: this.pageSize,
      ...sortParams
    }).subscribe({
      next: (res) => {
        this.promos = res.promos ?? [];
        this.pageNumber    = Number.isFinite(res.pageNumber) ? res.pageNumber : page;
        this.totalPages    = Number.isFinite(res.totalPages) ? res.totalPages : this.totalPages;
        this.totalElements = Number.isFinite(res.totalElements) ? res.totalElements : this.totalElements;
        this.loading = false;
      },
      error: (err) => {
        console.error(err);
        this.toastErr(this.extractErrorMessage(err, 'Failed to load promos'));
        this.loading = false;
      }
    });
  }

  applyFilters() { this.load(0); }

  clearFilters() {
    this.fCode = '';
    this.fStatus = '';
    this.fStartISO = '';
    this.fEndISO = '';
    this.fStart = null;
    this.fEnd = null;
    this.fStartTime = '';
    this.fEndTime = '';
    this.load(0);
  }

  // ---- Create / Edit / Delete ----
  startCreate() {
    this.isEditing = false;
    this.form.reset({
      id: '',
      code: '',
      discountPercentage: null,
      discountValue: null,
      expiryDate: null,
      expiryTime: '',
      usageLimit: null,
      status: 'ACTIVE'
    });
    if (!this.auth.isAdmin()) this.form.disable(); else this.form.enable();
  }

  startEdit(p: PromoDto) {
    this.isEditing = true;

    const date = p.expiryDate ? new Date(p.expiryDate) : null;
    this.form.reset({
      id: p.id,
      code: p.code ?? '',
      discountPercentage: p.discountPercentage,
      discountValue: p.discountValue,
      expiryDate: date,
      expiryTime: date ? this.toHHmm(date) : '',
      usageLimit: p.usageLimit,
      status: p.status ?? ''
    });
    if (!this.auth.isAdmin()) this.form.disable(); else this.form.enable();
  }

  submit() {
    if (!this.auth.isAdmin()) return;

    const errorMsg = this.isEditing ? this.validateUpdate() : this.validateCreate();
    if (errorMsg) {
      this.error = errorMsg;
      this.toastErr(errorMsg);
      return;
    }

    const v = this.form.value;

    let expiryOut: string | undefined;
    if (v.expiryDate instanceof Date) {
      const combined = this.combineDateAndTime(v.expiryDate, v.expiryTime);
      expiryOut = this.toCustomDate(combined);
    } else if (typeof v.expiryDate === 'string' && v.expiryDate) {
      const combined = this.combineDateAndTime(new Date(v.expiryDate), v.expiryTime);
      expiryOut = this.toCustomDate(combined);
    } else {
      expiryOut = undefined;
    }

    const payload: any = {
      code: this.isEditing ? (v.code || undefined) : v.code,
      discountPercentage: v.discountPercentage ?? undefined,
      discountValue: v.discountValue ?? undefined,
      expiryDate: expiryOut,
      usageLimit: v.usageLimit ?? undefined,
      status: v.status || undefined
    };

    if (this.isEditing && v.id) {
      Object.keys(payload).forEach(k => payload[k] === undefined && delete payload[k]);
      this.promoApi.updatePromo(v.id, payload).subscribe({
        next: () => { this.afterMutation('Promo updated'); },
        error: (e) => { console.error(e); this.toastErr(this.extractErrorMessage(e, 'Update failed')); }
      });
    } else {
      this.promoApi.createPromo(payload).subscribe({
        next: () => { this.afterMutation('Promo created'); },
        error: (e) => { console.error(e); this.toastErr(this.extractErrorMessage(e, 'Create failed')); }
      });
    }
  }

  delete(p: PromoDto) {
    if (!this.auth.isAdmin()) return;

    const ref = this.dialog.open(ConfirmDialogComponent, {
      data: {
        title: 'Delete promo?',
        message: `Are you sure you want to delete "${p.code}"?`,
        confirmText: 'Delete',
        cancelText: 'Cancel'
      }
    });

    ref.afterClosed().subscribe(ok => {
      if (!ok) return;

      this.promoApi.deletePromo(p.id).subscribe({
        next: () => { this.afterMutation('Promo deleted'); },
        error: (e) => { console.error(e); this.error = 'Delete failed'; }
      });
    });
  }

  private afterMutation(okMessage: string) {
    this.load(this.pageNumber);
    this.startCreate();
    this.toastOk(okMessage);
  }

  protected toCustomDate(input: string | Date): string {
    const d = typeof input === 'string' ? new Date(input) : input;
    const yyyy = d.getFullYear();
    const MM   = this.pad(d.getMonth() + 1);
    const dd   = this.pad(d.getDate());
    const hh   = this.pad(d.getHours());
    const mm   = this.pad(d.getMinutes());
    const ss   = this.pad(d.getSeconds());
    const mmm  = this.pad(d.getMilliseconds(), 3);
    return `${yyyy}-${MM}-${dd}T${hh}:${mm}:${ss}.${mmm}`;
  }

  private pad(n: number, len = 2) { return String(n).padStart(len, '0'); }

  private toHHmm(d: Date): string {
    return `${this.pad(d.getHours())}:${this.pad(d.getMinutes())}`;
  }

  private combineDateAndTime(date: Date, hhmm?: string): Date {
    const d = new Date(date);
    if (hhmm && /^\d{2}:\d{2}$/.test(hhmm)) {
      const [h, m] = hhmm.split(':').map(Number);
      d.setHours(h, m, 0, 0);
    } else {
      d.setHours(0, 0, 0, 0);
    }
    return d;
  }

  private mapSortForApi(): { sort?: 'CODE'|'EXPIRATION_DATE', direction?: 'ASC'|'DESC' } {
    if (!this.sortActive || !this.sortDirection) return {};
    const sort = this.sortActive === 'code' ? 'CODE' : 'EXPIRATION_DATE';
    const direction = this.sortDirection.toUpperCase() as 'ASC' | 'DESC';
    return { sort, direction };
  }

  private extractErrorMessage(err: any, fallback: string): string {
    if (err && err.error && typeof err.error.message === 'string' && err.error.message.trim()) {
      return err.error.message;
    }
    return fallback;
  }
}
