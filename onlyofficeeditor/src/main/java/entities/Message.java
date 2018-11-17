package main.java.entities;

import org.json.JSONObject;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 返回信息的类
 */
@XmlRootElement(name="Message")
public class Message {
    private int code;
    private String msg;
    @XmlElement
    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    
    @XmlElement
    public String getMsg() {
        return msg;
    }

    private void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return (new JSONObject(this)).toString();
    }

    public Message(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Message(int code) {
        this.code = code;
        switch (code){
            case CODE.SUCCESS: setMsg("success"); break;
            case CODE.UNKNOWN_ERROR: setMsg("Unknown Error"); break;
            case CODE.LIGIN_FAILED: setMsg("Login failed"); break;
            default: setMsg("Unknown Error"); setCode(CODE.UNKNOWN_ERROR);
        }
    }

    public Message() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Message)) return false;

        Message message = (Message) o;

        if (code != message.code) return false;
        return msg != null ? msg.equals(message.msg) : message.msg == null;
    }

    @Override
    public int hashCode() {
        int result = code;
        result = 31 * result + (msg != null ? msg.hashCode() : 0);
        return result;
    }

    public static final class CODE{
        public static final int UNKNOWN_ERROR = -1;
        public static final int SUCCESS = 1;

        //与UserInfo有关的信息 2000 - 2999
        public static final int LIGIN_FAILED = 2000;
    }
}
