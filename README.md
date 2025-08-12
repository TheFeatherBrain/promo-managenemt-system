# Promo Code Management System — Multi‑Tenant (Greece & Japan)

A full‑stack demo of a **multi‑tenant Promo Code Management System** with **Java 21 / Spring Boot 3.5.4** (backend), **Angular 19** (frontend), **Keycloak** for Authentication and Authorization (with custom tenant claim), and **PostgreSQL** using **schema‑per‑tenant** isolation. Database migrations are managed by **Flyway**. Everything runs via **Docker Compose**.

> **Tenants in this demo:** `Greece` and `Japan`\
> **Default users:** 6 accounts (Admin, Business, User for each tenant).

---

## ✨ Features

- **Promo Codes**: Create, update, delete, and list promo codes per tenant.
    - Fields: `code` (string), `amount` (percentage or fixed), `expiration date`, `usage limit`, `usages`, `status`.
- **Reporting / Listing** (tenant‑scoped):
    - Filter by **code**, **status**, **expiration date (start/end)**.
    - **Ordering** by **code** or **expiration date** (ASC/DESC).
    - **Pagination** supported.
    - **Usage count** per promo code.
- **Multi‑Tenancy**:
    - **Schema‑per‑tenant** in PostgreSQL.
    - Tenant derived from a **Keycloak custom claim** in the access token.
    - All APIs are **tenant‑aware**; strict data isolation between tenants.
- **Authentication & Authorization** (Keycloak):
    - **ADMIN**: Full access to promo management.
    - **BUSINESS**: Read‑only promo view.
    - **USER**: Place orders and try a promo (no promo management).
- **Backend**:
    - REST APIs with validation and centralized exception handling.
    - Flyway migrations per schema.
- **Frontend**:
    - Login page.
    - `/promo` page (Admin: CRUD; Business: view‑only).
    - `/order` page (User: apply a promo and complete an order).

## Quick Start

```bash
# 1) Clone the repo
git clone https://github.com/TheFeatherBrain/promo-managenemt-system.git
cd promo-managenemt-system

# 2) Start everything
docker compose up
```

After all services are healthy:

- **Keycloak Admin Console**: [http://localhost:8080](http://localhost:8080)\
  Admin user: `admin` / `admin`
- **Frontend (Angular)**: [http://localhost:4200](http://localhost:4200)
- **Backend (Spring Boot)**: [http://localhost:9090](http://localhost:9090)
- **PostgreSQL**: localhost:5432

---

## Tenants & Default Users

This demo comes with two tenants and six users:

| # | Username         | Password | Role     | Tenant |
| - | ---------------- | -------- | -------- | ------ |
| 1 | georgeadmin      | admin    | ADMIN    | Greece |
| 2 | georgebusiness   | business | BUSINESS | Greece |
| 3 | georgeuser       | user     | USER     | Greece |
| 4 | torakusuadmin    | admin    | ADMIN    | Japan  |
| 5 | torakusubusiness | business | BUSINESS | Japan  |
| 6 | torakusuuser     | user     | USER     | Japan  |

**Role access:**

- **ADMIN** → `/promo` (create/update/delete/view, tenant‑scoped)
- **BUSINESS** → `/promo` (view‑only, tenant‑scoped)
- **USER** → `/order` (apply promo and complete an order)

---

## Using the App (Demo Flow)

1. Open [**http://localhost:4200**](http://localhost:4200) and log in.
2. As **ADMIN** (e.g., `georgeadmin` / `admin`), go to **/promo** and create a few promo codes for **Greece**.
3. Log out and sign in as **USER** (e.g., `georgeuser` / `user`). Go to **/order**, try applying one of the created promo codes, and complete an order.
4. Sign in as **BUSINESS** (e.g., `georgebusiness` / `business`) and verify you can **view** promos but **not** edit them.

**Listing & Reporting:**

- Filter by **code**, **status**, **expiration date start** and **end**.
- Sort by **code** or **expiration date** (ASC/DESC).
- Results are **pageable** and include **usage count** per code.

---

## Extending to More Tenants

1. Add the new tenant value to the **Keycloak** user attributes.
2. Update backend enum com.promo.management.system.promomanagement.model.enumeration.Tenant enum with the new tenant.
3. Add the new value to application.yml at key pms.mutli-tenant.tenants. 
3. **Flyway** will take care the creation of the new schema and its tables.
