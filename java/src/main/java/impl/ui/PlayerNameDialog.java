package impl.ui;
import javax.swing.*;
import java.awt.*;

public class PlayerNameDialog {

    public static String[] askPlayerNames() {
        JTextField p1Field = new JTextField("player 1");
        JTextField p2Field = new JTextField("player 2");

        JPanel panel = new JPanel(new GridLayout(2, 2));
        panel.add(new JLabel("player 1 name:"));
        panel.add(p1Field);
        panel.add(new JLabel("player 2 name:"));
        panel.add(p2Field);

        int result = JOptionPane.showConfirmDialog(
                null, panel, "enter players name", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            return new String[]{ p1Field.getText(), p2Field.getText() };
        } else {
            return new String[]{ "player 1", "player 2" };
        }
    }
}
