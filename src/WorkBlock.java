/**
 The WorkBlock class represents a block of work sources in a mine.
 A WorkBlock consists of one or more WorkSources, which produce WorkUnits.
 The WorkBlock class keeps track of the number of work sources in the block.
 Each WorkBlock object has a unique name that is automatically generated upon instantiation.
 */
public class WorkBlock {

    private String name;

    private static final String WORKBLOCK_NAME = "WorkBlock";

    private static int workBlockCount = 0;
    private int sourceCount;

    public WorkBlock(int sourceCount) {
        name = WORKBLOCK_NAME + workBlockCount++;
        this.sourceCount = sourceCount;
    }

    public int getSourceCount() {
        return sourceCount;
    }

    public String getName() {
        return name;
    }
}
