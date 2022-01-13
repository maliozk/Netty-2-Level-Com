package tr.com.net.message;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Message2 implements IMessage{
    int data1;

    @Override
    public int getType() {
        return 2;
    }

    @Override
    public byte[] encode() {
        byte[] d = new byte[5];
        d[0] = 2;
        d[1] = (byte) (data1 & 0xFF);
        d[2] = (byte) ((data1 >> 8) & 0xFF);
        d[3] = (byte) ((data1 >> 16) & 0xFF);
        d[4] = (byte) ((data1 >> 24) & 0xFF);
        return d;
    }
}
