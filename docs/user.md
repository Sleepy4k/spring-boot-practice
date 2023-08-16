# User API Spec

## Get User

Endpoint : GET /api/users/me

Request Header :

- X-API-TOKEN : Token (MANDATORY)

Response Body (Success) :

```json
{
  "data": {
    "name": "Naka Eldiaro",
    "username": "sleepy4k"
  }
}
```

Response Body (Failed) :

```json
{
  "errors": "Unauthorized, etc"
}
```

## Update User

Endpoint : PATCH /api/users/me

Request Header :

- X-API-TOKEN : Token (MANDATORY)

Request Body :

```json
{
  "name": "Naka Eldiaro", // add this data if you want to update name
  "password": "new password" // add this data if you want to update password
}
```

Response Body (Success) :

```json
{
  "data": {
    "name": "Naka Eldiaro",
    "username": "sleepy4k"
  }
}
```

Response Body (Failed) :

```json
{
  "errors": "Unauthorized, etc"
}
```
