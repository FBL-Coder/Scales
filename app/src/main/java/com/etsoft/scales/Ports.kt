package com.etsoft.scales


/**
 * Author：FBL  Time： 2018/8/8.
 * 接口管理
 */
class Ports {
    companion object {
        /**
         * 跟接口
         */
        var port = "http://garbage.lilinyun.com/"
//        var port = "http://testgarbage.lilinyun.com/"
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
        var INPUTBACKLIST = port + "webapi/Replenishstockdetails/listV2"
        /**
         * 新增入库
         * 单个入库
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
        var NOTIFICATIONLISTINFO = port + "webapi/Notice/info"

        /**
         * 回收物列表
         */
        var RECYCLELIST = port + "webapi/Recyclingprice/list"
        /**
         * 定位获取服务站
         */
        var LOCATION_SERVER = port + "webapi/Servicepoint/positioning"

        /**
         * 服务站列表
         */
        var SERVERLIST = port + "webapi/Servicepoint/list"

        /**
         * 服务站详情
         */
        var SERVERLISTINFO = port + "webapi/Servicepoint/info"

        /**
         * 客户注册
         */
        var REISTERUSER = port + "webapi/User/insertApi"

        /**
         * 用户查询
         */
        var USERQUERY = port + "webapi/User/getInfoByPhoneApi"
        /**
         * 新增入库
         * 多条入库
         */
        var ADDOUTBACKLIST = port + "webapi/Replenishstockdetails/insertList"

        /**
         * 礼品接口
         */
        var GIFTLIST = port + "webapi/Gift/list"
    }
}
