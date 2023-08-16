# Address API Spec

## Create Address

Endpoint : POST /api/contacts/{id_contact}/addresses

Request Header :

- X-API-TOKEN : Token (MANDATORY)

Request Body :

```json
{
  "street": "some street",
  "city": "some city",
  "province": "some province",
  "country": "some country",
  "postalCode": "some postal code"
}
```

Response Body (Success) :

```json
{
  "data": {
    "id": "12334",
    "street": "some street",
    "city": "some city",
    "province": "some province",
    "country": "some country",
    "postalCode": "some postal code"
  }
}
```

Response Body (Failed) :

```json
{
  "errors": "Contact not found, Address not found, etc"
}
```

## Update Address

Endpoint : PUT /api/contacts/{id_contact}/addresses/{id_address}

Request Header :

- X-API-TOKEN : Token (MANDATORY)

Request Body :

```json
{
  "street": "some street",
  "city": "some city",
  "province": "some province",
  "country": "some country",
  "postalCode": "some postal code"
}
```

Response Body (Success) :

```json
{
  "data": {
    "id": "12334",
    "street": "some street",
    "city": "some city",
    "province": "some province",
    "country": "some country",
    "postalCode": "some postal code"
  }
}
```

Response Body (Failed) :

```json
{
  "errors": "Contact not found, Address not found, etc"
}
```

## Get Address

Endpoint : GET /api/contacts/{id_contact}/addresses/{id_address}

Request Header :

- X-API-TOKEN : Token (MANDATORY)

```json
{
  "data": {
    "id": "12334",
    "street": "some street",
    "city": "some city",
    "province": "some province",
    "country": "some country",
    "postalCode": "some postal code"
  }
}
```

Response Body (Failed) :

```json
{
  "errors": "Contact not found, Address not found, etc"
}
```

## Remove Address

Endpoint : DELETE /api/contacts/{id_contact}/addresses/{id_address}

Request Header :

- X-API-TOKEN : Token (MANDATORY)

```json
{
  "data": "OK"
}
```

Response Body (Failed) :

```json
{
  "errors": "Contact not found, Address not found, etc"
}
```

## List Address

Endpoint : GET /api/contacts/{id_contact}/addresses

Request Header :

- X-API-TOKEN : Token (MANDATORY)

Response Body (Success) :

```json
{
  "data": [
    {
      "id": "12334",
      "street": "some street",
      "city": "some city",
      "province": "some province",
      "country": "some country",
      "postalCode": "some postal code"
    }
  ]
}
```

Response Body (Failed) :

```json
{
  "errors": "Contact not found, Address not found, etc"
}
```
