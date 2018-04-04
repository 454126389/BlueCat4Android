package com.tongge.ui.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.PixelFormat
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import java.security.MessageDigest

/**
 * Created by stai on 2017/5/28.
 *
 */
class GlideCircleTransformation(private val context: Context) : BitmapTransformation() {
    private val ID: String by lazy {
        this.javaClass.simpleName
    }

    override fun equals(other: Any?): Boolean {
        return other is GlideCircleTransformation
    }

    override fun hashCode(): Int {
        return ID.hashCode()
    }

    override fun updateDiskCacheKey(messageDigest: MessageDigest?) {
        messageDigest?.update(ID.toByteArray(Charsets.UTF_8))
    }

    override fun transform(pool: BitmapPool, toTransform: Bitmap, outWidth: Int, outHeight: Int): Bitmap {
        val roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(context.resources, toTransform)
        roundedBitmapDrawable.isCircular = true

        // 取 drawable 的长宽
        val w = roundedBitmapDrawable.intrinsicWidth;
        val h = roundedBitmapDrawable.intrinsicHeight;
        val wh = Math.min(w,h)
        // 取 drawable 的颜色格式
        val config = if (roundedBitmapDrawable.opacity != PixelFormat.OPAQUE) Bitmap.Config.ARGB_8888
                    else Bitmap.Config.RGB_565
        // Bitmap.Config.ARGB_8888
        // 建立对应 bitmap
        val bitmap = Bitmap.createBitmap(wh, wh, config);
        // 建立对应 bitmap 的画布
        val canvas = Canvas(bitmap);
        roundedBitmapDrawable.setBounds(0, 0, wh, wh);
        // 把 drawable 内容画到画布中
        roundedBitmapDrawable.draw(canvas);
        return bitmap
    }
}