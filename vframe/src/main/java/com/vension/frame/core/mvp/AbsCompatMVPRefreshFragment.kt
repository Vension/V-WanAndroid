package com.vension.frame.core.mvp

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.vension.frame.R
import com.vension.frame.VFrame
import com.vension.frame.adapter.recy.decoration.SpaceItemDecoration
import com.vension.frame.ext.showToast
import com.vension.frame.utils.NetWorkUtil
import com.vension.frame.views.MultiStatusLayout


/**
 * ========================================================
 * 作  者：Vension
 * 日  期：2018/10/29 12:21
 * 描  述：带 MVP 的列表 Fragment - 基类
 *
 *       repeat：函数是一个单独的函数,repeat(3) { block }->就是循环执行多少次block中内容
 *       with：  函数也是一个单独的函数，并不是Kotlin中的extension，指定的T作为闭包的receiver，使用参数中闭包的返回结果
 *       let:    首先let()的定义是这样的，默认当前这个对象作为闭包的it参数，返回值是函数里面最后一行，或者指定return
 *       apply： 函数是这样的，调用某对象的apply函数，在函数范围内，可以任意调用该对象的任意方法，并返回该对象
 *       run：   run函数和apply函数很像，只不过run函数是使用最后一行的返回，apply返回当前自己的对象。
 *       also:   执行block，返回this，
 *       takeIf: 满足block中条件，则返回当前值，否则返回null，block的返回值Boolean类型
 *       takeUnless : 和takeIf相反，如不满足block中的条件，则返回当前对象，否则为null
 *
 * ========================================================
 */
@SuppressLint("RestrictedApi")
abstract class AbsCompatMVPRefreshFragment<data,in V: IViewRefresh<data>,P : IPresenter<V>>
    : AbsCompatMVPFragment<V, P>(),
    SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener, IViewRefresh<data> {

    private lateinit var mRefreshSwipeRefreshLayout: SwipeRefreshLayout
    private lateinit var mRefreshMultiStatusLayout: MultiStatusLayout
    private lateinit var mRefreshRecyclerView: RecyclerView
    private lateinit var mRefreshFloatingActionButton: FloatingActionButton
//    private val mRefreshSwipeRefreshLayout: SwipeRefreshLayout by bindView(R.id.refresh_SwipeRefreshLayout)
//    private val mRefreshMultiStateLayout: MultiStateLayout by bindView(R.id.refresh_MultiStateLayout)
//    private val mRefreshRecyclerView: RecyclerView by bindView(R.id.refresh_RecyclerView)
//    private val mRefreshFloatingActionButton: FloatingActionButton by bindView(R.id.refresh_fab_top)

    private var page: Int = 0 //页数
    private val limit: Int = 10 //条目数

    protected val mRecyAdapter: BaseQuickAdapter<data, BaseViewHolder> by lazy {
        createRecyAdapter()
    }

    /**
     * create RecyclerView.LayoutManager
     */
    private val recyLayoutManager: RecyclerView.LayoutManager by lazy {
        createRecyLayoutManager()
    }

    /**
     * createRecyclerView.ItemDecoration
     */
    private val recyItemDecoration : RecyclerView.ItemDecoration by lazy {
        createRecyItemDecoration()
    }

    /**
     * 创建adapter
     */
    abstract fun createRecyAdapter(): BaseQuickAdapter<data, BaseViewHolder>
    /**
     * adapter 的点击事件处理
     */
    abstract fun addItemClickListener(mAdapter : BaseQuickAdapter<data, BaseViewHolder>)
    /**
     * 请求Api数据
     */
    abstract fun onTargetRequestApi(isRefresh: Boolean, page: Int, limit: Int)

    /**
     * 创建RecyclerView 的 LayoutManager
     * @return  默认LinearLayoutManager
     */
    open fun createRecyLayoutManager(): RecyclerView.LayoutManager {
        return LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
    }
    /**
     * 创建RecyclerView.ItemDecoration
     * @return  默认 SpaceItemDecoration
     */
    open fun createRecyItemDecoration(): RecyclerView.ItemDecoration {
        return SpaceItemDecoration(activity!!)
    }
    /**
     * 对mRecyAdapter item 的 childView 的点击监听
     */
    open fun addItemChildClickListener(mAdapter : BaseQuickAdapter<data, BaseViewHolder>) {}

    /**
     * 是否开启加载更多
     *
     * @return false
     */
    open fun enableLoadMore(): Boolean {
        return true
    }


    override fun attachLayoutRes(): Int {
        return R.layout.fragment_base_refresh
    }

    override fun initViewAndData(view: View, savedInstanceState: Bundle?) {
        //initViewAndData 实现了Presenter的attachView方法,一定要super
        //设置多页面布局
        super.initViewAndData(view, savedInstanceState)
        mRefreshSwipeRefreshLayout = findViewById(R.id.refresh_SwipeRefreshLayout)
        mRefreshMultiStatusLayout = findViewById(R.id.refresh_MultiStatusLayout)
        mRefreshRecyclerView = findViewById(R.id.refresh_RecyclerView)
        mRefreshFloatingActionButton = findViewById(R.id.refresh_fab_top)

        fraMultiStatusLayout = mRefreshMultiStatusLayout

        //初始化刷新控件
        mRefreshSwipeRefreshLayout.run {
            setColorSchemeColors(VFrame.getColor(R.color.colorAppTheme))
            setOnRefreshListener(this@AbsCompatMVPRefreshFragment)
        }

        //初始化RecyclerView
        mRefreshRecyclerView.run {
            setHasFixedSize(true)
            layoutManager = recyLayoutManager
            adapter = mRecyAdapter
            itemAnimator = DefaultItemAnimator()
            recyItemDecoration?.let { addItemDecoration(it) }
        }


        //悬浮按钮点击监听
        mRefreshFloatingActionButton.run {
            setOnClickListener(this@AbsCompatMVPRefreshFragment)
        }

        mRecyAdapter.run {
            bindToRecyclerView(mRefreshRecyclerView)
            openLoadAnimation(BaseQuickAdapter.SLIDEIN_BOTTOM)
            setEnableLoadMore(enableLoadMore())
            if (enableLoadMore()){
                setOnLoadMoreListener(this@AbsCompatMVPRefreshFragment, mRefreshRecyclerView)
            }
            addItemClickListener(this)
            addItemChildClickListener(this)
        }
    }

    /**
     *  加载数据
     */
    override fun lazyLoadData() {
        mRefreshSwipeRefreshLayout.postDelayed({
            mRefreshSwipeRefreshLayout.isRefreshing = true
            onRefresh()
        },300)
    }


    /**
     * 因为showEmpty()或shouContent()在下面{@link setRefreshData() @link setMoreData()}
     * 中已经实现，故这里需要重写{@link hideLoading()}去掉里面的super()引用
     * 这是为了解决用RxJava 处理网络数据回调时onNext()已经回调了结果，但是onComplete()最后执行
     * 导致设置完数据之后已经调用showEmpty()后有执行了hideLoading()里面的showContent(),
     * 使得最终空布局没显示出来
     */
    override fun hideLoading() {
    }


    /**
     * 下拉刷新数据
     */
    override fun onRefresh() {
        if (isCheckNet()){
            if (!NetWorkUtil.isNetworkAvailable(context!!)){
                fraMultiStatusLayout?.let {
                    it
                        .setErrorImage(R.drawable.img_default_no_net)
                        .setErrorText(R.string.page_not_network)
                        .showError()
                }
                stopRefresh(true)
                return
            }
        }
        page = 0
        mRecyAdapter.setEnableLoadMore(false)//这里的作用是防止下拉刷新的时候还可以上拉加载
        onTargetRequestApi(true,page,limit)
    }

    /**
     * 加载更多
     */
    override fun onLoadMoreRequested() {
        stopRefresh(false)
        onTargetRequestApi(false, ++page, limit)
    }


    override fun onClick(v: View?) {
        super.onClick(v)
        when(v?.id){
            R.id.refresh_fab_top-> {
                scrollToTop()
            }
        }
    }


    override fun setRefreshData(listData: MutableList<data>) {
        setRefreshData(listData,null,null)
    }


    override fun setRefreshData(listData: MutableList<data>, headers: List<View>?, footers: List<View>?) {
        //设置刷新数据
        stopRefresh(true)
        mRecyAdapter.run {
            setEnableLoadMore(true)
            removeAllHeaderView()
            removeAllFooterView()

            //添加头部
            if (!headers.isNullOrEmpty()){
                for (view in headers) {
                    addHeaderView(view)
                }
            }

            //添加尾部
            if (!footers.isNullOrEmpty()){
                for (view in footers) {
                    addFooterView(view)
                }
            }

            //添加内容
            if (headers.isNullOrEmpty() && footers.isNullOrEmpty() && listData.isNullOrEmpty()){
                fraMultiStatusLayout?.showEmpty()
            } else {
                fraMultiStatusLayout?.showContent()
                replaceData(listData)
                val size = listData.size
                if (size < limit) {
                    loadMoreEnd()//没有更多了
                } else {
                    loadMoreComplete()//加载更多完成
                }
            }
        }
    }

    override fun setMoreData(listData: MutableList<data>) {
        //设置下一页数据
        stopRefresh(true)
        fraMultiStatusLayout?.showContent()
        if (listData.isNullOrEmpty()) {
            showToast("没有更多了")
            mRecyAdapter.loadMoreEnd()//没有更多了
        }else{
            mRecyAdapter.run {
                addData(listData)
                val size = listData.size
                if (size < limit) {
                    loadMoreEnd()//没有更多了
                } else {
                    loadMoreComplete()//加载更多完成
                }
            }
        }
    }

    /**
     * 获取数据失败
     */
    override fun loadFail() {
        mRefreshSwipeRefreshLayout.run {
            isRefreshing = false
        }
        mRecyAdapter.run {
            if (page == 0){
                setEnableLoadMore(true)
            }else{
                mRefreshSwipeRefreshLayout.isEnabled = true //加载更多时不能同时下拉刷新
                loadMoreFail()
            }
        }
    }

    /**取消刷新动画*/
    fun stopRefresh(enabled: Boolean) {
        mRefreshSwipeRefreshLayout.run {
            isRefreshing = false
            isEnabled = enabled //加载更多时不能同时下拉刷新
        }
    }

    private fun scrollToTop(){
        mRefreshRecyclerView.run {
            if (mRecyAdapter.data.size > limit) {
                scrollToPosition(0)
            } else {
                smoothScrollToPosition(0)
            }
            mRefreshFloatingActionButton.visibility = View.INVISIBLE
        }
    }

}