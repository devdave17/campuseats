# Network Analysis

## Website

URL: https://jsonplaceholder.typicode.com/

## Browser

Firefox

## Network Configuration

- DevTools: Network panel
- Disable cache: Enabled
- Page was reloaded after enabling Disable cache

## Results

### Request Count

Total requests: 14

### Total Page Size

Total transferred: 459.10 kB

Total resource size: 811.03 kB

### Slowest Resource

Resource: `553780578-52b3039d-1e4c-4c68-951c-93f0f1e73611.png`

Duration: 1328 ms (1.328 seconds)

Transferred: 341.08 kB

Resource size: 340.51 kB

### 3xx / 4xx Responses

- `302` — GitHub image resource: `52b3039d-1e4c-4c68-951c-93f0f1e73611`
- `302` — GitHub image resource: `adfee31f8ab86-4684-9a9b-f4f03ac5b75`
- No 4xx responses were observed in the Network panel.

## Additional Observation

The page finished loading in 2.16 seconds.

The DOMContentLoaded event occurred at approximately 362 ms.

The Network waterfall shows that the slowest resource was the PNG image, which took 1328 ms to load.

Two image requests returned HTTP 302 redirects.

Some requests were blocked by the uBlock Origin extension, but these were not HTTP 4xx responses.
