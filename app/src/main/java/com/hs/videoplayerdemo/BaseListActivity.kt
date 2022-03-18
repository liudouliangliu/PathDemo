package com.hs.videoplayerdemo

import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.hs.list.adapter.OnItemClickListener
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import java.lang.reflect.ParameterizedType
/**
 * @author Yang Hao
 * @date 2020/11/18
 */
abstract class BaseListActivity<T : BaseItem, VM : BaseListViewModel<T>> : BaseActivity(), OnItemClickListener<T> {

    val viewModel: VM by lazy {
        val parameterizedType = javaClass.genericSuperclass as ParameterizedType
        val cla = parameterizedType.actualTypeArguments[1] as Class<VM>

        ViewModelProvider(this).get(cla)
    }

    private var refreshLayout: SmartRefreshLayout? = null
    private var emptyView: EmptyView? = null

    private lateinit var recyclerView: RecyclerView

    override fun getLayoutId() = R.layout.activity_base_list

    override fun initView() {
        refreshLayout = findViewById(R.id.baseRefreshLayout)
        recyclerView = findViewById(R.id.baseRecyclerView)
        emptyView = findViewById(R.id.baseEmptyView)
        val adapter = adapter()
        adapter.itemClickListener = this
        recyclerView.init(adapter)
        refreshLayout?.setOnRefreshListener {
            viewModel.invalidate()
        }
    }

    override fun initData() {
        viewModel.observeDataObserver(this,
            { adapter().submitList(null)
                adapter().submitList(it) },
            { refreshFinished(it) },
            { loadMoreFinished(it) })

        viewModel.observeAdapterObserver(this,
            { position, payload ->
                adapter().notifyItemChanged(position, payload)
            },
            {
            })
    }

    override fun itemClicked(view: View, item: T, position: Int) {

    }

    open fun refreshFinished(result: RefreshResult) {
        refreshLayout?.finishRefresh()
        emptyView?.apply {
            state = when (result) {
                RefreshResult.SUCCEED -> EmptyView.Status.DISMISS
                RefreshResult.FAILED -> EmptyView.Status.LOAD_FAILED
                RefreshResult.NO_DATA -> EmptyView.Status.NO_DATA
                RefreshResult.NO_MORE -> EmptyView.Status.DISMISS
            }
        }
    }

    private fun loadMoreFinished(result: RefreshResult) {
        refreshLayout?.finishLoadMore()
        when (result) {
            RefreshResult.SUCCEED -> { }
            RefreshResult.FAILED -> { }
            RefreshResult.NO_MORE -> { }
            else -> { }
        }
    }

    abstract fun adapter(): BasePagedAdapter<T>

}