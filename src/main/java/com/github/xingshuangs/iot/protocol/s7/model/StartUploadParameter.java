package com.github.xingshuangs.iot.protocol.s7.model;


import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 开始上传参数
 *
 * @author xingshuang
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class StartUploadParameter extends Parameter implements IByteArray {

    private boolean moreDataFollowing = false;

    private boolean errorStatus = false;

    private byte[] unknownBytes = new byte[2];

    private int uploadId = 0x00000000;

    private int filenameLength = 0;

    private String fileIdentifier = "";

    private String blockType = "";

    @Override
    public int byteArrayLength() {
        return 0;
    }

    @Override
    public byte[] toByteArray() {
        return new byte[0];
    }
}
