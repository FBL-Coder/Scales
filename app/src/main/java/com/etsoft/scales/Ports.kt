package com.etsoft.scales


/**
 * Author：FBL  Time： 2018/8/8.
 * 接口管理
 */
class Ports {
    companion object {

        var port = "http://garbage.lilinyun.com/"
        /**
         * 登陆接口
         */
        var LOGIN = port + "webapi/Staff/login"
        /**
         * 注册接口
         */
        var REGISTER = port + ""
        /**
         * 忘记密码
         */
        var FORGET = port + ""
        /**
         * 出库列表
         */
        var OUTBACKLIST = port + "webapi/Outbound/appList"
        /**
         * 入库列表
         */
        var INPUTBACKLIST = port + "webapi/Replenishstockdetails/list"
        /**
         * 新增入库
         */
        var ADDINPUTBACK = port + "webapi/Replenishstockdetails/insert"
        /**
         * 新增出库
         */
        var ADDOUTBACK = port + "webapi/Outbound/insert"
        /**
         * 通知列表
         */
        var NOTIFICATIONLIST = port + "webapi/Notice/list"
        /**
         * 通知详情
         */
        var NOTIFICATIONINFO = port + "webapi/Notice/info"
        /**
         * 服务站点
         */
        var SITEINFO = port + "webapi/Servicepoint/list"
        /**
         * 回收物列表
         */
        var RECYCLELIST = port + "webapi/Recyclingprice/list"
        /**
         * 定位获取服务站
         */
        var LOCATION_SERVER = port + "webapi/Servicepoint/positioning"



    }
}
