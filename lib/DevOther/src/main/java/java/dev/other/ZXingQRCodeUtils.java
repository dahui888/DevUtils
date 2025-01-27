package java.dev.other;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.util.EnumMap;
import java.util.Map;

import dev.utils.DevFinal;
import dev.utils.LogPrintUtils;
import dev.utils.common.thread.DevThreadManager;

/**
 * detail: ZXing 二维码工具类
 * @author Ttt
 * @deprecated 推荐使用 {@link dev.engine.barcode.DevBarCodeEngine} 实现类
 */
@Deprecated
public final class ZXingQRCodeUtils {

    private ZXingQRCodeUtils() {
    }

    // 日志 TAG
    private static final String TAG = ZXingQRCodeUtils.class.getSimpleName();

    // ============
    // = 生成二维码 =
    // ============

    /**
     * detail: 二维码编码 ( 生成 ) 回调
     * @author Ttt
     */
    public interface QREncodeCallback {

        /**
         * 二维码编码 ( 生成 ) 回调
         * @param success 是否成功
         * @param bitmap  成功图片
         * @param error   异常信息
         */
        void onResult(
                boolean success,
                Bitmap bitmap,
                Throwable error
        );
    }

    // =

    /**
     * 编码 ( 生成 ) 二维码图片
     * @param content 生成内容
     * @param size    图片宽高大小 ( 正方形 px )
     */
    public static void encodeQRCode(
            final String content,
            final int size
    ) {
        encodeQRCode(content, size, null, null);
    }

    /**
     * 编码 ( 生成 ) 二维码图片
     * @param content  生成内容
     * @param size     图片宽高大小 ( 正方形 px )
     * @param callback 生成结果回调
     */
    public static void encodeQRCode(
            final String content,
            final int size,
            final QREncodeCallback callback
    ) {
        encodeQRCode(content, size, null, callback);
    }

    /**
     * 编码 ( 生成 ) 二维码图片
     * @param content  生成内容
     * @param size     图片宽高大小 ( 正方形 px )
     * @param logo     中间 Logo
     * @param callback 生成结果回调
     */
    public static void encodeQRCode(
            final String content,
            final int size,
            final Bitmap logo,
            final QREncodeCallback callback
    ) {
        DevThreadManager.getInstance(10).execute(new Runnable() {
            @Override
            public void run() {
                try {
                    // 编码 ( 生成 ) 二维码图片
                    Bitmap qrCodeBitmap = encodeQRCodeSync(content, size);
                    if (logo != null) { // 中间 Logo
                        qrCodeBitmap = addLogoToQRCode(qrCodeBitmap, logo);
                    }
                    // 触发回调
                    if (callback != null) {
                        callback.onResult(true, qrCodeBitmap, null);
                    }
                } catch (Exception e) {
                    LogPrintUtils.eTag(TAG, e, "encodeQRCode");
                    // 触发回调
                    if (callback != null) {
                        callback.onResult(false, null, e);
                    }
                }
            }
        });
    }

    // =======
    // = 编码 =
    // =======

    // 编码类型
    private static final Map<EncodeHintType, Object> ENCODE_HINTS = new EnumMap<>(EncodeHintType.class);

    static {
        // 编码类型
        ENCODE_HINTS.put(EncodeHintType.CHARACTER_SET, DevFinal.ENCODE.UTF_8);
        // 指定纠错等级, 纠错级别 ( L 7%、M 15%、Q 25%、H 30% )
        ENCODE_HINTS.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        // 设置二维码边的空度, 非负数
        ENCODE_HINTS.put(EncodeHintType.MARGIN, 0);
    }

    /**
     * 编码 ( 生成 ) 二维码图片
     * @param content 生成内容
     * @param size    图片宽高大小 ( 正方形 px )
     * @return 二维码图片
     */
    public static Bitmap encodeQRCodeSync(
            final String content,
            final int size
    )
            throws Exception {
        return encodeQRCodeSync(content, size, Color.BLACK, Color.WHITE);
    }

    /**
     * 编码 ( 生成 ) 二维码图片
     * <pre>
     *     该方法是耗时操作, 请在子线程中调用
     * </pre>
     * @param content         生成内容
     * @param size            图片宽高大小 ( 正方形 px )
     * @param foregroundColor 二维码图片的前景色
     * @param backgroundColor 二维码图片的背景色
     * @return 二维码图片
     */
    public static Bitmap encodeQRCodeSync(
            final String content,
            final int size,
            final int foregroundColor,
            final int backgroundColor
    )
            throws Exception {
        BitMatrix matrix = new MultiFormatWriter().encode(
                content, BarcodeFormat.QR_CODE, size, size, ENCODE_HINTS
        );
        int[] pixels = new int[size * size];
        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                if (matrix.get(x, y)) {
                    pixels[y * size + x] = foregroundColor;
                } else {
                    pixels[y * size + x] = backgroundColor;
                }
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, size, 0, 0, size, size);
        return bitmap;
    }

    // ============
    // = 解析二维码 =
    // ============

    // 解析配置
    private static final Map<DecodeHintType, Object> DECODE_HINTS = new EnumMap<>(DecodeHintType.class);

    /**
     * detail: 二维码解码 ( 解析 ) 回调
     * @author Ttt
     */
    public interface QRDecodeCallback {

        /**
         * 二维码解码 ( 解析 ) 回调
         * @param success 是否成功
         * @param result  识别数据 {@link Result}
         * @param error   异常信息
         */
        void onResult(
                boolean success,
                Result result,
                Throwable error
        );
    }

    // =

    /**
     * 解码 ( 解析 ) 二维码图片
     * @param bitmap   待解析的二维码图片
     * @param callback 解析结果回调
     */
    public static void decodeQRCode(
            final Bitmap bitmap,
            final QRDecodeCallback callback
    ) {
        if (bitmap == null) {
            if (callback != null) {
                callback.onResult(false, null, new Exception("bitmap is null"));
            }
            return;
        }
        // 开始解析
        DevThreadManager.getInstance(5).execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Result result = decodeQRCodeSync(bitmap);
                    // 触发回调
                    if (callback != null) {
                        callback.onResult((result != null), result, null);
                    }
                } catch (Exception e) {
                    LogPrintUtils.eTag(TAG, e, "decodeQRCode");
                    // 触发回调
                    if (callback != null) {
                        callback.onResult(false, null, e);
                    }
                }
            }
        });
    }

    /**
     * 解码 ( 解析 ) 二维码图片
     * @param bitmap 待解析的二维码图片
     * @return {@link Result}
     */
    public static Result decodeQRCodeSync(final Bitmap bitmap)
            throws Exception {
        if (bitmap != null) {
            int   width  = bitmap.getWidth();
            int   height = bitmap.getHeight();
            int[] pixels = new int[width * height];
            bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
            RGBLuminanceSource source = new RGBLuminanceSource(width, height, pixels);
            Result result = new MultiFormatReader().decode(
                    new BinaryBitmap(new HybridBinarizer(source)), DECODE_HINTS
            );
            return result;
        }
        return null;
    }

    // ==========
    // = 其他功能 =
    // ==========

    /**
     * 添加 Logo 到二维码图片上
     * <pre>
     *     非最优方法, 该方式是直接把 Logo 覆盖在图片上会遮挡部分内容
     * </pre>
     * @param src  二维码图片
     * @param logo Logo
     * @return 含 Logo 二维码图片
     */
    public static Bitmap addLogoToQRCode(
            final Bitmap src,
            final Bitmap logo
    ) {
        if (src == null || logo == null) return src;
        // 获取图片宽度高度
        int srcWidth   = src.getWidth();
        int srcHeight  = src.getHeight();
        int logoWidth  = logo.getWidth();
        int logoHeight = logo.getHeight();
        // 缩放图片
        float  scaleFactor = srcWidth * 1.0f / 4 / logoWidth; // 这里的 4 决定 Logo 在图片的比例 四分之一
        Bitmap bitmap      = Bitmap.createBitmap(srcWidth, srcHeight, Bitmap.Config.ARGB_8888);
        try {
            Canvas canvas = new Canvas(bitmap);
            canvas.drawBitmap(src, 0, 0, null);
            canvas.scale(scaleFactor, scaleFactor, srcWidth / 2, srcHeight / 2);
            canvas.drawBitmap(logo, (srcWidth - logoWidth) / 2, (srcHeight - logoHeight) / 2, null);
            canvas.save();
            canvas.restore();
        } catch (Exception e) {
            LogPrintUtils.eTag(TAG, e, "addLogoToQRCode");
            bitmap = null;
        }
        return bitmap;
    }

    // ==========
    // = 获取结果 =
    // ==========

    /**
     * 获取扫描结果数据
     * @param result 识别数据 {@link Result}
     * @return 扫描结果数据
     */
    public static String getResultData(final Result result) {
        return (result != null) ? result.getText() : null;
    }
}