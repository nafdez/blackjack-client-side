package es.ignaciofp.blackjackclient.utils;

public enum ActionsEnum {
    START_GAME("N"), HIT("H"), STAND("S"), STOP_GAME("F");

    private final String ACTIONCMD;

    ActionsEnum(String actionCmd) {
        this.ACTIONCMD = actionCmd;
    }

    public String getACTIONCMD() {
        return ACTIONCMD;
    }

}
