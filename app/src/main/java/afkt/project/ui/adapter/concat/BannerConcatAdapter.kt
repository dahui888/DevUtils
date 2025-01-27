package afkt.project.ui.adapter.concat

import afkt.project.R
import afkt.project.databinding.AdapterConcatBannerBinding
import afkt.project.databinding.AdapterConcatBannerImageBinding
import afkt.project.model.bean.BannerBean
import afkt.project.util.ProjectUtils
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.youth.banner.adapter.BannerAdapter
import com.youth.banner.indicator.CircleIndicator
import dev.engine.DevEngine

/**
 * detail: Banner Adapter
 * @author Ttt
 */
class BannerConcatAdapter(
    val owner: LifecycleOwner,
    private val bannerLists: List<BannerBean>
) : RecyclerView.Adapter<BannerConcatAdapter.ItemHolder>() {

    var context: Context? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ItemHolder {
        context = parent.context

        return ItemHolder(
            AdapterConcatBannerBinding.inflate(
                LayoutInflater.from(context),
                parent, false
            )
        )
    }

    override fun onBindViewHolder(
        holder: ItemHolder,
        position: Int
    ) {
        holder.binding.vidAcbBanner.setAdapter(
            object : BannerAdapter<BannerBean, BannerViewHolder>(bannerLists) {
                override fun onCreateHolder(
                    parent: ViewGroup,
                    viewType: Int
                ): BannerViewHolder {
                    return BannerViewHolder(
                        AdapterConcatBannerImageBinding.inflate(
                            LayoutInflater.from(parent.context),
                            parent, false
                        )
                    )
                }

                override fun onBindView(
                    holder: BannerViewHolder,
                    data: BannerBean,
                    position: Int,
                    size: Int
                ) {
                    DevEngine.getImage()?.display(
                        holder.binding.vidAcbiIgview,
                        data.imageUrl,
                        ProjectUtils.roundConfig10
                    )
                }
            }
        ).addBannerLifecycleObserver(owner)
            .indicator = CircleIndicator(context)
    }

    override fun getItemCount(): Int {
        return 1
    }

    override fun getItemViewType(position: Int): Int {
        return R.layout.adapter_concat_banner
    }

    class ItemHolder(val binding: AdapterConcatBannerBinding) : RecyclerView.ViewHolder(
        binding.root
    )

    class BannerViewHolder(val binding: AdapterConcatBannerImageBinding) : RecyclerView.ViewHolder(
        binding.root
    )
}