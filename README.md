
# Collectors Hub



## API Reference

### Authentication

#### Login

```http
POST /api/auth/login
```

| Parameter   | Type     | Description                 |
|-------------|----------|-----------------------------|
| `username`  | `string` | **Required**. Your username |
| `password`  | `string` | **Required**. Your password |

Authenticates the user and returns a JWT token.

**Response:**

```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc0NzA5OTY0MCwiZXhwIjoxNzQ3MTAzMjQwfQ.x0yiUqb4QinGd7dNpHzAWl5kycRiycQDz8mhITIw3HM"
}
```

**Status:** `200 OK`

---

### Users

#### Get Users

```http
GET /api/users
```

Returns a list of user basic information such as username and email.

**Response:**

```json
[
  {
    "username": "admin",
    "email": "admin@gmail.com"
  },
  {
    "username": "exampleUser",
    "email": "example@example.com"
  }
]
```

**Status:** `200 OK`

---

#### Register New User

```http
POST /api/users
```

**Request Body:**

```json
{
  "username": "exampleUser",
  "password": "examplePassword",
  "email": "example@example.com",
  "roles": "USER"
}
```

Creates a new user account in the database.

**Status:** `201 Created`

---

#### Delete User

```http
DELETE /api/users/{id}
```

**JWT Token Authentication is Required**

**Must be logged in as an Admin**

| Path Variable | Type   | Description                |
|---------------|--------|----------------------------|
| `id`          | `Long` | **Required**. User ID      |

Deletes a user account.

**Status:** `204 No Content`

---

#### Edit User

```http
PUT /api/users/{id}
```

**JWT Token Authentication is Required**

**Must be logged in as an Admin**

| Path Variable | Type   | Description                |
|---------------|--------|----------------------------|
| `id`          | `Long` | **Required**. User ID      |

Edits a user account.

**Status:** `204 No Content`

### Collections

#### Get Collections for Current User

```http
GET /api/collections
```

**JWT Token Authentication is Required**

Returns a list of the current user's collections with basic information such as ID, name, and description.

**Response:**

```json
[
  {
    "id": 1,
    "name": "exampleCollection",
    "description": "example description"
  },
  {
    "id": 2,
    "name": "exampleCollection2",
    "description": "example description 2"
  }
]
```

**Status:** `200 OK`

---

#### Create New Collection

```http
POST /api/collections
```

**JWT Token Authentication is Required**

**Request Body:**

```json
{
  "name": "exampleCollection",
  "description": "example description"
}
```

Creates a new collection for the current user.

**Response:**

```json
{
  "id": 1
}
```

**Status:** `201 Created`

---

### Items

#### Get All Items for Current User

```http
GET /api/items
```

**JWT Token Authentication is Required**

Returns a list of all items for the current user with basic information such as ID, name, description, category name, and attributes.

**Response:**

```json
[
  {
    "id": 1,
    "name": "exampleItem",
    "description": "example description",
    "categoryName": "exampleCategory",
    "attributes": {
      "key1": "value1",
      "key2": "value2"
    }
  },
  {
    "id": 2,
    "name": "exampleItem2",
    "description": "example description 2",
    "categoryName": null,
    "attributes": null
  }
]
```

**Status:** `200 OK`

---

#### Add New Item

```http
POST /api/items
```

**JWT Token Authentication is Required**

**Request Body:**

```json
{
  "name": "exampleItem",
  "description": "example description",
  "categoryId": 1,
  "collectionId": 1,
  "attributes": {
    "key1": "value1",
    "key2": "value2"
  }
}
```

Creates a new item for the current user.

**Response:**

```json
{
  "id": 1
}
```

**Status:** `201 Created`

---

#### Delete Item

```http
DELETE /api/items/{id}
```

**JWT Token Authentication is Required**

| Path Variable | Type   | Description           |
|---------------|--------|-----------------------|
| `id`          | `Long` | **Required**. Item ID |

Deletes an item by its ID.

**Status:** `204 No Content`

---

#### Edit Item

```http
PUT /api/items/{id}
```

**JWT Token Authentication is Required**

| Path Variable | Type   | Description           |
|---------------|--------|-----------------------|
| `id`          | `Long` | **Required**. Item ID |

**Request Body:**

```json
{
  "name": "updatedItem",
  "description": "updated description",
  "categoryId": 2,
  "collectionId": 1,
  "attributes": {
    "key1": "newValue1",
    "key3": "value3"
  }
}
```

Edits an existing item by its ID.

**Status:** `204 No Content`

### Categories

#### Get All Categories for Current User

```http
GET /api/categories
```

**JWT Token Authentication is Required**

Returns a list of all categories for the current user with basic information such as ID, name, username, and attributes.

**Response:**

```json
[
  {
    "id": 1,
    "name": "exampleCategory",
    "username": "exampleUser",
    "attributes": ["attribute1", "attribute2"]
  },
  {
    "id": 2,
    "name": "exampleCategory2",
    "username": "exampleUser",
    "attributes": null
  }
]
```

**Status:** `200 OK`

---

#### Create New Category

```http
POST /api/categories
```

**JWT Token Authentication is Required**

**Request Body:**

```json
{
  "name": "exampleCategory",
  "attributes": ["attribute1", "attribute2"]
}
```

Creates a new category for the current user.

**Response:**

```json
{
  "id": 1
}
```

**Status:** `201 Created`

---

#### Delete Category

```http
DELETE /api/categories/{id}
```

**JWT Token Authentication is Required**

| Path Variable | Type   | Description                |
|---------------|--------|----------------------------|
| `id`          | `Long` | **Required**. Category ID  |

Deletes a category by its ID.

**Status:** `204 No Content`

---

#### Edit Category

```http
PUT /api/categories/{id}
```

**JWT Token Authentication is Required**

| Path Variable | Type   | Description                |
|---------------|--------|----------------------------|
| `id`          | `Long` | **Required**. Category ID  |

**Request Body:**

```json
{
  "name": "updatedCategory",
  "attributes": ["updatedAttribute1", "updatedAttribute2"]
}
```

Edits an existing category by its ID.

**Status:** `204 No Content`

### Email

#### Send Email

```http
POST /api/email/send
```

**JWT Token Authentication is Required**

Sends an email to the specified recipient with the given subject and text.

**Request Parameters:**

| Parameter | Type     | Description                  |
|-----------|----------|------------------------------|
| `to`      | `string` | **Required**. Recipient email address |
| `subject` | `string` | **Required**. Email subject  |
| `text`    | `string` | **Required**. Email content  |

**Response:**

```json
{
  "message": "Email sent successfully to example@example.com"
}
```

**Status:** `200 OK`

### Friends

#### Get All Friends for Current User

```http
GET /api/friends
```

**JWT Token Authentication is Required**

Returns a list of all friends for the current user with basic information such as username and email.

**Response:**

```json
[
  {
    "friendUsername": "exampleFriend",
    "friendEmail": "friend@example.com"
  },
  {
    "friendUsername": "exampleFriend2",
    "friendEmail": "friend2@example.com"
  }
]
```

**Status:** `200 OK`

---

#### Send Friend Request

```http
POST /api/friends/invite/{username}
```

**JWT Token Authentication is Required**

| Path Variable | Type     | Description                     |
|---------------|----------|---------------------------------|
| `username`    | `String` | **Required**. Friend's username |

Sends a friend request to the specified user.

**Response:**

```json
{
  "message": "Friend request sent successfully to exampleFriend"
}
```

**Status:** `201 Created`

---

#### Confirm Friend Request

```http
GET /api/friends/confirm/{invitationId}
```

| Path Variable    | Type   | Description                     |
|------------------|--------|---------------------------------|
| `invitationId`   | `Long` | **Required**. Invitation ID     |

Confirms a pending friend request.

**Status:** `202 Accepted`

---

#### Reject Friend Request

```http
POST /api/friends/reject/{invitationId}
```

**JWT Token Authentication is Required**

| Path Variable    | Type   | Description                     |
|------------------|--------|---------------------------------|
| `invitationId`   | `Long` | **Required**. Invitation ID     |

Rejects a pending friend request.

**Status:** `204 No Content`

---

#### Get All Collections of a Friend

```http
GET /api/friends/collections/{username}
```

**JWT Token Authentication is Required**

| Path Variable | Type     | Description                     |
|---------------|----------|---------------------------------|
| `username`    | `String` | **Required**. Friend's username |

Returns a list of all collections belonging to the specified friend.

**Response:**

```json
[
  {
    "id": 1,
    "name": "exampleCollection",
    "description": "example description"
  },
  {
    "id": 2,
    "name": "exampleCollection2",
    "description": "example description 2"
  }
]
```

**Status:** `200 OK`
