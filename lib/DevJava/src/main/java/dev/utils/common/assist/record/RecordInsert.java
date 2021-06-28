package dev.utils.common.assist.record;

/**
 * detail: 日志记录插入信息
 * @author Ttt
 */
public class RecordInsert {

    // 文件信息 ( 一个文件只会添加一次文件信息, 且在最顶部 )
    private String mFileInfo;

    // 每条日志头部信息
    private String mLogHeader;

    // 每条日志尾部信息
    private String mLogTail;

    // ==========
    // = 构造函数 =
    // ==========

    public RecordInsert(String fileInfo) {
        this.mFileInfo = fileInfo;
    }

    public RecordInsert(
            String fileInfo,
            String logHeader,
            String logTail
    ) {
        this.mFileInfo  = fileInfo;
        this.mLogHeader = logHeader;
        this.mLogTail   = logTail;
    }

    // ===========
    // = get/set =
    // ===========

    public String getFileInfo() {
        return mFileInfo;
    }

    public RecordInsert setFileInfo(String fileInfo) {
        this.mFileInfo = fileInfo;
        return this;
    }

    public String getLogHeader() {
        return mLogHeader;
    }

    public RecordInsert setLogHeader(String logHeader) {
        this.mLogHeader = logHeader;
        return this;
    }

    public String getLogTail() {
        return mLogTail;
    }

    public RecordInsert setLogTail(String logTail) {
        this.mLogTail = logTail;
        return this;
    }
}