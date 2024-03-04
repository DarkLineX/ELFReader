package com.eeextend.elfreader

import com.xuexiang.xui.adapter.recyclerview.BaseRecyclerAdapter
import com.xuexiang.xui.adapter.recyclerview.RecyclerViewHolder
import com.xuexiang.xui.utils.ResUtils

class SimpleRecyclerAdapter(list: MutableCollection<FileInfo>?) : BaseRecyclerAdapter<FileInfo>(list) {
    override fun bindData(holder: RecyclerViewHolder, position: Int, item: FileInfo?) {
        holder.text(android.R.id.text1,item!!.typeName)
        holder.text(android.R.id.text2,item!!.typeValue)
        holder.textColorId(android.R.id.text2, com.xuexiang.xui.R.color.xui_config_color_light_blue_gray)
    }

    override fun getItemLayoutId(viewType: Int): Int {
        return android.R.layout.simple_list_item_2
    }
}