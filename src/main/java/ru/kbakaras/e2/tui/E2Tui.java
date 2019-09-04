package ru.kbakaras.e2.tui;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import com.googlecode.lanterna.terminal.TerminalResizeListener;

import java.io.IOException;

public class E2Tui {


    public static void main(String[] args) {
        try {

            TerminalScreen screen = new DefaultTerminalFactory().createScreen();
            screen.getTerminal().addResizeListener(new TerminalResizeListener() {
                @Override
                public void onResized(Terminal terminal, TerminalSize newSize) {
                    System.out.println("resized");
                }
            });

            screen.startScreen();


            screen.setCursorPosition(null);
            screen.clear();
            screen.refresh();

            KeyStroke stroke = screen.readInput();

            screen.stopScreen();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}