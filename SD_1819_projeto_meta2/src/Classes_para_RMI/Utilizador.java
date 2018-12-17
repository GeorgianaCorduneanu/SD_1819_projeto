package Classes_para_RMI;

import java.io.Serializable;
import java.util.ArrayList;

public class Utilizador implements Serializable {
    private String username, password;
    private Boolean editor;
    private ArrayList<Musica> lista_musica_upload;

    Utilizador(){}
    Utilizador(String username, String password){
        this.username = username;
        this.password = password;
        this.editor = false;
    }
    Utilizador(String username, String password, Boolean editor){
        this.username = username;
        this.password = password;
        this.editor = editor;
    }
    private void add_upload(Musica m, Upload u){
        lista_musica_upload.add(m);
        m.setUpload(u);
    }
    private void remove_upload(Musica m){
        for(Musica item : lista_musica_upload){
            if(item.equals(m)){
                m.setUpload(null);
                lista_musica_upload.remove(m);
            }
        }
    }
    private void set_privacidade_upload(Boolean b, Musica m){
        m.getUpload().setPrivacidade(b);
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getEditor() {
        return editor;
    }

    public void setEditor(Boolean editor) {
        this.editor = editor;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
