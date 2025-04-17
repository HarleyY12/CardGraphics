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

    // Rectangle object represents a rectangle
    private Rectangle button;

    public DrawPanel() {
        button = new Rectangle(147, 280, 160, 26);
        this.addMouseListener(this);
        hand = Card.buildHand();
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

            if((i+1) % 3 == 0){
                x = 50;
                y = y + c.getImage().getHeight() + 10;
            }
        }

        // Drawing the bottom button
        // font, boldness, size
        // includes a border around the text
        g.setFont(new Font("Courier New", Font.BOLD, 20));
        g.drawString("GET NEW CARDS", 150, 300);
        g.drawRect((int)button.getX(), (int)button.getY(), (int)button.getWidth(), (int)button.getHeight());
    }

    public int getCardNumber(Card cards){
        String value = cards.getValue();
        if(value.equals("A")){
            return 1;
        }
        if(value.equals("J") || value.equals("Q") || value.equals("K")){
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
                hand = Card.buildHand();
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




    }

    public void mouseReleased(MouseEvent e) { }
    public void mouseEntered(MouseEvent e) { }
    public void mouseExited(MouseEvent e) { }
    public void mouseClicked(MouseEvent e) { }
}