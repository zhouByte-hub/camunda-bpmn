package org.zhoubyte.camunda_api.util;

/**
 * Camunda 8 定时器周期格式工具类
 *
 * <p>Camunda 8 使用 ISO 8601 重复时间间隔格式作为 Timer 的 Cycle 值，格式规范如下：</p>
 *
 * <pre>
 * 格式：R[重复次数]/<Duration>
 *
 * - R        : 表示重复（Repeat），R 后不跟数字表示无限重复
 * - 重复次数  : 可选，正整数，省略则无限重复，例如 R3 表示重复 3 次
 * - Duration : ISO 8601 持续时间，格式为 P[年]Y[月]M[天]DT[时]H[分]M[秒]S
 *              其中 T 为日期与时间的分隔符，T 之前为日期部分，T 之后为时间部分
 *
 * 示例：
 *   R/P1D       每天触发一次，无限重复
 *   R3/P1D      每天触发一次，共触发 3 次
 *   R/PT1H      每小时触发一次，无限重复
 *   R5/PT30M    每 30 分钟触发一次，共触发 5 次
 *   R/P1DT2H    每 1 天 2 小时触发一次，无限重复
 * </pre>
 */
public class TimerCycleUtil {

    private TimerCycleUtil() {
    }

    /**
     * 根据天数生成 Camunda 8 Timer Cycle 字符串（无限重复）
     *
     * <p>生成格式符合 ISO 8601 重复时间间隔规范：{@code R/P<days>D}</p>
     * <p>例如：传入 {@code 1} 返回 {@code "R/P1D"}，表示每天触发一次，无限重复</p>
     *
     * <p><b>Camunda 8 ISO 8601 重复时间间隔格式说明：</b><br>
     * {@code R[重复次数]/P[年]Y[月]M[天]DT[时]H[分]M[秒]S}<br>
     * 其中 {@code R} 后不跟数字表示无限重复，{@code P} 后跟持续时间描述符。<br>
     * 示例：{@code R/P7D} 表示每 7 天触发一次，无限重复。</p>
     *
     * @param days 间隔天数，必须大于 0
     * @return 符合 ISO 8601 重复时间间隔格式的字符串，例如 {@code "R/P3D"}
     * @throws IllegalArgumentException 当 days 小于或等于 0 时抛出
     */
    public static String ofDays(int days) {
        if (days <= 0) {
            throw new IllegalArgumentException("days 必须大于 0，当前值：" + days);
        }
        return "R/P" + days + "D";
    }

    /**
     * 根据天数和重复次数生成 Camunda 8 Timer Cycle 字符串
     *
     * <p>生成格式符合 ISO 8601 重复时间间隔规范：{@code R<repeat>/P<days>D}</p>
     * <p>例如：传入天数 {@code 1}，重复次数 {@code 3}，返回 {@code "R3/P1D"}，表示每天触发一次，共触发 3 次</p>
     *
     * <p><b>Camunda 8 ISO 8601 重复时间间隔格式说明：</b><br>
     * {@code R[重复次数]/P[年]Y[月]M[天]DT[时]H[分]M[秒]S}<br>
     * 其中 {@code R} 后跟正整数表示有限次重复，{@code P} 后跟持续时间描述符。<br>
     * 示例：{@code R3/P7D} 表示每 7 天触发一次，共触发 3 次。</p>
     *
     * @param days   间隔天数，必须大于 0
     * @param repeat 重复次数，必须大于 0
     * @return 符合 ISO 8601 重复时间间隔格式的字符串，例如 {@code "R3/P7D"}
     * @throws IllegalArgumentException 当 days 或 repeat 小于或等于 0 时抛出
     */
    public static String ofDays(int days, int repeat) {
        if (repeat <= 0 && days <= 0) {
            throw new IllegalArgumentException("days 必须大于 0，当前值：" + days);
        }
        if (repeat <= 0) {
            throw new IllegalArgumentException("repeat 必须大于 0，当前值：" + repeat);
        }
        return "R" + repeat + "/P" + days + "D";
    }

    /**
     * 生成一个永远不会触发的 Camunda 8 Timer Cycle 字符串
     *
     * <p>通过将重复次数设置为 {@code 0}，即 {@code R0/P1D}，使定时器永远不会触发。</p>
     * <p>适用于需要动态禁用定时器的场景，例如某些条件下不需要逾期提醒时，
     * 可将 {@code overdue_timer_cycle} 变量设置为此方法的返回值。</p>
     *
     * @return {@code "R0/P1D"}，重复次数为 0，定时器永远不会触发
     */
    public static String never() {
        return "R0/P1D";
    }

}
