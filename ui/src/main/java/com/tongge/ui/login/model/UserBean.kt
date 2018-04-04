package com.tongge.ui.login.model

import android.databinding.ObservableField
import com.tongge.manager.user.IUser

/**
 * Created by DZ on 2017/5/24.
 *
 */
class UserBean : IUser {

    var username: ObservableField<String> = ObservableField()
    var password: ObservableField<String> = ObservableField()
    var code: ObservableField<String> = ObservableField()
    var phoneArea: ObservableField<String> = ObservableField("+86")

    override fun getCUsername(): String {
        return username.get()
    }

    override fun getCPassword(): String {
        return password.get()
    }

    override fun getCCode(): String {
        return code.get()
    }

    override fun getCPhoneArea(): String {
        return phoneArea.get()
    }

    override fun clean() {
    }

}