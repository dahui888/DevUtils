package afkt.project.ui.activity

import afkt.project.R
import afkt.project.base.app.BaseActivity
import afkt.project.base.config.RouterPath
import afkt.project.databinding.BaseViewRecyclerviewBinding
import afkt.project.ui.adapter.TextAdapter
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import com.alibaba.android.arouter.facade.annotation.Route
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import dev.base.widget.BaseTextView
import dev.utils.app.ResourceUtils
import dev.utils.app.SizeUtils
import dev.utils.app.helper.quick.QuickHelper
import dev.utils.app.helper.view.ViewHelper
import dev.utils.common.ChineseUtils
import dev.utils.common.RandomUtils
import java.util.*

/**
 * detail: Flexbox LayoutManager
 * @author Ttt
 * Google FlexboxLayout 推荐使用该库, 支持 RecyclerView ( FlexboxLayoutManager )
 * @see https://github.com/google/flexbox-layout
 * Android 可伸缩布局 FlexboxLayout ( 支持 RecyclerView 集成 )
 * @see https://juejin.im/post/58d1035161ff4b00603ca9c4
 */
@Route(path = RouterPath.FlexboxLayoutManagerActivity_PATH)
class FlexboxLayoutManagerActivity : BaseActivity<BaseViewRecyclerviewBinding>() {

    override fun baseLayoutId(): Int = R.layout.base_view_recyclerview

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val parent = binding.vidBvrRecy.parent as? ViewGroup
        ViewHelper.get().setPadding(
            0, 0, SizeUtils.dipConvertPx(5F),
            SizeUtils.dipConvertPx(5F), parent
        )

        val view = QuickHelper.get(BaseTextView(this))
            .setText("刷新")
            .setBold()
            .setTextColors(ResourceUtils.getColor(R.color.red))
            .setTextSizeBySp(15.0f)
            .setPaddingLeft(30)
            .setPaddingRight(30)
            .setOnClick { initValue() }.getView<View>()
        toolbar?.addView(view)
    }

    override fun initValue() {
        super.initValue()

        var lists = mutableListOf<String>()
        for (i in 1..20) {
            val text = ChineseUtils.randomWord(RandomUtils.getRandom(8)) +
                    RandomUtils.getRandomLetters(RandomUtils.getRandom(8))
            val randomText =
                i.toString() + "." + RandomUtils.getRandom(text.toCharArray(), text.length)
            lists.add(randomText)
        }

        val layoutManager = FlexboxLayoutManager(this)
        layoutManager.flexDirection = FlexDirection.ROW
        layoutManager.justifyContent = JustifyContent.FLEX_START

        binding.vidBvrRecy.adapter = TextAdapter(lists)
        binding.vidBvrRecy.layoutManager = layoutManager
    }
}