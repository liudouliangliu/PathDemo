package com.hs.list.adapter

import android.view.View

/**
 * @author Yang Hao
 * @Date 2020/7/21
 */
interface OnItemClickListener<T> {
    fun itemClicked(view: View, item: T, position: Int)
}