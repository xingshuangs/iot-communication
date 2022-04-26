package com.github.xingshuangs.iot.protocol.s7.model;


import com.github.xingshuangs.iot.protocol.s7.enums.EArea;
import com.github.xingshuangs.iot.protocol.s7.enums.EItemVariableType;
import com.github.xingshuangs.iot.protocol.s7.enums.ESyntaxID;
import com.github.xingshuangs.iot.utils.IntegerUtil;
import com.github.xingshuangs.iot.utils.ShortUtil;
import lombok.Data;

/**
 * 请求项
 *
 * @author xingshuang
 */
@Data
public class RequestItem implements IByteArray {

    /**
     * 变量规范，对于读/写消息，它总是具有值0x12 <br>
     * 字节大小：1 <br>
     * 字节序数：0
     */
    private byte specificationType = (byte) 0x12;

    /**
     * 其余部分的长度规范 <br>
     * 字节大小：1 <br>
     * 字节序数：1
     */
    private byte lengthOfFollowing = (byte) 0x10;

    /**
     * 寻址模式和项结构其余部分的格式，它具有任意类型寻址的常量值0x10 <br>
     * 字节大小：1 <br>
     * 字节序数：2
     */
    private ESyntaxID syntaxId = ESyntaxID.S7ANY;

    /**
     * 变量的类型和长度BIT，BYTE，WORD，DWORD，COUNTER <br>
     * 字节大小：1 <br>
     * 字节序数：3
     */
    private EItemVariableType variableType = EItemVariableType.BYTE;

    /**
     * 读取长度 <br>
     * 字节大小：2 <br>
     * 字节序数：4-5
     */
    private int count = 0x0000;

    /**
     * 即 DB 编号，如果访问的不是DB区域，此处为0x0000 <br>
     * 字节大小：2 <br>
     * 字节序数：6-7
     */
    private int dbNumber = 0x0000;

    /**
     * 存储区类型DB存储区 <br>
     * 字节大小：1 <br>
     * 字节序数：8
     */
    private EArea area = EArea.DATA_BLOCKS;

    /**
     * 开始字节地址 <br>
     * 字节大小：3 <br>
     * 字节序数：9-11
     */
    private int address = 0x000000;

    @Override
    public int byteArrayLength() {
        return 12;
    }

    @Override
    public byte[] toByteArray() {
        byte[] res = new byte[12];
        byte[] countBytes = ShortUtil.toByteArray((short) this.count);
        byte[] dbNumberBytes = ShortUtil.toByteArray((short) this.dbNumber);
        byte[] addressBytes = IntegerUtil.toByteArray(this.address);

        res[0] = this.specificationType;
        res[1] = this.lengthOfFollowing;
        res[2] = this.syntaxId.getCode();
        res[3] = this.variableType.getCode();
        res[4] = countBytes[0];
        res[5] = countBytes[1];
        res[6] = dbNumberBytes[0];
        res[7] = dbNumberBytes[1];
        res[8] = this.area.getCode();
        res[9] = addressBytes[1];
        res[10] = addressBytes[2];
        res[11] = addressBytes[3];
        return res;
    }
}
