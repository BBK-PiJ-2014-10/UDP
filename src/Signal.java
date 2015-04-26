public enum Signal {
    GET_ID("Can I have an ID please, make it unique!"),
    CHECK_IF_FIRST("Am I the first client of yours, my dear server?"),
    BROADCASTER_PRESENT("We have a broadcaster already, grab some fresh tunes over UDP.");

    private String signal;

    Signal(String signal) {
        this.signal = signal;
    }

    public String getSignal() {
        return signal;
    }
}