package Client;

import interfaces.IShop;
import model.Client;
import model.ItemType;

import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    static IShop shoplookup;
    public static void main(String[] args) {
        //tutaj chyba musi sie połączyć do sklepu
        if (System.getSecurityManager() == null) {
            System.setProperty("java.security.policy","grantallpolicies.policy");
            System.setSecurityManager(new RMISecurityManager());
        }
        System.out.println("Podaj port: ");
        Scanner scanner = new Scanner(System.in);
        int port = scanner.nextInt();
        try {
            Registry registry = LocateRegistry.getRegistry(port);
            shoplookup = (IShop) registry.lookup("Shop");
            App app = new App(shoplookup);
            app.setVisible(true);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
