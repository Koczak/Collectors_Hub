Api Documentation:

Authentication:
POST Method /api/auth/login
Params: username (string), password (string)

Returns:

Http status: 200 OK

Body (example)

{

    "token": "qweqciOiJIUqeqwNiqeeyJzdWiJhZG1eqweqpblhdCI6MTc0NzAweqweqiZXhwIjoqweqeTAzMjQwfQ.x0qweq4QinGd7dNpHzAWl5kycRiycqweqw3HM"

}

User:

GET Method /api/users , no auth

Returns:

Http status: 200 OK

Body (example)

[
    {
        "username": "admin",
        "email": "admin@gmail.com"
    },
    {
        "username": "exampleUsers",
        "email": "example@example.com"
    }
]


