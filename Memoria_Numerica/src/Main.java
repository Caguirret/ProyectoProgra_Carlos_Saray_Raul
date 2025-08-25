import UI.PrincipalForm;

import javax.swing.*;

public class Main {
    public static void main(String[] args){
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {}
            PrincipalForm principal = new PrincipalForm();
            principal.setVisible(true);
        });
    }
}
