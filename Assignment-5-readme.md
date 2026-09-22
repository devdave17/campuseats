# CampusEats Assignment 5 — HTTP Methods & Headers

## Team Information

**Team ID:** 18

**Members:**
- 20251651034 — Dev Dave
- 20251651102 — Vikash Kumar
- 20251651053 — Kuldeep Rout
- 20251651029 — Arjun Bodana

---

# 1. CampusEats Method Map

| Action | Method | URL | Success |
|---|---|---|---|
| Create menu item | POST | `/menu-items` | 201 Created |
| Get one menu item | GET | `/menu-items/{id}` | 200 OK |
| List/filter menu items | GET | `/menu-items?category={category}` | 200 OK |
| Change availability | POST | `/menu-items/{id}/availability` | 200 OK |
| Discover supported methods | OPTIONS | `/menu-items` | 200 OK |

`POST /menu-items/{id}/availability` is used because changing availability
is a non-CRUD action represented as a sub-resource.

---

# 2. Safety and Idempotency

| Endpoint | Safe? | Idempotent? | Retry Mechanism |
|---|---|---|---|
| GET `/menu-items/{id}` | Yes | Yes | Normal GET / conditional GET |
| GET `/menu-items?category=...` | Yes | Yes | Normal GET |
| POST `/menu-items` | No | Not naturally | `Idempotency-Key` |
| POST `/menu-items/{id}/availability` | No | Final-state update can be repeated safely | `If-Match` |
| OPTIONS `/menu-items` | Yes | Yes | Normal retry |

---

# 3. Full HTTP Exchange

## Request

```http
POST /menu-items HTTP/1.1
Host: localhost:8080
Content-Type: application/json
Idempotency-Key: d1-create-001

{"name":"D1 Test Burger","description":"D1 curl test","price":160,"category":"Test"}
