package com.github.yoojia.keyboard;

/**
 * 车牌号提交时回调的接口
 * @author  yoojia.chen@gmail.com
 * @version version 2015-04-24
 * @since   1.0
 */
public interface OnNumberCommitListener {

    /**
     * 提交车牌号
     * @param number 必定是完整的车牌号。
     */
    void onCommit(String number);
}
