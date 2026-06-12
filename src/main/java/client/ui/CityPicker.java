package client.ui;

import org.jline.terminal.Attributes;
import org.jline.terminal.Terminal;
import org.jline.utils.NonBlockingReader;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Scanner;

/**
 * Lets the user choose one option from a list.
 * Arrow keys + Enter when the terminal supports raw mode (cmd, PowerShell);
 * falls back to a numbered list in dumb terminals (IDE consoles, pipes).
 * Selection instead of typing also sidesteps console-encoding issues with
 * accented city names like St-Léonard.
 */
public class CityPicker {

    private static final int KEY_UP = 1;
    private static final int KEY_DOWN = 2;
    private static final int KEY_ENTER = 3;
    private static final int KEY_OTHER = 0;

    private final Terminal terminal;
    private final Scanner keyboard;

    /** terminal may be null — the picker then always uses the numbered fallback. */
    public CityPicker(Terminal terminal, Scanner keyboard) {
        this.terminal = terminal;
        this.keyboard = keyboard;
    }

    public String pick(String prompt, List<String> options) {
        if (supportsArrows()) {
            try {
                return pickWithArrows(prompt, options);
            } catch (IOException e) {
                // raw mode failed mid-flight: fall through to the numbered list
            }
        }
        return pickNumbered(prompt, options);
    }

    private boolean supportsArrows() {
        return terminal != null
                && !Terminal.TYPE_DUMB.equals(terminal.getType())
                && !Terminal.TYPE_DUMB_COLOR.equals(terminal.getType());
    }

    // --- arrow-key mode -------------------------------------------------

    private String pickWithArrows(String prompt, List<String> options) throws IOException {
        PrintWriter out = terminal.writer();
        Attributes previous = terminal.enterRawMode();
        out.print("\033[?25l"); // hide cursor while navigating
        try {
            int selected = 0;
            boolean firstDraw = true;
            while (true) {
                render(prompt, options, selected, firstDraw);
                firstDraw = false;
                switch (readKey()) {
                    case KEY_UP -> selected = (selected + options.size() - 1) % options.size();
                    case KEY_DOWN -> selected = (selected + 1) % options.size();
                    case KEY_ENTER -> {
                        return options.get(selected);
                    }
                    default -> { /* ignore other keys */ }
                }
            }
        } finally {
            out.print("\033[?25h"); // show cursor again
            out.println();
            out.flush();
            terminal.setAttributes(previous);
        }
    }

    private void render(String prompt, List<String> options, int selected, boolean firstDraw) {
        PrintWriter out = terminal.writer();
        if (!firstDraw) {
            out.print("\033[" + (options.size() + 1) + "A"); // cursor up to the prompt line
        }
        out.print("\r\033[K" + prompt + " (arrow keys, Enter to confirm)\r\n");
        for (int i = 0; i < options.size(); i++) {
            String line = (i == selected)
                    ? "\033[7m> " + options.get(i) + " \033[0m"  // inverse video on the selection
                    : "  " + options.get(i);
            out.print("\r\033[K" + line + "\r\n");
        }
        out.flush();
    }

    private int readKey() throws IOException {
        NonBlockingReader reader = terminal.reader();
        int c = reader.read();
        if (c == '\r' || c == '\n') {
            return KEY_ENTER;
        }
        if (c == 27) { // ESC: arrow keys arrive as ESC [ A (up) / ESC [ B (down)
            int second = reader.read(50);
            if (second == '[' || second == 'O') {
                int third = reader.read(50);
                if (third == 'A') return KEY_UP;
                if (third == 'B') return KEY_DOWN;
            }
        }
        return KEY_OTHER;
    }

    // --- numbered fallback ----------------------------------------------

    private String pickNumbered(String prompt, List<String> options) {
        System.out.println(prompt);
        for (int i = 0; i < options.size(); i++) {
            System.out.printf("%2d) %s%n", i + 1, options.get(i));
        }
        while (true) {
            System.out.print("Number: ");
            try {
                int choice = Integer.parseInt(keyboard.nextLine().trim());
                if (choice >= 1 && choice <= options.size()) {
                    return options.get(choice - 1);
                }
            } catch (NumberFormatException e) {
                // fall through and re-prompt
            }
            System.out.println("Please enter a number between 1 and " + options.size() + ".");
        }
    }
}
