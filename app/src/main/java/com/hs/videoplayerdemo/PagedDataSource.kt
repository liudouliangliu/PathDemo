package com.hs.videoplayerdemo

import androidx.paging.PageKeyedDataSource

class PagedDataSource<T : Any>(private var dataLoader: PagedDataLoader<T>?) : PageKeyedDataSource<Int, T>() {

    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, T>) {
        dataLoader?.loadInitial(params, callback)
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, T>) {
        dataLoader?.loadAfter(params, callback)
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, T>) {
    }
}