package menu_inicial.action;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.interceptor.SessionAware;
import java.util.Map;

public class Menu_action extends ActionSupport implements SessionAware {
    private boolean musica, artista, album, editor;
    private Map<String, Object> session;

    public String execute(){
        editor = (boolean)session.get("editor");
        if(musica) {
            return "musica";
        }if(album) {
            return "album";
        }if(artista) {
            return "artista";
        }
        return "insuccess";
    }

    public void setMusica(boolean musica){
        this.musica = musica;
    }
    public void setArtista(boolean artista){
        this.artista = artista;
    }
    public void setAlbum(boolean album){
        this.album = album;
    }

    @Override
    public void setSession(Map<String, Object> map) {
        this.session = map;
    }
    /*private Servlet s;
    private Map<String, Object> session;

    Menu_action(Servlet s) {
        this.s = s;
    }

    public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {
        this.s.service(req, res);
    }

    // forward the rest
    public void init(ServletConfig config) throws ServletException {
        s.init(config);
    }

    public ServletConfig getServletConfig() {
        return s.getServletConfig();
    }

    public String getServletInfo() {
        return s.getServletInfo();
    }

    public void destroy() {
        s.destroy();
    }

    public String toString() {
        return s.toString();
    }*/
}
