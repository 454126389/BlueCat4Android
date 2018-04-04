package com.tongge.common.otherUtils;

import android.util.SparseArray;

import com.tongge.common.sysUtils.STLog;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * <pre>
 *      线程池工具类
 * </pre>
 * <code>
 *     使用:<Br/>
 *     ThreadPoolUtils.getInstace().setExecs_key(getLastkey()).execute(...);
 * </code><Br/>
 * Created by DZ on 2017/6/30.
 *
 */
public class ThreadPoolUtils {

    /**
     * 线程池的四种类型
     */
    public static final int FixedThread             = 0x1;
    public static final int CachedThread            = 0x2;
    public static final int SingleThread            = 0x3;
    public static final int ScheduleThread          = 0x4;

    private static final class ClassHolder{
        private static final ThreadPoolUtils SINGLETON = new ThreadPoolUtils();
    }

    public static ThreadPoolUtils getInstace(){
        return ClassHolder.SINGLETON;
    }

    /**
     * 默认只有一条FixedThread线程池
     */
    private ThreadPoolUtils() {
        if (execs == null)
            execs = new SparseArray<>();
        execs.put(FixedThread, Executors.newFixedThreadPool(4));
    }

    /**
     * 用于保存多条线程池，默认只有一条FixedThread
     */
    private SparseArray<ExecutorService> execs;
    /**
     * 需要使用线程池的key
     */
    private int execs_key = FixedThread;
    /**
     * 当前4个线程池未创建满时，特殊线程池的key
     */
    private int other_execs_key = 4;

    /**
     * ThreadPoolUtils构造函数
     *
     * @param type         线程池类型
     * @param corePoolSize 只对Fixed和Scheduled线程池起效
     */
    /**
     * 添加新的线程池
     * @param type          线程池类型
     * @param corePoolSize  核心线程数
     * @param key           是否是特殊的线程池<Br/>&nbsp&nbsp&nbsp&nbsp
     *                         true: 是特殊线程池，不管是否存在相同类型的线程池，都添加新的<Br/>&nbsp&nbsp&nbsp&nbsp
     *                         false: 不是特殊线程池，如果已经存在相同的线程池，就不添加 <Br/>
     * @return  返回线程池的key
     *          <p>-1:线程池中已经存在，不能在添加</p>
     *          <p>非特殊线程池，key只能是{@link #FixedThread}
     *          {@link #CachedThread}
     *          {@link #SingleThread}
     *          {@link #ScheduleThread}</p>
     */
    private int addExecutorService(int type, int corePoolSize, Boolean key) {
        // 构造有定时功能的线程池
        // ThreadPoolExecutor(corePoolSize, Integer.MAX_VALUE, 10L, TimeUnit.MILLISECONDS, new BlockingQueue<Runnable>)
        ExecutorService es;
        switch (type) {
            case FixedThread:
                // 构造一个固定线程数目的线程池
                // ThreadPoolExecutor(corePoolSize, corePoolSize, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
                es = Executors.newFixedThreadPool(corePoolSize);
                return addExecutors(type, es, key);
            case SingleThread:
                // 构造一个只支持一个线程的线程池,相当于newFixedThreadPool(1)
                // ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>())
                es = Executors.newSingleThreadExecutor();
                return addExecutors(type, es, key);
            case CachedThread:
                // 构造一个缓冲功能的线程池
                // ThreadPoolExecutor(0, Integer.MAX_VALUE, 60L, TimeUnit.SECONDS, new SynchronousQueue<Runnable>());
                es = Executors.newCachedThreadPool();
                return addExecutors(type, es, key);
            case ScheduleThread:
                es = Executors.newScheduledThreadPool(corePoolSize);
                return addExecutors(type, es, key);
            default:
                STLog.e(this, "create ExecutorService failed ,use default ExecutorService");
                return -1;
        }
    }

    private int addExecutors(int type, ExecutorService es, Boolean key){
        if (key){
            int exec_key;
            if (execs.get(CachedThread)!=null &&
                execs.get(SingleThread)!=null &&
                    execs.get(ScheduleThread)!=null){
                exec_key = execs.size();
            }else{
                exec_key = other_execs_key++;
            }
            execs.put(exec_key, es);
            return getLastkey();
        }
        if (execs.get(type) == null) {
            execs.put(SingleThread, es);
            return type;
        }
        return -1;
    }

    /**
     * 使用哪个线程池执行任务，默认使用FixedThread
     * @param execs_key 线程池的key
     * @return ThreadPoolUtils的单例
     */
    public ThreadPoolUtils setExecs_key(int execs_key) {
        this.execs_key = execs_key;
        return this;
    }

    /**
     * 获取最后一个线程池的key
     * @return  最后一个线程池的key
     */
    public int getLastkey(){
        return execs.size()-1;
    }

    /**
     * 在未来某个时间执行给定的命令
     * <p>该命令可能在新的线程、已入池的线程或者正调用的线程中执行，这由 Executor 实现决定。</p>
     *
     * @param command 命令
     */
    public void execute(Runnable command) {
        execs.get(execs_key).execute(command);
    }

    /**
     * 在未来某个时间执行给定的命令链表
     * <p>该命令可能在新的线程、已入池的线程或者正调用的线程中执行，这由 Executor 实现决定。</p>
     *
     * @param commands 命令链表
     */
    public void execute(List<Runnable> commands) {
        for (Runnable command : commands) {
            execs.get(execs_key).execute(command);
        }
    }

    /**
     * 待以前提交的任务执行完毕后关闭线程池
     * <p>启动一次顺序关闭，执行以前提交的任务，但不接受新任务。
     * 如果已经关闭，则调用没有作用。</p>
     */
    public void shutDown() {
        execs.get(execs_key).shutdown();
    }

    /**
     * 试图停止所有正在执行的活动任务
     * <p>试图停止所有正在执行的活动任务，暂停处理正在等待的任务，并返回等待执行的任务列表。</p>
     * <p>无法保证能够停止正在处理的活动执行任务，但是会尽力尝试。</p>
     *
     * @return 等待执行的任务的列表
     */
    public List<Runnable> shutDownNow() {
        return execs.get(execs_key).shutdownNow();
    }

    /**
     * 判断线程池是否已关闭
     *
     * @return {@code true}: 是<br>{@code false}: 否
     */
    public boolean isShutDown() {
        return execs.get(execs_key).isShutdown();
    }

    /**
     * 关闭线程池后判断所有任务是否都已完成
     * <p>注意，除非首先调用 shutdown 或 shutdownNow，否则 isTerminated 永不为 true。</p>
     *
     * @return {@code true}: 是<br>{@code false}: 否
     */
    public boolean isTerminated() {
        return execs.get(execs_key).isTerminated();
    }


    /**
     * 请求关闭、发生超时或者当前线程中断
     * <p>无论哪一个首先发生之后，都将导致阻塞，直到所有任务完成执行。</p>
     *
     * @param timeout 最长等待时间
     * @param unit    时间单位
     * @return {@code true}: 请求成功<br>{@code false}: 请求超时
     * @throws InterruptedException 终端异常
     */
    public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
        return execs.get(execs_key).awaitTermination(timeout, unit);
    }

    /**
     * 提交一个Callable任务用于执行
     * <p>如果想立即阻塞任务的等待，则可以使用{@code result = exec.submit(aCallable).get();}形式的构造。</p>
     *
     * @param task 任务
     * @param <T>  泛型
     * @return 表示任务等待完成的Future, 该Future的{@code get}方法在成功完成时将会返回该任务的结果。
     */
    public <T> Future<T> submit(Callable<T> task) {
        return execs.get(execs_key).submit(task);
    }

    /**
     * 提交一个Runnable任务用于执行
     *
     * @param task   任务
     * @param result 返回的结果
     * @param <T>    泛型
     * @return 表示任务等待完成的Future, 该Future的{@code get}方法在成功完成时将会返回该任务的结果。
     */
    public <T> Future<T> submit(Runnable task, T result) {
        return execs.get(execs_key).submit(task, result);
    }

    /**
     * 提交一个Runnable任务用于执行
     *
     * @param task 任务
     * @return 表示任务等待完成的Future, 该Future的{@code get}方法在成功完成时将会返回null结果。
     */
    public Future<?> submit(Runnable task) {
        return execs.get(execs_key).submit(task);
    }

    /**
     * 执行给定的任务
     * <p>当所有任务完成时，返回保持任务状态和结果的Future列表。
     * 返回列表的所有元素的{@link Future#isDone}为{@code true}。
     * 注意，可以正常地或通过抛出异常来终止已完成任务。
     * 如果正在进行此操作时修改了给定的 collection，则此方法的结果是不确定的。</p>
     *
     * @param tasks 任务集合
     * @param <T>   泛型
     * @return 表示任务的 Future 列表，列表顺序与给定任务列表的迭代器所生成的顺序相同，每个任务都已完成。
     * @throws InterruptedException 如果等待时发生中断，在这种情况下取消尚未完成的任务。
     */
    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks) throws InterruptedException {
        return execs.get(execs_key).invokeAll(tasks);
    }

    /**
     * 执行给定的任务
     * <p>当所有任务完成或超时期满时(无论哪个首先发生)，返回保持任务状态和结果的Future列表。
     * 返回列表的所有元素的{@link Future#isDone}为{@code true}。
     * 一旦返回后，即取消尚未完成的任务。
     * 注意，可以正常地或通过抛出异常来终止已完成任务。
     * 如果此操作正在进行时修改了给定的 collection，则此方法的结果是不确定的。</p>
     *
     * @param tasks   任务集合
     * @param timeout 最长等待时间
     * @param unit    时间单位
     * @param <T>     泛型
     * @return 表示任务的 Future 列表，列表顺序与给定任务列表的迭代器所生成的顺序相同。如果操作未超时，则已完成所有任务。如果确实超时了，则某些任务尚未完成。
     * @throws InterruptedException 如果等待时发生中断，在这种情况下取消尚未完成的任务
     */
    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws
            InterruptedException {
        return execs.get(execs_key).invokeAll(tasks, timeout, unit);
    }

    /**
     * 执行给定的任务
     * <p>如果某个任务已成功完成（也就是未抛出异常），则返回其结果。
     * 一旦正常或异常返回后，则取消尚未完成的任务。
     * 如果此操作正在进行时修改了给定的collection，则此方法的结果是不确定的。</p>
     *
     * @param tasks 任务集合
     * @param <T>   泛型
     * @return 某个任务返回的结果
     * @throws InterruptedException 如果等待时发生中断
     * @throws ExecutionException   如果没有任务成功完成
     */
    public <T> T invokeAny(Collection<? extends Callable<T>> tasks) throws InterruptedException, ExecutionException {
        return execs.get(execs_key).invokeAny(tasks);
    }

    /**
     * 执行给定的任务
     * <p>如果在给定的超时期满前某个任务已成功完成（也就是未抛出异常），则返回其结果。
     * 一旦正常或异常返回后，则取消尚未完成的任务。
     * 如果此操作正在进行时修改了给定的collection，则此方法的结果是不确定的。</p>
     *
     * @param tasks   任务集合
     * @param timeout 最长等待时间
     * @param unit    时间单位
     * @param <T>     泛型
     * @return 某个任务返回的结果
     * @throws InterruptedException 如果等待时发生中断
     * @throws ExecutionException   如果没有任务成功完成
     * @throws TimeoutException     如果在所有任务成功完成之前给定的超时期满
     */
    public <T> T invokeAny(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws
            InterruptedException, ExecutionException, TimeoutException {
        return execs.get(execs_key).invokeAny(tasks, timeout, unit);
    }

    /**
     * 延迟执行Runnable命令
     *
     * @param command 命令
     * @param delay   延迟时间
     * @param unit    单位
     * @return 表示挂起任务完成的ScheduledFuture，并且其{@code get()}方法在完成后将返回{@code null}
     */
    public ScheduledFuture<?> schedule(Runnable command, long delay, TimeUnit unit) {
        if (execs_key != ScheduleThread){
            STLog.e(this,"no ScheduleThread");
            return null;
        }
        ScheduledExecutorService ses = (ScheduledExecutorService) execs.get(execs_key);
        return ses.schedule(command, delay, unit);
    }

    /**
     * 延迟执行Callable命令
     *
     * @param callable 命令
     * @param delay    延迟时间
     * @param unit     时间单位
     * @param <V>      泛型
     * @return 可用于提取结果或取消的ScheduledFuture
     */
    public <V> ScheduledFuture<V> schedule(Callable<V> callable, long delay, TimeUnit unit) {
        if (execs_key != ScheduleThread){
            STLog.e(this,"no ScheduleThread");
            return null;
        }
        ScheduledExecutorService ses = (ScheduledExecutorService) execs.get(execs_key);
        return ses.schedule(callable, delay, unit);
    }

    /**
     * 延迟并循环执行命令
     *
     * @param command      命令
     * @param initialDelay 首次执行的延迟时间
     * @param period       连续执行之间的周期
     * @param unit         时间单位
     * @return 表示挂起任务完成的ScheduledFuture，并且其{@code get()}方法在取消后将抛出异常
     */
    public ScheduledFuture<?> scheduleWithFixedRate(Runnable command, long initialDelay, long period, TimeUnit unit) {
        if (execs_key != ScheduleThread){
            STLog.e(this,"no ScheduleThread");
            return null;
        }
        ScheduledExecutorService ses = (ScheduledExecutorService) execs.get(execs_key);
        return ses.scheduleAtFixedRate(command, initialDelay, period, unit);
    }

    /**
     * 延迟并以固定休息时间循环执行命令
     *
     * @param command      命令
     * @param initialDelay 首次执行的延迟时间
     * @param delay        每一次执行终止和下一次执行开始之间的延迟
     * @param unit         时间单位
     * @return 表示挂起任务完成的ScheduledFuture，并且其{@code get()}方法在取消后将抛出异常
     */
    public ScheduledFuture<?> scheduleWithFixedDelay(Runnable command, long initialDelay, long delay, TimeUnit unit) {
        if (execs_key != ScheduleThread){
            STLog.e(this,"no ScheduleThread");
            return null;
        }
        ScheduledExecutorService ses = (ScheduledExecutorService) execs.get(execs_key);
        return ses.scheduleWithFixedDelay(command, initialDelay, delay, unit);
    }
}