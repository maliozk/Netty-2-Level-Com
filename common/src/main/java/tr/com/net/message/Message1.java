package tr.com.net.message;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class Message1 implements IMessage {
    int data1;
    int data2;

    @Override
    public int getType() {
        return 1;
    }

    @Override
    public byte[] encode() {
        byte[] d = new byte[9];
        d[0] = 1;
        d[1] = (byte) (data1 & 0xFF);
        d[2] = (byte) ((data1 >> 8) & 0xFF);
        d[3] = (byte) ((data1 >> 16) & 0xFF);
        d[4] = (byte) ((data1 >> 24) & 0xFF);
        d[1] = (byte) (data2 & 0xFF);
        d[2] = (byte) ((data2 >> 8) & 0xFF);
        d[3] = (byte) ((data2 >> 16) & 0xFF);
        d[4] = (byte) ((data2 >> 24) & 0xFF);
        return d;
    }
}
