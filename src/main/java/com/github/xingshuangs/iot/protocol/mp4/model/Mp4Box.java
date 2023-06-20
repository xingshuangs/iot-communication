package com.github.xingshuangs.iot.protocol.mp4.model;


import com.github.xingshuangs.iot.protocol.common.IObjectByteArray;
import com.github.xingshuangs.iot.protocol.mp4.enums.EMp4Type;

/**
 * Mp4的盒子
 *
 * @author xingshuang
 */
public class Mp4Box implements IObjectByteArray {

    /**
     * 32位为box type，一般是4个字符，如“ftyp”、“moov”等，这些box type都是已经预定义好的，分别表示固定的意义,
     * 其值为类型的ASCII值，当type是uuid时，代表Box中的数据是用户自定义扩展类型
     */
    protected EMp4Type mp4Type;


    /**
     * 标准的box开头的4个字节（32位）为box size，box size值包括box header和box body整个box的大小
     *
     * @return 总字节长度
     */
    @Override
    public int byteArrayLength() {
        return 0;
    }

    @Override
    public byte[] toByteArray() {
        return new byte[0];
    }
}
