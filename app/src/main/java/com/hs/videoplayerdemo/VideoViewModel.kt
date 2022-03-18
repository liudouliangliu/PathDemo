package com.hs.videoplayerdemo

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class VideoViewModel: BaseListViewModel<RecordsBean>() {

    open var param:VideoListParam = VideoListParam()

    override fun loadData(page: Int, onResponse: (ArrayList<RecordsBean>?) -> Unit) {
        param.current = page
        val data:ArrayList<RecordsBean> = arrayListOf()
        for (index in 0..10){
            val bean:RecordsBean = RecordsBean("areaCode",
                "busCode",
                "busName",
                "channelId","companyCode","deviceId","name","onLine","operator","operatorTime",
                "remark","state","https://stream7.iqilu.com/10339/article/202002/17/778c5884fa97f460dac8d90493c451de.mp4","id")
            data.add(bean)
        }
        GlobalScope.launch(Dispatchers.Main){
            onResponse(data)
        }
        Log.d("VideoViewModel", "json loadData ")
    }

}