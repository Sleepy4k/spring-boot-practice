# Auth API Spec

## Register User

Endpoint : POST /api/auth/register

Request Body :

```json
{
  "name": "Naka Eldiaro",
  "username": "sleepy4k",
  "password": "password"
}
```

Response Body (Success) :

```json
{
  "data": "OK"
}
```

Response Body (Failed) :

```json
{
  "errors": "Username must not blank, etc"
}
```

## Login User

Endpoint : POST /api/auth/login

Request Body :

```json
{
  "username": "sleepy4k",
  "password": "password"
}
```

Response Body (Success) :

```json
{
  "data": {
    "token": "XXXXXXXXX",
    "expiredAt": 123453 // miliseconds
  }
}
```

Response Body (Failed) :

```json
{
  "errors": "Username or password is invalid, etc"
}
```

## Logout User

Endpoint : DELETE /api/auth/logout

Request Header :

- X-API-TOKEN : Token (MANDATORY)

Response Body (Success) :

```json
{
  "data": "OK"
}
```

Response Body (Failed) :

```json
{
  "errors": "Unauthorized, etc"
}
```
