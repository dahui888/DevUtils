package afkt.project.ui.activity

import afkt.project.R
import afkt.project.base.app.BaseActivity
import afkt.project.base.config.RouterPath
import afkt.project.databinding.ActivityWaveViewBinding
import android.graphics.Color
import android.widget.SeekBar
import com.alibaba.android.arouter.facade.annotation.Route
import dev.utils.app.BarUtils
import dev.utils.app.ResourceUtils
import dev.widget.ui.WaveView
import dev.widget.utils.WaveHelper

/**
 * detail: 波浪 View
 * @author Ttt
 */
@Route(path = RouterPath.WaveViewActivity_PATH)
class WaveViewActivity : BaseActivity<ActivityWaveViewBinding>() {

    val helper: WaveHelper by lazy {
        val lazyObj = WaveHelper.get(binding.vidAwvWave)
        // 通过属性动画进行设置波浪 View 动画效果
        lazyObj.buildPropertyAnimation(
            WaveHelper.WaveProperty.Builder()
                // 设置水位高度属性值
                .setWaterLevelRatio(
                    0f, 0.7f, 10000L
                ).build()
        )
        lazyObj
    }

    override fun baseLayoutId(): Int = R.layout.activity_wave_view

    override fun onPause() {
        super.onPause()
        helper.cancel()
    }

    override fun onResume() {
        super.onResume()
        helper.start()
    }

    override fun initView() {
        super.initView()

        toolbar?.let { bar ->
            val rgb = ResourceUtils.getColor(R.color.color_55)
            bar.setBackgroundColor(rgb)
            BarUtils.addMarginTopEqualStatusBarHeight(bar)
            BarUtils.setStatusBarColor(mActivity, rgb)
        }
    }

    override fun initListener() {
        super.initListener()

        // 设置波浪外形形状
        binding.vidAwvShapeChoice.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.vid_awv_shape_circle -> helper.shapeType = WaveView.ShapeType.CIRCLE
                R.id.vid_awv_shape_square -> helper.shapeType = WaveView.ShapeType.SQUARE
            }
        }

        // 设置波浪边框信息
        binding.vidAwvSeekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(
                seekBar: SeekBar?,
                progress: Int,
                fromUser: Boolean
            ) {
                helper.setBorder(progress.toFloat(), helper.borderColor)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })

        // 设置波浪颜色
        binding.vidAwvColorChoice.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.vid_awv_color_default -> {
                    helper.setBorder(
                        helper.borderWidth, WaveView.DEFAULT_BORDER_COLOR
                    ).setWaveColor(
                        WaveView.DEFAULT_BEHIND_WAVE_COLOR,
                        WaveView.DEFAULT_FRONT_WAVE_COLOR,
                    )
                }
                R.id.vid_awv_color_red -> {
                    helper.setBorder(
                        helper.borderWidth, Color.parseColor("#44f16d7a")
                    ).setWaveColor(
                        Color.parseColor("#28f16d7a"),
                        Color.parseColor("#3cf16d7a")
                    )
                }
                R.id.vid_awv_color_green -> {
                    helper.setBorder(
                        helper.borderWidth, Color.parseColor("#B0b7d28d")
                    ).setWaveColor(
                        Color.parseColor("#40b7d28d"),
                        Color.parseColor("#80b7d28d")
                    )
                }
                R.id.vid_awv_color_blue -> {
                    helper.setBorder(
                        helper.borderWidth, Color.parseColor("#b8f1ed")
                    ).setWaveColor(
                        Color.parseColor("#88b8f1ed"),
                        Color.parseColor("#b8f1ed")
                    )
                }
            }
        }
    }
}