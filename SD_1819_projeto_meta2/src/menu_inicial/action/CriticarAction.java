package menu_inicial.action;

import com.opensymphony.xwork2.ActionSupport;
import com.sun.org.apache.xml.internal.serialize.LineSeparator;
import menu_inicial.model.Login_bean;
import org.apache.struts2.interceptor.SessionAware;
import ws.WebSocketAnnotation;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CriticarAction extends ActionSupport implements SessionAware {
    private Login_bean login_bean;
    private Map<String, Object> session;
    private String todosAlbuns, radioListaAlbuns, pontuacaoFeita, criticaFeita;
    private List<String> listaTodosAlbuns;
    private boolean botaoCriticar;
    @Override
    public String execute() {
        login_bean = getLogin_bean();
        int pontuacao = 0;
        String mensagem;
        if (botaoCriticar && radioListaAlbuns != null && !criticaFeita.equals("") && !pontuacaoFeita.equals("")) {
            pontuacao = Integer.parseInt(pontuacaoFeita);
            if (pontuacao > 0 && pontuacao <= 10) {
                mensagem = login_bean.criticarAlbum(radioListaAlbuns, criticaFeita, pontuacaoFeita);
                if (mensagem.equals("Critica Adicionada")) {
                    String listaPessoasCriticaramAlbum = login_bean.getListaPessoasCriticaramAlbum(radioListaAlbuns);
                    String[] listaPessoasCriticaramAlbumSeparadas = listaPessoasCriticaramAlbum.split(";");
                    String pontuacaoMediaAlbum = login_bean.getPontuacaoMediaAlbum(radioListaAlbuns);

                    WebSocketAnnotation ws = new WebSocketAnnotation();
                    Set<WebSocketAnnotation> listaUsers = ws.getUsers1();
                    String mensagemNotificacao = "critica:Album " + radioListaAlbuns + " foi criticado, atualizado[" + pontuacaoFeita + ", " + login_bean.getUsername() + "]  com pontuacao media de " + pontuacaoMediaAlbum;
                    System.out.println("tamanho listacriticadores: " + listaPessoasCriticaramAlbumSeparadas.length + " tamanho de pessoas online: " + listaUsers.size());
                    for (WebSocketAnnotation item : listaUsers) {
                        for (String user:listaPessoasCriticaramAlbumSeparadas) {
                        System.out.println("\nListaPessoasCriticaramAlbumSeparadas: " + user);
                            System.out.println(item.getUsername());
                            System.out.println("Comparacao de criticadores: " + item.getUsername() + "->" + user + "->" + login_bean.getUsername());
                            System.out.println(user + " ; " + mensagemNotificacao);
                            if (item.getUsername().equals(user) && !user.equals(login_bean.getUsername())) {
                                System.out.println("Notificacao ao inserir album: " + mensagemNotificacao);
                                //login_bean.inserirNotificacao(listaPessoasCriticaramAlbumSeparadas[i],mensagemNotificacao);
                                try {
                                    item.getSession().getBasicRemote().sendText(mensagemNotificacao);
                                    System.out.println("mandou para a session: " + mensagemNotificacao);
                                    continue;
                                } catch (IOException e) {
                                    e.printStackTrace();
                                } catch (IllegalStateException c) {
                                    System.out.println("erro: " + c);
                                }
                            } /*else{
                                login_bean.inserirNotificacao(user, mensagemNotificacao);
                                System.out.println("mandou apra historico do elif: " + mensagemNotificacao);
                            }*/
                            if(!user.equals(login_bean.getUsername()))
                              login_bean.inserirNotificacao(user, mensagemNotificacao);
                        }

                    }
                    System.out.println("*********************************************************");
                    return SUCCESS;
                }
            }
        }
        System.out.println("*******************************************************");
        return "insuccess";
    }

    public String getPontuacaoFeita() {
        return pontuacaoFeita;
    }

    public void setPontuacaoFeita(String pontuacaoFeita) {
        this.pontuacaoFeita = pontuacaoFeita;
    }

    public String getCriticaFeita() {
        return criticaFeita;
    }

    public void setCriticaFeita(String criticaFeita) {
        this.criticaFeita = criticaFeita;
    }

    public String getTodosAlbuns() {
        return todosAlbuns =  login_bean.getTodosAlbuns();
    }

    public void setTodosAlbuns(String todosAlbuns) {
        this.todosAlbuns = todosAlbuns;
    }

    public String getRadioListaAlbuns() {
        return radioListaAlbuns;
    }

    public void setRadioListaAlbuns(String radioListaAlbuns) {
        this.radioListaAlbuns = radioListaAlbuns;
    }

    public List<String> getListaTodosAlbuns() {
        return listaTodosAlbuns = login_bean.getLista_todos_utilizadores();
    }

    public void setListaTodosAlbuns(List<String> listaTodosAlbuns) {
        this.listaTodosAlbuns = listaTodosAlbuns;
    }

    public boolean isBotaoCriticar() {
        return botaoCriticar;
    }

    public void setBotaoCriticar(boolean botaoCriticar) {
        this.botaoCriticar = botaoCriticar;
    }

    public Login_bean getLogin_bean() {
        if(session.containsKey("login_bean"))
            return (Login_bean)session.get("login_bean");
        else
            return null;
    }
    @Override
    public void setSession(Map<String, Object> map) {
        this.session = map;
    }
}
