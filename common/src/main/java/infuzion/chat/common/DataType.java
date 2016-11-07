package infuzion.chat.common;

public enum DataType {
    Message((byte) 0x1),
    EndOfData((byte) 0x2),
    ColorChange((byte) 0x3),
    ClientHello((byte) 0x4),
    UUIDAssign((byte) 0x5),
    Heartbeat((byte) 0x6),
    Command((byte) 0x7);

    public byte byteValue;
    DataType(byte i) {
        byteValue = i;
    }

    public static DataType valueOf(byte messageType) {
        for(DataType e: values()){
            if(e.byteValue == messageType){
                return e;
            }
        }
        return null;
    }
}
