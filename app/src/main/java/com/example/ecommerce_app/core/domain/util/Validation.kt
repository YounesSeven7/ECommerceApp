package com.example.ecommerce_app.core.domain.util

import android.content.Context
import android.util.Patterns
import com.example.ecommerce_app.R
import com.example.ecommerce_app.core.data.model.User
import com.example.ecommerce_app.feature_auth.domain.util.LoginFields
import com.example.ecommerce_app.feature_auth.domain.util.RegisterFields

fun Context.registerFieldsValidation(user: User, password: String): RegisterFields {
     return RegisterFields(
         firstName = firstNameValidation(user.firstName),
         lastName = lastNameValidation(user.lastName),
         email = emailValidation(user.email),
         password = passwordValidation(password)
    )
}

fun Context.loginFieldsValidation(email: String, password: String): LoginFields {
    return LoginFields(
        email = emailValidation(email),
        password = passwordValidation(password)
    )
}

fun Context.firstNameValidation(firstName: String): Field {
    return if (firstName.isEmpty())
        Field(message = getString(R.string.first_name_cant_be_empty))
    else
        Field(validation = true)
}

fun Context.lastNameValidation(lastName: String): Field {
    return if (lastName.isEmpty())
        Field(message = getString(R.string.last_name_cant_be_empty))
    else
        Field(validation = true)
}

fun Context.emailValidation(email: String): Field {
    return if (email.isEmpty())
        Field(message = getString(R.string.email_cant_be_empty))
    else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        Field(message = getString(R.string.valid_email))
    else
        Field(validation = true)
}

fun Context.passwordValidation(password: String): Field {
    return if (password.isEmpty())
        Field(message = getString(R.string.password_cant_be_empty))
    else if (password.length < 6)
        Field(message = getString(R.string.password_at_least_six))
    else
        Field(validation = true)
}


