import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Point;
import java.util.ArrayList;
import java.awt.Font;

class DrawPanel extends JPanel implements MouseListener {

    private ArrayList<Card> hand;
    private ArrayList<Card> deck;

    // Rectangle object represents a rectangle
    private Rectangle button;
    private Rectangle playAgain;
    private boolean gameOver;

    public DrawPanel() {
        button = new Rectangle(275, 30, 160, 26);
        this.addMouseListener(this);
        deck = Card.buildDeck();
        hand = buildHandFromDeck();
        playAgain = new Rectangle(100, 250, 160, 26);
        gameOver = false;
    }

    public ArrayList<Card> buildHandFromDeck() {
        ArrayList<Card> newHand = new ArrayList<Card>();
        for (int i = 0; i < 9 && !deck.isEmpty(); i++) {
            int r = (int) (Math.random() * deck.size());
            newHand.add(deck.remove(r));
        }
        return newHand;
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int x = 50;
        int y = 10;
        for (int i = 0; i < hand.size(); i++) {
            Card c = hand.get(i);

            // represents the border around a
            // right-clicked card
            if (c.getHighlight()) {
                g.drawRect(x, y, c.getImage().getWidth(), c.getImage().getHeight());
            }
            // sets the location of the rectangle "hitbox"
            c.setRectangleLocation(x, y);

            g.drawImage(c.getImage(), x, y, null);
            x = x + c.getImage().getWidth() + 10;

            if ((i + 1) % 3 == 0) {
                x = 50;
                y = y + c.getImage().getHeight() + 10;
            }
        }

        // Drawing the bottom button
        // font, boldness, size
        // includes a border around the text
        g.setFont(new Font("Courier New", Font.BOLD, 20));
        g.drawString("GET NEW CARDS", 275, 50);
        g.drawRect((int) button.getX(), (int) button.getY(), (int) button.getWidth(), (int) button.getHeight());

        g.drawString("Cards left: " + deck.size(), 50, 450);
        if (deck.isEmpty() && hand.isEmpty()) {
            g.drawString("You win!", 130, 390);
            gameOver = true;
        }
        if (!hasValidMove()) {
            g.drawString("You lose!", 130, 390);
            gameOver = true;
        }
        g.drawString("Play again", 120, 270);
        g.drawRect((int) playAgain.getX(), (int) playAgain.getY(),
                (int) playAgain.getWidth(), (int) playAgain.getHeight());


    }

    public boolean hasValidMove() {
        for (int i = 0; i < hand.size(); i++) {
            for (int j = i + 1; j < hand.size(); j++) {
                if (getCardNumber(hand.get(i)) + getCardNumber(hand.get(j)) == 11) {
                    return true;
                }
            }
        }
        boolean hasJ = false;
        boolean hasQ = false;
        boolean hasK = false;
        for (Card c : hand) {
            if (c.getValue().equals("J")) {
                hasJ = true;
            }
            if (c.getValue().equals("Q")) {
                hasQ = true;
            }
            if (c.getValue().equals("K")) {
                hasK = true;
            }
        }
        return hasJ && hasQ && hasK;
    }

    public int getCardNumber(Card cards) {
        String value = cards.getValue();
        if (value.equals("A")) {
            return 1;
        }
        if (value.equals("J") || value.equals("Q") || value.equals("K")) {
            return 0;
        }
        return Integer.parseInt(value);
    }


    public void mousePressed(MouseEvent e) {

        Point clicked = e.getPoint();

        // left click
        if (e.getButton() == 1) {

            // evaluates if mouse clicked the coordinates
            // within the button
            if (button.contains(clicked)) {
                ArrayList<Integer> selectedIndexes = new ArrayList<>();
                ArrayList<Card> selectedCards = new ArrayList<>();

                for (int i = 0; i < hand.size(); i++) {
                    if (hand.get(i).getHighlight()) {
                        selectedIndexes.add(i);
                        selectedCards.add(hand.get(i));
                    }
                }

                boolean valid = false;
                if (selectedCards.size() == 2) {
                    int sum = getCardNumber(selectedCards.get(0)) + getCardNumber(selectedCards.get(1));
                    valid = (sum == 11);

                } else if (selectedCards.size() == 3) {
                    boolean hasJ = false;
                    boolean hasK = false;
                    boolean hasQ = false;
                    for (Card c : selectedCards) {
                        if (c.getValue().equals("J")) {
                            hasJ = true;
                        }
                        if (c.getValue().equals("K")) {
                            hasK = true;
                        }
                        if (c.getValue().equals("Q")) {
                            hasQ = true;
                        }
                    }
                    valid = hasJ && hasQ && hasK;
                }

                if (valid) {
                    for (int i = 0; i < selectedIndexes.size(); i++) {
                        int index = selectedIndexes.get(i);
                        if (!deck.isEmpty()) {
                            int random = (int) (Math.random() * deck.size());
                            Card newCard = deck.remove(random);
                            newCard.setHighlight(false);
                            hand.set(index, newCard);
                        }
                    }
                }
            }

            if (playAgain.contains(clicked)) {
                deck = Card.buildDeck();
                hand = buildHandFromDeck();
                gameOver = false;
            }

            // checks if any card was clicked
            // if so, the card is flipped
            for (int i = 0; i < hand.size(); i++) {
                Rectangle box = hand.get(i).getCardBox();
                if (box.contains(clicked)) {
                    hand.get(i).flipCard();
                }
            }
        }

        // right click
        if (e.getButton() == 3) {
            for (int i = 0; i < hand.size(); i++) {
                Rectangle box = hand.get(i).getCardBox();
                if (box.contains(clicked)) {
                    hand.get(i).flipHighlight();
                }
            }
        }

        repaint();
    }


    public void mouseReleased(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mouseClicked(MouseEvent e) {
    }
}
