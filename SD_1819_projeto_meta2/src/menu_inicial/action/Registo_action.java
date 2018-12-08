package menu_inicial.action;

import com.opensymphony.xwork2.ActionSupport;
import menu_inicial.model.Registo_bean;
import org.apache.struts2.interceptor.SessionAware;

import java.rmi.RemoteException;
import java.util.Map;

public class Registo_action extends ActionSupport implements SessionAware {
    private static final long serialVersionUID = 4L;
    private Map<String, Object> session;
    private String username = null, password = null, conf_password=null;
    boolean editor=false;

    @Override
    public String execute() {
        // any username is accepted without confirmation (should check using RMI)
        if (this.username != null && !username.equals("") && this.password != null && this.password.equals(this.conf_password)) {
            this.getRegisto_bean().setUsername(this.username);
            this.getRegisto_bean().setPassword(this.password);
            //if(this.getLogin_bean().getUserMatchesPassword()) {/*tratar de remote exception*/

            int aux = 0;//0-nao existe 1-utilizador sem privilegios 2-utilizador com privilegios
            try {
                aux = this.getRegisto_bean().getUserMatchesPassword();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            if (aux == 0) {//caso o utilizador nao exista //devolve 3 se o servidor estiver a null
                int aux_2 = this.getRegisto_bean().regista_utilizador();
                /*1-criou utilizador editor false
                  2-criou utilizador editor true-primeiro utilizador

                  0-nao criou utilizador, ja tem conta
                 */
                if (aux_2 == 1) {
                    session.put("username", username);
                    session.put("editor", false);
                    System.out.println("Nao encontrou, editor false");
                    return SUCCESS;
                } else if (aux_2 == 2) {
                    session.put("username", username);
                    session.put("editor", true);
                    System.out.println("Nao encontrou, editor false");
                    return SUCCESS;
                } else if (aux_2 == 0) {
                    System.out.println("Nao encontrou, editor false");
                    return "insuccess";
                }
            }
            return "insuccess";
        }
        return "insuccess";
    }


    public void setUsername (String username){
        this.username = username; // will you sanitize this input? maybe use a prepared statement?
    }
    public void setConf_password(String conf_password){
        this.conf_password = conf_password;
    }

    public void setPassword (String password){
        this.password = password; // what about this input?
    }

    private Registo_bean getRegisto_bean () {
        //verificar se alguém já tem a sessao iniciada
        if (!session.containsKey("registo_bean"))
            this.setRegisto_bean(new Registo_bean());
        return (Registo_bean) session.get("registo_bean");
    }

    private void setRegisto_bean (Registo_bean registo_bean){
        this.session.put("registo_bean", registo_bean);
    }

    @Override
    public void setSession (Map < String, Object > session){
        this.session = session;
    }

    public String getConf_password() {
        return conf_password;
    }
}
