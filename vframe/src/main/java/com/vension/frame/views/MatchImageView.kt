package com.vension.frame.views

import android.content.Context
import android.support.v7.widget.AppCompatImageView
import android.util.AttributeSet
import com.vension.frame.R

/**
 * ////////////////////////////////////////////////////////////////////
 * //                          _ooOoo_                               //
 * //                         o8888888o                              //
 * //                         88" . "88                              //
 * //                         (| ^_^ |)                              //
 * //                         O\  =  /O                              //
 * //                      ____/`---'\____                           //
 * //                    .'  \\|     |//  `.                         //
 * //                   /  \\|||  :  |||//  \                        //
 * //                  /  _||||| -:- |||||-  \                       //
 * //                  |   | \\\  -  /// |   |                       //
 * //                  | \_|  ''\---/''  |   |                       //
 * //                  \  .-\__  `-`  ___/-. /                       //
 * //                ___`. .'  /--.--\  `. . ___                     //
 * //              ."" '<  `.___\_<|>_/___.'  >'"".                  //
 * //            | | :  `- \`.;`\ _ /`;.`/ - ` : | |                 //
 * //            \  \ `-.   \_ __\ /__ _/   .-` /  /                 //
 * //      ========`-.____`-.___\_____/___.-`____.-'========         //
 * //                           `=---='                              //
 * //      ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^        //
 * //         佛祖保佑       永无BUG     永不修改                        //
 * ////////////////////////////////////////////////////////////////////
 * ========================================================
 * 作  者：Vension
 * 日  期：2018/12/3 16:56
 * 描  述：支持设置宽高自适应的ImageView
 * ========================================================
 */

class MatchImageView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr) {

    private var isMatch = false//是否自适应宽高

    init {
        val a = context.obtainStyledAttributes(attrs, R.styleable.MatchImageView)
        isMatch = a.getBoolean(R.styleable.MatchImageView_is_match, isMatch)// 默认为自适应
        a.recycle()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        if (isMatch) {
            if(widthMeasureSpec > heightMeasureSpec){
                super.onMeasure(widthMeasureSpec, widthMeasureSpec)
            }else{
                super.onMeasure(heightMeasureSpec, heightMeasureSpec)
            }
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        }
    }

}