package com.best.now.autoclick.lifecycle

import android.app.Activity
import java.util.*


/**
 * Created by luyao
 * on 2019/8/6 10:58
 */
object KtxManager {

    var mActivityList = LinkedList<Activity>()

    val currentActivity: Activity?
        get() =
            if (mActivityList.isEmpty()) null
            else mActivityList.last


    /**
     * push the specified [activity] into the list
     */
    fun pushActivity(activity: Activity) {
        if (mActivityList.contains(activity)) {
            if (mActivityList.last != activity) {
                mActivityList.remove(activity)
                mActivityList.add(activity)
            }
        } else {
            mActivityList.add(activity)
        }
    }

    /**
     * pop the specified [activity] into the list
     */
    fun popActivity(activity: Activity) {
        mActivityList.remove(activity)
    }

    fun finishCurrentActivity() {
        currentActivity?.finish()
    }

    fun finishActivity(activity: Activity) {
        mActivityList.remove(activity)
        activity.finish()
    }

    fun finishActivity(clazz: Class<*>) {
        for (activity in mActivityList)
            if (activity.javaClass == clazz)
                activity.finish()
    }

    fun finishAllActivity() {
        for (activity in mActivityList)
            activity.finish()
    }

    /**
     * 占中是否有某个activity
     */
    fun hasActivity(clazz: Class<*>): Boolean {
        for (activity in mActivityList)
            if (activity.javaClass == clazz)
                return true
        return false
    }
    //获取栈顶activity
    fun getTopActivity():Activity{
       return mActivityList[mActivityList.size-1]
    }

    fun isTopActivity(activity: Activity): Boolean {
        var top = getTopActivity()
        return activity == top
    }
}