package sample;

import common.Data;
import java.sql.Time;
import java.util.Date;

public class Controller {

    /**
     * Forwards a request to the controller which in turn
     * forwards a request to the database to get a data object with
     * all the specified attributes
     *
     * @param gender String represenation of gender groups
     * @param age String representation of age groups
     * @param income String representation of income groups
     * @param dateFrom String representation of the start filter date
     * @param dateTo String representation of the end filter date
     * @param timeFrom String representation of the start filter time
     * @param timeTo String representation of the end filter time
     * @return Data object with the specified attributes
     */
    public Data getRequest(String gender, String age, String income,
                           String dateFrom, String dateTo, String timeFrom,
                           String timeTo){

        /*
        Will work something like this:

        gender could be "MF" or "M" or "F"

        age could be "1234" where 1 to 4 are the available age groups
        e.g. if you only wanted oldest and youngest just do "14"

        income could be "123" if you wanted low, medium and high, "1" if you just want low etc

        //controller will then forward this request to database then when it receives will return the data object
        Data d = controller.get(gender, age ... timeFrom, timeTo);
        return d
        */

        return null;

    }


}