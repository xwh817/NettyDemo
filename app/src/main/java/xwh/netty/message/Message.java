package xwh.netty.message;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by xwh on 18-7-14.
 */

public class Message {
    private int type;
    private String body;
    private int sequence;

    public Message(){}
    public Message(int type, String body) {
        this.type = type;
        this.body = body;
    }

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

    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        try {
            json.put("type", type);
            json.put("sequence", sequence);
            json.put("body", body);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }

    @Override
    public String toString() {
        return "Message{" +
                "type=" + type +
                ", body='" + body + '\'' +
                ", sequence=" + sequence +
                '}';
    }
}
