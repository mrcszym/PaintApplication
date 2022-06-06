package com.mrx.paintapplication

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Context
import android.graphics.*
import android.media.MediaScannerConnection
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.mrx.paintapplication.PaintView.Companion.colorList
import com.mrx.paintapplication.PaintView.Companion.currentBrushColor
import com.mrx.paintapplication.PaintView.Companion.pathList
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


class MainActivity : AppCompatActivity() {

    companion object { // like public static
        var path = Path()
        var paintBrush = Paint()
    }

    @SuppressLint("ShowToast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()

        var pd = ProgressDialog(this)

        val violetBtn = findViewById<ImageButton>(R.id.violetColor)
        val pinkBtn = findViewById<ImageButton>(R.id.pinkColor)
        val navyBtn = findViewById<ImageButton>(R.id.navyColor)
        val blackBtn = findViewById<ImageButton>(R.id.blackColor)
        val sponge = findViewById<Button>(R.id.sponge) // eraser
        val save = findViewById<Button>(R.id.save)

        violetBtn.setOnClickListener {
            paintBrush.color = Color.parseColor("#7c1091")
            currentColor(paintBrush.color)
        }
        pinkBtn.setOnClickListener {
            paintBrush.color = Color.parseColor("#d40f8b")
            currentColor(paintBrush.color)
        }
        navyBtn.setOnClickListener {
            paintBrush.color = Color.parseColor("#1b2e96")
            currentColor(paintBrush.color)
        }
        blackBtn.setOnClickListener {
            paintBrush.color = Color.BLACK
            currentColor(paintBrush.color)
        }
        sponge.setOnClickListener {
            val erasedToast = Toast.makeText(this, "Wyczyszczono", Toast.LENGTH_LONG)
            erasedToast.setGravity(Gravity.CENTER, 0, 0)
            erasedToast.show()

            pathList.clear()
            colorList.clear()
            path.reset()
        }
        save.setOnClickListener {

            val savingLayout = findViewById<View>(R.id.rlid) as RelativeLayout
            val file = saveBitMap(this, savingLayout)
            if (file != null) {
                pd.cancel()
                val savedToast = Toast.makeText(this, "Arcydzieło zapisane", Toast.LENGTH_LONG)
                savedToast.setGravity(Gravity.CENTER, 0, 0)
                savedToast.show()
            } else {
                pd.cancel()
                Log.i("TAG", "Image could not be saved.")
                val savedToast = Toast.makeText(this, "Błąd zapisu", Toast.LENGTH_LONG)
                savedToast.setGravity(Gravity.CENTER, 0, 0)
                savedToast.show()
            }
        }
    }

    private fun currentColor(color: Int) {
        currentBrushColor = color
        path = Path() // without new()
    }

    fun getBitmapFromView(view: View): Bitmap {
        val returnedBitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(returnedBitmap)
        val bgDrawable = view.background
        if (bgDrawable != null) {
            bgDrawable.draw(canvas)
        } else {
            canvas.drawColor(Color.WHITE)
        }
        view.draw(canvas)
        return returnedBitmap
    }


    private fun saveBitMap(context: Context, drawView: View): File? {
        val pictureFileDir = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
            "Arcydziela"
        )
        if (!pictureFileDir.exists()) {
            val isDirectoryCreated: Boolean = pictureFileDir.mkdirs()
            if (!isDirectoryCreated) Log.i("TAG", "Can't create directory to save the image")
            return null
        }
        val filename: String =
            pictureFileDir.getPath() + File.separator + System.currentTimeMillis()
                .toString() + ".jpg"
        val pictureFile = File(filename)
        val bitmap: Bitmap = getBitmapFromView(drawView)
        try {
            pictureFile.createNewFile()
            val oStream = FileOutputStream(pictureFile)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, oStream)
            oStream.flush()
            oStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
            Log.i("TAG", "There was an issue saving the image.")
        }
        scanGallery(context, pictureFile.getAbsolutePath())
        return pictureFile
    }
    private fun scanGallery(cntx: Context, path: String) {
        try {
            MediaScannerConnection.scanFile(
                cntx, arrayOf(path), null
            ) { path, uri -> }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.i("TAG", "There was an issue scanning gallery.")
        }
    }
}