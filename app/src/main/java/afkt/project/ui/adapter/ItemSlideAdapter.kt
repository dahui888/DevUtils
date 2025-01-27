package afkt.project.ui.adapter

import afkt.project.R
import afkt.project.databinding.AdapterMultiSelectBinding
import afkt.project.model.bean.CommodityEvaluateBean
import afkt.project.util.ProjectUtils
import android.view.ViewGroup
import dev.adapter.DevDataAdapter
import dev.base.adapter.DevBaseViewBindingVH
import dev.base.adapter.newBindingViewHolder
import dev.engine.DevEngine
import dev.utils.app.helper.view.ViewHelper
import dev.utils.common.BigDecimalUtils
import java.math.BigDecimal

/**
 * detail: Item Slide Adapter
 * @author Ttt
 */
class ItemSlideAdapter(data: List<CommodityEvaluateBean>) : DevDataAdapter<CommodityEvaluateBean, DevBaseViewBindingVH<AdapterMultiSelectBinding>>() {

    init {
        setDataList(data, false)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DevBaseViewBindingVH<AdapterMultiSelectBinding> {
        return newBindingViewHolder(parent, R.layout.adapter_multi_select)
    }

    override fun onBindViewHolder(
        holder: DevBaseViewBindingVH<AdapterMultiSelectBinding>,
        position: Int
    ) {
        val item = getDataItem(position)
        ViewHelper.get()
            .setVisibilitys(position == 0, holder.binding.vidAmsLine)
            .setText(item.commodityName, holder.binding.vidAmsNameTv)
            .setText(
                "￥${BigDecimalUtils.round(item.commodityPrice, 2, BigDecimal.ROUND_HALF_UP)}",
                holder.binding.vidAmsPriceTv
            )
        // 商品图片
        DevEngine.getImage()?.display(
            holder.binding.vidAmsIgview,
            item.commodityPicture,
            ProjectUtils.roundConfig3
        )
    }
}