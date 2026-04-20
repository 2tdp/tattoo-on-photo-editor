package com.tattoo.tattoomaker.on.myphoto.activity.edit

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toDrawable
import androidx.recyclerview.widget.GridLayoutManager
import com.tattoo.tattoomaker.on.myphoto.R
import com.tattoo.tattoomaker.on.myphoto.adapter.FontTextAdapter
import com.tattoo.tattoomaker.on.myphoto.callback.ICallBackItem
import com.tattoo.tattoomaker.on.myphoto.data.DataFont
import com.tattoo.tattoomaker.on.myphoto.databinding.DialogAddTextBinding
import com.tattoo.tattoomaker.on.myphoto.extensions.setOnUnDoubleClickListener
import com.tattoo.tattoomaker.on.myphoto.model.ColorModel
import com.tattoo.tattoomaker.on.myphoto.model.ShadowModel
import com.tattoo.tattoomaker.on.myphoto.model.text.FontModel
import com.tattoo.tattoomaker.on.myphoto.model.text.TextModel
import com.tattoo.tattoomaker.on.myphoto.utils.Utils

class AddTextDialog(private val context: AppCompatActivity, private var textModel: TextModel?) : AlertDialog(context, R.style.SheetDialog) {

    private val binding: DialogAddTextBinding = DialogAddTextBinding.inflate(LayoutInflater.from(context))

    var callBack: ICallBackItem? = null

    init {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window?.setBackgroundDrawable(Color.BLACK.toDrawable())
        setView(binding.root)
        setCancelable(false)
        window?.setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val fontTextAdapter = FontTextAdapter(context).apply {
            object : ICallBackItem {
                override fun callBack(ob: Any, position: Int) {
                    Utils.hideKeyboard(context, binding.root)
                    if (ob is FontModel) {
                        textModel?.let { it.fontModel = ob }
                        binding.edt.typeface =
                            Typeface.createFromAsset(context.assets, "font_text/${ob.nameFont}")
                    }
                }
            }
        }

        fontTextAdapter.setData(DataFont.getDataFont(context))

        binding.rcv.apply {
            layoutManager = GridLayoutManager(context, 3)
            adapter = fontTextAdapter
        }

        textModel?.let {
            if (it.fontModel != null) {
                fontTextAdapter.setCurrent(it.fontModel!!.nameFont)
                binding.rcv.scrollToPosition(fontTextAdapter.getPosition(it.fontModel!!.nameFont))
            }
        }

        binding.ivExit.setOnUnDoubleClickListener { dismiss() }
        binding.ivDone.setOnUnDoubleClickListener {
            textModel?.let {
                it.content = if (binding.edt.text.toString() != "") binding.edt.text.toString()
                else context.resources.getString(R.string.tattoo)

                callBack?.callBack(it,-1)
            }
            dismiss()
        }

        setOnDismissListener { Utils.hideKeyboard(context, binding.root) }
    }

    fun showDialog() {
        show()

        window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT)

        window?.decorView?.systemUiVisibility = hideSystemBars()
        window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE)

        setTitle()
    }

    private fun setTitle() {
        textModel?.let { textModel ->
            binding.tvTitle.text = context.resources.getString(R.string.edit_text)
            binding.edt.setText(textModel.content)
            binding.edt.typeface =
                Typeface.createFromAsset(context.assets, "font_text/${textModel.fontModel?.nameFont}")
        } ?: run {
            binding.tvTitle.text = context.resources.getString(R.string.text)
            textModel = TextModel().apply {
                content = context.resources.getString(R.string.tattoo)
                fontModel = FontModel("solway_medium.ttf", true)
                colorModel = ColorModel()
                shadowModel = ShadowModel(0F, 0F, 5F, ContextCompat.getColor(context, R.color.black))
            }
        }
    }

    @Suppress("DEPRECATION")
    private fun hideSystemBars(): Int = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)

}