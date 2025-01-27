package afkt.project.ui.activity

import afkt.project.R
import afkt.project.base.app.BaseActivity
import afkt.project.base.config.RouterPath
import afkt.project.databinding.BaseViewRecyclerviewBinding
import afkt.project.model.bean.ItemBean
import afkt.project.model.bean.ItemBean.Companion.newItemBean
import afkt.project.ui.adapter.LinearSnapAdapter
import android.os.Bundle
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.facade.annotation.Route
import dev.utils.app.helper.view.ViewHelper
import java.util.*

/**
 * detail: LinearSnapHelper - RecyclerView
 * @author Ttt
 * LinearSnapHelper : 滑动多页居中显示, 类似 Gallery
 */
@Route(path = RouterPath.LinearSnapActivity_PATH)
class LinearSnapActivity : BaseActivity<BaseViewRecyclerviewBinding>() {

    override fun baseLayoutId(): Int = R.layout.base_view_recyclerview

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val parent = binding.vidBvrRecy.parent as? ViewGroup
        // 根布局处理
        ViewHelper.get().setPadding(0, parent)
    }

    override fun initValue() {
        super.initValue()

        val lists: MutableList<ItemBean> = ArrayList()
        for (i in 0..9) lists.add(newItemBean())

        // 初始化布局管理器、适配器
        binding.vidBvrRecy.layoutManager =
            LinearLayoutManager(this, RecyclerView.HORIZONTAL, false) // VERTICAL
        binding.vidBvrRecy.adapter = LinearSnapAdapter(lists)
        val helper = LinearSnapHelper()
        helper.attachToRecyclerView(binding.vidBvrRecy)
    }
}