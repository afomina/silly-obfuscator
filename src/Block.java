/**
 * Created by alexa on 21.12.2015.
 */
public class Block {
    private StringBuilder head = new StringBuilder();
    private StringBuilder tail = new StringBuilder();

    public Block appendToHead(String s) {
        head.append(s + '\n');
        return this;
    }

    public Block appendToTail(String s) {
        tail.append(s + '\n');
        return this;
    }

    @Override
    public String toString() {
        return head.append(tail).toString();
    }
}
