package com.vension.frame.views

import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Build
import android.support.annotation.ColorInt
import android.support.annotation.DrawableRes
import android.support.annotation.LayoutRes
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.widget.AppCompatImageView
import android.support.v7.widget.AppCompatTextView
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ProgressBar
import com.vension.frame.R
import java.util.*



/**
 * ===================================================================
 * @author: Created by Vension on 2019/1/21 14:59.
 * @email:  250685***4@qq.com
 * @update: update by *** on 2019/1/21 14:59
 * @desc:   多状态Layout
 * ===================================================================
 */
class MultiStatusLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    /*LayoutResId*/
    private var mLoadingLayoutId: Int = R.layout.layout_mul_default_loading
    private var mEmptyLayoutId: Int = R.layout.layout_mul_default_empty
    private var mErrorLayoutId: Int = R.layout.layout_mul_default_error
    private var mContentLayoutId: Int = View.NO_ID

    /*TextView相关*/
    private var mTextColor: Int = Color.parseColor("#cdcdcd")
    private var mTextSize:Int = 14
    private var mRetryTextColor: Int = Color.parseColor("#cdcdcd")
    private var mRetryTextSize:Int = 14
    private var mRetryBackground: Int = R.drawable.shape_default_retry
    //loadingLayout
    private var mLoadingColor: Int = Color.parseColor("#009844")
    private var mLoadingText: String = R.string.page_loading.toString()
    //空布局
    private var mEmptyImage: Int = R.drawable.img_default_empty
    private var mEmptyText: String = R.string.page_empty.toString()
    //异常布局
    private var mErrorImage: Int = R.drawable.img_default_error
    private var mErrorText: String = R.string.page_empty.toString()
    private var mRetryText: String = R.string.page_empty.toString()
    private var mRetryListener: View.OnClickListener? = null


    private var mLayouts: MutableMap<Int, View> = HashMap()

    private val mInflater: LayoutInflater by lazy {
        LayoutInflater.from(context)
    }

    init {
        //取xml文件中设定的参数
        val ta =
            context.obtainStyledAttributes(attrs, R.styleable.MultiStatusLayout, defStyleAttr, R.style.MultiStatusLayout_Style)
        mLoadingLayoutId = ta.getResourceId(R.styleable.MultiStatusLayout_msl_LoadingResId, R.layout.layout_mul_default_loading)
        mEmptyLayoutId = ta.getResourceId(R.styleable.MultiStatusLayout_msl_EmptyResId, R.layout.layout_mul_default_empty)
        mErrorLayoutId = ta.getResourceId(R.styleable.MultiStatusLayout_msl_ErrorResId, R.layout.layout_mul_default_error)

        mTextColor = ta.getColor(R.styleable.MultiStatusLayout_msl_TextColor, Color.parseColor("#cdcdcd"))
        mTextSize = ta.getDimensionPixelSize(R.styleable.MultiStatusLayout_msl_TextSize, dp2px(14f))

        mEmptyImage = ta.getResourceId(R.styleable.MultiStatusLayout_msl_EmptyImage, R.drawable.img_default_empty)
        mEmptyText = ta.getString(R.styleable.MultiStatusLayout_msl_EmptyText)

        mLoadingText = ta.getString(R.styleable.MultiStatusLayout_msl_LoadingText)
        mLoadingColor = ta.getColor(R.styleable.MultiStatusLayout_msl_LoadingColor,Color.parseColor("#009844"))

        mErrorImage = ta.getResourceId(R.styleable.MultiStatusLayout_msl_ErrorImage, R.drawable.img_default_error)
        mErrorText = ta.getString(R.styleable.MultiStatusLayout_msl_ErrorText)
        mRetryText = ta.getString(R.styleable.MultiStatusLayout_msl_RetryText)

        mRetryTextColor = ta.getColor(R.styleable.MultiStatusLayout_msl_RetryTextColor, Color.parseColor("#009844"))
        mRetryTextSize = ta.getDimensionPixelSize(R.styleable.MultiStatusLayout_msl_RetryTextSize, dp2px(14f))
        mRetryBackground = ta.getResourceId(R.styleable.MultiStatusLayout_msl_RetryBackground,R.drawable.shape_default_retry)

        ta.recycle()
    }


    override fun onFinishInflate() {
        super.onFinishInflate()
        //此方法在xml加载完成后执行
        if (childCount == 0) {
            throw RuntimeException("content view can not be null")
        }
        if (childCount > 1) {
            removeViews(1, childCount - 1)
        }
        val view = getChildAt(0)
        setContentView(view)//设置内容布局
        showContent()//默认显示内容布局
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        //activity从window窗口离开时执行
        removeAllViews()
        if (!mLayouts.isNullOrEmpty()){
            mLayouts.clear()
        }
        if (null != mRetryListener) {
            mRetryListener = null
        }
    }

    fun wrap(activity: Activity): MultiStatusLayout {
        return wrap((activity.findViewById<View>(android.R.id.content) as ViewGroup).getChildAt(0))
    }

    fun wrap(fragment: Fragment): MultiStatusLayout {
        return wrap(fragment.view)
    }

    fun wrap(view: View?): MultiStatusLayout {
        if (view == null) {
            throw RuntimeException("content view can not be null")
        }
        val parent = view.parent as ViewGroup
        val lp = view.layoutParams
        val index = parent.indexOfChild(view)
        parent.removeView(view)

        val layout = MultiStatusLayout(view.context)
        parent.addView(layout, index, lp)
        layout.addView(view)
        layout.setContentView(view)
        return layout
    }


    fun showLoading() {
        show(mLoadingLayoutId)
    }

    fun showEmpty() {
        show(mEmptyLayoutId)
    }

    fun showError() {
        show(mErrorLayoutId)
    }

    fun showContent() {
        show(mContentLayoutId)
    }

    private fun show(layoutId: Int) {
        for (view in mLayouts.values) {
            view.visibility = View.GONE
        }
        prepareLayout(layoutId).visibility = View.VISIBLE
    }

    private fun remove(layoutId: Int) {
        if (mLayouts.containsKey(layoutId)) {
            val vg = mLayouts.remove(layoutId)
            removeView(vg)
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private fun prepareLayout(layoutId: Int): View {
        if (mLayouts.containsKey(layoutId)) {
            return mLayouts[layoutId]!!
        }
        val layout = mInflater.inflate(layoutId, this, false)
        layout.visibility = View.GONE
        addView(layout, 0, layoutParams)
        mLayouts[layoutId] = layout
        when (layoutId) {
            mLoadingLayoutId -> {
                val pbLoading = layout.findViewById<View>(R.id.pb_loading) as ProgressBar
                val tvLoading = layout.findViewById<View>(R.id.tv_page_loading) as AppCompatTextView
                pbLoading.apply {
                    indeterminateTintList = ColorStateList.valueOf(mLoadingColor)
                }
                tvLoading.apply {
                    text = mLoadingText
                    setTextColor(mLoadingColor)
                    setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSize.toFloat())
                }
            }
            mEmptyLayoutId -> {
                val ivEmpty = layout.findViewById<View>(R.id.iv_page_empty) as AppCompatImageView
                val tvEmpty = layout.findViewById<View>(R.id.tv_page_empty) as AppCompatTextView
                ivEmpty.apply {
                    setImageResource(mEmptyImage)
                }
                tvEmpty.apply {
                    text = mEmptyText
                    setTextColor(mTextColor)
                    setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSize.toFloat())
                }
            }
            mErrorLayoutId -> {
                val ivError = layout.findViewById<View>(R.id.iv_page_error) as AppCompatImageView
                val tvError = layout.findViewById<View>(R.id.tv_page_error) as AppCompatTextView
                val retryView = layout.findViewById<View>(R.id.tv_page_error_retry) as AppCompatTextView
                ivError.apply {
                    setImageResource(mErrorImage)
                }
                tvError.apply {
                    text = mErrorText
                    setTextColor(mTextColor)
                    setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSize.toFloat())
                }
                retryView.apply {
                    text = mRetryText
                    setTextColor(mRetryTextColor)
                    setTextSize(TypedValue.COMPLEX_UNIT_PX, mRetryTextSize.toFloat())
                    background = ContextCompat.getDrawable(context,mRetryBackground)
                    if (null != mRetryListener) {
                        setOnClickListener(mRetryListener)
                    }

                }
            }
        }
        return layout
    }

    private fun setContentView(view: View) {
        mContentLayoutId = view.id
        mLayouts[mContentLayoutId] = view
    }

    fun setLoadingLayout(@LayoutRes resId: Int): MultiStatusLayout {
        if (mLoadingLayoutId != id) {
            remove(mLoadingLayoutId)
            mLoadingLayoutId = id
        }
        return this
    }

    fun setEmptyLayout(@LayoutRes resId: Int): MultiStatusLayout{
        if (mEmptyLayoutId != resId) {
            remove(mEmptyLayoutId)
            mEmptyLayoutId = resId
        }
        return this
    }

    fun setErrorLayout(@LayoutRes resId: Int): MultiStatusLayout{
        if (mErrorLayoutId != resId) {
            remove(mErrorLayoutId)
            mErrorLayoutId = resId
        }
        return this
    }


    fun setEmptyImage(@DrawableRes resId: Int): MultiStatusLayout {
        mEmptyImage = resId
        setImageRes(mEmptyLayoutId, R.id.iv_page_empty, resId)
        return this
    }

    fun setEmptyText(value: String): MultiStatusLayout {
        mEmptyText = value
        setText(mEmptyLayoutId, R.id.tv_page_empty, value)
        return this
    }

    fun setErrorImage(@DrawableRes resId: Int): MultiStatusLayout {
        mErrorImage = resId
        setImageRes(mErrorLayoutId, R.id.iv_page_error, resId)
        return this
    }

    fun setErrorText(value: Int): MultiStatusLayout {
        mErrorText = resources.getString(value).toString()
        setText(mErrorLayoutId, R.id.tv_page_error, mErrorText!!)
        return this
    }
    fun setErrorText(value: String): MultiStatusLayout {
        mErrorText = value
        setText(mErrorLayoutId, R.id.tv_page_error, mErrorText!!)
        return this
    }

    fun setLoadingText(value: CharSequence): MultiStatusLayout {
        mLoadingText = value.toString()
        setText(mLoadingLayoutId, R.id.tv_page_loading, value)
        return this
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    fun setLoadingColor(@ColorInt color : Int) : MultiStatusLayout{
        mLoadingColor = color
        val layout = prepareLayout(mLoadingLayoutId)
        val pbLoading = layout.findViewById<View>(R.id.pb_loading) as ProgressBar
        val tvLoading = layout.findViewById<View>(R.id.tv_page_loading) as AppCompatTextView
        pbLoading.apply {
            indeterminateTintList = ColorStateList.valueOf(mLoadingColor)
        }
        tvLoading.apply {
            setTextColor(mLoadingColor)
        }
        return this
    }

    fun setRetryText(text: String): MultiStatusLayout {
        mRetryText = text
        setText(mErrorLayoutId, R.id.tv_page_error_retry, text)
        return this
    }

    fun setRetryTextSize(size : Int) : MultiStatusLayout{
        mRetryTextSize = size
        setTextSize(mErrorLayoutId, R.id.tv_page_error_retry, size)
        return this
    }

    fun setRetryTextColor(color : Int) : MultiStatusLayout{
        mRetryTextColor = color
        setTextColor(mErrorLayoutId, R.id.tv_page_error_retry, color)
        return this
    }

    fun setRetryBackground(@DrawableRes resId : Int) : MultiStatusLayout{
        mRetryBackground = resId
        val view = prepareLayout(mErrorLayoutId).findViewById(R.id.tv_page_error_retry) as AppCompatTextView
        view.apply {
            background = ContextCompat.getDrawable(context,resId)
        }
        return this
    }

    fun setTextColor(color : Int) : MultiStatusLayout{
        mTextColor = color
        setTextColor(mEmptyLayoutId, R.id.tv_page_empty, color)
        setTextColor(mErrorLayoutId, R.id.tv_page_error, color)
        return this
    }

    fun setTextSize(size : Int) : MultiStatusLayout{
        mTextSize = size
        setTextSize(mEmptyLayoutId, R.id.tv_page_empty, size)
        setTextSize(mErrorLayoutId, R.id.tv_page_error, size)
        return this
    }



    /**
     * 设置重试点击事件
     *
     * @param listener 重试点击事件
     */
    fun setOnRetryClickListener(listener: View.OnClickListener): MultiStatusLayout {
        mRetryListener = listener
        return this
    }

    /**
     * 设置文本
     * @param layoutId 布局id
     * @param viewId  layoutId中的ImageView id
     * @param resId 要设置的图片资源id
     */
    private fun setText(layoutId: Int, viewId: Int, value: CharSequence) {
        val view = prepareLayout(layoutId).findViewById(viewId) as AppCompatTextView
        view.apply {
            text = value
        }
    }

    private fun setTextColor(layoutId: Int, viewId: Int, color: Int) {
        val view = prepareLayout(layoutId).findViewById(viewId) as AppCompatTextView
        view.apply {
            setTextColor(ContextCompat.getColor(context,color))
        }
    }

    private fun setTextSize(layoutId: Int, viewId: Int, size: Int) {
        val view = prepareLayout(layoutId).findViewById(viewId) as AppCompatTextView
        view.apply {
            textSize = size.toFloat()
        }
    }

    /**
     * 设置ImageView图片
     * @param layoutId 布局id
     * @param ivID  layoutId中的ImageView id
     * @param resId 要设置的图片资源id
     */
    private fun setImageRes(layoutId: Int, viewId: Int, resId: Int) {
        if (mLayouts.containsKey(layoutId)) {
            val view = prepareLayout(layoutId).findViewById(viewId) as AppCompatImageView
            view?.apply {
                setImageResource(resId)
            }
        }
    }


    private fun dp2px(dp: Float): Int {
        return (resources.displayMetrics.density * dp).toInt()
    }

}