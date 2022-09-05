package com.example.cashcount.features.auth.data.mappers

import com.example.cashcount.datastore.user.models.UserDataStoreModel
import com.google.firebase.auth.FirebaseUser

fun FirebaseUser.toUserDataStoreModels(): UserDataStoreModel {
    return UserDataStoreModel(
        userName = this.phoneNumber ?: "",
        userPhoneNumber = this.phoneNumber ?: "",
        userId = this.uid
    )
}