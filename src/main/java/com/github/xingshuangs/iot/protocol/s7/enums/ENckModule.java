package com.github.xingshuangs.iot.protocol.s7.enums;


import java.util.HashMap;
import java.util.Map;

/**
 * NCK的模块
 *
 * @author xingshuang
 */
public enum ENckModule {
    Y((byte) 0x10, "Global system data"),
    YNCFL((byte) 0x11, "NCK instruction groups"),
    FU((byte) 0x12, "NCU global settable frames"),
    FA((byte) 0x13, "Active NCU global frames"),
    TO((byte) 0x14, "Tool data"),
    RP((byte) 0x15, "Arithmetic parameters"),
    SE((byte) 0x16, "Setting data"),
    SGUD((byte) 0x17, "SGUD( (byte) ,Block"),
    LUD((byte) 0x18, "Local userdata"),
    TC((byte) 0x19, "Toolholder parameters"),
    M((byte) 0x1a, "Machine data"),
    WAL((byte) 0x1C, "Working area limitation"),
    DIAG((byte) 0x1e, "Internal diagnostic data"),
    CC((byte) 0x1f, "Unknown"),
    FE((byte) 0x20, "Channel( (byte) ,specific external frame"),
    TD((byte) 0x21, "Tool data: General data"),
    TS((byte) 0x22, "Tool edge data: Monitoring data"),
    TG((byte) 0x23, "Tool data: Grinding( (byte) ,specific data"),
    TU((byte) 0x24, "Tool data"),
    TUE((byte) 0x25, "Tool edge data, userdefined data"),
    TV((byte) 0x26, "Tool data, directory"),
    TM((byte) 0x27, "Magazine data: General data"),
    TP((byte) 0x28, "Magazine data: Location data"),
    TPM((byte) 0x29, "Magazine data: Multiple assignment of location data"),
    TT((byte) 0x2a, "Magazine data: Location typ"),
    TMV((byte) 0x2b, "Magazine data: Directory"),
    TMC((byte) 0x2c, "Magazine data: Configuration data"),
    MGUD((byte) 0x2d, "MGUD( (byte) ,Block"),
    UGUD((byte) 0x2e, "UGUD( (byte) ,Block"),
    GUD4((byte) 0x2f, "GUD4( (byte) ,Block"),
    GUD5((byte) 0x30, "GUD5( (byte) ,Block"),
    GUD6((byte) 0x31, "GUD6( (byte) ,Block"),
    GUD7((byte) 0x32, "GUD7( (byte) ,Block"),
    GUD8((byte) 0x33, "GUD8( (byte) ,Block"),
    GUD9((byte) 0x34, "GUD9( (byte) ,Block"),
    PA((byte) 0x35, "Channel( (byte) ,specific protection zones"),
    GD1((byte) 0x36, "SGUD( (byte) ,Block GD1"),
    NIB((byte) 0x37, "State data: Nibbling"),
    ETP((byte) 0x38, "Types of events"),
    ETPD((byte) 0x39, "Data lists for protocolling"),
    SYNACT((byte) 0x3a, "Channel( (byte) ,specific synchronous actions"),
    DIAGN((byte) 0x3b, "Diagnostic data"),
    VSYN((byte) 0x3c, "Channel( (byte) ,specific user variables for synchronous actions"),
    TUS((byte) 0x3d, "Tool data: user monitoring data"),
    TUM((byte) 0x3e, "Tool data: user magazine data"),
    TUP((byte) 0x3f, "Tool data: user magazine place data"),
    TF((byte) 0x40, "Parameterizing, return parameters of _N_TMGETT, _N_TSEARC"),
    FB((byte) 0x41, "Channel( (byte) ,specific base frames"),
    SSP2((byte) 0x42, "State data: Spindle"),
    PUD((byte) 0x43, "programmglobale Benutzerdaten"),
    TOS((byte) 0x44, "Edge( (byte) ,related location( (byte) ,dependent fine total offsets"),
    TOST((byte) 0x45, "Edge( (byte) ,related location( (byte) ,dependent fine total offsets, transformed"),
    TOE((byte) 0x46, "Edge( (byte) ,related coarse total offsets, setup offsets"),
    TOET((byte) 0x47, "Edge( (byte) ,related coarse total offsets, transformed setup offsets"),
    AD((byte) 0x48, "Adapter data"),
    TOT((byte) 0x49, "Edge data: Transformed offset data"),
    AEV((byte) 0x4a, "Working offsets: Directory"),
    YFAFL((byte) 0x4b, "NCK instruction groups (Fanuc)"),
    FS((byte) 0x4c, "System( (byte) ,Frame"),
    SD((byte) 0x4d, "Servo data"),
    TAD((byte) 0x4e, "Application( (byte) ,specific data"),
    TAO((byte) 0x4f, "Application( (byte) ,specific cutting edge data"),
    TAS((byte) 0x50, "Application( (byte) ,specific monitoring data"),
    TAM((byte) 0x51, "Application( (byte) ,specific magazine data"),
    TAP((byte) 0x52, "Application( (byte) ,specific magazine location data"),
    MEM((byte) 0x53, "Unknown"),
    SALUC((byte) 0x54, "Alarm actions: List in reverse chronological order"),
    AUXFU((byte) 0x55, "Auxiliary functions"),
    TDC((byte) 0x56, "Tool/Tools"),
    CP((byte) 0x57, "Generic coupling"),
    SDME((byte) 0x6e, "Unknown"),
    SPARPI((byte) 0x6f, "Program pointer on interruption"),
    SEGA((byte) 0x70, "State data: Geometry axes in tool offset memory (extended)"),
    SEMA((byte) 0x71, "State data: Machine axes (extended)"),
    SSP((byte) 0x72, "State data: Spindle"),
    SGA((byte) 0x73, "State data: Geometry axes in tool offset memory"),
    SMA((byte) 0x74, "State data: Machine axes"),
    SALAL((byte) 0x75, "Alarms: List organized according to time"),
    SALAP((byte) 0x76, "Alarms: List organized according to priority"),
    SALA((byte) 0x77, "Alarms: List organized according to time"),
    SSYNAC((byte) 0x78, "Synchronous actions"),
    SPARPF((byte) 0x79, "Program pointers for block search and stop run"),
    SPARPP((byte) 0x7a, "Program pointer in automatic operation"),
    SNCF((byte) 0x7b, "Active G functions"),
    SPARP((byte) 0x7d, "Part program information"),
    SINF((byte) 0x7e, "Part( (byte) ,program( (byte) ,specific status data"),
    S((byte) 0x7f, "State data"),
    UNKNOWN1((byte) 0x80, "State data"),
    UNKNOWN2((byte) 0x81, "State data"),
    UNKNOWN3((byte) 0x82, "State data"),
    UNKNOWN4((byte) 0x83, "State data"),
    UNKNOWN5((byte) 0x84, "State data"),
    UNKNOWN6((byte) 0x85, "State data"),
    ;

    private static Map<Byte, ENckModule> map;

    public static ENckModule from(byte data) {
        if (map == null) {
            map = new HashMap<>();
            for (ENckModule item : ENckModule.values()) {
                map.put(item.code, item);
            }
        }
        return map.get(data);
    }

    private final byte code;

    private final String description;

    ENckModule(byte code, String description) {
        this.code = code;
        this.description = description;
    }

    public byte getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}
