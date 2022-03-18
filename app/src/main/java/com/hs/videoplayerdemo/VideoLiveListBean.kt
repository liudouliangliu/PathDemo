package com.hs.videoplayerdemo

data class VideoLiveListBeanItem(
    var current: String,
    var pages: String,
    var searchCount: Boolean,
    var size: String,
    var total: String,
    var records: ArrayList<RecordsBean>
):BaseItem()
data class RecordsBean(
    var areaCode: String,
    var busCode: String,
    var busName: String,
    var channelId: String,
    var companyCode: String,
    var deviceId: String,
    var name: String,
    var onLine: String,
    var operator: String,
    var operatorTime: String,
    var remark: String,
    var state: String,
    var rtmp:String?,
    var id: String
):BaseItem()