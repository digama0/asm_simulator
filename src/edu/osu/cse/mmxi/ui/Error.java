package edu.osu.cse.mmxi.ui;

public class Error {

    private final int        line;
    private final String     message;
    private final ErrorCodes code;

    public Error(final ErrorCodes c) {
        this(-1, null, c);
    }

    public Error(final String m) {
        this(-1, m, ErrorCodes.UNKNOWN);
    }

    public Error(final String m, final ErrorCodes c) {
        this(-1, m, c);
    }

    public Error(final String m, final ErrorLevels l) {
        this(-1, m, ErrorCodes.UNKNOWN);
    }

    public Error(final int line, final String m) {
        this(line, m, ErrorCodes.UNKNOWN);
    }

    public Error(final int line, final ErrorCodes c) {
        this(line, null, c);
    }

    public Error(final int line, final String m, final ErrorCodes c) {
        this.line = line;
        message = m;
        code = c;
    }

    public int getLine() {
        return line;
    }

    public String getMessage() {
        return message;
    }

    public ErrorLevels getLevel() {
        return code.getLevel();
    }

    @Override
    public String toString() {
        String s = code.getLevel() + " " + code.getCode()
            + (line == -1 ? "" : ": Line " + line);
        if (code.getMsg() == null)
            s += message == null ? "" : ": " + message;
        else
            s += ": " + code.getMsg()
                + (message == null ? "" : "\n\tdetails: " + message);
        return s + "\n";
    }
}
