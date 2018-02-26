package com.simplemobiletools.draw.dialogs

import android.support.v7.app.AlertDialog
import android.view.WindowManager
import com.simplemobiletools.commons.dialogs.FilePickerDialog
import com.simplemobiletools.commons.extensions.*
import com.simplemobiletools.commons.models.FileDirItem
import com.simplemobiletools.draw.R
import com.simplemobiletools.draw.activities.SimpleActivity
import com.simplemobiletools.draw.helpers.JPG
import com.simplemobiletools.draw.helpers.PNG
import com.simplemobiletools.draw.helpers.SVG
import com.simplemobiletools.draw.models.Svg
import com.simplemobiletools.draw.views.MyCanvas
import kotlinx.android.synthetic.main.dialog_save_image.view.*
import java.io.File
import java.io.OutputStream

class SaveImageDialog(val activity: SimpleActivity, val defaultExtension: String, val defaultPath: String, val defaultFilename: String,
                      val canvas: MyCanvas, callback: (path: String, extension: String) -> Unit) {
    private val SIMPLE_DRAW = "Simple Draw"

    init {
        val initialFilename = getInitialFilename()
        var folder = if (defaultPath.isEmpty()) "${activity.internalStoragePath}/$SIMPLE_DRAW" else defaultPath
        val view = activity.layoutInflater.inflate(R.layout.dialog_save_image, null).apply {
            save_image_filename.setText(initialFilename)
            save_image_radio_group.check(when (defaultExtension) {
                JPG -> R.id.save_image_radio_jpg
                SVG -> R.id.save_image_radio_svg
                else -> R.id.save_image_radio_png
            })

            save_image_path.text = activity.humanizePath(folder)
            save_image_path.setOnClickListener {
                FilePickerDialog(activity, folder, false, showFAB = true) {
                    save_image_path.text = activity.humanizePath(it)
                    folder = it
                }
            }
        }

        AlertDialog.Builder(activity)
                .setPositiveButton(R.string.ok, null)
                .setNegativeButton(R.string.cancel, null)
                .create().apply {
            window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
            activity.setupDialogStuff(view, this, R.string.save_as) {
                getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
                    val filename = view.save_image_filename.value
                    if (filename.isEmpty()) {
                        activity.toast(R.string.filename_cannot_be_empty)
                        return@setOnClickListener
                    }

                    val extension = when (view.save_image_radio_group.checkedRadioButtonId) {
                        R.id.save_image_radio_png -> PNG
                        R.id.save_image_radio_svg -> SVG
                        else -> JPG
                    }

                    val newFile = File(folder, "$filename.$extension")
                    if (!newFile.name.isAValidFilename()) {
                        activity.toast(R.string.filename_invalid_characters)
                        return@setOnClickListener
                    }

                    if (saveFile(newFile)) {
                        callback(newFile.absolutePath, extension)
                        dismiss()
                    } else {
                        activity.toast(R.string.unknown_error_occurred)
                    }
                }
            }
        }
    }

    private fun saveFile(file: File): Boolean {
        if (!file.parentFile.exists()) {
            if (!file.parentFile.mkdir()) {
                return false
            }
        }

        when (file.extension) {
            SVG -> Svg.saveSvg(activity, file, canvas)
            else -> saveImageFile(file.absolutePath)
        }
        activity.scanFile(file) {}
        return true
    }

    private fun saveImageFile(path: String) {
        val fileDirItem = FileDirItem(path, path.getFilenameFromPath())
        activity.getFileOutputStream(fileDirItem, true) {
            writeToOutputStream(path, it!!)
            activity.toast(R.string.file_saved)
        }
    }

    private fun writeToOutputStream(path: String, out: OutputStream) {
        out.use {
            canvas.getBitmap().compress(path.getCompressionFormat(), 70, out)
        }
    }

    private fun getInitialFilename(): String {
        val newFilename = "image_${activity.getCurrentFormattedDateTime()}"
        return if (defaultFilename.isEmpty()) newFilename else defaultFilename
    }
}
