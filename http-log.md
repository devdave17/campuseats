# HTTP by Hand — Request/Response Log

## Request 1 — GET /users/1

### Request

```http
> GET /users/1 HTTP/2
> Host: jsonplaceholder.typicode.com
> User-Agent: curl/8.5.0
> Accept: */*
```

### Response

```
< HTTP/2 200
HTTP/2 200
< date: Sun, 16 Aug 2026 04:50:01 GMT
date: Sun, 16 Aug 2026 04:50:01 GMT
< content-type: application/json; charset=utf-8
content-type: application/json; charset=utf-8
< content-length: 509
content-length: 509
< access-control-allow-credentials: true
access-control-allow-credentials: true
< cache-control: max-age=43200
cache-control: max-age=43200
< etag: W/"1fd-+2Y3G3w049iSZtw5t1mzSnunngE"
etag: W/"1fd-+2Y3G3w049iSZtw5t1mzSnunngE"
< expires: -1
expires: -1
< nel: {"report_to":"heroku-nel","response_headers":["Via"],"max_age":3600,"success_fraction":0.01,"failure_fraction":0.1}
nel: {"report_to":"heroku-nel","response_headers":["Via"],"max_age":3600,"success_fraction":0.01,"failure_fraction":0.1}
< pragma: no-cache
pragma: no-cache
< report-to: {"group":"heroku-nel","endpoints":[{"url":"https://nel.heroku.com/reports?s=hcim2HEuPeWVzmNQK6NPKzhenGdnMXEOagm00OjdjcU%3D\u0026sid=e11707d5-02a7-43ef-b45e-2cf4d2036f7d\u0026ts=1786848777"}],"max_age":3600}
report-to: {"group":"heroku-nel","endpoints":[{"url":"https://nel.heroku.com/reports?s=hcim2HEuPeWVzmNQK6NPKzhenGdnMXEOagm00OjdjcU%3D\u0026sid=e11707d5-02a7-43ef-b45e-2cf4d2036f7d\u0026ts=1786848777"}],"max_age":3600}
< reporting-endpoints: heroku-nel="https://nel.heroku.com/reports?s=hcim2HEuPeWVzmNQK6NPKzhenGdnMXEOagm00OjdjcU%3D&sid=e11707d5-02a7-43ef-b45e-2cf4d2036f7d&ts=1786848777"
reporting-endpoints: heroku-nel="https://nel.heroku.com/reports?s=hcim2HEuPeWVzmNQK6NPKzhenGdnMXEOagm00OjdjcU%3D&sid=e11707d5-02a7-43ef-b45e-2cf4d2036f7d&ts=1786848777"
< server: cloudflare
server: cloudflare
< vary: Origin, Accept-Encoding
vary: Origin, Accept-Encoding
< via: 2.0 heroku-router
via: 2.0 heroku-router
< x-content-type-options: nosniff
x-content-type-options: nosniff
< x-powered-by: Express
x-powered-by: Express
< x-ratelimit-limit: 1000
x-ratelimit-limit: 1000
< x-ratelimit-remaining: 997
x-ratelimit-remaining: 997
< x-ratelimit-reset: 1786848823
x-ratelimit-reset: 1786848823
< age: 7024
age: 7024
< accept-ranges: bytes
accept-ranges: bytes
< cf-cache-status: HIT
cf-cache-status: HIT
< cf-ray: a2bdd0d79a862ded-SIN
cf-ray: a2bdd0d79a862ded-SIN
< alt-svc: h3=":443"; ma=86400
alt-svc: h3=":443"; ma=86400

<
{
  "id": 1,
  "name": "Leanne Graham",
  "username": "Bret",
  "email": "Sincere@april.biz",
  "address": {
    "street": "Kulas Light",
    "suite": "Apt. 556",
    "city": "Gwenborough",
    "zipcode": "92998-3874",
    "geo": {
      "lat": "-37.3159",
      "lng": "81.1496"
    }
  },
  "phone": "1-770-736-8031 x56442",
  "website": "hildegard.org",
  "company": {
    "name": "Romaguera-Crona",
    "catchPhrase": "Multi-layered client-server neural-net",
    "bs": "harness real-time e-markets"
  }
```

**Status: 200** — The request was successfully processed and the server returned the requested resource.

**Content-Type**: application/json.

## Request 2 — GET /posts/1 HTTP/2

### Request

```
> GET /posts/1 HTTP/2
> Host: jsonplaceholder.typicode.com
> User-Agent: curl/8.5.0
> Accept: */*
```

### Response

```
HTTP/2 200
date: Sun, 16 Aug 2026 05:09:28 GMT
content-type: application/json; charset=utf-8
content-length: 292

{
  "userId": 1,
  "id": 1,
  "title": "sunt aut facere repellat provident occaecati excepturi optio reprehenderit",
  "body": "quia et suscipit\nsuscipit recusandae consequuntur expedita et cum\nreprehenderit molestiae ut ut quas totam\nnostrum rerum est autem sunt rem eveniet architecto"
}
```

**Status: 200** — The request was successfully processed and the server returned the requested resource.

**Content-Type**: application/json.

## Request 3 — GET todos/1 HTTP/2

### Request

```
> GET /todos/1 HTTP/2
> Host: jsonplaceholder.typicode.com
> User-Agent: curl/8.5.0
> Accept: */*

```

### Response

```
HTTP/2 200
date: Sun, 16 Aug 2026 05:21:13 GMT
content-type: application/json; charset=utf-8
content-length: 83

{
  "userId": 1,
  "id": 1,
  "title": "delectus aut autem",
  "completed": false
}

```

**Status: 200** — The request was successfully processed and the server returned the requested resource.

**Content-Type**: application/json.

## Request 4 — GET /comments/1 HTTP/2

### Request

```
> GET /comments/1 HTTP/2
> Host: jsonplaceholder.typicode.com
> User-Agent: curl/8.5.0
> Accept: */*

```

### Response

```
HTTP/2 200
date: Sun, 16 Aug 2026 05:24:23 GMT
content-type: application/json; charset=utf-8
content-length: 268

{
  "postId": 1,
  "id": 1,
  "name": "id labore ex et quam laborum",
  "email": "Eliseo@gardner.biz",
  "body": "laudantium enim quasi est quidem magnam voluptate ipsam eos\ntempora quo necessitatibus\ndolor quam autem quasi\nreiciendis et nam sapiente accusantium"
}
```

**Status: 200** — The request was successfully processed and the server returned the requested resource.

**Content-Type**: application/json.

## Request 5 — GET /users/9999 HTTP/2

### Request

```
> GET /users/9999 HTTP/2
> Host: jsonplaceholder.typicode.com
> User-Agent: curl/8.5.0
> Accept: */*


```

### Response

```
HTTP/2 404
date: Sun, 16 Aug 2026 05:27:28 GMT
content-type: application/json; charset=utf-8
content-length: 2
```

**Note** : 404 means the requested resource was not found.
