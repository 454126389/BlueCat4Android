package com.tongge.manager.user

/**
 * Created by DZ on 2017/5/24.
 *
 */
interface IUser {
    fun getCUsername(): String
    fun getCPassword(): String
    fun getCCode(): String
    fun getCPhoneArea(): String
    fun clean()
}