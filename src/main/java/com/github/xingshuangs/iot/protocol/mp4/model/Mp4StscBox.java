package com.github.xingshuangs.iot.protocol.mp4.model;


import com.github.xingshuangs.iot.protocol.mp4.enums.EMp4Type;

/**
 * Sample To Chunk Box(stbl-stsc),用chunk组织sample可以方便优化数据获取，一个chunk包含一个或多个sample。“stsc”中用一个表描述了
 * sample与chunk的映射关系，查看这张表就可以找到包含指定sample的thunk，从而找到这个sample，当然每个table entry可能包含一个或者多个chunk。fmp4方式，此box不必赋值，简单封装一个box即可
 *
 * @author xingshuang
 */
public class Mp4StscBox extends Mp4StcoBox {

    public Mp4StscBox() {
        this.mp4Type = EMp4Type.STSC;
        this.version = 0;
        this.flags = new byte[3];
        this.entryCount = 0;
    }
}
