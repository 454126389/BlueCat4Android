package com.tongge.eventBus;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by DZ on 2017/7/5.
 */

public class EventBusUtils {

    /**
     * (priority = x,是优先级，数字自定义)
     * ThreadMode.MAIN          不管从哪个线程发出的事件，MAIN模式都会在UI（主线程）线程执行
     * @Subscribe(threadMode = ThreadMode.MAIN, priority = 1)
     * ThreadMode.POSTING       事件从哪个线程发布出来的就会在该线程中运行
     * @Subscribe(threadMode = ThreadMode.POSTING, priority = 2)
     * ThreadMode.BACKGROUND    如果发送事件的线程是UI线程，则重新创建新的子线程执行，因此不能执行更新UI的操作
     * @Subscribe(threadMode = ThreadMode.BACKGROUND, priority = 3)
     * ThreadMode.ASYNC         不管从哪个线程发出的事件，ASYNC模式都会创建一个新的子线程来执行</span>
     * @Subscribe(threadMode = ThreadMode.ASYNC, priority = 4)
     */
    private EventBusUtils() {
    }

    /**
     * 注册EventBus
     *
     * @param subscriber 订阅者对象
     */
    public static void register(Object subscriber) {
        if (!EventBus.getDefault().isRegistered(subscriber)) EventBus.getDefault().register(subscriber);
    }

    /**
     * 取消注册EventBus
     *
     * @param subscriber 订阅者对象
     */
    public static void unregister(Object subscriber) {
        EventBus.getDefault().unregister(subscriber);
    }

    /**
     * 发布订阅事件
     *
     * @param event 事件对象
     */
    public static void post(Object event) {
        EventBus.getDefault().post(event);
    }

    /**
     *  发布粘性订阅事件
     *  默认就是非粘性事件，如果是粘性事件，只需要在事件的注解上面加上
     *  sticky = true
     *
     *  粘性事件，在注解上加上 priority = 优先级数（int值）
     *  priority = 1
     * @param event 事件对象
     */
    public static void postSticky(Object event) {
        EventBus.getDefault().postSticky(event);
    }

    /**
     * 移除指定的粘性订阅事件
     *
     * @param eventType class的字节码，例如：String.class
     */
    public static <T> void removeStickyEvent(Class<T> eventType) {
        T stickyEvent = EventBus.getDefault().getStickyEvent(eventType);
        if (stickyEvent != null) {
            EventBus.getDefault().removeStickyEvent((T) stickyEvent);
        }
    }

    /**
     * 移除所有的粘性订阅事件
     */
    public static void removeAllStickyEvents() {
        EventBus.getDefault().removeAllStickyEvents();
    }

    /**
     * 取消事件传送
     *
     * @param event 事件对象
     */
    public static void cancelEventDelivery(Object event) {
        EventBus.getDefault().cancelEventDelivery(event);
    }
}
