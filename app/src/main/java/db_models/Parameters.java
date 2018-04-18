package db_models;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Tiago on 2/11/17.
 */

@Entity(nameInDb = "parameters")
public class Parameters {

    @Id(autoincrement = true)
    private Long id;
    @Property(nameInDb = "start_hour")
    private String start_hour;
    @Property(nameInDb = "end_hour")
    private String end_hour;
    @Property(nameInDb = "laboral_days")
    private String laboral_days;
    @Property(nameInDb = "threshold")
    private double threshold;
    @Generated(hash = 1058979360)
    public Parameters(Long id, String start_hour, String end_hour,
            String laboral_days, double threshold) {
        this.id = id;
        this.start_hour = start_hour;
        this.end_hour = end_hour;
        this.laboral_days = laboral_days;
        this.threshold = threshold;
    }
    @Generated(hash = 1848746217)
    public Parameters() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getStart_hour() {
        return this.start_hour;
    }
    public void setStart_hour(String start_hour) {
        this.start_hour = start_hour;
    }
    public String getEnd_hour() {
        return this.end_hour;
    }
    public void setEnd_hour(String end_hour) {
        this.end_hour = end_hour;
    }
    public String getLaboral_days() {
        return this.laboral_days;
    }
    public void setLaboral_days(String laboral_days) {
        this.laboral_days = laboral_days;
    }
    public double getThreshold() {
        return this.threshold;
    }
    public void setThreshold(double threshold) {
        this.threshold = threshold;
    }
}
