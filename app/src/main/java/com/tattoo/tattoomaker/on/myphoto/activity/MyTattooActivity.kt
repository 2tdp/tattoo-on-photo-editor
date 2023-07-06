package com.tattoo.tattoomaker.on.myphoto.activity

import android.app.AlertDialog
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.viewpager2.widget.ViewPager2
import com.tattoo.tattoomaker.on.myphoto.R
import com.tattoo.tattoomaker.on.myphoto.activity.base.BaseActivity
import com.tattoo.tattoomaker.on.myphoto.adapter.ViewPagerAddFragmentsAdapter
import com.tattoo.tattoomaker.on.myphoto.addview.ViewMyStory
import com.tattoo.tattoomaker.on.myphoto.addview.viewdialog.ViewDialogText
import com.tattoo.tattoomaker.on.myphoto.callback.ICallBackCheck
import com.tattoo.tattoomaker.on.myphoto.callback.ICallBackItem
import com.tattoo.tattoomaker.on.myphoto.fragment.ItemMyStoryFragment
import com.tattoo.tattoomaker.on.myphoto.fragment.PreviewFragment
import com.tattoo.tattoomaker.on.myphoto.model.ProjectModel
import com.tattoo.tattoomaker.on.myphoto.sharepref.DataLocalManager
import com.tattoo.tattoomaker.on.myphoto.utils.Constant
import com.tattoo.tattoomaker.on.myphoto.utils.Utils

class MyTattooActivity: BaseActivity() {

    private lateinit var viewMyStory: ViewMyStory

    private var completeItemFragment: ItemMyStoryFragment? = null
    private var draftItemFragment: ItemMyStoryFragment? = null
    private var lstComplete = ArrayList<ProjectModel>()
    private var lstDraft = ArrayList<ProjectModel>()
    private var isDel = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewMyStory = ViewMyStory(this@MyTattooActivity)
        setContentView(viewMyStory)

        setData()
        evenClick()
    }

    override fun onResume() {
        super.onResume()
        setData()
    }

    private fun evenClick() {
        viewMyStory.tvComplete.setOnClickListener {
            viewMyStory.clickOption(0)
            viewMyStory.viewPager.setCurrentItem(0, true)
        }
        viewMyStory.tvDrafts.setOnClickListener {
            viewMyStory.clickOption(1)
            viewMyStory.viewPager.setCurrentItem(1, true)
        }

        viewMyStory.viewToolbar.ivRight.setOnClickListener {
            if (!isDel) {
                if (viewMyStory.viewPager.currentItem == 0) {
                    if (completeItemFragment?.setMultiChoose() != true) {
                        Toast.makeText(this@MyTattooActivity, resources.getString(R.string.empty), Toast.LENGTH_SHORT).show()
                        return@setOnClickListener
                    }
                    else viewMyStory.viewToolbar.ivRight.setImageResource(R.drawable.ic_del)
                }
                else {
                    if (draftItemFragment?.setMultiChoose() != true) {
                        Toast.makeText(this@MyTattooActivity, resources.getString(R.string.empty), Toast.LENGTH_SHORT).show()
                        return@setOnClickListener
                    }
                    else viewMyStory.viewToolbar.ivRight.setImageResource(R.drawable.ic_del)
                }
                isDel = true
            } else {
                val lstDel = if (viewMyStory.viewPager.currentItem == 0) completeItemFragment?.getLstDel()!!
                else draftItemFragment?.getLstDel()!!
                if (lstDel.isEmpty())
                    Toast.makeText(this@MyTattooActivity, getString(R.string.please_choose_other_project), Toast.LENGTH_SHORT).show()
                else showDialogDel(lstDel)
            }
        }
        viewMyStory.viewToolbar.ivBack.setOnClickListener { finish() }
    }

    private fun setData() {
        val viewPagerAdapter = ViewPagerAddFragmentsAdapter(supportFragmentManager, lifecycle)

        lstComplete = DataLocalManager.getListProject(this@MyTattooActivity, Constant.LIST_COMPLETE)
        completeItemFragment = ItemMyStoryFragment.newInstance(lstComplete, 0, object : ICallBackItem {
            override fun callBack(ob: Any, position: Int) {
                if (position > -1) goToPreview(ob, 3)
                else {
                    viewMyStory.viewToolbar.ivRight.setImageResource(R.drawable.ic_del)
                    isDel = true
                }
            }
        })
        viewPagerAdapter.addFrag(completeItemFragment!!)

        lstDraft = DataLocalManager.getListProject(this@MyTattooActivity, Constant.LIST_DRAFT)
        draftItemFragment = ItemMyStoryFragment.newInstance(lstDraft, 1, object : ICallBackItem {
            override fun callBack(ob: Any, position: Int) {
                if (position > -1) goToPreview(ob, 1)
                else {
                    viewMyStory.viewToolbar.ivRight.setImageResource(R.drawable.ic_del)
                    isDel = true
                }
            }
        })
        viewPagerAdapter.addFrag(draftItemFragment!!)

        viewMyStory.viewPager.adapter = viewPagerAdapter

        viewMyStory.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                viewMyStory.clickOption(position)
            }
        })
    }

    private fun goToPreview(ob: Any, type: Int) {
        val previewFragment =
            PreviewFragment.newInstance(ob as ProjectModel, type, object : ICallBackCheck {
            override fun check(isCheck: Boolean) {
                if (!isCheck) {
                    DataLocalManager.setProject(ob, Constant.PROJECT)

                    setIntent(EditActivity::class.java.name, false)
                } else setData()
            }
        })
        replaceFragment(supportFragmentManager, previewFragment, true, true, true)
    }

    private fun showDialogDel(lstDel: ArrayList<ProjectModel>) {
        val w = resources.displayMetrics.widthPixels / 100F
        val viewDialog = ViewDialogText(this@MyTattooActivity).apply {
            tv.text = getString(R.string.are_you_sure_want_to_delete_this_project)
            tvYes.text = getString(R.string.yes)
            tvCancel.text = getString(R.string.cancel)
        }
        val dialog = AlertDialog.Builder(this@MyTattooActivity, R.style.SheetDialog).create()
        dialog.setView(viewDialog)
        dialog.setCancelable(true)
        dialog.show()

        viewDialog.layoutParams.width = (75f * w).toInt()
        viewDialog.layoutParams.height = (33.333f * w).toInt()

        viewDialog.tvCancel.setOnClickListener { dialog.cancel() }
        viewDialog.tvYes.setOnClickListener {
            viewMyStory.viewLoading.visibility = View.VISIBLE
            Thread {
                if (viewMyStory.viewPager.currentItem == 0) {
                    for (pro in lstDel) {
                        Utils.delFileInFolder(this@MyTattooActivity, pro.nameFolder!!, "")
                        lstComplete.remove(pro)
                    }

                    Handler(Looper.getMainLooper()).post {
                        isDel = false
                        Toast.makeText(this@MyTattooActivity, getString(R.string.done), Toast.LENGTH_SHORT).show()
                        viewMyStory.viewLoading.visibility = View.GONE
                        viewMyStory.viewToolbar.ivRight.setImageResource(R.drawable.ic_item_tick)

                        DataLocalManager.setListProject(this@MyTattooActivity, lstComplete, Constant.LIST_COMPLETE)
                        setData()
                        dialog.cancel()
                    }
                } else {

                    for (pro in lstDel) {
                        Utils.delFileInFolder(this@MyTattooActivity, pro.nameFolder!!, "")
                        lstDraft.remove(pro)
                    }

                    Handler(Looper.getMainLooper()).post {
                        isDel = false
                        Toast.makeText(this@MyTattooActivity, getString(R.string.done), Toast.LENGTH_SHORT).show()
                        viewMyStory.viewLoading.visibility = View.GONE
                        viewMyStory.viewToolbar.ivRight.setImageResource(R.drawable.ic_item_tick)

                        DataLocalManager.setListProject(this@MyTattooActivity, lstDraft, Constant.LIST_DRAFT)
                        setData()
                        dialog.cancel()
                    }
                }
            }.start()
        }
    }
}