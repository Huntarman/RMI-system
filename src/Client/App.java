package Client;

import interfaces.IShop;
import interfaces.IStatusListener;
import model.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;

public class App extends JFrame  {
    private JPanel contentPane;
    private JTextField numOfItemsToAdd;
    private JTextField selectedItem;
    private JTextField nameInputField;
    private JTextField textField_3;

    private ArrayList<ItemType> stuff = new ArrayList<>();
    private ArrayList<OrderLine> toOrder = new ArrayList<OrderLine>();

    private ArrayList<ClientStatus> yourIDorders = new ArrayList<>();
    private boolean subscribed = false;
    private boolean registered = false;
    private int id, selectedItemNum;

    ActionListener refreshListener;
    IStatusListener listener = (id, status) -> { refreshListener.actionPerformed(new ActionEvent(new Object(),0,""));
    };
    private String name;
    private double totalSum = 0,balance = 500.0;
    private Client client = new Client();
    private JPanel panellistOfItems = new JPanel();
    private JPanel panelKoszyk = new JPanel();
    JPanel panelOrdersStatus = new JPanel();
    private Order koszyk;

    public App(IShop shop) {


        panellistOfItems.setBackground(Color.white);
        panelKoszyk.setBackground(Color.white);
        panelOrdersStatus.setBackground(Color.white);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 600, 600);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

        setContentPane(contentPane);
        contentPane.setLayout(null);


        JLabel quantityLabel = new JLabel("Quantity:");
        quantityLabel.setBounds(10, 176, 62, 13);
        contentPane.add(quantityLabel);

        JLabel unimportantLabel = new JLabel("Item currently selected:");
        unimportantLabel.setBounds(10, 131, 150, 13);
        contentPane.add(unimportantLabel);

        selectedItem = new JTextField();//selected item
        selectedItem.setBounds(10, 147, 150, 19);
        contentPane.add(selectedItem);
        selectedItem.setColumns(10);


        JLabel koszykLabel = new JLabel("Koszyk:");
        koszykLabel.setBounds(10, 226, 45, 13);
        contentPane.add(koszykLabel);

        numOfItemsToAdd = new JTextField("");//quantity of items
        numOfItemsToAdd.setBounds(10, 197, 38, 19);
        contentPane.add(numOfItemsToAdd);
        numOfItemsToAdd.setColumns(10);


        panellistOfItems.setBounds(202, 42, 374, 511);
        contentPane.add(panellistOfItems);

        panelKoszyk.setBounds(10, 249, 150, 169);
        contentPane.add(panelKoszyk);

        nameInputField = new JTextField("");//name
        nameInputField.setBounds(9, 20, 96, 19);
        contentPane.add(nameInputField);
        nameInputField.setColumns(10);

        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setBounds(8, 8, 45, 13);
        contentPane.add(nameLabel);

        JLabel total = new JLabel("Total:");
        total.setBounds(10, 428, 35, 13);
        contentPane.add(total);

        JLabel totalNum = new JLabel("");
        totalNum.setBounds(47, 428, 45, 13);
        contentPane.add(totalNum);

        JLabel userBalance = new JLabel("Balance:");
        userBalance.setBounds(107, 176, 53, 13);
        contentPane.add(userBalance);

        JLabel userBalanceNumber = new JLabel();
        userBalanceNumber.setBounds(158, 176, 45, 13);
        contentPane.add(userBalanceNumber);

        JLabel user_name = new JLabel("User:");
        user_name.setHorizontalAlignment(SwingConstants.CENTER);
        user_name.setBounds(10, 40, 182, 13);
        contentPane.add(user_name);

        panelOrdersStatus.setBounds(10, 476, 150, 77);
        contentPane.add(panelOrdersStatus);

        JButton register_button = new JButton("<font color=red>AAA</font>");
        register_button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (!registered) {
                    if (nameInputField.getText().isEmpty() || Objects.equals(nameInputField.getText(), "Enter a valid name")) {
                        nameInputField.setText("Enter a valid name");
                    } else {
                        name = nameInputField.getText();
                        client.setName(name);
                        try {
                            id = shop.register(client);

                        } catch (RemoteException ex) {
                            throw new RuntimeException(ex);
                        }
                        user_name.setText("User: " + name + "      Id: " + id);
                        registered = true;
                        balance = 500.0;
                        userBalance.setText(String.valueOf(balance));
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "You're already registered!", "", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
        register_button.setBounds(107, 19, 85, 21);
        contentPane.add(register_button);


        JButton subscribeButton = new JButton("Subscribe");
        subscribeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (registered && !subscribed) {
                    try {
                        shop.subscribe((IStatusListener) UnicastRemoteObject.exportObject(listener, 0), id);
                    } catch (RemoteException ex) {
                        throw new RuntimeException(ex);
                    }
                    subscribed = true;
                    koszyk = new Order(id);

                } else if (!registered) {
                    JOptionPane.showMessageDialog(null, "You're not registered!", "", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "You're already subscribed!", "", JOptionPane.INFORMATION_MESSAGE);
                }

            }
        });
        subscribeButton.setBounds(202, 19, 182, 21);
        contentPane.add(subscribeButton);

        JButton unsubscribeButton = new JButton("Unsubscribe");
        unsubscribeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (registered && subscribed) {
                    try {
                        shop.unsubscribe(id);
                        UnicastRemoteObject.unexportObject(listener, true);
                    } catch (RemoteException ex) {
                        throw new RuntimeException(ex);
                    }
                    subscribed = false;
                    JOptionPane.showMessageDialog(null, "If you're not subscribed, you won't receive updates!", "", JOptionPane.INFORMATION_MESSAGE);
                } else if (!registered) {
                    JOptionPane.showMessageDialog(null, "You're not registered!", "", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "You're already unsubscribed!", "", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
        unsubscribeButton.setBounds(394, 19, 182, 21);
        contentPane.add(unsubscribeButton);

        JButton show_item_list = new JButton("Show item list");
        show_item_list.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (registered) {
                    JList<ItemType> listOfItems = new JList<>();
                    panellistOfItems.removeAll();
                    try {
                        stuff = (ArrayList<ItemType>) shop.getItemList();
                        listOfItems = new JList<>(shop.getItemList().toArray(new ItemType[0]));
                        listOfItems.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                        listOfItems.setLayoutOrientation(JList.HORIZONTAL_WRAP);
                        listOfItems.setVisibleRowCount(-1);
                        listOfItems.setLayoutOrientation(JList.VERTICAL);
                        listOfItems.setCellRenderer(new RendererItem());
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    selectedItemNum = 0;
                    selectedItem.setText(stuff.get(0).getName() + " | " + stuff.get(0).getPrice());
                    panellistOfItems.add(listOfItems);
                    panellistOfItems.repaint();
                    panellistOfItems.revalidate();
                } else {
                    JOptionPane.showMessageDialog(null, "You're not registered!", "", JOptionPane.INFORMATION_MESSAGE);
                }
            }

        });
        show_item_list.setBounds(10, 55, 182, 21);
        contentPane.add(show_item_list);

        JButton select_previous_item = new JButton("Select previous item");
        select_previous_item.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (registered && !stuff.isEmpty()) {
                    selectedItemNum = ((selectedItemNum - 1) % stuff.size() + stuff.size()) % stuff.size();
                    selectedItem.setText(stuff.get(selectedItemNum).getName() + " | " + stuff.get(selectedItemNum).getPrice());
                } else if (!registered) {
                    JOptionPane.showMessageDialog(null, "You're not registered!", "", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "Look up the item list first!", "", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
        select_previous_item.setBounds(10, 80, 182, 21);
        contentPane.add(select_previous_item);

        JButton select_next_item = new JButton("Select next item");
        select_next_item.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (registered && !stuff.isEmpty() ) {
                    selectedItemNum = (selectedItemNum + 1) % stuff.size();
                    selectedItem.setText(stuff.get(selectedItemNum).getName() + " | " + stuff.get(selectedItemNum).getPrice());
                } else if (!registered){
                    JOptionPane.showMessageDialog(null, "You're not registered!", "", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "Look up the item list first!", "", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
        select_next_item.setBounds(10, 100, 182, 21);
        contentPane.add(select_next_item);

        JButton add_to_cart = new JButton("Add to cart");
        add_to_cart.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (registered) {
                    if (!numOfItemsToAdd.getText().isEmpty() && !numOfItemsToAdd.getText().equals("0")) {
                        try {
                            int quantity = Integer.parseInt(numOfItemsToAdd.getText());
                            String msg = JOptionPane.showInputDialog("Would you like to write a message? Leave blank if not");
                            if (msg.isEmpty()) msg = "";
                            toOrder.add(new OrderLine(stuff.get(selectedItemNum), quantity, msg));
                            totalSum += stuff.get(selectedItemNum).getPrice() * quantity;
                            totalNum.setText(String.valueOf(totalSum));
                            panelKoszyk.removeAll();
                            JList<OrderLine> koszykItems = new JList<>();
                            try {
                                koszykItems = new JList<>(toOrder.toArray(new OrderLine[0]));
                                koszykItems.setSelectionMode(JList.HORIZONTAL_WRAP);
                                koszykItems.setLayoutOrientation(JList.HORIZONTAL_WRAP);
                                koszykItems.setVisibleRowCount(-1);
                                koszykItems.setLayoutOrientation(JList.VERTICAL);
                                koszykItems.setCellRenderer(new RendererKoszyk());
                            } catch (Exception ex1) {
                                ex1.printStackTrace();
                            }
                            panelKoszyk.add(koszykItems);
                            panelKoszyk.repaint();
                            panelKoszyk.revalidate();
                        } catch (NumberFormatException ex) {
                            JOptionPane.showMessageDialog(null, "Value must be an integer", "", JOptionPane.INFORMATION_MESSAGE);
                        }

                    } else if (numOfItemsToAdd.getText().equals("0")) {
                        JOptionPane.showMessageDialog(null, "Quantity cannot be 0", "", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(null, "Specify quantity", "", JOptionPane.INFORMATION_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "You're not registered!", "", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
        add_to_cart.setBounds(58, 196, 102, 21);
        contentPane.add(add_to_cart);

        JButton clearKoszykButton = new JButton("Clear");
        clearKoszykButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (registered) {
                    if (!toOrder.isEmpty()) {
                        //for (OrderLine order: toOrder) {
                        //  koszyk.removeOrderLine(order);
                        //}
                        toOrder.clear();
                        totalSum = 0;
                        totalNum.setText(String.valueOf(totalSum));
                        panelKoszyk.removeAll();
                        JList<OrderLine> koszykItems = new JList<>();
                        try {
                            koszykItems = new JList<>(koszyk.getOll().toArray(new OrderLine[0]));
                            koszykItems.setSelectionMode(JList.HORIZONTAL_WRAP);
                            koszykItems.setLayoutOrientation(JList.HORIZONTAL_WRAP);
                            koszykItems.setVisibleRowCount(-1);
                            koszykItems.setLayoutOrientation(JList.VERTICAL);
                            koszykItems.setCellRenderer(new RendererKoszyk());
                        } catch (Exception ex1) {
                            ex1.printStackTrace();
                        }
                        panelKoszyk.add(koszykItems);
                        panelKoszyk.repaint();
                        panelKoszyk.revalidate();
                    } else {
                        JOptionPane.showMessageDialog(null, "Order is empty", "", JOptionPane.INFORMATION_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "You're not registered!", "", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
        clearKoszykButton.setBounds(98, 424, 94, 21);
        contentPane.add(clearKoszykButton);

        JButton sendorder_button = new JButton("Order");
        sendorder_button.setBounds(65, 222, 95, 21);
        sendorder_button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (registered) {
                    if (!toOrder.isEmpty() && totalSum<=balance) {
                        for (OrderLine order : toOrder) {
                            koszyk.addOrderLine(order);
                        }
                        try {
                            int id = shop.placeOrder(koszyk);
                            yourIDorders.add(new ClientStatus(id));
                        } catch (RemoteException ex) {
                            throw new RuntimeException(ex);
                        }
                        koszyk.getOll().clear();
                        totalNum.setText("0");
                        balance = balance - totalSum;
                        userBalance.setText(String.valueOf(balance));
                        panelKoszyk.removeAll();
                        JList<OrderLine> koszykItems = new JList<>();
                        try {
                            koszykItems = new JList<>(koszyk.getOll().toArray(new OrderLine[0]));
                            koszykItems.setSelectionMode(JList.HORIZONTAL_WRAP);
                            koszykItems.setLayoutOrientation(JList.HORIZONTAL_WRAP);
                            koszykItems.setVisibleRowCount(-1);
                            koszykItems.setLayoutOrientation(JList.VERTICAL);
                            koszykItems.setCellRenderer(new RendererKoszyk());
                        } catch (Exception ex1) {
                            ex1.printStackTrace();
                        }
                        panelKoszyk.add(koszykItems);
                        panelKoszyk.repaint();
                        panelKoszyk.revalidate();
                        if (!subscribed){
                            JOptionPane.showMessageDialog(null, "You're not registered. Be carefu. You won't be able to track order status!", "", JOptionPane.INFORMATION_MESSAGE);
                        }
                        panelOrdersStatus.removeAll();
                        JList<ClientStatus> statusList = new JList<>();
                        try {
                            statusList = new JList<>(yourIDorders.toArray(new ClientStatus[0]));
                            statusList.setSelectionMode(JList.HORIZONTAL_WRAP);
                            statusList.setLayoutOrientation(JList.HORIZONTAL_WRAP);
                            statusList.setVisibleRowCount(-1);
                            statusList.setLayoutOrientation(JList.VERTICAL);
                            statusList.setCellRenderer(new RendererStatus());
                        } catch (Exception ex1) {
                            ex1.printStackTrace();
                        }
                        panelOrdersStatus.add(statusList);
                        panelOrdersStatus.repaint();
                        panelOrdersStatus.revalidate();
                    } else if(toOrder.isEmpty()){
                        JOptionPane.showMessageDialog(null, "Order is empty", "", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(null, "Total exceeds balance", "", JOptionPane.INFORMATION_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "You're not registered!", "", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
        contentPane.add(sendorder_button);

        refreshListener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (registered && subscribed) {
                    try {
                            for(int i = 0; i<yourIDorders.size(); i++){
                                yourIDorders.get(i).setStatus(shop.getStatus(yourIDorders.get(i).getId() - 1));
                            }
                            panelOrdersStatus.removeAll();
                            JList<ClientStatus> statusList = new JList<>();
                            try {
                                statusList = new JList<>(yourIDorders.toArray(new ClientStatus[0]));
                                statusList.setSelectionMode(JList.HORIZONTAL_WRAP);
                                statusList.setLayoutOrientation(JList.HORIZONTAL_WRAP);
                                statusList.setVisibleRowCount(-1);
                                statusList.setLayoutOrientation(JList.VERTICAL);
                                statusList.setCellRenderer(new RendererStatus());
                            } catch (Exception ex1) {
                                ex1.printStackTrace();
                            }
                            panelOrdersStatus.add(statusList);
                            panelOrdersStatus.repaint();
                            panelOrdersStatus.revalidate();
                    }catch (Exception ex1) {
                        ex1.printStackTrace();
                    }
                }else if(!registered){
                    JOptionPane.showMessageDialog(null, "You're not registered!", "", JOptionPane.INFORMATION_MESSAGE);
                }else{
                    JOptionPane.showMessageDialog(null, "You're not subscribed!", "", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        };
    }

    public class RendererItem extends JLabel implements ListCellRenderer<ItemType> {

        public RendererItem() {
            setOpaque(true);
        }

        @Override
        public Component getListCellRendererComponent(JList<? extends ItemType> list, ItemType value, int index, boolean isSelected, boolean cellHasFocus) {
            setText(value.getName() + " | " + value.getPrice());
            return this;
        }
    }

    public class RendererKoszyk extends JLabel implements ListCellRenderer<OrderLine> {
        public RendererKoszyk() {
            setOpaque(true);
        }

        @Override
        public Component getListCellRendererComponent(JList<? extends OrderLine> list, OrderLine value, int index, boolean isSelected, boolean cellHasFocus) {
            setText(value.getIt().getName() + " | " + value.getQuantity() + " | " + value.getAdvert());
            return this;
        }
    }

    public class RendererStatus extends JLabel implements ListCellRenderer<ClientStatus> {
        public RendererStatus() {
            setOpaque(true);
        }

        @Override
        public Component getListCellRendererComponent(JList<? extends ClientStatus> list, ClientStatus value, int index, boolean isSelected, boolean cellHasFocus) {
                setText("Order:" + value.getId() + " | " + value.getStatus());
            return this;
        }
    }
}