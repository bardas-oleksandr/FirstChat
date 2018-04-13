package Chat;

class Message {
    private String header;
    private String body;

    Message(){
        this.header = new String();
        this.body = new String();
    }

    Message(String header, String body){
        this.header = header;
        this.body = body;
    }

    String header(){
        return this.header;
    }

    String body(){
        return this.body;
    }

    void changeBody(String body){
        this.body = body;
    }
}
