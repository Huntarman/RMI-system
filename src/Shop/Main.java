package Shop;

import interfaces.IShop;

import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

//Wazne - parametryzzajca, RMI security manager
public class Main {
    //
    // Michał Zajdel compiled with java 17
    // compilation:
    // cd lab07_pop
    //
    // javac --module-path src/gadgets.jar -d build/production/Lab07 -sourcepath src/ src/Shop/Main.java
    // javac --module-path src/gadgets.jar -d build/production/Lab07 -sourcepath src/ src/Client/Main.java
    // javac --module-path src/gadgets.jar -d build/production/Lab07 -sourcepath src/ src/Seller/Main.java
    //
    // running
    // java -p out/production/Lab07_pop;src/gadgets.jar -m Lab07.pop/Shop.Main
    // java -p out/production/Lab07_pop;src/gadgets.jar -m Lab07.pop/Client.Main
    // java -p out/production/Lab07_pop;src/gadgets.jar -m Lab07.pop/Seller.Main
    //
    // jar creation
    // jar cvf Lab07_pop.jar -C out/production/Lab07_pop .
    //
    // jar running
    // Lab07_pop>java -p ./Lab07_pop.jar;src/gadgets.jar -m Lab07.pop/Shop.Main
    // Lab07_pop>java -p ./Lab07_pop.jar;src/gadgets.jar -m Lab07.pop/Shop.Main
    // Lab07_pop>java -p ./Lab07_pop.jar;src/gadgets.jar -m Lab07.pop/Shop.Main
    //
    //
    public static void main(String[] args) {
        if (System.getSecurityManager() == null) {
            System.setProperty("java.security.policy","grantallpolicies.policy");
            System.setSecurityManager(new RMISecurityManager());
        }
        System.out.println("Podaj port: ");
        Scanner scanner = new Scanner(System.in);
        int port = scanner.nextInt();
        try {
             IShop shop = new Shop();
            UnicastRemoteObject.exportObject(shop, 0);
            Registry registry = LocateRegistry.createRegistry(port);
            registry.bind("Shop", shop);
            ShopGui gui = new ShopGui(shop);
            gui.setVisible(true);
        }catch(Exception e){
            System.err.println("Server exception: " + e);
            e.printStackTrace();
        }

    }
}
