package com.junseo.postboy.util.toast

import android.content.Context
import android.widget.Toast

object ToastUtil {

    fun showToast(context: Context, msg: String?) = Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()

}