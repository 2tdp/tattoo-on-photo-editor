package com.tattoo.tattoomaker.on.myphoto.fragment

import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.tattoo.tattoomaker.on.myphoto.R
import com.tattoo.tattoomaker.on.myphoto.adapter.FontTextAdapter
import com.tattoo.tattoomaker.on.myphoto.addview.viewedit.ViewAddText
import com.tattoo.tattoomaker.on.myphoto.callback.ICallBackItem
import com.tattoo.tattoomaker.on.myphoto.data.DataFont
import com.tattoo.tattoomaker.on.myphoto.model.ColorModel
import com.tattoo.tattoomaker.on.myphoto.model.ShadowModel
import com.tattoo.tattoomaker.on.myphoto.model.text.FontModel
import com.tattoo.tattoomaker.on.myphoto.model.text.TextModel
import com.tattoo.tattoomaker.on.myphoto.utils.Utils

class AddTextFragment(textModel: TextModel?, callback: ICallBackItem): Fragment() {

    private lateinit var viewAddText: ViewAddText

    private var callback: ICallBackItem
    private var textModel: TextModel? = null

    companion object {
        fun newInstance(textModel: TextModel?, callback: ICallBackItem): AddTextFragment {
            val args = Bundle()

            val fragment = AddTextFragment(textModel, callback)
            fragment.arguments = args
            return fragment
        }
    }

    init {
        this.callback = callback
        this.textModel = textModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewAddText = ViewAddText(requireContext())

        if (textModel == null) {
            viewAddText.viewToolbar.tvTitle.text = resources.getString(R.string.text)
            textModel = TextModel().apply {
                content = resources.getString(R.string.tattoo)
                fontModel = FontModel("solway_medium.ttf", true)
                colorModel = ColorModel(
                    ContextCompat.getColor(requireContext(), R.color.black),
                    ContextCompat.getColor(requireContext(), R.color.black),0, true)
                shadowModel = ShadowModel(0F, 0F, 5F, ContextCompat.getColor(requireContext(), R.color.black))
            }
        } else {
            viewAddText.viewToolbar.tvTitle.text = resources.getString(R.string.edit_text)
            textModel?.let {
                viewAddText.vAddText.setText(it.content)
                viewAddText.vAddText.typeface =
                    Typeface.createFromAsset(requireContext().assets, "font_text/${it.fontModel!!.nameFont}")
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return viewAddText
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        evenClick()
    }

    private fun evenClick() {
        viewAddText.viewToolbar.ivBack.setOnClickListener {
            Utils.clearBackStack(parentFragmentManager)
            Utils.hideKeyboard(requireContext(), viewAddText)
        }
        viewAddText.viewToolbar.ivRight.setOnClickListener {
            textModel?.let {
                callback.callBack(it.apply {
                    content = if (viewAddText.vAddText.text.toString() != "")
                        viewAddText.vAddText.text.toString()
                    else resources.getString(R.string.tattoo) },
                    -1)
            }
            Utils.clearBackStack(parentFragmentManager)
            Utils.hideKeyboard(requireContext(), viewAddText)
        }
    }

    private fun initView() {
        val fontTextAdapter = FontTextAdapter(requireContext(), object : ICallBackItem {
            override fun callBack(ob: Any, position: Int) {
                Utils.hideKeyboard(requireContext(), viewAddText)
                val font = ob as FontModel
                textModel?.let { it.fontModel = font }
                viewAddText.vAddText.typeface =
                    Typeface.createFromAsset(requireContext().assets, "font_text/${font.nameFont}")
            }
        })

        fontTextAdapter.setData(DataFont.getDataFont(requireContext()))

        viewAddText.rcv.layoutManager = GridLayoutManager(requireContext(), 3)
        viewAddText.rcv.adapter = fontTextAdapter

        textModel?.let {
            fontTextAdapter.setCurrent(it.fontModel!!.nameFont)
            viewAddText.rcv.scrollToPosition(fontTextAdapter.getPosition(it.fontModel!!.nameFont))
        }
    }
}