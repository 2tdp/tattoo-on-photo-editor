package com.tattoo.tattoomaker.on.myphoto.fragment

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.tattoo.tattoomaker.on.myphoto.R
import com.tattoo.tattoomaker.on.myphoto.adapter.BucketAdapter
import com.tattoo.tattoomaker.on.myphoto.adapter.ChooseBackgroundAdapter
import com.tattoo.tattoomaker.on.myphoto.addview.ViewPickPicture
import com.tattoo.tattoomaker.on.myphoto.callback.ICallBackCheck
import com.tattoo.tattoomaker.on.myphoto.callback.ICallBackItem
import com.tattoo.tattoomaker.on.myphoto.data.DataPic
import com.tattoo.tattoomaker.on.myphoto.model.picture.BucketPicModel
import com.tattoo.tattoomaker.on.myphoto.sharepref.DataLocalManager
import com.tattoo.tattoomaker.on.myphoto.utils.Constant

class PickPictureFragment(callBack: ICallBackItem, isFinish: ICallBackCheck) : Fragment() {

    lateinit var viewPickPicture: ViewPickPicture

    private lateinit var bucketAdapter: BucketAdapter
    lateinit var detailPictureAdapter: ChooseBackgroundAdapter
    private var callBack: ICallBackItem
    private var isFinish: ICallBackCheck

    lateinit var animation: Animation

    init {
        this.callBack = callBack
        this.isFinish = isFinish
    }

    companion object {
        fun newInstance(callBack: ICallBackItem, isFinish: ICallBackCheck): PickPictureFragment {
            val args = Bundle()

            val fragment = PickPictureFragment(callBack, isFinish)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewPickPicture = ViewPickPicture(requireContext())
        viewPickPicture.rcvPicture.visibility = View.GONE

        requireActivity().onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                try {
                    val lstFragment = parentFragmentManager.fragments
                    if (lstFragment.isNotEmpty()) {
                        if (viewPickPicture.rcvPicture.visibility == View.VISIBLE)
                            switchLayoutPicture(0)
                        else {
                            parentFragmentManager.popBackStack()
                            isFinish.check(true)
                        }
                    }
                } catch (_ : Exception) {
                }
            }
        })

        if (DataLocalManager.getListBucket(Constant.LIST_ALL_PIC).isEmpty()) {
            Thread {
                DataPic.getBucketPictureList(requireContext())
                handler.sendEmptyMessage(0)
            }.start()
        } else handler.sendEmptyMessage(0)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        initView()
        evenClick()
        return viewPickPicture
    }

    private fun initView() {
        bucketAdapter = BucketAdapter(requireContext(), object : ICallBackItem {
            override fun callBack(ob: Any, position: Int) {
                val bucketPicModel = ob as BucketPicModel

                switchLayoutPicture(1)

                detailPictureAdapter =
                    ChooseBackgroundAdapter(requireContext(), object : ICallBackItem {
                        override fun callBack(ob: Any, position: Int) {
                            callBack.callBack(ob, position)
                            parentFragmentManager.popBackStack()
                        }
                    })

                detailPictureAdapter.setData(bucketPicModel.lstPic)

                viewPickPicture.rcvPicture.layoutManager = GridLayoutManager(requireContext(), 3)
                viewPickPicture.rcvPicture.adapter = detailPictureAdapter
            }
        })

        val manager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        viewPickPicture.rcvBucket.layoutManager = manager
        viewPickPicture.rcvBucket.adapter = bucketAdapter
    }

    private fun evenClick() {
        viewPickPicture.viewToolbar.ivBack.setOnClickListener {
            if (viewPickPicture.rcvPicture.visibility == View.VISIBLE)
                switchLayoutPicture(0)
            else {
                parentFragmentManager.popBackStack()
                isFinish.check(true)
            }
        }
    }

    private var handler = Handler(Looper.getMainLooper()) { msg ->
        if (msg.what == 0) {
            viewPickPicture.viewLoading.visibility = View.GONE
            val lstBucket = DataLocalManager.getListBucket(Constant.LIST_ALL_PIC)
            if (lstBucket.isNotEmpty()) bucketAdapter.setData(lstBucket)
        }
        true
    }

    fun switchLayoutPicture(pos: Int) {
        when (pos) {
            0 -> {
                //gone rcv Picture
                animation = AnimationUtils.loadAnimation(requireContext(), R.anim.slide_out_right)
                viewPickPicture.rcvPicture.animation = animation
                viewPickPicture.rcvPicture.visibility = View.GONE
                animation =
                    AnimationUtils.loadAnimation(requireContext(), R.anim.slide_in_left_small)
                viewPickPicture.rcvBucket.animation = animation
                viewPickPicture.rcvBucket.visibility = View.VISIBLE
            }
            1 -> {
                //gone rcv Bucket
                animation = AnimationUtils.loadAnimation(requireContext(), R.anim.slide_out_left)
                viewPickPicture.rcvBucket.animation = animation
                viewPickPicture.rcvBucket.visibility = View.GONE
                animation = AnimationUtils.loadAnimation(requireContext(), R.anim.slide_in_right)
                viewPickPicture.rcvPicture.animation = animation
                viewPickPicture.rcvPicture.visibility = View.VISIBLE
            }
        }

        animation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {}
            override fun onAnimationEnd(animation: Animation) {
                viewPickPicture.clearAnimation()
            }

            override fun onAnimationRepeat(animation: Animation) {}
        })
    }
}