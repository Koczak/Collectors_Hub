# Collectors Hub

## Initiate

./gradlew clean build

docker-compose up --build

## API Reference

### Authentication

#### Login

```http
POST /api/auth/login
```

| Parameter  | Type     | Description                 |
| ---------- | -------- | --------------------------- |
| `username` | `string` | **Required**. Your username |
| `password` | `string` | **Required**. Your password |

Authenticates the user and sets a JWT token as an HTTP-only cookie.

**Response:**

```json
{
	"message": "Logowanie udane"
}
```

**Status:** `200 OK`

**Cookie:** `jwt` (HTTP-only) containing the JWT token

#### Logout

```http
POST /api/auth/logout
```

Logs out the user by clearing the JWT cookie.

**Response:**

```json
{
	"message": "Wylogowano pomyślnie"
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

| Path Variable | Type   | Description           |
| ------------- | ------ | --------------------- |
| `id`          | `Long` | **Required**. User ID |

Deletes a user account.

**Status:** `204 No Content`

---

#### Edit User

```http
PUT /api/users/{id}
```

**JWT Token Authentication is Required**

**Must be logged in as an Admin**

| Path Variable | Type   | Description           |
| ------------- | ------ | --------------------- |
| `id`          | `Long` | **Required**. User ID |

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
| ------------- | ------ | --------------------- |
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
| ------------- | ------ | --------------------- |
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

| Path Variable | Type   | Description               |
| ------------- | ------ | ------------------------- |
| `id`          | `Long` | **Required**. Category ID |

Deletes a category by its ID.

**Status:** `204 No Content`

---

#### Edit Category

```http
PUT /api/categories/{id}
```

**JWT Token Authentication is Required**

| Path Variable | Type   | Description               |
| ------------- | ------ | ------------------------- |
| `id`          | `Long` | **Required**. Category ID |

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

| Parameter | Type     | Description                           |
| --------- | -------- | ------------------------------------- |
| `to`      | `string` | **Required**. Recipient email address |
| `subject` | `string` | **Required**. Email subject           |
| `text`    | `string` | **Required**. Email content           |

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

#### Get Pending Friend Requests

```http
GET /api/friends/pending
```

**JWT Token Authentication is Required**

Returns a list of pending friend requests that have been sent to the current user.

**Response:**

```json
[
	{
		"id": 1,
		"senderUsername": "exampleUser",
		"senderEmail": "user@example.com"
	},
	{
		"id": 2,
		"senderUsername": "anotherUser",
		"senderEmail": "another@example.com"
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
| ------------- | -------- | ------------------------------- |
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

| Path Variable  | Type   | Description                 |
| -------------- | ------ | --------------------------- |
| `invitationId` | `Long` | **Required**. Invitation ID |

Confirms a pending friend request.

**Status:** `202 Accepted`

---

#### Reject Friend Request

```http
POST /api/friends/reject/{invitationId}
```

**JWT Token Authentication is Required**

| Path Variable  | Type   | Description                 |
| -------------- | ------ | --------------------------- |
| `invitationId` | `Long` | **Required**. Invitation ID |

Rejects a pending friend request.

**Status:** `204 No Content`

---

#### Remove Friend

```http
DELETE /api/friends/{username}
```

**JWT Token Authentication is Required**

| Path Variable | Type     | Description                     |
| ------------- | -------- | ------------------------------- |
| `username`    | `String` | **Required**. Friend's username |

Removes a friend from your friend list.

**Status:** `204 No Content`

---

#### Get All Collections of a Friend

```http
GET /api/friends/collections/{username}
```

**JWT Token Authentication is Required**

| Path Variable | Type     | Description                     |
| ------------- | -------- | ------------------------------- |
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

### Comments

#### Get All Comments for an Item

```http
GET /api/comments/item/{itemId}
```

**JWT Token Authentication is Required**

| Path Variable | Type   | Description           |
| ------------- | ------ | --------------------- |
| `itemId`      | `Long` | **Required**. Item ID |

Returns a list of all comments for the specified item.

**Response:**

```json
[
	{
		"id": 1,
		"content": "This is a great item!",
		"createdAt": "2023-05-20T15:32:45",
		"username": "exampleUser",
		"userId": 2
	},
	{
		"id": 2,
		"content": "I love this collection piece!",
		"createdAt": "2023-05-21T10:15:22",
		"username": "anotherUser",
		"userId": 3
	}
]
```

**Status:** `200 OK`

---

#### Get All Comments for a Collection

```http
GET /api/comments/collection/{collectionId}
```

**JWT Token Authentication is Required**

| Path Variable  | Type   | Description                 |
| -------------- | ------ | --------------------------- |
| `collectionId` | `Long` | **Required**. Collection ID |

Returns a list of all comments for all items in the specified collection.

**Response:**

```json
[
	{
		"id": 1,
		"content": "This is a great item!",
		"createdAt": "2023-05-20T15:32:45",
		"username": "exampleUser",
		"userId": 2
	},
	{
		"id": 2,
		"content": "I love this collection piece!",
		"createdAt": "2023-05-21T10:15:22",
		"username": "anotherUser",
		"userId": 3
	}
]
```

**Status:** `200 OK`

---

#### Add a Comment

```http
POST /api/comments
```

**JWT Token Authentication is Required**

**Request Body:**

```json
{
	"content": "This is a great item!",
	"itemId": 1
}
```

Adds a new comment to the specified item.

**Response:**

```json
1
```

**Status:** `201 Created`

---

#### Delete a Comment

```http
DELETE /api/comments/{commentId}
```

**JWT Token Authentication is Required**

| Path Variable | Type   | Description              |
| ------------- | ------ | ------------------------ |
| `commentId`   | `Long` | **Required**. Comment ID |

Deletes the specified comment. Only the comment author can delete their comments.

**Status:** `204 No Content`
