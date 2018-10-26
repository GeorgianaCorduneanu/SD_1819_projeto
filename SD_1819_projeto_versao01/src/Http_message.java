import java.io.Serializable;

public class Http_message implements Serializable {
    private int number;
    private String message;

    Http_message(){}

    Http_message(int number){
        this.number = number;
        this.message =get_Message(number);
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String get_Message(int number){
        switch (number){
            case 200:
                return "OK";
            case 201:
                return "CREATED";
            case 202:
                return "ACCEPTED";
            case 203:
                return "NON-AUTHORITATIVE INFORMATION";
            case 204:
                return "NO CONTENT";
            case 302:
                return "FOUND";
            case 400:
                return "BAD REQUEST";
            case 404:
                return "NOT FOUND";
            case 409:
                return "CONFLICT";
            default:
                return "not a http number";
        }
    }
}
