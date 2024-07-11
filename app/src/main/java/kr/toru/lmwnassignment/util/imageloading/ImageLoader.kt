package kr.toru.lmwnassignment.util.imageloading

import android.content.Context
import coil.ImageLoader
import coil.decode.SvgDecoder

fun Context.getImageLoader() =
    ImageLoader.Builder(this)
        .components {
            add(SvgDecoder.Factory())
        }.build()