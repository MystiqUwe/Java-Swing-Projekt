package ablesebogen;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.function.Function;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class Util {

	public static void handleTabOrder(ArrayList<JComponent> list,Function<Void,Boolean> saveMethod) {
		if (list==null || list.size()<2) {
			return;
		}
		for (int i = 0; i < list.size()-1; i++) {
			final JComponent nextItem=list.get(i+1);
			list.get(i).addKeyListener(new KeyAdapter() {
				@Override
				public void keyPressed(KeyEvent e) {
					if (e.getKeyCode() == KeyEvent.VK_ENTER) {
						nextItem.requestFocus();
					}
				}
			});
		}
		
		list.get(list.size()-1).addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					if (saveMethod.apply(null)) {
						list.get(0).requestFocus();
					}

				}
			}
		});
	}

	private static JFrame dialogFrame = new JFrame();
	public static void errorMessage(String message) {
		JOptionPane.showMessageDialog(dialogFrame, message, "", JOptionPane.ERROR_MESSAGE);
	}
	public static boolean optionMessage(String message) {
		//int result = 0;
		int result = JOptionPane.showConfirmDialog(dialogFrame, message, "",
				JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
		return JOptionPane.OK_OPTION==result;
	}
}
