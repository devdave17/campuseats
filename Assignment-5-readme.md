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
| POST `/menu-items` | No | No | `Idempotency-Key` |
| POST `/menu-items/{id}/availability` | No | No | `If-Match` |
| OPTIONS `/menu-items` | Yes | Yes | Normal retry |

GET requests only retrieve data and therefore do not modify the resource.

The create operation is not naturally idempotent because repeating the same
POST can create another resource. The `Idempotency-Key` prevents duplicate
creation during retries.

The availability operation changes the resource state. `If-Match` is used
to prevent an update based on an outdated resource version.

---

# 3. Full HTTP Exchange

## Request

```http
POST /menu-items HTTP/1.1
Host: localhost:8080
Content-Type: application/json
Idempotency-Key: d1-create-001

{"name":"D1 Test Burger","description":"D1 curl test","price":160,"category":"Test"}
```

## Response

```http
HTTP/1.1 201
Location: /menu-items/2bbdf2d6-f9b1-4115-9868-cc97fc409e2a
Content-Type: application/json

{"available":true,"category":"Test","description":"D1 curl test","id":"2bbdf2d6-f9b1-4115-9868-cc97fc409e2a","name":"D1 Test Burger","price":160.0,"version":1}
```

The `201 Created` response indicates that the menu item was successfully
created. The `Location` header identifies the newly created resource.

---

# 4. Headers Table

| Endpoint | Request Headers | Response Headers |
|---|---|---|
| `POST /menu-items` | `Content-Type`, `Idempotency-Key` | `Content-Type`, `Location` |
| `GET /menu-items/{id}` | `Accept`, `If-None-Match` | `Content-Type`, `ETag` |
| `GET /menu-items?category=...` | `Accept` | `Content-Type` |
| `POST /menu-items/{id}/availability` | `Content-Type`, `If-Match` | `Content-Type`, `ETag` |
| `OPTIONS /menu-items` | `Origin`, `Access-Control-Request-Method` | `Allow`, CORS headers |

---

# 5. Safe-Retry Plan

| Endpoint | Risk During Retry | Mechanism | Reason |
|---|---|---|---|
| `GET /menu-items/{id}` | Low | `If-None-Match` | Avoids transferring unchanged data |
| `GET /menu-items?category=...` | Low | Normal GET | GET is safe and idempotent |
| `POST /menu-items` | Duplicate creation | `Idempotency-Key` | Same key returns original result |
| `POST /menu-items/{id}/availability` | Stale update | `If-Match` | Prevents update using old version |
| `OPTIONS /menu-items` | Low | Normal retry | Does not modify resource state |

---

# 6. Assignment Questions

## Q1. For three endpoints give the method, success status, most important single response header, and why.

### Create Menu Item

**Method:** POST  
**Endpoint:** `/menu-items`  
**Success Status:** `201 Created`  
**Important Header:** `Location`

The `Location` header identifies the URI of the newly created menu item.

### Get Menu Item

**Method:** GET  
**Endpoint:** `/menu-items/{id}`  
**Success Status:** `200 OK`  
**Important Header:** `ETag`

The ETag identifies the current version of the resource and can be used for
conditional requests.

### Change Availability

**Method:** POST  
**Endpoint:** `/menu-items/{id}/availability`  
**Success Status:** `200 OK`  
**Important Header:** `ETag`

The returned ETag represents the updated version of the resource.

---

## Q2. Which endpoints are safe/idempotent, which are neither, and how are they made retry-safe?

The GET endpoints are safe and idempotent because they only retrieve data.

`OPTIONS /menu-items` is safe and idempotent because it only describes the
supported methods.

`POST /menu-items` is neither safe nor naturally idempotent because a
successful request creates a new resource. It is made retry-safe by using an
`Idempotency-Key`.

`POST /menu-items/{id}/availability` is not safe because it changes resource
state. It uses `If-Match` to protect against stale updates.

---

## Q3. Show ETag, a request returning 304, and a write returning 412. What does each save or prevent?

The server returns an ETag with a single-item GET:

```http
ETag: "1"
```

The client can then send:

```http
If-None-Match: "1"
```

If the resource has not changed, the server returns:

```http
HTTP/1.1 304 Not Modified
ETag: "1"
```

A `304 Not Modified` response avoids transferring the complete resource when
the client already has the current representation.

For a conditional write, the client sends:

```http
If-Match: "1"
```

If the resource has changed and the supplied ETag is stale, the server returns:

```http
HTTP/1.1 412 Precondition Failed
```

This prevents a stale client from overwriting a newer version of the resource.

---

## Q4. Give one case for 422 and another for 400. What is the difference?

A `400 Bad Request` is appropriate when the HTTP request itself is malformed.

Example:

```http
POST /menu-items HTTP/1.1
Content-Type: application/json

{"name":
```

The JSON body is malformed and cannot be parsed correctly.

A `422 Unprocessable Content` is appropriate when the JSON syntax is valid
but the submitted value violates an application rule.

Example:

```http
POST /menu-items HTTP/1.1
Content-Type: application/json

{"name":"Test Burger","description":"Test","price":-50,"category":"Test"}
```

The JSON is valid, but a negative price is not a valid menu item value.

---

## Q5. A cross-origin browser call is blocked while the server logs 200. Who blocked it, and which response header fixes it?

The browser can block access to the response because of the same-origin
policy even when the server successfully processes the request.

The server needs an appropriate CORS response header:

```http
Access-Control-Allow-Origin: <allowed-origin>
```

For preflight requests, the server can also return:

```http
Access-Control-Allow-Methods: GET, POST, OPTIONS
```

---

## Q6. Give one response where Cache-Control allows caching and one where it should be no-store. Why?

A GET response can allow caching:

```http
Cache-Control: max-age=60
```

This allows the response to be reused by a cache for the specified period.

For a response that should not be stored:

```http
Cache-Control: no-store
```

This prevents caches from storing the response.

---

## Q7. The search endpoint is GET. When would POST be right instead, and what would be given up?

GET is appropriate when search parameters can be represented using the query
string:

```http
GET /menu-items?category=Fast%20Food
```

POST can be appropriate when the search contains a large or complex
structured query.

For example:

```http
POST /menu-items/search
Content-Type: application/json

{
  "categories": ["Fast Food", "Pizza"],
  "minPrice": 50,
  "maxPrice": 250
}
```

Using POST gives up some of the normal advantages of GET, such as simple
bookmarkability and straightforward caching of the URL.

---

## Q8. What does Location mean on 201 and 3xx? What does it point to?

For a `201 Created` response, the `Location` header identifies the newly
created resource.

Example:

```http
HTTP/1.1 201 Created
Location: /menu-items/2bbdf2d6-f9b1-4115-9868-cc97fc409e2a
```

For a `3xx` response, the `Location` header identifies the URI to which the
client should redirect or continue the request according to the particular
3xx status code.

---

# 7. HTTP Status Code Summary

| Situation | Status |
|---|---|
| Successful create | `201 Created` |
| Successful read | `200 OK` |
| Successful update | `200 OK` |
| Successful delete | `204 No Content` |
| Malformed request | `400 Bad Request` |
| Missing resource | `404 Not Found` |
| Conflict | `409 Conflict` |
| Domain validation failure | `422 Unprocessable Content` |
| Missing/invalid authentication | `401 Unauthorized` |
| Failed conditional request | `412 Precondition Failed` |
| Conditional GET with unchanged resource | `304 Not Modified` |
| Rate limit exceeded | `429 Too Many Requests` |

---

# 8. Conditional GET

A normal GET returns the current resource together with its ETag:

```http
GET /menu-items/2bbdf2d6-f9b1-4115-9868-cc97fc409e2a HTTP/1.1
Host: localhost:8080
```

Example response:

```http
HTTP/1.1 200 OK
ETag: "1"
Content-Type: application/json
```

The client can use the ETag in a later request:

```http
GET /menu-items/2bbdf2d6-f9b1-4115-9868-cc97fc409e2a HTTP/1.1
Host: localhost:8080
If-None-Match: "1"
```

If the resource has not changed:

```http
HTTP/1.1 304 Not Modified
ETag: "1"
```

---

# 9. Conditional Update

The client can use the ETag received from a previous GET:

```http
POST /menu-items/2bbdf2d6-f9b1-4115-9868-cc97fc409e2a/availability HTTP/1.1
Host: localhost:8080
Content-Type: application/json
If-Match: "1"

{"available":false}
```

If the ETag is current, the update is performed.

If the ETag is stale:

```http
HTTP/1.1 412 Precondition Failed
```

The stale update is rejected instead of overwriting a newer version.

---

# 10. Idempotency-Key

The first create request uses:

```http
POST /menu-items HTTP/1.1
Host: localhost:8080
Content-Type: application/json
Idempotency-Key: d1-create-001

{"name":"D1 Test Burger","description":"D1 curl test","price":160,"category":"Test"}
```

The server creates the resource and returns:

```http
HTTP/1.1 201 Created
Location: /menu-items/2bbdf2d6-f9b1-4115-9868-cc97fc409e2a
```

A repeated request with the same `Idempotency-Key` returns the original
result instead of creating another menu item.

This prevents duplicate resources when a client retries after a timeout or
lost response.

---

# 11. OPTIONS

The service supports method discovery through:

```http
OPTIONS /menu-items HTTP/1.1
Host: localhost:8080
```

The response can advertise supported methods using:

```http
Allow: GET, POST, OPTIONS
```

OPTIONS does not modify menu item data.

---

# 12. Authentication and Security Headers

Protected endpoints use:

```http
Authorization: Bearer <token>
```

A missing or empty Authorization header results in:

```http
HTTP/1.1 401 Unauthorized
```

Security-related response headers include:

```http
X-Content-Type-Options: nosniff
Strict-Transport-Security: max-age=31536000
```

HSTS is intended for HTTPS deployments.

---

# 13. CORS

For browser requests coming from another origin, the server provides an
appropriate CORS header:

```http
Access-Control-Allow-Origin: <allowed-origin>
```

For browser preflight requests, the server can return:

```http
Access-Control-Allow-Methods: GET, POST, OPTIONS
```

These headers allow the browser to determine whether the cross-origin
request is permitted.

---

# 14. Rate Limiting

The API exposes rate-limit information using:

```http
X-RateLimit-Limit: <limit>
X-RateLimit-Remaining: <remaining>
```

When the request limit is exceeded:

```http
HTTP/1.1 429 Too Many Requests
Retry-After: <seconds>
```

`Retry-After` tells the client when it can try the request again.

---

# 15. Content Negotiation

JSON request bodies use:

```http
Content-Type: application/json
```

Clients can request JSON responses using:

```http
Accept: application/json
```

If the requested response representation is not supported, the server can
return:

```http
HTTP/1.1 406 Not Acceptable
```

Malformed JSON requests result in:

```http
HTTP/1.1 400 Bad Request
```

The API therefore uses HTTP methods, status codes and headers to clearly
define how clients create, retrieve and modify menu items while handling
conditional requests, retries, authentication, CORS, caching and rate
limiting.
