package com.app.qrcodeapplication.extensions

import android.util.Size

val Size.isEmpty: Boolean
    get() = this.width <= 0 || this.height <= 0
