import java.awt.EventQueue;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;

public class game1 {

    private JFrame frame;
    private BoardPanel boardPanel;
    private JLabel turnLabel;
    private JButton rollDiceButton;
    private JLabel rollResultLabel;
    private JLabel mostRolledLabel;
    private int player1Pos = 0;
    private int player2Pos = 0;
    private boolean player1Turn = true;

    private LinkedList<int[]> snakes;
    private LinkedList<int[]> ladders;
    
    private int[] diceRollFrequency;
    
    private static final int SIZE = 10;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    game1 window = new game1();
                    window.frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public game1() {
        initialize();
    }

    private void initialize() {
        frame = new JFrame();
        frame.setBounds(100, 100, 600, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(new BorderLayout());

        snakes = new LinkedList<>();
        ladders = new LinkedList<>();
        
        initializeSnakesAndLadders();
        
        diceRollFrequency = new int[6];

        boardPanel = new BoardPanel();
        frame.getContentPane().add(boardPanel, BorderLayout.CENTER);

        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new FlowLayout());
        
        turnLabel = new JLabel("Player 1's Turn");
        controlPanel.add(turnLabel);

        rollDiceButton = new JButton("Roll Dice");
        rollDiceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int diceRoll = (int) (Math.random() * 6) + 1;
                movePlayer(diceRoll);
                rollResultLabel.setText("Rolled: " + diceRoll);
                diceRollFrequency[diceRoll - 1]++;
                displayMostRolledNumber();
            }
        });
        controlPanel.add(rollDiceButton);

        rollResultLabel = new JLabel("");
        controlPanel.add(rollResultLabel);
        
        mostRolledLabel = new JLabel("Most Rolled Number: ");
        controlPanel.add(mostRolledLabel);

        frame.getContentPane().add(controlPanel, BorderLayout.SOUTH);
        updateTurnStatus();
        resetGame();
    }

    private void initializeSnakesAndLadders() {
        snakes.add(new int[]{98, 28});
        snakes.add(new int[]{95, 24});
        snakes.add(new int[]{92, 51});
        snakes.add(new int[]{83, 19});
        snakes.add(new int[]{73, 1});
        snakes.add(new int[]{69, 33});
        snakes.add(new int[]{64, 36});
        snakes.add(new int[]{59, 17});
        snakes.add(new int[]{55, 7});
        snakes.add(new int[]{52, 11});
        snakes.add(new int[]{48, 9});
        snakes.add(new int[]{46, 5});
        snakes.add(new int[]{44, 22});

        ladders.add(new int[]{8, 26});
        ladders.add(new int[]{21, 82});
        ladders.add(new int[]{43, 77});
        ladders.add(new int[]{50, 91});
        ladders.add(new int[]{54, 93});
        ladders.add(new int[]{62, 96});
        ladders.add(new int[]{66, 87});
        ladders.add(new int[]{80, 100});
        
        
    }

    private void movePlayer(int diceRoll) {
        if (player1Turn) {
            player1Pos += diceRoll;
            player1Pos = checkSnakesAndLadders(player1Pos);
            if (player1Pos >= 100) {
                JOptionPane.showMessageDialog(frame, "Player 1 Wins!");
                displayMostRolledNumber();
                resetGame();
                return;
            }
        } else {
            player2Pos += diceRoll;
            player2Pos = checkSnakesAndLadders(player2Pos);
            if (player2Pos >= 100) {
                JOptionPane.showMessageDialog(frame, "Player 2 Wins!");
                displayMostRolledNumber();
                resetGame();
                return;
            }
        }
        player1Turn = !player1Turn;
        updateTurnStatus();
        boardPanel.repaint();
    }

    private int checkSnakesAndLadders(int pos) {
        for (int[] snake : snakes) {
            if (snake[0] == pos) {
                return snake[1];
            }
        }
        for (int[] ladder : ladders) {
            if (ladder[0] == pos) {
                return ladder[1];
            }
        }
        return pos;
    }
    
    private void updateTurnStatus() {
        if (player1Turn) {
        	turnLabel.setText("Player 1's Turn");
        } else {
        	turnLabel.setText("Player 2's Turn");
        }
    }
    
    private void displayMostRolledNumber() {
        int mostRolledNumber = 1;
        int maxFrequency = diceRollFrequency[0];
        for (int i = 1; i < 6; i++) {
            if (diceRollFrequency[i] > maxFrequency) {
                mostRolledNumber = i + 1;
                maxFrequency = diceRollFrequency[i];
            }
        }
        mostRolledLabel.setText("Most Rolled Number: " + mostRolledNumber + " (Frequency: " + maxFrequency + ")");
        
    }
    
    private void resetGame() {
        player1Pos = 0;
        player2Pos = 0;
        player1Turn = true;
        updateTurnStatus();
        rollResultLabel.setText("");
        mostRolledLabel.setText("Most Rolled Number: ");
        for (int i = 0; i < 6; i++) {
            diceRollFrequency[i] = 0;
        }
        
        boardPanel.repaint();
    }

    
    
    private class BoardPanel extends JPanel {
        private static final int SIZE = 10;
        private static final int CELL_SIZE = 50;
        
        
        
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            drawBoard(g);
            drawPlayers(g);
            
        }

        private void drawBoard(Graphics g) {
            for (int row = 0; row < SIZE; row++) {
                for (int col = 0; col < SIZE; col++) {
                    int x = col * CELL_SIZE;
                    int y = row * CELL_SIZE;
                    int position = getPosition(row, col);
                    g.drawRect(x, y, CELL_SIZE, CELL_SIZE);
                    g.drawString(String.valueOf(position), x + 20, y + 20);
                    
                    for (int[] snake : snakes) {
                        if (snake[0] == position) {
                            g.drawString("S " + snake[0] + "->" + snake[1], x + 2, y + CELL_SIZE-5);
                        }
                    }

                    for (int[] ladder : ladders) {
                        if (ladder[0] == position) {
                            g.drawString("L " + ladder[0] + "->" + ladder[1], x + 2,y + CELL_SIZE-5);
                        }
                    }
                }
            }
        }
        
        
        
        
        private void drawPlayers(Graphics g) {
            int player1X = getPlayerX(player1Pos);
            int player1Y = getPlayerY(player1Pos);
            g.setColor(Color.RED);
            g.fillOval(player1X, player1Y, 30, 30);

            int player2X = getPlayerX(player2Pos);
            int player2Y = getPlayerY(player2Pos);
            g.setColor(Color.BLUE);
            g.fillOval(player2X, player2Y, 30, 30);
        }

        private int getPosition(int row, int col) {
            int position;
            if (row % 2 == 0) {
            	position = (SIZE * SIZE) - (row * SIZE + col);
            } else {
                position = (SIZE * SIZE) - (row * SIZE + (SIZE - col - 1));
            }
            return position;
        }

        private int getPlayerX(int pos) {
            int row = (pos - 1) / SIZE;
            int col = (pos - 1) % SIZE;
            if (row % 2 == 1) {
                col = SIZE - 1 - col;
            }
            return col * CELL_SIZE + 10;
        }

        private int getPlayerY(int pos) {
            int row = (pos - 1) / SIZE;
            return (SIZE - 1 - row) * CELL_SIZE + 10;
        }
       
    }
}
