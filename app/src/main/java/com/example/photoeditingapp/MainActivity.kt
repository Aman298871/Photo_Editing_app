package com.example.photoeditingapp

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Paint
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Sets Compose UI
        setContent {
            PhotoEditingApp()
        }
    }
}

@Composable
fun PhotoEditingApp() {

    // Stores selected image URI
    var imageUri by remember {
        mutableStateOf<Uri?>(null)
    }

    // Filter values
    var brightness by remember {
        mutableStateOf(0f)
    }

    var contrast by remember {
        mutableStateOf(1f)
    }

    var saturation by remember {
        mutableStateOf(1f)
    }

    // Which filter is currently selected
    var activeFilter by remember {
        mutableStateOf<String?>(null)
    }

    val context = LocalContext.current

    // Opens gallery
    val launcher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent()
        ) { uri ->

            imageUri = uri
        }

    // Convert Uri to Bitmap
    val bitmap = remember(imageUri) {

        imageUri?.let {
            loadBitmapFromUri(
                it,
                context
            )
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),

        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // Show image if selected
        if (bitmap == null) {

            Image(
                painter = painterResource(
                    id = R.drawable.outline_animated_images_24
                ),
                contentDescription = "Placeholder",

                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
            )

        } else {

            // Apply filters
            val editedBitmap =
                remember(
                    bitmap,
                    brightness,
                    contrast,
                    saturation
                ) {

                    applyFilter(
                        bitmap,
                        brightness,
                        contrast,
                        saturation
                    )
                }

            Image(
                bitmap = editedBitmap.asImageBitmap(),
                contentDescription = null,

                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
            )
        }

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        // Pick image button
        Button(
            onClick = {
                launcher.launch("image/*")
            }
        ) {
            Text("Select Image")
        }

        if (bitmap != null) {

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            // Filter buttons
            Row(
                horizontalArrangement =
                    Arrangement.spacedBy(8.dp)
            ) {

                Button(
                    onClick = {
                        activeFilter = "Brightness"
                    }
                ) {
                    Text("Brightness")
                }

                Button(
                    onClick = {
                        activeFilter = "Contrast"
                    }
                ) {
                    Text("Contrast")
                }

                Button(
                    onClick = {
                        activeFilter = "Saturation"
                    }
                ) {
                    Text("Saturation")
                }
            }

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            // Show selected filter slider
            activeFilter?.let { filter ->

                FilterControl(
                    name = filter,

                    value = when (filter) {

                        "Brightness" ->
                            brightness

                        "Contrast" ->
                            contrast

                        "Saturation" ->
                            saturation

                        else -> 0f
                    },

                    min = if (
                        filter == "Contrast" ||
                        filter == "Saturation"
                    ) {
                        0f
                    } else {
                        -100f
                    },

                    max = if (
                        filter == "Contrast" ||
                        filter == "Saturation"
                    ) {
                        2f
                    } else {
                        100f
                    }

                ) { value ->

                    when (filter) {

                        "Brightness" ->
                            brightness = value

                        "Contrast" ->
                            contrast = value

                        "Saturation" ->
                            saturation = value
                    }
                }
            }

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            // Save image
            Button(
                onClick = {

                    bitmap?.let {

                        val editedBitmap =
                            applyFilter(
                                it,
                                brightness,
                                contrast,
                                saturation
                            )

                        saveImageToGallery(
                            context,
                            editedBitmap
                        )
                    }
                }
            ) {
                Text("Save To Gallery")
            }
        }
    }
}

@Composable
fun FilterControl(
    name: String,
    value: Float,
    min: Float,
    max: Float,
    onValueChange: (Float) -> Unit
) {

    Column(
        horizontalAlignment =
            Alignment.CenterHorizontally
    ) {

        Text(
            text = "$name : ${
                "%.2f".format(value)
            }",
            fontSize = 18.sp
        )

        Slider(
            value = value,

            onValueChange = onValueChange,

            valueRange = min..max,

            modifier =
                Modifier.fillMaxWidth()
        )
    }
}

// Loads image from gallery Uri
fun loadBitmapFromUri(
    uri: Uri,
    context: Context
): Bitmap {

    val inputStream =
        context.contentResolver
            .openInputStream(uri)

    return BitmapFactory.decodeStream(
        inputStream
    )!!
}

// Applies brightness, contrast and saturation
fun applyFilter(
    bitmap: Bitmap,
    brightness: Float,
    contrast: Float,
    saturation: Float
): Bitmap {

    val resultBitmap =
        Bitmap.createBitmap(
            bitmap.width,
            bitmap.height,
            Bitmap.Config.ARGB_8888
        )

    val canvas =
        Canvas(resultBitmap)

    val paint = Paint()

    val colorMatrix =
        ColorMatrix()

    // Saturation
    colorMatrix.setSaturation(
        saturation
    )

    // Contrast
    val translate =
        (-0.5f * contrast + 0.5f) * 255f

    val contrastMatrix =
        ColorMatrix(
            floatArrayOf(
                contrast,0f,0f,0f,translate,
                0f,contrast,0f,0f,translate,
                0f,0f,contrast,0f,translate,
                0f,0f,0f,1f,0f
            )
        )

    colorMatrix.postConcat(
        contrastMatrix
    )

    // Brightness
    val brightnessMatrix =
        ColorMatrix(
            floatArrayOf(
                1f,0f,0f,0f,brightness,
                0f,1f,0f,0f,brightness,
                0f,0f,1f,0f,brightness,
                0f,0f,0f,1f,0f
            )
        )

    colorMatrix.postConcat(
        brightnessMatrix
    )

    paint.colorFilter =
        ColorMatrixColorFilter(
            colorMatrix
        )

    canvas.drawBitmap(
        bitmap,
        0f,
        0f,
        paint
    )

    return resultBitmap
}

// Save image in gallery
fun saveImageToGallery(
    context: Context,
    bitmap: Bitmap
) {

    val fileName =
        "Edited_${System.currentTimeMillis()}.jpg"

    val values =
        ContentValues().apply {

            put(
                MediaStore.Images.Media.DISPLAY_NAME,
                fileName
            )

            put(
                MediaStore.Images.Media.MIME_TYPE,
                "image/jpeg"
            )
        }

    val resolver =
        context.contentResolver

    val uri =
        resolver.insert(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            values
        )

    uri?.let {

        resolver.openOutputStream(it)?.use { stream ->

            bitmap.compress(
                Bitmap.CompressFormat.JPEG,
                100,
                stream
            )
        }

        Toast.makeText(
            context,
            "Image Saved Successfully",
            Toast.LENGTH_SHORT
        ).show()
    }
}