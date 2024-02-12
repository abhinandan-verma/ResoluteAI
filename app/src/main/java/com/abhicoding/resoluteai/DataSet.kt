package com.abhicoding.resoluteai

import com.abhicoding.resoluteai.chat.Message
import com.abhicoding.resoluteai.chat.screens.groups.FireGroup
import com.abhicoding.resoluteai.signup.User

sealed class DataState {

    class MessageSuccess(val data: MutableList<Message>): DataState()
    class UserSuccess(val data: MutableList<User>): DataState()
    class Success(val data: MutableList<FireGroup>): DataState()
    class Failure(val message: String): DataState()
    data object  Loading: DataState()
    data object Empty : DataState()
}