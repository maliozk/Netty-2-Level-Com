package tr.com.net.message;

public interface IMessage {
    int getType();

    byte[] encode();
}
