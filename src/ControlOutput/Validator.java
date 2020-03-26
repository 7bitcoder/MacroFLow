package ControlOutput;

import java.util.Set;


public class Validator {
    public static class ParserExcetption extends Exception {
        public ParserExcetption(String msg) {
            super(msg);
        }
    }

    private static final Validator val = new Validator();

    private Validator() {
    }

    public static Validator getVaidator() {
        return val;
    }

    public static void valSize(String[] args, int min, int max) throws ParserExcetption {
        if (args.length < min + 1 || args.length > max + 1)
            throw new ParserExcetption(String.format("Wrong arguments number. Instruction %s should have %d-%d arguments", args[0], min, max));
    }

    public static void valSize(int size, String[] args) throws ParserExcetption {
        if (args.length != size + 1)
            throw new ParserExcetption(String.format("Wrong arguments number. Instruction %s should have %d arguments", args[0], size));
    }

    public static boolean valBool(String[] args, int index) throws ParserExcetption {
        String read = args[index];
        var res = Boolean.parseBoolean(read);
        if (res)
            return true;
        return valNum(args, index) != 0;
    }

    public static int valNum(String[] args, int index) throws ParserExcetption {
        String read = args[index];
        int res = 0;
        try {
            res = Integer.parseInt(read);
        } catch (NumberFormatException e) {
            error(index, kind.numeric, read, e.getMessage());
        }
        return res;
    }

    public static String valStr(String[] args, int index, Set<String> req) throws ParserExcetption {
        String read = args[index];
        if (req.isEmpty() || req == null) // only runtime checking
            return read;
        if (!req.contains(read)) {
            if (req.size() > 10)
                error(index, kind.string, read, "Argument value is not allowed. Too see allowed values see manual");
            else
                error(index, kind.string, read, String.format("Argument value is not allowed. Allowed values: %s", req.toString()));
        }
        return read;
    }

    public static enum kind {
        bool, string, numeric;
    }

    public static void error(int index, kind kin, String value, String msg) throws ParserExcetption {
        throw new ParserExcetption(String.format("Index: %d. Could not parse %s argument '%s'. Error: %s", index, kin.name(), value, msg));
    }
}
