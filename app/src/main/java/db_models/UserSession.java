package db_models;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Tiago on 1/11/17.
 */

@Entity(nameInDb = "user_session")
public class UserSession {
    @Id(autoincrement = true)
    private Long id;
    @Property(nameInDb = "id_server")
    private String id_server;
    @Property(nameInDb = "name")
    private String name;
    @Property(nameInDb = "username")
    private String username;
    @Property(nameInDb = "mail")
    private String mail;
    @Property(nameInDb = "phone")
    private String phone;
    @Property(nameInDb = "password")
    private String password;
    @Property(nameInDb = "rol_id")
    private String rol_id;
    @Property(nameInDb = "nrol")
    private int nrol;

    @Generated(hash = 1080677650)
    public UserSession(Long id, String id_server, String name, String username,
            String mail, String phone, String password, String rol_id, int nrol) {
        this.id = id;
        this.id_server = id_server;
        this.name = name;
        this.username = username;
        this.mail = mail;
        this.phone = phone;
        this.password = password;
        this.rol_id = rol_id;
        this.nrol = nrol;
    }
    @Generated(hash = 875065627)
    public UserSession() {
    }
    
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getUsername() {
        return this.username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getMail() {
        return this.mail;
    }
    public void setMail(String mail) {
        this.mail = mail;
    }
    public String getPhone() {
        return this.phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getPassword() {
        return this.password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getRol_id() {
        return this.rol_id;
    }
    public void setRol_id(String rol_id) {
        this.rol_id = rol_id;
    }

    public String getId_server() {
        return id_server;
    }

    public void setId_server(String id_server) {
        this.id_server = id_server;
    }

    public int getNrol() {
        return nrol;
    }

    public void setNrol(int nrol) {
        this.nrol = nrol;
    }
}
