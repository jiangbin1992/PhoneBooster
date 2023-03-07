package com.best.now.myad.view

import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.View.OnClickListener
import androidx.databinding.DataBindingUtil
import com.best.now.myad.R
import com.best.now.myad.databinding.RewardsPopLayoutBinding
import razerdp.basepopup.BasePopupWindow

/**
author:zhoujingjin
date:2023/1/27
 */
class AdsRewardsPop(context: Context,times:Int = 0,clickListener: OnClickListener):BasePopupWindow(context) {
    init {
        val view = createPopupById(R.layout.rewards_pop_layout)
        val rewardsPopLayoutBinding =  DataBindingUtil.bind<RewardsPopLayoutBinding>(view)
        contentView = view
        rewardsPopLayoutBinding?.apply {
            ivClose.setOnClickListener { dismiss() }
            when(times){
                0->{
                    llVideo.visibility = View.GONE
                }
                1->{
                    llVideo.visibility = View.VISIBLE
                    ll2.setOnClickListener(clickListener)
                    ll3.setOnClickListener(clickListener)
                }
                2->{
                    llVideo.visibility = View.VISIBLE
                    ll2.setBackgroundResource(R.mipmap.back_done)
                    tv2.text = "Done"
                    tv2.setTextColor(context.resources.getColor(R.color.black60))
                    tv2.compoundDrawablePadding = 3
                    val drawable =context.resources.getDrawable(R.drawable.icon_done)
                    tv2.setCompoundDrawablesWithIntrinsicBounds(drawable,null,null,null)
                    iv2.setImageResource(R.mipmap.video_watched)
                    ll3.setOnClickListener(clickListener)
                }
            }
            tvWatch.setOnClickListener(clickListener)
        }
        popupGravity = Gravity.CENTER
    }
    override fun setContentView(layoutResID: Int) {
        super.setContentView(layoutResID)
    }
    override fun createPopupById(layoutId: Int): View {
        return super.createPopupById(layoutId)
    }
}