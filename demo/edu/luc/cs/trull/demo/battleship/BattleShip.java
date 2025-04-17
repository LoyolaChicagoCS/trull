package edu.luc.cs.trull.demo.battleship;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import edu.luc.cs.trull.AwaitOne;
import edu.luc.cs.trull.Component;
import edu.luc.cs.trull.Composite;
import edu.luc.cs.trull.Done;
import edu.luc.cs.trull.Emit;
import edu.luc.cs.trull.EmitComponent;
import edu.luc.cs.trull.EventValuator;
import edu.luc.cs.trull.Local;
import edu.luc.cs.trull.Loop;
import edu.luc.cs.trull.Rename;
import edu.luc.cs.trull.Switch;
import edu.luc.cs.trull.demo.Applet;

/**
 * A simple implementation of two ships.
 * The user can place the two ships and shoot at them from either ship.
 *
 * <PRE>
 *  Main:
 *
 *    local PLACE1 PLACE2 in
 *       rename PLACE to PLACE1 in BattleShip(5, 2)
 *    || rename PLACE to PLACE2 in BattleShip(3, 3)
 *
 *  BattleShip:
 *
 *    await PLACE -> { place ship }
 *      loop
 *        await SHOT ->
 *          switch { miss/hit/sunk ? }
 *            hit  -> emit HIT
 *            sunk -> emit SUNK
 *            miss -> done
 * </PRE>
 * <P>
 * <A HREF="../../../../demo/triveni/demo/battleship/BattleShip.java">view source</A>
 * <P>
 * <A HREF="doc-files/battleship.html">run applet</A>
 */

public class BattleShip extends Applet implements EventLabels {

    public void init() {
        super.init();
        String PLACE1 = "Place1";
        String PLACE2 = "Place2";
        setComponent(
            new Local(new String[] { PLACE1, PLACE2 },
                new Composite(new Component[] {
                    new Rename(PLACE, PLACE1, new Ship(5, 2)),
                    new WindowShip(PLACE1),
                    new Rename(PLACE, PLACE2, new Ship(3, 3)),
                    new WindowShip(PLACE2)
                })
            )
        );
    }

    public static void main(String[] args) {
        runInFrame("Battleship", new BattleShip());
    }
}

/**
 * A ship of a given length and witdh.
 */

class Ship extends AwaitOne implements EventLabels {

    int x = -1;
    int y = -1;

    int length;
    int width;

    boolean[][] shot;
    int left;

    public Ship(int l, int w) {
        length = Math.max(l, w);
        width = Math.min(l, w);

        shot = new boolean[length][length]; // to handle horizontal/vertical
        left = length * width;

        setStartEvent(PLACE);
        setStartAction(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                if (x >= 0 && y >= 0) {
                    return;
                }
                PlaceData pevt = (PlaceData) evt.getNewValue();
                x = pevt.getX();
                y = pevt.getY();
                if (pevt.isHorizontal()) {
                    int temp = Ship.this.length;
                    Ship.this.length = Ship.this.width;
                    Ship.this.width = temp;
                }
                System.out.println("placing ship at <" + x + ", " + y + "> " +
                    (pevt.isHorizontal() ? "horizontally" : "vertically"));
            }
        });
        setBody(new Loop(
            new AwaitOne(SHOT,
                new Switch(
                    new EventValuator() {
                        public int apply(PropertyChangeEvent evt) {
                            ShotData sevt = (ShotData) evt.getNewValue();
                            int sx = sevt.getX();
                            int sy = sevt.getY();
                            if (x <= sx && sx < x + width &&
                                y <= sy && sy < y + length &&
                                ! shot[sx - x][sy - y]
                            ) {
                                System.out.println("ouch!");
                                shot[sx - x][sy - y] = true;
                                left --;
                                return left <= 0 ? 0 : 1;
                            }
                            return 2;
                        }
                    },
                    new Component[] {
                        new Emit(SUNK), /* 0 */
                        new Emit(HIT),  /* 1 */
                        new Done()      /* 2 */
                    }
                )
            )
        ));
    }
}

/**
 * Event labels for the battle ship demo.
 */

interface EventLabels {
    final static String PLACE = "Place";
    final static String HIT   = "Hit";
    final static String SUNK  = "Sunk";
    final static String SHOT  = "Shot";
}

/**
 * An events that carries coordinates.
 */

class PositionData {

    int x, y;

    public PositionData(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}


/**
 * An event for firing a shot.
 */

class ShotData extends PositionData {

    public ShotData(int x, int y) {
        super(x, y);
    }
}

/**
 * An event for placing a ship.
 */

class PlaceData extends PositionData {

    boolean horizontal;

    public PlaceData(int x, int y, boolean horizontal) {
        super(x, y);
        this.horizontal = horizontal;
    }

    public boolean isHorizontal() {
        return horizontal;
    }
}


/**
 * User interface for entering Place and Shot events.
 */

class WindowShip extends EmitComponent implements EventLabels {

    static int windowNumber = 0;

    JCheckBox hitIndicator  = new JCheckBox("hit", false);
    JCheckBox sunkIndicator = new JCheckBox("sunk", false);

    public WindowShip(final String placeLabel) {

        final JFrame frame = new JFrame("Event Source");

        final JButton placeButton = new JButton(placeLabel);
        final JButton shootButton = new JButton("Shoot");

        final JTextField placeX = new JTextField(2);
        final JTextField placeY = new JTextField(2);

        final JRadioButton verticalOrientation = new JRadioButton("vertical", false);
        final JRadioButton horizontalOrientation = new JRadioButton("horizontal", true);
        final ButtonGroup orientationGroup = new ButtonGroup();

        orientationGroup.add(verticalOrientation);
        orientationGroup.add(horizontalOrientation);

        hitIndicator.setEnabled(false);
        sunkIndicator.setEnabled(false);

        placeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                try {
                    firePropertyChange(placeLabel,
                        new PlaceData(toInt(placeX), toInt(placeY),
                                       horizontalOrientation.isSelected()
                        )
                    );
                } catch (NumberFormatException e) {
                }
            }
        });

        shootButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                try {
                    hitIndicator.setSelected(false);
                    sunkIndicator.setSelected(false);
                    firePropertyChange(SHOT,
                        new ShotData(toInt(placeX), toInt(placeY))
                    );
                } catch (NumberFormatException e) {
                }
            }
        });

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel contentPane = new JPanel();

        contentPane.setLayout(new FlowLayout());
        contentPane.add(new JLabel(" X:"));
        contentPane.add(placeX);
        contentPane.add(new JLabel(" Y:"));
        contentPane.add(placeY);
        contentPane.add(verticalOrientation);
        contentPane.add(horizontalOrientation);
        contentPane.add(placeButton);
        contentPane.add(shootButton);
        contentPane.add(hitIndicator);
        contentPane.add(sunkIndicator);

        frame.setContentPane(contentPane);
        frame.pack();

        frame.setLocation(10, windowNumber * (frame.getSize().height + 25));
        windowNumber ++;

        frame.setVisible(true);
    }

    public void propertyChange(PropertyChangeEvent evt) {
        final Object label = evt.getPropertyName();
        if (HIT.equals(label)) {
          hitIndicator.setSelected(true);
        } else if (SUNK.equals(label)) {
          hitIndicator.setSelected(true);
          sunkIndicator.setSelected(true);
        }
    }
    
    public static int toInt(JTextField t) {
        return Integer.parseInt(t.getText().trim());
    }
}
