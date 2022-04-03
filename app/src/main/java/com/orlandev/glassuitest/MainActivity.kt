package com.orlandev.glassuitest

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
    val bitmap = BitmapFactory.decodeResource(
        LocalContext.current.resources,
        R.drawable.image_test  //Test changing  the image to webp
    )



    val rs = RenderScript.create(LocalContext.current)
    val bitmapAlloc = Allocation.createFromBitmap(rs, bitmap)
    ScriptIntrinsicBlur.create(rs, bitmapAlloc.element).apply {
        setRadius(BLUR_RADIUS)
        setInput(bitmapAlloc)
        forEach(bitmapAlloc)
    }
    bitmapAlloc.copyTo(bitmap)
    rs.destroy()

    Image(
        modifier = modifier,
        bitmap = bitmap.asImageBitmap(),
        contentDescription = null
    )
}

@Composable
fun AppScreen() {
    BackgroundBlurImage(modifier = Modifier.fillMaxSize())
}