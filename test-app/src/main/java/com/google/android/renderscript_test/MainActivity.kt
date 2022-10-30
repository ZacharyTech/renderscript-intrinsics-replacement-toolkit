/*
 * Copyright (C) 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.android.renderscript_test

import android.graphics.BitmapFactory
import android.graphics.RenderEffect
import android.graphics.Shader
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.android.renderscript.Range2d
import com.google.android.renderscript.Toolkit

private const val TAG = "MainActivity"

@ExperimentalUnsignedTypes
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val begin = System.currentTimeMillis()
        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.img)
        val radius = 25.0f

        val blur = Toolkit.blur(bitmap, radius.toInt().coerceIn(1, 25))
        findViewById<ImageView>(R.id.img).setImageBitmap(blur)
        val end = System.currentTimeMillis()
        Log.d(TAG, "onCreate: ${end - begin}")

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val effect = RenderEffect.createBlurEffect(
                radius,
                radius,
                Shader.TileMode.CLAMP
            )

            findViewById<ImageView>(R.id.img2).apply {
                setImageBitmap(bitmap)
                setRenderEffect(effect)
            }
        }


        // To debug resources not destroyed
        // "A resource failed to call destroy."
        try {
            Class.forName("dalvik.system.CloseGuard")
                .getMethod("setEnabled", Boolean::class.javaPrimitiveType)
                .invoke(null, true)
        } catch (e: ReflectiveOperationException) {
            throw RuntimeException(e)
        }
    }
}
