//point coordinates on board
class Point {
    private int x;
    private int y;

    //constructor
    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    //move in chosen direction
    public void move(Direction d, int value) {
        switch (d) {
            case UP -> this.y -= value;
            case DOWN -> this.y += value;
            case RIGHT -> this.x += value;
            case LEFT -> this.x -= value;
        }
    }

    //get methods
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    //compare method
    public boolean equals(Point p) {
        return this.x == p.getX() && this.y == p.getY();
    }

    //coordinates in string
    public String toString() {
        return "(" + x + ", " + y + ")";
    }

    //check if coordinates intersect
    public boolean intersects(Point p, int tolerance) {
        int diffX = Math.abs(x - p.getX());
        int diffY = Math.abs(y - p.getY());

        return this.equals(p) || (diffX <= tolerance && diffY <= tolerance);
    }
}
