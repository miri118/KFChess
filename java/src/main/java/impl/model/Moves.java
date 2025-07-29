package impl.model;
import java.nio.file.Path;
import java.util.List;


public class Moves {
    private Path txtPath;
    private int[] dims;

    /**
     * Initialize moves with rules from text file and board dimensions.
     */
    public Moves(Path txtPath, int[] dims) {
        this.txtPath = txtPath;
        this.dims = dims;
    }
    public Moves()
    {

    }

    /**
     * Get all possible moves from a given position.
     */
    public List<int[]> getMoves(int r, int c) {
        // ...existing code...
        return null;
    }


    // Getters and setters
    public Path getTxtPath() {
        return txtPath;
    }


    public void setTxtPath(Path txtPath) {
        this.txtPath = txtPath;
    }


    public int[] getDims() {
        return dims;
    }


    public void setDims(int[] dims) {
        this.dims = dims;
    }
}