//Alexander Urbanyak
//CS1302
//Color Assistant
//displays pixel RGB, sets that color as background
//works outside the windows borders

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Robot;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import static java.lang.Thread.sleep;

public class ColorAssistant {
    public ColorAssistant(String s){
             try {
                    JFrame frame = new JFrame(s);
                    BorderLayout layout = new BorderLayout();
                    frame.setLayout(layout);
                    frame.setSize(600, 300);
                    frame.add(new LabelText(), BorderLayout.NORTH);
                    frame.add(new SetColor(), BorderLayout.CENTER);
                  //window starts out the center of the screen
                    frame.setLocationRelativeTo(null);
                    frame.setVisible(true);
                    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                } catch (Exception exp) {
                    exp.printStackTrace();
                }
    }
                
        public class LabelText extends JPanel implements MouseMonitorListener {
        public final Robot robot;
      //labels for text and RGB value
        public final JLabel red;
        public final JLabel rvalue;
        public final JLabel green;
        public final JLabel gvalue;
        public final JLabel blue;
        public final JLabel bvalue;
        public LabelText() throws AWTException {
            red = new JLabel();
            add(red);
            rvalue = new JLabel();
            add(rvalue);
            green = new JLabel();
            add(green);
            gvalue = new JLabel();
            add(gvalue);
            blue = new JLabel();
            add(blue);
            bvalue = new JLabel();
            add(bvalue);
            robot = new Robot();
          //cursor location
            Point p = MouseInfo.getPointerInfo().getLocation();
            updateText(p);
            MouseMonitor monitor = new MouseMonitor();
            monitor.setMouseMonitorListener(this);
            monitor.start();
        }
        public void updateText(Point p) {
            setBackground(Color.black);
            Color pixelColor = robot.getPixelColor(p.x, p.y);
            red.setText("RED : ");
            rvalue.setText("" + pixelColor.getRed() + "    ");
            green.setText("GREEN : ");
            gvalue.setText("" + pixelColor.getGreen() + "    ");
            blue.setText("BLUE : ");
            bvalue.setText("" + pixelColor.getBlue());
            //
            red.setFont(new Font("Arial", Font.PLAIN, 20));
            rvalue.setFont(new Font("Arial", Font.PLAIN, 20));
            red.setForeground (Color.red);
            rvalue.setForeground (Color.red);
            //
            green.setFont(new Font("Arial", Font.PLAIN, 20));
            gvalue.setFont(new Font("Arial", Font.PLAIN, 20));
            green.setForeground(Color.green);
            gvalue.setForeground (Color.green);
            //
            blue.setFont(new Font("Arial", Font.PLAIN, 20));
            bvalue.setFont(new Font("Arial", Font.PLAIN, 20));
            blue.setForeground (Color.blue);
            bvalue.setForeground (Color.blue);
        }
      //update text and color 
        @Override
        public void mousePositionChanged(Point p) {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    updateText(p);
                }
            });
        }
    }
    public class SetColor extends JPanel implements MouseMonitorListener {
        public final Robot robot;
        public SetColor() throws AWTException {
            robot = new Robot();
            Point p = MouseInfo.getPointerInfo().getLocation();
            updateColor(p);
            MouseMonitor monitor = new MouseMonitor();
            monitor.setMouseMonitorListener(this);
            monitor.start();
        }
        public void updateColor(Point p) {
            Color pixelColor = robot.getPixelColor(p.x, p.y);
            setBackground(pixelColor);
        }
        @Override
        public void mousePositionChanged(Point p) {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    updateColor(p);
                }
            });
        }
    }
    public interface MouseMonitorListener {
        public void mousePositionChanged(Point p);
    }

    public static class MouseMonitor extends Thread {
        private MouseMonitorListener listener;
        public MouseMonitor() {
        }
        public void setMouseMonitorListener(MouseMonitorListener listener) {
            this.listener = listener;
        }
        public MouseMonitorListener getMouseMonitorListener() {
            return listener;
        }
        @Override
        public void run() {
           Point last = MouseInfo.getPointerInfo().getLocation();
            while (true) {
                try {
                    sleep(250);
                } catch (InterruptedException ex) {
                }

                Point current = MouseInfo.getPointerInfo().getLocation();
                if (!current.equals(last)) {
                    last = current;
                    MouseMonitorListener listener = getMouseMonitorListener();
                    if (listener != null) {
                        listener.mousePositionChanged((Point)last.clone());
                    }
                }

            }
        }
    }
}
