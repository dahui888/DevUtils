package afkt.project.ui.activity

import afkt.project.R
import afkt.project.base.app.BaseActivity
import afkt.project.base.config.RouterPath
import afkt.project.databinding.BaseViewRecyclerviewBinding
import afkt.project.model.bean.ItemBean
import afkt.project.ui.adapter.PagerSnapMAXAdapter
import android.os.Bundle
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.facade.annotation.Route
import dev.engine.DevEngine
import dev.utils.app.ListViewUtils
import dev.utils.app.helper.view.ViewHelper
import java.util.*

/**
 * detail: PagerSnapHelper - 无限滑动
 * @author Ttt
 * PagerSnapHelper : 每次滑动一页居中显示, 类似 ViewPager
 */
@Route(path = RouterPath.PagerSnapMAXActivity_PATH)
class PagerSnapMAXActivity : BaseActivity<BaseViewRecyclerviewBinding>() {

    private lateinit var adapter: PagerSnapMAXAdapter

    override fun baseLayoutId(): Int = R.layout.base_view_recyclerview

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val parent = binding.vidBvrRecy.parent as? ViewGroup
        ViewHelper.get().setPadding(0, parent)
    }

    override fun initValue() {
        super.initValue()

        val lists: MutableList<ItemBean> = ArrayList()
        for (i in 0..9) lists.add(ItemBean.newItemBeanPager())

        // 初始化布局管理器、适配器
        adapter = PagerSnapMAXAdapter(lists)
        binding.vidBvrRecy.layoutManager =
            LinearLayoutManager(this, RecyclerView.HORIZONTAL, false) // VERTICAL
        binding.vidBvrRecy.adapter = adapter
        val helper = PagerSnapHelper()
        helper.attachToRecyclerView(binding.vidBvrRecy)
        val size = lists.size
        // 滑动到中间 ( 无滑动过程 )
        (binding.vidBvrRecy.layoutManager as? LinearLayoutManager)?.scrollToPositionWithOffset(
            size * 100 - 1, 10
        )
        // 复位到中间
        ListViewUtils.smoothScrollToPosition(binding.vidBvrRecy, size * 100 + 1)
    }

    override fun initListener() {
        super.initListener()
        binding.vidBvrRecy.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(
                recyclerView: RecyclerView,
                newState: Int
            ) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    val layoutManager = recyclerView.layoutManager
                    if (layoutManager is LinearLayoutManager) {
                        // 获取最后一个可见 view 的位置
                        val lastItemPosition = layoutManager.findLastVisibleItemPosition()
                        // 获取第一个可见 view 的位置
                        val firstItemPosition = layoutManager.findFirstVisibleItemPosition()
                        // 获取居中索引
                        val currentPosition = (lastItemPosition + firstItemPosition) / 2
                        // 真实索引
                        val index = adapter.getRealIndex(currentPosition)

                        DevEngine.getLog()?.dTag(
                            TAG, "%s - %s 当前显示索引: %s - %s",
                            lastItemPosition,
                            firstItemPosition,
                            currentPosition,
                            index
                        )
                    }
                }
            }
        })
    }
}