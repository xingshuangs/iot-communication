package com.github.xingshuangs.iot.protocol.mp4.model;


import com.github.xingshuangs.iot.protocol.mp4.enums.EMp4Type;

/**
 * Decoding Time to Sample Box(stbl-stts),存储了sample的duration，描述了sample时序的映射方法，我们通过它可以找到任何时间的sample,
 * “stts”也可包含一个压缩的表来映射时间和sample序号，用其他的表来提供每个sample的长度和指针，表中每个条目提供了在同一个时间偏移量里面
 * 连续的sample序号，以及samples的偏移量。递增这些偏移量，就可以建立一个完整的time to sample表。在fmp4中这些信息以moof中为主，这里
 * 可不必赋值，只做一个box即可
 *
 * @author xingshuang
 */
public class Mp4SttsBox extends Mp4StcoBox {

    public Mp4SttsBox() {
        this.mp4Type = EMp4Type.STTS;
        this.version = 0;
        this.flags = new byte[3];
        this.entryCount = 0;
    }
}
