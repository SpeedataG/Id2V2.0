package cn.ywho.api.decode;

/**
 * 身份证图片解码
 *
 * @author xqs
 */
public class DecodeWlt {
    /**
     * @param wlt     wlt文件数据,1024字节
     * @param bmp     解析的RGB数据，102*126*3字节，可根据需求生成BMP或者JPG，图像数据BGR格式，需要将B、R值互换。
     * @param bmpSave 709: bmp file save,
     *                708: bmp file doesn't save（在内存读写）
     * @return <li><b>1</b> 正确</li>
     * <li><b>0</b> bmpSave参数错误</li>
     */
    private static native int wlt2bmp(byte[] wlt, byte[] bmp, int bmpSave);

    //加载照片解码库
    static {
        System.loadLibrary("DecodeWlt");
    }

    //身份证图片解码
    public static int hxgcWlt2Bmp(byte[] wlt, byte[] bmp, int bmpSave) {
        return wlt2bmp(wlt, bmp, bmpSave);
    }
}
