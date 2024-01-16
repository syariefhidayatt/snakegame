import java.util.ArrayList;

public class Snake {
    // direction in which snake is moving
    private Direction direction;

    // head of snake
    private final Point head;

    //tail of snake
    private ArrayList<Point> tail;

    //constructor
    public Snake(int x, int y) {
        this.head = new Point(x, y);
        this.direction = Direction.RIGHT;
        this.tail = new ArrayList<>();

        //set to size 3
        this.tail.add(new Point(0, 0));
        this.tail.add(new Point(0, 0));
        this.tail.add(new Point(0, 0));
    }

    //move the snake
    public void move() {
        ArrayList<Point> newTail = new ArrayList<>();

        for (int i = 0, size = tail.size(); i < size; i++) {
            Point previous = i == 0 ? head : tail.get(i - 1);

            newTail.add(new Point(previous.getX(), previous.getY()));
        }

        this.tail = newTail;

        this.head.move(this.direction, 10);
    }

    //add to the snake
    public void addTail() {
        this.tail.add(new Point(-10, -10));
    }

    //change direction
    public void turn(Direction d) {
        if (d.isX() && direction.isY() || d.isY() && direction.isX()) {
            direction = d;
        }
    }

    //get methods
    public ArrayList<Point> getTail() {
        return this.tail;
    }
    public Point getHead() {
        return this.head;
    }
}
