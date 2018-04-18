package model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Tiago on 5/02/18.
 */

public class MoodEventContainer implements Serializable {

    private List<MoodsEventResponse> moodsEventResponseList;
    private int resource;
    private String moodTitle;

    public MoodEventContainer(List<MoodsEventResponse> moodsEventResponseList, int resource, String moodTitle) {
        this.moodsEventResponseList = moodsEventResponseList;
        this.resource = resource;
        this.moodTitle = moodTitle;
    }

    public List<MoodsEventResponse> getMoodsEventResponseList() {
        return moodsEventResponseList;
    }

    public void setMoodsEventResponseList(List<MoodsEventResponse> moodsEventResponseList) {
        this.moodsEventResponseList = moodsEventResponseList;
    }

    public int getResource() {
        return resource;
    }

    public void setResource(int resource) {
        this.resource = resource;
    }

    public String getMoodTitle() {
        return moodTitle;
    }

    public void setMoodTitle(String moodTitle) {
        this.moodTitle = moodTitle;
    }
}
