package es.ignaciofp.blackjackclient.utils;

public enum ActionsEnum {
    START_GAME("N"), HIT("H"), STAND("S"), STOP_GAME("F"), TEST("TEST");

    private final String ACTIONCMD;

    ActionsEnum(String actionCmd) {
        this.ACTIONCMD = actionCmd;
    }

    public String getACTIONCMD() {
        return ACTIONCMD;
    }

    public static ActionsEnum fromString(String actionCmd) {
        for (ActionsEnum a : ActionsEnum.values()) {
            if (actionCmd.equals(a.getACTIONCMD())) return a;
        }
        return null;
    }
}
