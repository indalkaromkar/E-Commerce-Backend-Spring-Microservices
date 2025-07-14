# API Documentation

## Authentication

All protected endpoints require JWT token in the Authorization header:
```
Authorization: Bearer <jwt-token>
```

## User Service API (Port: 9050)

### Authentication Endpoints

#### Login
```http
POST /user-service/api/auth/login
Content-Type: application/json

{
  "username": "string",
  "password": "string"
}
```

#### Register
```http
POST /user-service/api/auth/register
Content-Type: application/json

{
  "username": "string",
  "email": "string",
  "password": "string",
  "firstName": "string",
  "lastName": "string"
}
```

### User Management

#### Get All Users
```http
GET /user-service/api/users
Authorization: Bearer <token>
```

#### Get User by ID
```http
GET /user-service/api/users/{userId}
Authorization: Bearer <token>
```

#### Update User
```http
PUT /user-service/api/users/{userId}
Authorization: Bearer <token>
Content-Type: application/json

{
  "firstName": "string",
  "lastName": "string",
  "email": "string",
  "phone": "string"
}
```

#### Delete User
```http
DELETE /user-service/api/users/{userId}
Authorization: Bearer <token>
```

### Address Management

#### Get User Addresses
```http
GET /user-service/api/addresses
Authorization: Bearer <token>
```

#### Add Address
```http
POST /user-service/api/addresses
Authorization: Bearer <token>
Content-Type: application/json

{
  "street": "string",
  "city": "string",
  "state": "string",
  "zipCode": "string",
  "country": "string",
  "isDefault": boolean
}
```

#### Update Address
```http
PUT /user-service/api/addresses/{addressId}
Authorization: Bearer <token>
Content-Type: application/json

{
  "street": "string",
  "city": "string",
  "state": "string",
  "zipCode": "string",
  "country": "string",
  "isDefault": boolean
}
```

#### Delete Address
```http
DELETE /user-service/api/addresses/{addressId}
Authorization: Bearer <token>
```

### Account Management

#### Activate Account
```http
POST /user-service/api/users/activate-account
Content-Type: application/json

{
  "token": "string"
}
```

#### Forgot Password
```http
POST /user-service/api/users/forgot-password
Content-Type: application/json

{
  "email": "string"
}
```

#### Reset Password
```http
POST /user-service/api/users/reset-password
Content-Type: application/json

{
  "token": "string",
  "newPassword": "string"
}
```

## Product Service API (Port: 9051)

### Product Management

#### Get All Products
```http
GET /product-service/api/products
Query Parameters:
- page: int (default: 0)
- size: int (default: 10)
- sort: string (default: "id")
- category: string (optional)
- minPrice: decimal (optional)
- maxPrice: decimal (optional)
```

#### Get Product by ID
```http
GET /product-service/api/products/{productId}
```

#### Create Product
```http
POST /product-service/api/products
Authorization: Bearer <token>
Content-Type: application/json

{
  "name": "string",
  "description": "string",
  "price": decimal,
  "categoryId": long,
  "brand": "string",
  "sku": "string",
  "stockQuantity": int,
  "images": ["string"]
}
```

#### Update Product
```http
PUT /product-service/api/products/{productId}
Authorization: Bearer <token>
Content-Type: application/json

{
  "name": "string",
  "description": "string",
  "price": decimal,
  "categoryId": long,
  "brand": "string",
  "stockQuantity": int,
  "images": ["string"]
}
```

#### Delete Product
```http
DELETE /product-service/api/products/{productId}
Authorization: Bearer <token>
```

### Category Management

#### Get All Categories
```http
GET /product-service/api/categories
```

#### Create Category
```http
POST /product-service/api/categories
Authorization: Bearer <token>
Content-Type: application/json

{
  "name": "string",
  "description": "string",
  "parentCategoryId": long
}
```

## Order Service API (Port: 9052)

### Order Management

#### Get User Orders
```http
GET /order-service/api/orders
Authorization: Bearer <token>
Query Parameters:
- status: string (optional)
- page: int (default: 0)
- size: int (default: 10)
```

#### Get Order by ID
```http
GET /order-service/api/orders/{orderId}
Authorization: Bearer <token>
```

#### Create Order
```http
POST /order-service/api/orders
Authorization: Bearer <token>
Content-Type: application/json

{
  "items": [
    {
      "productId": long,
      "quantity": int,
      "price": decimal
    }
  ],
  "shippingAddressId": long,
  "paymentMethod": "string"
}
```

#### Update Order Status
```http
PUT /order-service/api/orders/{orderId}/status
Authorization: Bearer <token>
Content-Type: application/json

{
  "status": "PENDING|CONFIRMED|SHIPPED|DELIVERED|CANCELLED"
}
```

#### Cancel Order
```http
DELETE /order-service/api/orders/{orderId}
Authorization: Bearer <token>
```

## Payment Service API (Port: 9053)

### Payment Management

#### Process Payment
```http
POST /payment-service/api/payments
Authorization: Bearer <token>
Content-Type: application/json

{
  "orderId": long,
  "amount": decimal,
  "paymentMethod": "CREDIT_CARD|DEBIT_CARD|PAYPAL|BANK_TRANSFER",
  "paymentDetails": {
    "cardNumber": "string",
    "expiryMonth": int,
    "expiryYear": int,
    "cvv": "string",
    "cardHolderName": "string"
  }
}
```

#### Get Payment Status
```http
GET /payment-service/api/payments/{paymentId}
Authorization: Bearer <token>
```

#### Refund Payment
```http
POST /payment-service/api/payments/{paymentId}/refund
Authorization: Bearer <token>
Content-Type: application/json

{
  "amount": decimal,
  "reason": "string"
}
```

## Shipping Service API (Port: 9054)

### Shipping Management

#### Get Shipping Options
```http
GET /shipping-service/api/shipping/options
Query Parameters:
- weight: decimal
- dimensions: string
- destination: string
```

#### Create Shipment
```http
POST /shipping-service/api/shipments
Authorization: Bearer <token>
Content-Type: application/json

{
  "orderId": long,
  "shippingMethod": "STANDARD|EXPRESS|OVERNIGHT",
  "trackingNumber": "string",
  "carrier": "string"
}
```

#### Track Shipment
```http
GET /shipping-service/api/shipments/{trackingNumber}/track
```

## Favourite Service API (Port: 9055)

### Favourite Management

#### Get User Favourites
```http
GET /favourite-service/api/favourites
Authorization: Bearer <token>
```

#### Add to Favourites
```http
POST /favourite-service/api/favourites
Authorization: Bearer <token>
Content-Type: application/json

{
  "productId": long
}
```

#### Remove from Favourites
```http
DELETE /favourite-service/api/favourites/{productId}
Authorization: Bearer <token>
```

## Error Responses

All APIs return consistent error responses:

```json
{
  "timestamp": "2024-01-01T12:00:00Z",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed",
  "path": "/api/users",
  "details": [
    {
      "field": "email",
      "message": "Email is required"
    }
  ]
}
```

## Status Codes

- `200 OK`: Successful GET, PUT requests
- `201 Created`: Successful POST requests
- `204 No Content`: Successful DELETE requests
- `400 Bad Request`: Invalid request data
- `401 Unauthorized`: Missing or invalid authentication
- `403 Forbidden`: Insufficient permissions
- `404 Not Found`: Resource not found
- `409 Conflict`: Resource already exists
- `500 Internal Server Error`: Server error

## Rate Limiting

API endpoints are rate-limited:
- Authenticated users: 1000 requests/hour
- Anonymous users: 100 requests/hour
- Rate limit headers included in responses:
  - `X-RateLimit-Limit`
  - `X-RateLimit-Remaining`
  - `X-RateLimit-Reset`