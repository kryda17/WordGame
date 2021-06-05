package wordgame.chris.jpa;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WordGameGUI {

    private FillTable table2;
    private JFrame frame;
    private JPanel panel;
    private List<List<JLabel>> labels = new ArrayList<>();

    public WordGameGUI(FillTable table) {
        table2 = table;
        frame = new JFrame("WordGame");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        panel = new JPanel(new GridLayout(table2.getTable().GRID_SIZE, table2.getTable().GRID_SIZE, -1, -1));
        for (int i = 0; i < table2.getTable().GRID_SIZE; i++) {
            labels.add(new ArrayList<>());
            for (int j = 0; j < table2.getTable().GRID_SIZE; j++) {
                JLabel label = new JLabel(table2.readCharacterAtCoordinate(new Coordinate(j, i)), SwingConstants.CENTER);
                if (table2.getTable().isCoordinateBlack(new Coordinate(j, i))) {
                    label.setBackground(Color.GRAY);
                }
                String name = i + ":" + j;
                label.setName(name);
                label.setBorder(new LineBorder(Color.BLACK));
                label.setOpaque(true);
                label.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        change(e);
                    }
                });
                labels.get(i).add(label);
                panel.add(labels.get(i).get(j));
            }
        }
        frame.add(panel);
        frame.setSize(800,600);
    }

    public WordGameGUI() {
    }



    public void show() {
        frame.setVisible(true);
    }




    public void change(MouseEvent e) {
        Coordinate coordinate = idToCoordinate(((JLabel) e.getComponent()).getName());
        if (table2.isEmptyCoordinate(coordinate) && table2.getTable().isGeneratedCoordDifferenceMinTwoV3(coordinate)) {
            ((JLabel) e.getComponent()).setText("");
            ((JLabel) e.getComponent()).setBackground(Color.GRAY);
            table2.insertBlack(coordinate);
        } else  {
            if ( table2.getTable().isCoordinateBlack(coordinate)) {
                ((JLabel) e.getComponent()).setText(table2.getTable().EMPTY_GRID_PLACEHOLDER);
                ((JLabel) e.getComponent()).setBackground(Color.WHITE);
                table2.deleteCoordinate(coordinate);
            }

        }
    }

    private Coordinate idToCoordinate(String id) {
        String[] split = id.split(":");
        return new Coordinate(Integer.parseInt(split[1]), Integer.parseInt(split[0]));
    }



    public static void main(String[] args) {
        Table2 table2 = new Table2(15, new Random(1));
        WordGameGUI gameGUI = new WordGameGUI(new FillTable(table2));
        gameGUI.show();
    }

    public FillTable getTable2() {
        return table2;
    }
}


