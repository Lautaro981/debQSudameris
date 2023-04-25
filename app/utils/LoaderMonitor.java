package utils;

import interfaces.IntegrationStatus;

import java.util.ArrayList;

public class LoaderMonitor {

    private static LoaderMonitor instance;
    private ArrayList<IntegrationStatus> monitors = new ArrayList<>();

    //TODO: Aca seria un buen lugar para adicionar los Monitores de integraciones
    private LoaderMonitor(){
        /**
         * TODO: Por ejemplo:
         *      addMonitor(new MacroMonitor()).addMonitor(new OtherMonitor());
         */
    };

    public void clearMonitors() {
        monitors.clear();
    }

    public static LoaderMonitor getInstance() {
        if(instance == null){
            synchronized (LoaderMonitor.class) {
                if(instance == null){
                    instance = new LoaderMonitor();
                }
            }
        }
        return instance;
    }

    public LoaderMonitor addMonitor(IntegrationStatus monitor) {
        monitors.add(monitor);
        return this;
    }

    public ArrayList<IntegrationStatus> getMonitors() {
        return monitors;
    }

}
