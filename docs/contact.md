# Contact API Spec

## Create Contact

Endpoint : POST /api/contacts

Request Header :

- X-API-TOKEN : Token (MANDATORY)

Request Body :

```json
{
  "firstName": "Naka",
  "lastName": "Eldiaro",
  "email": "naka@example.com",
  "phone": "082146432455"
}
```

Response Body (Success) :

```json
{
  "data": {
    "id": "123",
    "firstName": "Naka",
    "lastName": "Eldiaro",
    "email": "naka@example.com",
    "phone": "082146432455"
  }
}
```

Response Body (Failed) :

```json
{
  "errors": "Email format invalid, etc" 
}
```

## Update Contact

Endpoint : PUT /api/contacts/{id_contact}

Request Header :

- X-API-TOKEN : Token (MANDATORY)

Request Body :

```json
{
  "firstName": "Naka",
  "lastName": "Eldiaro",
  "email": "naka@example.com",
  "phone": "082146432455"
}
```

Response Body (Success) :

```json
{
  "data": {
    "id": "123",
    "firstName": "Naka",
    "lastName": "Eldiaro",
    "email": "naka@example.com",
    "phone": "082146432455"
  }
}
```

Response Body (Failed) :

```json
{
  "errors": "Email format invalid, etc" 
}
```

## Get Contact

Endpoint : GET /api/contacts/{id_contact}

Request Header :

- X-API-TOKEN : Token (MANDATORY)

Response Body (Success) :

```json
{
  "data": {
    "id": "123",
    "firstName": "Naka",
    "lastName": "Eldiaro",
    "email": "naka@example.com",
    "phone": "082146432455"
  }
}
```

Response Body (Failed) :

```json
{
  "errors": "Contact not found, etc" 
}
```

## Search Contact

Endpoint : GET /api/contacts

Query Param :

- name : String, contact first name or last name, using like query, optional
- phone : String, contact phone, using like query, optional
- email : String, contact email, using like query, optional
- page : Integer, start from 0, optional, default 0
- size : Integer, start from 10, optional, default 10

Request Header :

- X-API-TOKEN : Token (MANDATORY)

Response Body (Success) :

```json
{
  "data": [
    {
      "id": "123",
      "firstName": "Naka",
      "lastName": "Eldiaro",
      "email": "naka@example.com",
      "phone": "082146432455"
    }
  ],
  "paging": {
    "currentPage": 0,
    "totalPage": 10,
    "size": 10
  }
}
```

Response Body (Failed) :

```json
{
  "errors": "Unauthorized, etc"
}
```

## Remove Contact

Endpoint : DELETE /api/contacts/{id_contact}

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
  "errors": "Contact not found, etc" 
}
```
