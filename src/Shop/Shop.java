package Shop;

import interfaces.IShop;
import interfaces.IStatusListener;
import model.*;

import java.awt.font.ShapeGraphicAttribute;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Shop implements IShop {
    //listy,operacje
    int id = 0;
    HashMap<Integer,String> clientList = new HashMap<>();
    ArrayList<ItemType> itemList = new ArrayList<>();
    ArrayList<SubmittedOrder> ordersArray = new ArrayList<>();
    HashMap<Integer, IStatusListener> subscriberList = new HashMap<>();
    HashMap<Integer, SubmittedOrder> ordersOfClient = new HashMap<>();
    public Shop(){
    }

    @Override
    public int register(Client c) throws RemoteException {
        id++;
        clientList.put(id,c.getName());
        return id;
    }

    @Override
    public List<ItemType> getItemList() throws RemoteException {
        if (itemList.isEmpty()){
            ItemType item1 = new ItemType();
            item1.setCategory(1);item1.setName("Gra typu shooter");item1.setPrice(50);
            itemList.add(item1);

            ItemType item2 = new ItemType();
            item2.setCategory(1);item2.setName("Gra typu RPG");item2.setPrice(30);
            itemList.add(item2);

            ItemType item3 = new ItemType();
            item3.setCategory(1);item3.setName("Gra strategiczna");item3.setPrice(70);
            itemList.add(item3);
        }
        return itemList;
    }

    @Override
    public int placeOrder(Order o) throws RemoteException {
        SubmittedOrder order = new SubmittedOrder(o);
        int idOrder = order.getId();
        ordersArray.add(order);
        return idOrder;
    }

    @Override
    public List<SubmittedOrder> getSubmittedOrders() throws RemoteException {
        return ordersArray;
    }

    @Override
    public boolean setStatus(int id, Status s) throws RemoteException {
        ordersArray.get(id).setStatus(s);
        subscriberList.get(ordersArray.get(id).getOrder().getClientID()).statusChanged(id,s);
        return true;
    }

    @Override
    public Status getStatus(int id) throws RemoteException {
        return ordersArray.get(id).getStatus();
    }

    @Override
    public boolean subscribe(IStatusListener ic, int clientId) throws RemoteException {
        subscriberList.put(clientId,ic);
        return false;
    }

    @Override
    public boolean unsubscribe(int clientId) throws RemoteException {
        subscriberList.remove(clientId);
        return false;
    }
}
