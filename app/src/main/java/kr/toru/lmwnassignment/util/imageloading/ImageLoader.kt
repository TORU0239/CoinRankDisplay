package kr.toru.lmwnassignment.util.imageloading

import android.content.Context
import coil.ImageLoader
import coil.decode.SvgDecoder

fun getImageLoader(
    context: Context,
    isSVGSupported: Boolean = true
): ImageLoader {
    val imageLoader = ImageLoader.Builder(context)
    if (isSVGSupported) {
        imageLoader.components {
            add(SvgDecoder.Factory())
        }
    }
    return imageLoader.build()
}
