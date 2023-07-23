package com.github.xingshuangs.iot.protocol.s7.model;


import com.github.xingshuangs.iot.protocol.common.IObjectByteArray;
import com.github.xingshuangs.iot.protocol.s7.enums.EDestinationFileSystem;
import com.github.xingshuangs.iot.protocol.s7.enums.EFileBlockType;
import com.github.xingshuangs.iot.protocol.s7.enums.EFunctionCode;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 结束下载参数
 *
 * @author xingshuang
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class EndDownloadParameter extends DownloadParameter implements IObjectByteArray {

    public EndDownloadParameter() {
        this.functionCode = EFunctionCode.END_DOWNLOAD;
    }

    /**
     * 创建默认的下载中参数
     *
     * @param blockType             数据块类型
     * @param blockNumber           数据块编号
     * @param destinationFileSystem 目标文件系统
     * @return EndDownloadParameter
     */
    public static EndDownloadParameter createDefault(EFileBlockType blockType,
                                                     int blockNumber,
                                                     EDestinationFileSystem destinationFileSystem) {
        EndDownloadParameter parameter = new EndDownloadParameter();
        parameter.blockType = blockType;
        parameter.blockNumber = blockNumber;
        parameter.destinationFileSystem = destinationFileSystem;
        return parameter;
    }
}
