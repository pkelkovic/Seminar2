package rasus.lab3.service;
import java.util.ArrayList;


// Interface
public interface TemperatureService {
    public void initialize();
    public ArrayList<String> getData(int row);
    public void readData();
}