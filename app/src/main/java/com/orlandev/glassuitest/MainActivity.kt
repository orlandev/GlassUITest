package com.orlandev.glassuitest

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.renderscript.Allocation
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicBlur
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import com.orlandev.glassuitest.ui.theme.GlassUITestTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GlassUITestTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    AppScreen()
                }
            }
        }
    }
}

const val BLUR_RADIUS = 25f

@Composable
fun BackgroundBlurImage(modifier: Modifier = Modifier) {


    val bitmap = blurImage(
        BitmapFactory.decodeResource(
            LocalContext.current.resources,
            R.drawable.image_test  //Test changing  the image to webp
        ), LocalContext.current
    )

    Image(
        modifier = modifier.fillMaxSize(),
        bitmap = bitmap.asImageBitmap(),
        contentScale = ContentScale.Crop,
        contentDescription = null
    )
}

@Composable
fun AppScreen() {
    BackgroundBlurImage(modifier = Modifier.fillMaxSize())
}

fun blurImage(bitmap: Bitmap, context: Context): Bitmap {

    val rs = RenderScript.create(context)
    val bitmapAlloc = Allocation.createFromBitmap(rs, bitmap)
    ScriptIntrinsicBlur.create(rs, bitmapAlloc.element).apply {
        setRadius(BLUR_RADIUS)
        setInput(bitmapAlloc)
        forEach(bitmapAlloc)
    }
    bitmapAlloc.copyTo(bitmap)
    rs.destroy()

    return bitmap
}