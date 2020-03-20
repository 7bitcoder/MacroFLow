package ContrtolOutput;

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

    public static void valSize(int size, String[] args) throws ParserExcetption {
        if (args.length != size + 1)
            throw new ParserExcetption(String.format("Wrong arguments number. Instruction {} should have {} arguments", args[0], size));
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
        try {
            var res = Integer.parseInt(read);
            return res;
        } catch (NumberFormatException e) {
            throw new ParserExcetption(String.format("Could not parse numeric {} argument: {}", index, read));
        }
    }

    public static String valStr(String[] args, int index, Set<String> req) throws ParserExcetption {
        String read = args[index];
        if (req.isEmpty() || req == null) // only runtime checking
            return read;
        if (!req.contains(read))
            throw new ParserExcetption(String.format("Could not parse string {}, argument does not match one of these: {} argument: {}", index, req.toString(), read));
        return read;
    }

}
