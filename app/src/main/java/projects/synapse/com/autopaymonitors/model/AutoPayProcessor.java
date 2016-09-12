package projects.synapse.com.autopaymonitors.model;

import java.io.Serializable;

/**
 * Created by AdeolaOjo on 9/10/2016.
 */
public class AutoPayProcessor implements Serializable {


    public AutoPayProcessor(){

    }
    public AutoPayProcessor(String name, String description, String code){
        this.code = code;
        this.description = description;
        this.name = name;
    }

    public int bankId;
    public String name;
    public String description;
    public String code;
    public String lastEventDescription;
    public boolean isPaused;
    public boolean isEnabled;
}
