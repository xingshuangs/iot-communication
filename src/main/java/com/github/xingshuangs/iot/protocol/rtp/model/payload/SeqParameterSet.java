/*
 * MIT License
 *
 * Copyright (c) 2021-2099 Oscura (xingshuang) <xingshuang_cool@163.com>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.github.xingshuangs.iot.protocol.rtp.model.payload;


import lombok.Getter;

/**
 * sps信息
 *
 * @author xingshuang
 */
@Getter
public class SeqParameterSet {
    private int profileIdc;
    private boolean constraintSet0Flag;
    private boolean constraintSet1Flag;
    private boolean constraintSet2Flag;
    private boolean constraintSet3Flag;
    private boolean constraintSet4Flag;
    private boolean constraintSet5Flag;
    private long reservedZero2Bits;
    private int levelIdc;
    private int seqParameterSetId;
    private int log2MaxFrameNumMinus4;
    private int picOrderCntType;
    private int log2MaxPicOrderCntKLsbMinus4;
    private int numRefFrames;
    private boolean gapsInFrameNumValueAllowedFlag;
    private int picWidthInMbsMinus1;
    private int picHeightInMapUnitsMinus1;
    private boolean frameMbsOnlyFlag;
    private boolean mbAdaptiveFrameFieldFlag;
    private boolean direct8x8InferenceFlag;
    private boolean frameCroppingFlag;
    private int frameCropLeftOffset;
    private int frameCropRightOffset;
    private int frameCropTopOffset;
    private int frameCropBottomOffset;
    private boolean deltaPicOrderAlwaysZeroFlag;
    private int offsetForNonRefPic;
    private int offsetForTopToBottomField;
    private boolean vuiParametersPresentFlag;
    private boolean aspectRatioInfoPresentFlag;
    private double sarScale = 1;

    /**
     * 视频宽度
     *
     * @return 宽度
     */
    public int getWidth() {
        return (int) Math.ceil((((this.picWidthInMbsMinus1 + 1) * 16) - this.frameCropLeftOffset * 2 - this.frameCropRightOffset * 2) * this.sarScale);
    }

    /**
     * 视频高度
     *
     * @return 高度
     */
    public int getHeight() {
        return ((2 - (this.frameMbsOnlyFlag ? 1 : 0)) * (this.picHeightInMapUnitsMinus1 + 1) * 16)
                - ((this.frameMbsOnlyFlag ? 2 : 4) * (this.frameCropTopOffset + this.frameCropBottomOffset));
    }

    /**
     * 根据字节数组构建sps
     *
     * @param data 字节数组
     * @return sps
     */
    public static SeqParameterSet createSPS(byte[] data) {
        SeqParameterSet sps = new SeqParameterSet();
        ExpGolomb expGolomb = new ExpGolomb(data);
        // filter header
        expGolomb.skipBit(8);
        sps.profileIdc = (int) expGolomb.readNBit(8);
        sps.constraintSet0Flag = expGolomb.readBoolean();
        sps.constraintSet1Flag = expGolomb.readBoolean();
        sps.constraintSet2Flag = expGolomb.readBoolean();
        sps.constraintSet3Flag = expGolomb.readBoolean();
        sps.constraintSet4Flag = expGolomb.readBoolean();
        sps.constraintSet5Flag = expGolomb.readBoolean();
        sps.reservedZero2Bits = expGolomb.readNBit(2);
        sps.levelIdc = (int) expGolomb.readNBit(8);
        sps.seqParameterSetId = expGolomb.readUE();

        someProfilesHandle(sps, expGolomb);

        sps.log2MaxFrameNumMinus4 = expGolomb.readUE();
        sps.picOrderCntType = expGolomb.readUE();
        if (sps.picOrderCntType == 0) {
            sps.log2MaxPicOrderCntKLsbMinus4 = expGolomb.readUE();
        } else if (sps.picOrderCntType == 1) {
            sps.deltaPicOrderAlwaysZeroFlag = expGolomb.readBoolean();
            sps.offsetForNonRefPic = expGolomb.readSE();
            sps.offsetForTopToBottomField = expGolomb.readSE();
            int numRefFramesInPicOrderCntCycle = expGolomb.readUE();
            for (int i = 0; i < numRefFramesInPicOrderCntCycle; i++) {
                expGolomb.readSE();
            }
        }
        sps.numRefFrames = expGolomb.readUE();
        sps.gapsInFrameNumValueAllowedFlag = expGolomb.readBoolean();
        sps.picWidthInMbsMinus1 = expGolomb.readUE();
        sps.picHeightInMapUnitsMinus1 = expGolomb.readUE();
        sps.frameMbsOnlyFlag = expGolomb.readBoolean();
        if (!sps.frameMbsOnlyFlag) {
            sps.mbAdaptiveFrameFieldFlag = expGolomb.readBoolean();
        }
        sps.direct8x8InferenceFlag = expGolomb.readBoolean();
        sps.frameCroppingFlag = expGolomb.readBoolean();
        if (sps.frameCroppingFlag) {
            sps.frameCropLeftOffset = expGolomb.readUE();
            sps.frameCropRightOffset = expGolomb.readUE();
            sps.frameCropTopOffset = expGolomb.readUE();
            sps.frameCropBottomOffset = expGolomb.readUE();
        }
        sps.vuiParametersPresentFlag = expGolomb.readBoolean();
        sps.aspectRatioInfoPresentFlag = expGolomb.readBoolean();
        if (sps.vuiParametersPresentFlag && sps.aspectRatioInfoPresentFlag) {
            sps.sarScale = calculateSarRatio(expGolomb);
        }
        return sps;
    }

    /**
     * 特殊profiles的处理
     *
     * @param sps       sps
     * @param expGolomb 编码
     */
    private static void someProfilesHandle(SeqParameterSet sps, ExpGolomb expGolomb) {
        if (sps.profileIdc == 100
                || sps.profileIdc == 110
                || sps.profileIdc == 122
                || sps.profileIdc == 244
                || sps.profileIdc == 44
                || sps.profileIdc == 83
                || sps.profileIdc == 86
                || sps.profileIdc == 118
                || sps.profileIdc == 128) {
            int chromaFormatIdc = expGolomb.readUE();
            if (chromaFormatIdc == 3) {
                expGolomb.skipBit(1);
            }
            expGolomb.skipUE();
            expGolomb.skipUE();
            expGolomb.read1Bit();
            if (expGolomb.readBoolean()) {
                int scalingListCount = (chromaFormatIdc != 3) ? 8 : 12;
                for (int i = 0; i < scalingListCount; i++) {
                    if (expGolomb.readBoolean()) { // seq_scaling_list_present_flag[ i ]
                        if (i < 6) {
                            expGolomb.skipScalingList(16);
                        } else {
                            expGolomb.skipScalingList(64);
                        }
                    }
                }
            }
        }
    }

    /**
     * 计算sarRatio
     *
     * @param expGolomb 编码
     * @return sarRatio
     */
    private static double calculateSarRatio(ExpGolomb expGolomb) {
        int sarRatio1 = -1;
        int sarRatio2 = -1;
        int aspectRatioIdc = (int) expGolomb.readNBit(8);
        switch (aspectRatioIdc) {
            case 1:
                sarRatio1 = 1;
                sarRatio2 = 1;
                break;
            case 2:
                sarRatio1 = 12;
                sarRatio2 = 11;
                break;
            case 3:
                sarRatio1 = 10;
                sarRatio2 = 11;
                break;
            case 4:
                sarRatio1 = 16;
                sarRatio2 = 11;
                break;
            case 5:
                sarRatio1 = 40;
                sarRatio2 = 33;
                break;
            case 6:
                sarRatio1 = 24;
                sarRatio2 = 11;
                break;
            case 7:
                sarRatio1 = 20;
                sarRatio2 = 11;
                break;
            case 8:
                sarRatio1 = 32;
                sarRatio2 = 11;
                break;
            case 9:
                sarRatio1 = 80;
                sarRatio2 = 33;
                break;
            case 10:
                sarRatio1 = 18;
                sarRatio2 = 11;
                break;
            case 11:
                sarRatio1 = 15;
                sarRatio2 = 11;
                break;
            case 12:
                sarRatio1 = 64;
                sarRatio2 = 33;
                break;
            case 13:
                sarRatio1 = 160;
                sarRatio2 = 99;
                break;
            case 14:
                sarRatio1 = 4;
                sarRatio2 = 3;
                break;
            case 15:
                sarRatio1 = 3;
                sarRatio2 = 2;
                break;
            case 16:
                sarRatio1 = 2;
                sarRatio2 = 1;
                break;
            case 255:
                sarRatio1 = (int) ((expGolomb.readNBit(8) << 8) | expGolomb.readNBit(8));
                sarRatio2 = (int) ((expGolomb.readNBit(8) << 8) | expGolomb.readNBit(8));
                break;
            default:
                break;
        }
        if (sarRatio1 > 0 && sarRatio2 > 0) {
            return sarRatio1 * 1.0 / sarRatio2;
        } else {
            return 1;
        }
    }
}
