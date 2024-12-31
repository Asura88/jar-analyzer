/*
 * GPLv3 License
 *
 * Copyright (c) 2023-2025 4ra1n (Jar Analyzer Team)
 *
 * This project is distributed under the GPLv3 license.
 *
 * https://github.com/jar-analyzer/jar-analyzer/blob/master/LICENSE
 */

package me.n1ar4.jar.analyzer.analyze.asm;

import me.n1ar4.jar.analyzer.starter.Const;
import me.n1ar4.log.LogManager;
import me.n1ar4.log.Logger;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.util.ASMifier;
import org.objectweb.asm.util.Printer;
import org.objectweb.asm.util.Textifier;
import org.objectweb.asm.util.TraceClassVisitor;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintWriter;

public class ASMPrint {
    private static final Logger logger = LogManager.getLogger();

    public static String getPrint(InputStream is, boolean flag) {
        try {
            int parsingOptions = Const.GlobalASMOptions;
            Printer printer = flag ? new ASMifier() : new Textifier();
            ByteArrayOutputStream bao = new ByteArrayOutputStream();
            PrintWriter printWriter = new PrintWriter(bao, true);
            TraceClassVisitor traceClassVisitor = new TraceClassVisitor(null, printer, printWriter);
            new ClassReader(is).accept(traceClassVisitor, parsingOptions);
            return bao.toString();
        } catch (Exception ex) {
            logger.error("asm print error: {}", ex.toString());
        }
        return null;
    }
}