package view;

public enum DifficultyLevel {
    EASY(20),
    MEDIUM(30),
    HARD(40);

    private final int cellsToRemove;

    DifficultyLevel(int cellsToRemove) {
        this.cellsToRemove = cellsToRemove;
    }

    public int getCellsToRemove() {
        return cellsToRemove;
    }
}
