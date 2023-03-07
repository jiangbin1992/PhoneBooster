package com.best.now.myad.utils;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

/**
 * Copyright © 2021 www.qiantoon.cn
 * <p>
 * 行为埋点帮助类
 *
 * @author XGH
 * @since 2021/12/29 16:17
 */
public class ActionHelper {

    /**
     * key是埋点类型：open表示进入app、sellpage表示进入售卖页、click_buy表示点击订阅、buy_success表示订阅成功
     */
    public static void doAction(String key) {
        OkGo.<String>post(Constant.EVENTDOMAIN+"save.php?"+Constant.MIX_PARAMETER)     // 请求方式和请求url
                .params("sub_type", Constant.APP_PARAMETER)
                .params("key", key)
                .params("event_time", TimeUtils.getNowString())
                .params("remark", "/")
                .params("uid", "/")
                .params("version", "1.0.0")
                .params("ip", NetworkUtils.getIPAddress(true))
                .params("source", "Android")
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        if (response!=null){
                            LogUtils.i("请求结果："+ response.message());
                        }
                    }
                });
    }

}