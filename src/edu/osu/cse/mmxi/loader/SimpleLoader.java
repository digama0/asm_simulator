package edu.osu.cse.mmxi.loader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import edu.osu.cse.mmxi.machine.Machine;
import edu.osu.cse.mmxi.machine.memory.MemoryException;

public class SimpleLoader {

    public static void load(final String file, final Machine m)

    throws MemoryException, ParseException, IOException {

        final BufferedReader br = new BufferedReader(new InputStreamReader(
                new FileInputStream(new File(".", file))));

        String line = br.readLine();

        if (line != null && line.length() > 0) {
            line = line.split(" ")[0];
        }

        boolean foundH = false, foundE = false;

        for (int i = 1; line != null; i++) {

            if (line.length() == 0) {
                line = br.readLine();
                if (line != null && line.length() > 0) {
                    line = line.split(" ")[0];
                }
                continue;
            }

            switch (line.charAt(0)) {
            case 'H':
            case 'h':
                if (line.length() != 15) {
                    throw new ParseException("On line " + i + ": malformed header");
                }
                final String name = line.substring(1, 7);
                int begin,
                len;
                try {
                    begin = Integer.parseInt(line.substring(7, 11), 16);
                    len = Integer.parseInt(line.substring(11), 16);
                    p = new Program(name, begin, len, m);
                } catch (final NumberFormatException e) {
                    throw new ParseException("On line " + i + ": malformed header");
                }
                foundH = true;
                break;
            case 'T':
            case 't':
                if (!foundH) {
                    throw new ParseException("parser error: header not first record");
                }
                if (line.length() != 9) {
                    throw new ParseException("On line " + i + ": malformed text record");
                }
                int loc = 0,
                dat = 0;
                try {
                    loc = Integer.parseInt(line.substring(1, 5), 16);
                    dat = Integer.parseInt(line.substring(5), 16);
                } catch (final NumberFormatException e) {
                    throw new ParseException("On line " + i + ": malformed text record");
                }
                p.setMemory((short) loc, (short) dat);
                break;
            case 'E':
            case 'e':
                if (!foundH) {
                    throw new ParseException("parser error: header not first record");
                }
                if (line.length() != 5) {
                    throw new ParseException("On line " + i
                            + ": malformed execution record");
                }
                int ex = 0;
                try {
                    ex = Integer.parseInt(line.substring(1), 16);
                } catch (final NumberFormatException e) {
                    throw new ParseException("On line " + i
                            + ": malformed execution record");
                }
                m.r.pc = (short) ex;
                foundE = true;
                break;
            default:
                throw new ParseException("On line " + i + ": Unknown record type");
            }
            line = br.readLine();
            if (line != null && line.length() > 0) {
                line = line.split(" ")[0];
            }
        }

        if (!foundE) {
            throw new ParseException("parser error: no execution record found");
        }

    }
}