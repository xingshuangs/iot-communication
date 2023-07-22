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

    public static EndDownloadParameter createDefault(EFileBlockType blockType,
                                                  int blockNumber,
                                                  EDestinationFileSystem destinationFileSystem){
        EndDownloadParameter parameter = new EndDownloadParameter();
        parameter.blockType = blockType;
        parameter.blockNumber = blockNumber;
        parameter.destinationFileSystem = destinationFileSystem;
        return parameter;
    }
}
