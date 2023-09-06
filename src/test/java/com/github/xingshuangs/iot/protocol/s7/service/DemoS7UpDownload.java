package com.github.xingshuangs.iot.protocol.s7.service;


import com.github.xingshuangs.iot.protocol.s7.enums.EFileBlockType;
import com.github.xingshuangs.iot.protocol.s7.enums.EPlcType;
import com.github.xingshuangs.iot.protocol.s7.model.Mc7File;

/**
 * @author xingshuang
 */
public class DemoS7UpDownload {

    public static void main(String[] args) {
        S7PLC s7PLC = new S7PLC(EPlcType.S200_SMART, "127.0.0.1");

        //********************************* upload ***************************************/
        // upload file data, PLC -> PC, success in 200Smart
        byte[] bytes = s7PLC.uploadFile(EFileBlockType.OB, 1);

        //******************************** download **************************************/
        // 1. create mc7 file
        Mc7File mc7 = Mc7File.fromBytes(bytes);
        // 2. plc stop, stop plc before download file
        s7PLC.plcStop();
        // 3. download file data, PC -> PLC, success in 200Smart
        s7PLC.downloadFile(mc7);
        // 4. insert new filename
        s7PLC.insert(mc7.getBlockType(), mc7.getBlockNumber());
        // 5. hot restart, restart plc after download and insert file
        s7PLC.hotRestart();

        s7PLC.close();
    }
}
