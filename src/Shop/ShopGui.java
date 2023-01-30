package Shop;

import interfaces.IShop;
import model.Status;
import model.SubmittedOrder;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;

public class ShopGui extends JFrame {
    private JPanel contentPane;
    private JPanel tablePanel = new JPanel();;
    public ShopGui(IShop shop){
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 330, 300);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

        setContentPane(contentPane);
        contentPane.setLayout(null);

        tablePanel.setBackground(Color.white);
        tablePanel.setBounds(125, 10, 169, 243);
        contentPane.add(tablePanel);


        JButton refreshFromClient = new JButton("Refresh");
        refreshFromClient.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                tablePanel.removeAll();
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
               tablePanel.add(statusList);
                tablePanel.repaint();
                tablePanel.revalidate();
            }
        });
        refreshFromClient.setBounds(10, 36, 85, 21);
        contentPane.add(refreshFromClient);


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
