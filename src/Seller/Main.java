package Seller;

import interfaces.IShop;

import java.rmi.RMISecurityManager;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

public class Main {
    static IShop shoplookup;
    public static void main(String[] args) {
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
            AppSeller app = new AppSeller(shoplookup);
            app.setVisible(true);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
