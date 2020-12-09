package groupproject.gameengine.contracts;

public interface Debuggable {

    default boolean inDebuggingMode() {
        return System.getProperty("DEBUG", "false").equals("true");
    }

    default void setDebug(boolean debug){
        System.out.println(String.valueOf(debug));
        System.setProperty("DEBUG", String.valueOf(debug));
    }

}
