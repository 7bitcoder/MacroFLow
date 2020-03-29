package Main;

public class InstructionHelp {
    static String help = "options in brackets [] are optional\n" +
            "Every instruction starts with command name, then arguments must be provided\n" +
            "Eg: delay 500\n" +
            "Each line can contain only one instruction.\n" +
            "Available instructions:\n" +
            "delay time - suspend macro execution for time milliseconds\n" +
            "clipboard data - saves to clipboard data in string\n" +
            "paste - paste data from clipboard, this command is implemented as pressing combination CTRL+V\n" +
            "var name [initial] - creates variable with initialized with 0 or value initial if provided\n" +
            "add out in1 in2 - adds two variables (must be defined with command var) and assigns result to out variable out\n" +
            "sub out in1 in2 - subtracts two variables (must be defined with command var) and assigns result to out variable out\n" +
            "mul out in1 in2 - multiplies  two variables (must be defined with command var) and assigns result to out variable out\n" +
            "div out in1 in2 - divides  two variables (must be defined with command var) and assigns result to out variable out\n" +
            "bigger out in1 in2 - compares two variables (must be defined with command var) and assigns result to out variable out if in1 is bigger than in2 result is 1 otherwise 0\n" +
            "smaller out in1 in2 - compares two variables (must be defined with command var) and assigns result to out variable out if in1 is smaller than in2 result is 1 otherwise 0\n" +
            "equal out in1 in2 - compares two variables (must be defined with command var) and assigns result to out variable out if in1 equal to in2 result is 1 otherwise 0\n" +
            "assign out in - assign in variable to out\n" +
            "Label name - creates label, you can jump to this instruction by command Jmp label\n" +
            "Jmp name - jump instruction to label\n" +
            "If value - if value is not equal to zero program is execution next instruction otherwise is jumps to first endif statement\n" +
            "endif - closes if statement\n" +
            "loop value - if value is not equal to zero program is executing inside loop instructions otherwise is jumps to first endloop statement\n" +
            "endloop - closes loop statement\n" +
            "movemouse xcoord ycoord - moves mouse to position xcoord, ycoord on screen\n" +
            "presskey keyname - press keyboard key\n" +
            "releasekey keyname - release keyboard key\n" +
            "write string - writes string by pressing keys";
}

