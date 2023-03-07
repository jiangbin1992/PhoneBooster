package com.best.now.autoclick.ui

import android.content.Intent
import androidx.viewpager2.widget.ViewPager2
import com.best.now.autoclick.R
import com.best.now.autoclick.adapter.GuideAdapter
import com.best.now.autoclick.adapter.NextClickCallBack
import com.best.now.autoclick.databinding.ActivityGuideBinding
import com.best.now.myad.BaseVMActivity
import com.best.now.myad.ext.putSpValue
import com.gm.phonecleaner.ui.main.MainActivity

/**
author:zhoujingjin
date:2022/11/19
 */
class GuideActivity : BaseVMActivity(), NextClickCallBack {
    private lateinit var adapter: GuideAdapter
    private val binding by binding<ActivityGuideBinding>(R.layout.activity_guide)
    override fun initView() {
        binding.apply {
            viewpager.orientation = ViewPager2.ORIENTATION_HORIZONTAL
            adapter = GuideAdapter(this@GuideActivity)
            viewpager.adapter = adapter
        }
    }

    override fun initData() {
        val imageList = mutableListOf<Int>()
        imageList.add(R.drawable.guide_1)
        imageList.add(R.drawable.guide_2)
        imageList.add(R.drawable.guide_3)
        adapter.setList(imageList)
    }

    override fun clickNext(position: Int) {
        if (position != 2)
            binding.viewpager.setCurrentItem(position + 1, true)
        else {
            //记录已经不是第一次进来了
            putSpValue("First", false)
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}