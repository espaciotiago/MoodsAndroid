package model;

import java.io.Serializable;

/**
 * Created by Tiago on 5/02/18.
 */

public class MoodsEventResponse implements Serializable {

    private String name;
    private String id;
    private int quantity;

    public MoodsEventResponse(String name, String id, int quantity) {
        this.name = name;
        this.id = id;
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
