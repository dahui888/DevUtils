package afkt.project.ui.adapter;

import android.support.annotation.Nullable;
import android.view.View;

import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.LinkedHashMap;
import java.util.List;

import afkt.project.R;
import afkt.project.model.bean.CommodityEvaluateBean;
import afkt.project.ui.widget.BaseImageView;
import dev.assist.multiselect.MultiSelectMapAssist;
import dev.assist.multiselect.edit.IMultiSelectEdit;
import dev.other.GlideUtils;
import dev.utils.app.ResourceUtils;
import dev.utils.app.ViewUtils;
import dev.utils.app.helper.ViewHelper;
import dev.utils.common.BigDecimalUtils;
import dev.utils.common.DevCommonUtils;

/**
 * detail: 多选 Adapter
 * @author Ttt
 */
public class MultiSelectAdapter extends BaseQuickAdapter<CommodityEvaluateBean, BaseViewHolder> implements IMultiSelectEdit {

    // 圆角 RequestOptions
    private RequestOptions requestOptions;
    // 多选辅助类
    private MultiSelectMapAssist<Integer, CommodityEvaluateBean> multiSelectMapAssist = new MultiSelectMapAssist();

    public MultiSelectAdapter(@Nullable List<CommodityEvaluateBean> data) {
        super(R.layout.adapter_multi_select, data);
        // 获取默认 RequestOptions
        requestOptions = GlideUtils.defaultOptions();
        // 设置圆角, 使用 RoundedCorners 图片不会闪烁
        requestOptions.transform(new RoundedCorners((int) ResourceUtils.getDimension(R.dimen.un_radius)));
    }

    @Override
    protected void convert(BaseViewHolder helper, CommodityEvaluateBean item) {
        // 当前索引
        int position = helper.getLayoutPosition();
        // 判断是否显示边距
        ViewUtils.setVisibility(position == 0, helper.getView(R.id.vid_ams_line));

        // ============
        // = 商品信息 =
        // ============

        // 商品名
        helper.setText(R.id.vid_ams_name_tv, item.commodityName);
        // 商品价格
        helper.setText(R.id.vid_ams_price_tv,
                "￥" + BigDecimalUtils.round(item.commodityPrice, 2));
        // 商品图片
        GlideUtils.with().displayImage(item.commodityPicture, helper.getView(R.id.vid_ams_pic_igview), requestOptions);

        // ============
        // = 多选处理 =
        // ============

        BaseImageView vid_ams_igview = helper.getView(R.id.vid_ams_igview);
        // 是否显示编辑按钮、以及是否选中
        ViewHelper.get().setVisibility(isEditStatus(), vid_ams_igview)
                .setSelected(multiSelectMapAssist.isSelectKey(position), vid_ams_igview)
                .setOnClicks(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!isEditStatus()) return;
                        // 反选处理
                        multiSelectMapAssist.toggle(position, item);
                        // 设置是否选中
                        ViewUtils.setSelected(multiSelectMapAssist.isSelectKey(position), vid_ams_igview);
                        // 触发回调
                        if (selectListener != null) {
                            selectListener.onClickSelect(position, multiSelectMapAssist.isSelectKey(position));
                        }
                    }
                }, helper.getView(R.id.vid_ams_linear));
    }

    /**
     * 获取全选辅助类
     * @return {@link MultiSelectMapAssist}
     */
    public MultiSelectMapAssist getmultiSelectMapAssist() {
        return multiSelectMapAssist;
    }

    // ====================
    // = IMultiSelectEdit =
    // ====================

    // 判断是否编辑
    private boolean isEdit;

    @Override
    public boolean isEditStatus() {
        return isEdit;
    }

    @Override
    public void setEditStatus(boolean isEdit) {
        this.isEdit = isEdit;
        // 刷新适配器
        notifyDataSetChanged();
    }

    @Override
    public void toggleEditStatus() {
        // 切换选择状态
        setEditStatus(!isEdit);
    }

    @Override
    public void selectAll() {
        LinkedHashMap<Integer, CommodityEvaluateBean> linkedHashMap = new LinkedHashMap<>();
        for (int i = 0, len = getData().size(); i < len; i++) {
            linkedHashMap.put(i, getData().get(i));
        }
        multiSelectMapAssist.putSelects(linkedHashMap);
        // 刷新适配器
        notifyDataSetChanged();
    }

    @Override
    public void clearSelectAll() {
        // 清空选中
        multiSelectMapAssist.clearSelects();
        // 刷新适配器
        notifyDataSetChanged();
    }

    @Override
    public void inverseSelect() {
        // 获取目前选中的数据
        List<Integer> listKeys = multiSelectMapAssist.getSelectKeys();

        // 全选数据
        LinkedHashMap<Integer, CommodityEvaluateBean> linkedHashMap = new LinkedHashMap<>();
        for (int i = 0, len = getData().size(); i < len; i++) {
            linkedHashMap.put(i, getData().get(i));
        }
        multiSelectMapAssist.putSelects(linkedHashMap);

        // 反选处理
        if (!listKeys.isEmpty()) {
            for (Integer key : listKeys) {
                multiSelectMapAssist.unselect(key);
            }
        }
        // 刷新适配器
        notifyDataSetChanged();
    }

    @Override
    public boolean isSelectAll() {
        int size = multiSelectMapAssist.getSelectSize();
        if (size == 0) return false;
        // 判断数量是否一致
        return (DevCommonUtils.length(getData()) == multiSelectMapAssist.getSelectSize());
    }

    @Override
    public boolean isSelect() {
        return multiSelectMapAssist.isSelect();
    }

    @Override
    public boolean isNotSelect() {
        return !multiSelectMapAssist.isSelect();
    }

    // ================
    // = 操作监听事件 =
    // ================

    // 选择事件通知事件
    private OnSelectListener selectListener;

    /**
     * 设置选择事件通知事件
     * @param selectListener {@link OnSelectListener}
     * @return {@link MultiSelectAdapter}
     */
    public MultiSelectAdapter setSelectListener(OnSelectListener selectListener) {
        this.selectListener = selectListener;
        return this;
    }

    /**
     * detail: 选择事件通知事件
     * @author Ttt
     */
    public interface OnSelectListener {

        /**
         * 点击选中切换
         * @param position 对应的索引
         * @param now      新的状态
         */
        void onClickSelect(int position, boolean now);

    }
}