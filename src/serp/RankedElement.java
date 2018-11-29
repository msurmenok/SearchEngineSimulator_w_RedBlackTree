package serp;

/**
 * Inerface for classes that can be accepted by Heap and PriorityQueue classes.
 * @author msurmenok
 *
 */
public interface RankedElement
{
    int getScore();
    void setIndex(int index);
    int getIndex();
}
