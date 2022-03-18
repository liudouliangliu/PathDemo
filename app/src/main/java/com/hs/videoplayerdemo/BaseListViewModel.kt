package com.hs.videoplayerdemo

import androidx.annotation.NonNull
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PageKeyedDataSource
import androidx.paging.PagedList

abstract class BaseListViewModel<T : Any> : ViewModel(), PagedDataLoader<T> {

    open fun pageSize(): Int = 10

    private val data = ArrayList<T>()

    companion object {
        private const val TAG = "BaseListViewModel"
    }

    private val dataSourceFactory: DataSourceFactory<T> by lazy {
        DataSourceFactory(this)
    }

    private val loadLiveData = LivePagedListBuilder(dataSourceFactory, pageSize()).build()

    private val refreshLiveData: MutableLiveData<RefreshResult> = MutableLiveData()

    private val loadMoreLiveData: MutableLiveData<RefreshResult> by lazy { MutableLiveData<RefreshResult>() }

    private val notifyItemLiveData: MutableLiveData<Pair<Int, Any?>> by lazy { MutableLiveData<Pair<Int, Any?>>() }

    private val removeItemLiveData: MutableLiveData<Int> by lazy { MutableLiveData<Int>() }

    fun invalidate() {
        dataSourceFactory.sourceLiveData.value?.invalidate()
    }

    fun observeDataObserver(@NonNull owner: LifecycleOwner, data: (PagedList<T>) -> Unit, refreshResult: (RefreshResult) -> Unit, loadMoreResult: (RefreshResult) -> Unit) {
        loadLiveData.observe(owner) {
            it?.apply(data)
        }
        refreshLiveData.observe(owner) {
            refreshResult(it!!)
        }
        loadMoreLiveData.observe(owner) {
            loadMoreResult(it!!)
        }
    }

    fun observeAdapterObserver(@NonNull owner: LifecycleOwner, notifyItem: (Int, Any?) -> Unit, removeItem: (Int) -> Unit) {
        notifyItemLiveData.observe(owner) {
            it?.apply { notifyItem(first, second) }
        }
        removeItemLiveData.observe(owner) {
            removeItem(it!!)
        }
    }

    final override fun loadInitial(params: PageKeyedDataSource.LoadInitialParams<Int>, callback: PageKeyedDataSource.LoadInitialCallback<Int, T>) {
        refresh()
//        KLog.d(TAG, "loadInitial")
        data.clear()
        loadData(1) {
            when {
                it == null -> refreshLiveData.value = RefreshResult.FAILED
                it.isEmpty() -> refreshLiveData.value = RefreshResult.NO_DATA
                it.size < pageSize() -> {
                    data.addAll(it)
                    callback.onResult(it, null, null)
                    refreshLiveData.value = RefreshResult.NO_MORE
                }
                else -> {
//                    KLog.d(TAG, "loadInitial size = ${it.size}")
                    data.addAll(it)
                    callback.onResult(it, null, 2)
                    refreshLiveData.value = RefreshResult.SUCCEED
                }
            }
        }
    }

    final override fun loadAfter(params: PageKeyedDataSource.LoadParams<Int>, callback: PageKeyedDataSource.LoadCallback<Int, T>) {
        loadMore()
//        KLog.d(TAG, "loadAfter--${params.key}")
        loadData(params.key) {
            when {
                it == null -> loadMoreLiveData.value = RefreshResult.FAILED
                it.size < pageSize() -> {
                    data.addAll(it)
                    callback.onResult(it, null)
                    loadMoreLiveData.value = RefreshResult.NO_MORE
                }
                else -> {
                    data.addAll(it)
                    callback.onResult(it, params.key + 1)
                    loadMoreLiveData.value = RefreshResult.SUCCEED
                }
            }
        }
    }

    fun notifyItem(position: Int) {
        notifyItemLiveData.value = Pair(position, null)
    }

    fun notifyItem(position: Int, payload: Any?) {
        notifyItemLiveData.value = Pair(position, payload)
    }

    private fun removeItem(position: Int) {}

    override fun refresh() {}

    override fun loadMore() {}

    abstract fun loadData(page: Int, onResponse: (ArrayList<T>?) -> Unit)
}
