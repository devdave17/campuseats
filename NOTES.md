# CampusEats Assignment 4 - Catalogue Service

## Team ID: 18

## Members:
- 20251651034 Dave Dev
- 20251651102 Vikash Kumar
- 20251651053 Kuldeep Rout
- 20251651029 Arjun Bodana

## A4 Resource Table

| Method | URL | What it does | Success | Failure |
|---|---|---|---|---|
| POST | /menu-items | Create a menu item | 201 | 400, 409 |
| GET | /menu-items/{id} | Read one menu item | 200 | 404 |
| GET | /menu-items?category=Fast Food | List menu items by category | 200 | 400 |
| PATCH | /menu-items/{id}/availability | Change menu item availability | 200 | 404, 409 |

## A5 Hard Choice

The hardest operation was changing availability. I did not put the verb in the URL as `/setAvailability`. I used `/menu-items/{id}/availability` because availability is a state of a menu item and is represented as a state-changing sub-resource.

## D3 Fallback

If a dependent service is unavailable after the timeout and retries, the Catalogue service should return `503 Service Unavailable`. It should not return a successful result when the dependent operation has not actually completed, because that would give the client an incorrect result.

## 1. WSDL vs OpenAPI line count

Assignment 3 WSDL: **93 lines**

`openapi.yaml`: **193 lines**

Difference: **100 lines**

The difference is mainly made of the extra SOAP/WSDL contract structure such as XML message definitions, SOAP bindings, operations, port types, and service/port information. OpenAPI directly describes REST paths, HTTP methods, parameters, request/response bodies and schemas, so it does not need the same SOAP-specific declarations.

Two things the WSDL declared that the OpenAPI file does not need are:

1. **SOAP binding information**, including the SOAP transport/binding and SOAPAction.
2. **Separate WSDL message/operation declarations**, including SOAP input/output message definitions and port-type plumbing.

## 2. SOAP Fault to REST error

Assignment 3 partner SOAP Fault:

```xml
<soap:Fault>
    <faultcode>soap:Client</faultcode>
    <faultstring>Payment was declined</faultstring>
    <detail>
        <pay:ChargeFault>
            <pay:code>card_declined</pay:code>
            <pay:message>The payment could not be completed.</pay:message>
        </pay:ChargeFault>
    </detail>
</soap:Fault>
```

The REST version represents a failure with an HTTP status code and a problem JSON body instead of putting the failure inside a SOAP envelope.

Example REST problem response:

```http
HTTP/1.1 404 Not Found
Content-Type: application/json
```

```json
{
  "type": "https://campuseats.com/problems/not-found",
  "title": "Resource Not Found",
  "status": 404,
  "detail": "Menu item not found"
}
```

Returning the error inside `200 OK` is a problem because clients, gateways, caches and monitoring systems normally use the HTTP status code to identify success or failure. `200 OK` tells the network that the request succeeded even when the body contains an application error. This makes failures harder to detect, monitor, cache and handle correctly.

## 3. UDDI publish, find, bind

The three UDDI moves still exist conceptually, but not as the old explicit UDDI workflow.

- **Publish:** still exists through publishing the API contract and service endpoint.
- **Find:** still exists through API documentation, service catalogues, gateways, DNS or other discovery mechanisms.
- **Bind:** the explicit UDDI bind operation disappeared. The client instead uses the discovered HTTP endpoint and the OpenAPI contract to make the REST request.

OpenAPI and normal service-discovery/configuration mechanisms take over much of the practical job that a UDDI registry performed.

## 4. XML Schema vs validation in code

The Java function carrying the validation responsibility is:

```text
create()
```

in `MenuItemService`.

It checks the request before the menu item is created:

```java
if (request.getName() == null || request.getName().isBlank()) {
    throw new IllegalArgumentException("Name is required");
}

if (request.getCategory() == null || request.getCategory().isBlank()) {
    throw new IllegalArgumentException("Category is required");
}

if (request.getPrice() < 0) {
    throw new IllegalArgumentException("Price cannot be negative");
}
```

Without these checks, an invalid request such as an empty name, empty category, or negative price could reach the business logic and create invalid menu-item data.

## 5. Where I would still choose SOAP

I would still choose SOAP for the **external Payment Gateway** boundary used by CampusEats.

Assignment 3 already selected the external Payment Gateway and the `charge` operation because SOAP provides a strong contract, message-level security support and reliable transaction handling.

The exact guarantee I would be buying is a **strong WSDL/XSD-based message contract with structured SOAP fault handling**, which is useful for a formal enterprise payment integration. For the normal Catalogue Service operations, REST is simpler and better suited to resource-oriented operations such as create, read, filter and update availability.

## Curl Failure Paths

The submission must include failure paths in the curl transcript.

### 400 Bad Request

```powershell
curl.exe -i -X POST http://localhost:8080/menu-items -H "Content-Type: application/json" -H "Idempotency-Key: fail-400" -d '{"name":"","description":"Test","price":120,"category":"Fast Food"}'
```

Expected: `400 Bad Request`

### 404 Not Found

```powershell
curl.exe -i http://localhost:8080/menu-items/does-not-exist
```

Expected: `404 Not Found`

### 409 Conflict

Include the actual curl command and response only if the final implementation has a real conflict condition returning `409 Conflict`. Do not show a fabricated response.

### 422 Unprocessable Content

Include the actual curl command and response only if the final implementation has a semantic validation condition returning `422 Unprocessable Content`. Do not show a fabricated response.

## Curl Success Paths

### Create Menu Item

```powershell
curl.exe -i -X POST http://localhost:8080/menu-items -H "Content-Type: application/json" -H "Idempotency-Key: test-002" -d '{"name":"Veg Burger","description":"Campus burger","price":120,"category":"Fast Food"}'
```

Expected: `201 Created` with a `Location` header.

### Read Menu Item

```powershell
curl.exe -i http://localhost:8080/menu-items/{id}
```

Expected: `200 OK`

### Filter by Category

```powershell
curl.exe -i "http://localhost:8080/menu-items?category=Fast%20Food"
```

Expected: `200 OK`

### Update Availability

```powershell
curl.exe -i -X PATCH http://localhost:8080/menu-items/{id}/availability -H "Content-Type: application/json" -d '{"available":false}'
```

Expected: `200 OK`

### Idempotency

Repeat the create request using the same `Idempotency-Key`.

Expected: the original menu item ID is returned rather than creating a second menu item.