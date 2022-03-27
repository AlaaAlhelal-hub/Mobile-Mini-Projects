package com.example.manageincidents.presentaion.utils

import android.content.Context
import androidx.annotation.ArrayRes
import androidx.annotation.PluralsRes
import androidx.annotation.StringRes
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class StringProvider @Inject constructor (@ApplicationContext val context: Context){

    fun provide(@StringRes resourceID: Int): String =
        context.getString(resourceID)

    fun provide(@StringRes resourceID: Int, arg: String): String =
        context.getString(resourceID, arg)

    fun provideArray(@ArrayRes resourceID: Int): Array<String> =
        context.resources.getStringArray(resourceID)

    fun providePluralsWithIntQuantity(@PluralsRes resourceID: Int, quantity: Int): String =
        context.resources.getQuantityString(resourceID, quantity,quantity)

    fun providePluralsWithIntQuantity(@PluralsRes resourceID: Int, quantity: Int, formatArgs: Any): String =
        context.resources.getQuantityString(resourceID, quantity, formatArgs)

}