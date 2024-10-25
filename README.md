# WellMate Kotlin Multiplatform API Client Library

## Overview

The WellMate API Client library is a Kotlin Multiplatform library designed to interact with the WellMate API. It
supports various endpoints for user authentication, entry management, and administrative tasks.

## Supported platforms

- Android
- iOS
- JVM

## Features

- User authentication (login via password, Google, Facebook)
- User management (create, update, delete, activate)
- Entry management (meals, timers)
- Admin functionalities

## Installation

Add the following dependencies to your `build.gradle.kts` file:

```kotlin
dependencies {
    implementation("io.wellmate:api.client:$version")
    // Add other necessary dependencies
}
```

## Usage examples

### Authentication

#### Login via password

```kotlin
suspend fun loginWithPassword(username: String, password: String): Token {
    val response = WellMateClient.Api.Login.Password.submitForm(username, password)
    if (response.isSuccessful) {
        return response.body()
    }
    throw Exception("Failed to login with password")
}
```

#### Login via Google

```kotlin
suspend fun loginWithGoogle(oAuthToken: OAuthToken): Token {
    val response = WellMateClient.Api.Login.Google.post(body = oAuthToken)
    if (response.isSuccessful) {
        return response.body()
    }
    throw Exception("Failed to login with password")
}
```

### User management

#### Create user

```kotlin
suspend fun createUser(user: EmailPassword): User {
    val response = WellMateClient.Api.User.post(body = user)
    if (response.isSuccessful) {
        return response.body()
    }
    throw Exception("Failed to register user")
}
```

#### Update user

```kotlin
suspend fun updateUser(userId: Int, userInfo: UserInfoFields, token: Token): UserInfo {
    val response = WellMateClient.Api.User.Info.UserId(userId = userId).post(body = userInfo) {
        append(
            HttpHeaders.Authorization,
            token.authorizationHeader,
        )
    }
    if (response.isSuccessful) {
        return response.body()
    }
    throw Exception("Failed to update user info")
}
```

#### Delete user

```kotlin
suspend fun deleteUser(userId: Int, token: Token) {
    val response = WellMateClient.Api.User.UserId(userId = userId).delete {
        append(
            HttpHeaders.Authorization,
            token.authorizationHeader,
        )
    }
    if (!response.isSuccessful) {
        throw Exception("Failed to delete user")
    }
}
```

### Entry management

#### Create meal

```kotlin
suspend fun createMeal(meal: MealFieldsClient, token: Token): Meal {
    val response = WellMateClient.Api.Entry.Meal.post(body = meal) {
        append(
            HttpHeaders.Authorization,
            token.authorizationHeader,
        )
    }
    if (response.isSuccessful) {
        return response.body()
    }
    throw Exception("Failed to create meal")
}
```

#### Delete meal

```kotlin
suspend fun deleteMeal(mealId: Int, token: Token) {
    val response = WellMateClient.Api.Entry.Meal.MealId(mealId = mealId).delete {
        append(
            HttpHeaders.Authorization,
            token.authorizationHeader,
        )
    }
    if (!response.isSuccessful) {
        throw Exception("Failed to delete meal")
    }
}
```

## License

This project is licensed under the Apache License, Version 2.0. See the `LICENSE` file for details.
