package xwh.test.netty.server;

/**
 * Created by xwh on 18-7-14.
 */

public class RequestInfoVO {
    private int type;
    private String body;
    private int sequence;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public int getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }
}
