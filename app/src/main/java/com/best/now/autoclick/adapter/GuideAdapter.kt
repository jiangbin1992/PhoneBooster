package com.best.now.autoclick.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.best.now.autoclick.R
import com.best.now.autoclick.databinding.GuideItemBinding
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder

/**
author:zhoujingjin
date:2022/11/19
 */
class GuideAdapter(val nextClick:NextClickCallBack):BaseQuickAdapter<Int, BaseDataBindingHolder<GuideItemBinding>>(R.layout.guide_item) {
    override fun convert(holder: BaseDataBindingHolder<GuideItemBinding>, item: Int) {
        val position = getItemPosition(item)
        holder.dataBinding?.apply {
            ivImage.setImageResource(item)
            ivImage.setOnClickListener {
                nextClick.clickNext(position)
            }
        }
    }
}
interface NextClickCallBack{
    fun clickNext(position:Int)
}
