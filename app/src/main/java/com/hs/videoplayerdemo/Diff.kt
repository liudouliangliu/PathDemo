package com.hs.list.adapter

import androidx.recyclerview.widget.DiffUtil
import com.hs.videoplayerdemo.BaseItem

/**
 * @author Yang Hao
 * @Date 2020-01-14
 */
class Diff<T : BaseItem> : DiffUtil.ItemCallback<T>() {

    override fun areItemsTheSame(item: T, item1: T) = item.myId == item1.myId

    override fun areContentsTheSame(item: T, item1: T) = item.myId == item1.myId
}