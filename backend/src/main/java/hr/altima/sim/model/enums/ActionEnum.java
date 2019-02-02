package hr.altima.sim.model.enums;

public enum ActionEnum {
    ADD(0),
    MOD(1),
    DEL(2);

    private final int key;

    ActionEnum(int key) {
        this.key = key;
    }

    public int getKey() {
        return key;
    }
}