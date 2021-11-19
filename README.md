# JpetStore-usuarios

# Endpoints

## Comprobar si un usuario esta autenticado

HU14: Está autenticado

### URL

**POST** /api/users/auth

### Request body

```json
{
  "jwt": "dfasdgvlashngf;vlkasdjflkjasl'df"
}
```

### Response body

```json
{
  "message": "true"
}
```

## Registrar usuario

HU15: Crear cuenta, registro en el sistema

### URL

**POST** /api/users

### Request body

```json
{
  "password": "123456",
  "email": "test@test.com",
  "phone": "123456789",
  "address": "Calle falsa 123",
  "fullName": "Test"
}
```

### Response body

```json
{
  "message": "usuario creado correctamente"
}
```

## Obtener usuario por id

HU16: Obtener cuenta

### URL

**GET** /api/users/${id}

### Request body

sin request body

### Response body

```json
{
  "id": 1,
  "password": "123456",
  "email": "test@test.com",
  "phone": "123456789",
  "address": "Calle falsa 123",
  "fullName": "Test"
}
```

## Cerrar sesión

HU17: Cerrar sesión

### URL

**POST** /api/users/logout

### Request body

```json
{
  "jwt": "dfasdgvlashngf;vlkasdjflkjasl'df"
}
```

### Response body

```json
{
  "message": "sesion cerrada correctamente"
}
```

## Actualizar usuario

HU18: Actualizar cuenta

### URL

**PUT** /api/users/${id}

### Request body

```json
{
  "password": "123456",
  "correo": "test@test.com",
  "telefono": "123456789",
  "direccion": "Calle falsa 123",
  "nombres": "Test"
}
```

### Response body

```json
{
  "message": "usuario actualizado correctamente"
}
```

## Autenticarse en el sistema

HU21: Autenticarse en el sistema

### URL

**POST** /api/users/login

### Request body

```json
{
  "correo": "test@test.com",
  "password": "123456"
}
```

### Response body

```json
{
  "jwt": "dfasdgvlashngf;vlkasdjflkjasl'df"
}
```