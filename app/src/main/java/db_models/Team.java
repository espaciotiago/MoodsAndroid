package db_models;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Tiago on 13/11/17.
 */

@Entity(nameInDb = "team")
public class  Team {

    @Id(autoincrement = true)
    private Long id;
    @Property(nameInDb = "name")
    private String name;
    @Property(nameInDb = "idequipo")
    private String idequipo;

    @Generated(hash = 1113324952)
    public Team(Long id, String name, String idequipo) {
        this.id = id;
        this.name = name;
        this.idequipo = idequipo;
    }

    @Generated(hash = 882286361)
    public Team() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdequipo() {
        return idequipo;
    }

    public void setIdequipo(String idequipo) {
        this.idequipo = idequipo;
    }
}
