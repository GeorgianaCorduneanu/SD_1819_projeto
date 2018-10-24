import java.io.Serializable;

public class Upload implements Serializable {
    private Boolean privacidade;
    private Byte ficheiro_mp3;

    Upload(){}

    Upload(Boolean privacidade, Byte ficheiro_mp3){
        this.ficheiro_mp3 = ficheiro_mp3;
        this.privacidade = privacidade;
    }

    public Byte getFicheiro_mp3() {
        return ficheiro_mp3;
    }

    public void setFicheiro_mp3(Byte ficheiro_mp3) {
        this.ficheiro_mp3 = ficheiro_mp3;
    }

    public Boolean getPrivacidade() {
        return privacidade;
    }

    public void setPrivacidade(Boolean privacidade) {
        this.privacidade = privacidade;
    }
}
