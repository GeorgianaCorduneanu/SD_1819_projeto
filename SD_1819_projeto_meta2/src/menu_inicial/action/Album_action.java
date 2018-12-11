package menu_inicial.action;

public class Album_action{
    private boolean adicionar_album, eliminar_album, editar_album;

    public String execute(){
        if(adicionar_album)
            return "adicionar_album";
        else if(eliminar_album)
            return "eliminar_album";
        else if(editar_album)
            return "editar_album";
        return "insuccess";
    }
    public void setAdicionar_album(boolean adicionar_album){
        this.adicionar_album=adicionar_album;
    }
    public void setEliminar_album(boolean adicionar_album){
        this.adicionar_album=adicionar_album;
    }
}
