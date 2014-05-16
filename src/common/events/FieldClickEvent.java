package common.events;

public class FieldClickEvent extends GameEvent {
    // wspolrzedna x (czyli pozioma) klikniętego pola
    private int field_x;
    // wspolrzedna y (czyli pionowa) klikniętego pola
    private int field_y;

    /**
     * Konstruktor ustawiający paremetry field_x i field_y
     * @param field_x wspolrzedna x (czyli pozioma) klikniętego pola
     * @param field_y wspolrzedna y (czyli pionowa) klikniętego pola
     */
    public FieldClickEvent(int field_x, int field_y) {
        this.field_x = field_x;
        this.field_y = field_y;
    }

    /**
     * Getter dla field_x
     * @return wspolrzedna x (czyli pozioma) kliknietego pola
     */
    public int getFieldX() {
        return field_x;
    }

    /**
     * Getter dla field_y
     * @return wspolrzedna y (czyli pionowa) kliknietego pola
     */
    public int getFieldY() {
        return field_y;
    }
}
