package appcontrolescolarfx.modelo.pojo;

public class Rol {
    private int idRol;
    private String rol;

    public Rol() {
    }

    public Rol(int idRol, String rol) {
        this.idRol = idRol;
        this.rol = rol;
    }
    
    public void setIdRol(int idRol) {
        this.idRol = idRol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public int getIdRol() {
        return idRol;
    }

    public String getRol() {
        return rol;
    }

    @Override
    public String toString() {
        return rol;
    }
}
