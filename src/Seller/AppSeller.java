package Seller;

import Shop.ShopGui;
import interfaces.IShop;
import model.OrderLine;
import model.Status;
import model.SubmittedOrder;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.util.ArrayList;

public class AppSeller extends JFrame {
    JPanel contentpane = new JPanel();

    public AppSeller(IShop shop) {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 330, 400);
        getContentPane().setLayout(null);
        getContentPane().setLayout(null);
        getContentPane().setLayout(null);
        getContentPane().setLayout(new GridLayout(1, 0, 0, 0));
        getContentPane().setLayout(new GridLayout(1, 0, 0, 0));


        getContentPane().add(contentpane);
        contentpane.setLayout(null);

        JPanel listPanel = new JPanel();
        listPanel.setBounds(10, 72, 296, 291);
        listPanel.setBackground(Color.white);
        contentpane.add(listPanel);

        JButton btnNewButton_1 = new JButton("Refresh");
        btnNewButton_1.setBounds(10, 10, 140, 21);
        btnNewButton_1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                listPanel.removeAll();
                try {
                    for(int i = 0; i < shop.getSubmittedOrders().size(); i++){
                        if (shop.getSubmittedOrders().get(i).getStatus() == Status.NEW){
                            shop.setStatus(i, Status.PROCESSING);
                        }
                    }

                } catch (RemoteException ex) {
                    throw new RuntimeException(ex);
                }
                JList<SubmittedOrder> statusList = new JList<>();
                try {
                    statusList = new JList<>(shop.getSubmittedOrders().toArray(new SubmittedOrder[0]));
                    statusList.setSelectionMode(JList.HORIZONTAL_WRAP);
                    statusList.setLayoutOrientation(JList.HORIZONTAL_WRAP);
                    statusList.setVisibleRowCount(-1);
                    statusList.setLayoutOrientation(JList.VERTICAL);
                    statusList.setCellRenderer(new RendererStatus());
                } catch (Exception ex1) {
                    ex1.printStackTrace();
                }
                listPanel.add(statusList);
                listPanel.repaint();
                listPanel.revalidate();
            }
        });
        contentpane.add(btnNewButton_1);

        JButton btnNewButton_2 = new JButton("Check order");
        btnNewButton_2.setBounds(160, 10, 146, 21);
        btnNewButton_2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    int id_submit = Integer.parseInt(JOptionPane.showInputDialog("Give Id"));
                    if (id_submit > shop.getSubmittedOrders().size() || id_submit == 0) {
                        JOptionPane.showMessageDialog(null, "Write correct ID", "", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        ArrayList<String> orderToString = new ArrayList<>();
                        orderToString.add("Order Id: " + shop.getSubmittedOrders().get(id_submit-1).getId() + " | Client ID: " + shop.getSubmittedOrders().get(id_submit-1).getOrder().getClientID() + " | Total cost: " + shop.getSubmittedOrders().get(id_submit-1).getOrder().getCost());
                        for (OrderLine order: shop.getSubmittedOrders().get(id_submit-1).getOrder().getOll()) {
                            orderToString.add(order.getIt().getName() + " | Quantity: " + order.getQuantity() + " | " + order.getAdvert());
                        }
                        String msg = "";
                        for (String msg2 : orderToString){
                            msg = msg + msg2 + "\n";
                        }
                        JOptionPane.showMessageDialog(null, msg, "", JOptionPane.INFORMATION_MESSAGE);
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Write correct ID", "", JOptionPane.INFORMATION_MESSAGE);
                } catch (RemoteException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        contentpane.add(btnNewButton_2);

        JButton btnNewButton = new JButton("Answer order");
        btnNewButton.setBounds(10, 41, 140, 21);
        btnNewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    int id_submit = Integer.parseInt(JOptionPane.showInputDialog("Give Id"));
                    if (id_submit > shop.getSubmittedOrders().size() + 1 && shop.getSubmittedOrders().get(id_submit - 1).getStatus() == Status.PROCESSING ) {
                        JOptionPane.showMessageDialog(null, "Write correct ID", "", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        shop.setStatus(id_submit-1,Status.READY);

                        listPanel.removeAll();
                        JList<SubmittedOrder> statusList = new JList<>();
                        try {
                            statusList = new JList<>(shop.getSubmittedOrders().toArray(new SubmittedOrder[0]));
                            statusList.setSelectionMode(JList.HORIZONTAL_WRAP);
                            statusList.setLayoutOrientation(JList.HORIZONTAL_WRAP);
                            statusList.setVisibleRowCount(-1);
                            statusList.setLayoutOrientation(JList.VERTICAL);
                            statusList.setCellRenderer(new RendererStatus());
                        } catch (Exception ex1) {
                            ex1.printStackTrace();
                        }
                        listPanel.add(statusList);
                        listPanel.repaint();
                        listPanel.revalidate();
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Write correct ID", "", JOptionPane.INFORMATION_MESSAGE);
                } catch (RemoteException ex) {
                    throw new RuntimeException(ex);
                }
            };
        });
        contentpane.add(btnNewButton);

        JButton btnSendInfoClient = new JButton("Deliver(ready only)");
        btnSendInfoClient.setBounds(160, 41, 146, 21);
        btnSendInfoClient.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    int id_submit = Integer.parseInt(JOptionPane.showInputDialog("Give Id"));
                    if (shop.getStatus(id_submit-1) == Status.READY) {
                        shop.setStatus(id_submit-1 , Status.DELIVERED);
                    }else{
                        JOptionPane.showMessageDialog(null, "Order not ready to be delivered", "", JOptionPane.INFORMATION_MESSAGE);
                    }
                    listPanel.removeAll();
                    JList<SubmittedOrder> statusList = new JList<>();
                    try {
                        statusList = new JList<>(shop.getSubmittedOrders().toArray(new SubmittedOrder[0]));
                        statusList.setSelectionMode(JList.HORIZONTAL_WRAP);
                        statusList.setLayoutOrientation(JList.HORIZONTAL_WRAP);
                        statusList.setVisibleRowCount(-1);
                        statusList.setLayoutOrientation(JList.VERTICAL);
                        statusList.setCellRenderer(new RendererStatus());
                    } catch (Exception ex1) {
                        ex1.printStackTrace();
                    }
                    listPanel.add(statusList);
                    listPanel.repaint();
                    listPanel.revalidate();
                }catch(NumberFormatException ex1){
                    JOptionPane.showMessageDialog(null, "Write correct ID", "", JOptionPane.INFORMATION_MESSAGE);
                } catch (RemoteException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        contentpane.add(btnSendInfoClient);
    }

    public class RendererStatus extends JLabel implements ListCellRenderer<SubmittedOrder> {
        public RendererStatus() {
            setOpaque(true);
        }

        @Override
        public Component getListCellRendererComponent(JList<? extends SubmittedOrder> list, SubmittedOrder value, int index, boolean isSelected, boolean cellHasFocus) {
            setText("Order:" + value.getId() + " | " + value.getStatus());
            return this;
        }
    }
}
