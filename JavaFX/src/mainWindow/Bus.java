package mainWindow;

import javafx.beans.property.SimpleStringProperty;

public class Bus {

    private SimpleStringProperty busTime;
    private SimpleStringProperty busName;
    private SimpleStringProperty busDirection;

    public Bus (String busTime, String busName, String busDirection) {
        this.busTime = new SimpleStringProperty(busTime);
        this.busName = new SimpleStringProperty(busName);
        this.busDirection = new SimpleStringProperty(busDirection);
    }

    public String getBusTime() {
        return busTime.get();
    }

    public String getBusName() {
        return busName.get();
    }

    public String getBusDirection() {
        return busDirection.get();
    }
}
