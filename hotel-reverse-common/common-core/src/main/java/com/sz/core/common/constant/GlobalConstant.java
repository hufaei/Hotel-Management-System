package com.sz.core.common.constant;

/**
 * 常量管理
 * &#064;author:  botsuchi
 * &#064;date:  2022/8/23 10:20
 * &#064;description:
 */
public class GlobalConstant {

    public static final String UTF_8 = "utf-8";

    /**
     * redis的发布订阅channel（用于 [服务->websocket]方向的消息传递）
     */
    public static final String SERVICE_TO_WS = "channel:service_to_ws";

    /**
     * redis的发布订阅channel（用于 [websocket->服务]方向的消息传递）
     */
    public static final String WS_TO_SERVICE = "channel:ws_to_service";

    /**
     * redis的发布订阅channel （用于permission变更）
     */
    public static final String CHANGE_PERMISSIONS_SIGNAL = "change_permissions_signal";
    /**
     * todo rabbitMq的交换机名称--多个
     */
    public static final String EXCHANGE_NAME = "sz-exchange";
    /**
     * rabbitMq的队列名称--多
     */
    public static final String QUEUE_NAME = "sz-queue";
    /**
     * rabbitMq的routingKey--多
     */
    public static final String ROUTING_KEY = "sz-routingKey";
    /**
     * 超级管理员角色标识
     */
    public static final String SUPER_ROLE = "admin";

}
