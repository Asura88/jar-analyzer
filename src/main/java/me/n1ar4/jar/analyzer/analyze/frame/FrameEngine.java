/*
 * GPLv3 License
 *
 * Copyright (c) 2023-2024 4ra1n (Jar Analyzer Team)
 *
 * This project is distributed under the GPLv3 license.
 *
 * https://github.com/jar-analyzer/jar-analyzer/blob/master/LICENSE
 */

package me.n1ar4.jar.analyzer.analyze.frame;

import me.n1ar4.jar.analyzer.gui.MainForm;
import me.n1ar4.jar.analyzer.starter.Const;
import me.n1ar4.log.LogManager;
import me.n1ar4.log.Logger;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.analysis.Analyzer;
import org.objectweb.asm.tree.analysis.BasicValue;
import org.objectweb.asm.tree.analysis.SimpleVerifier;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FrameEngine {
    private static final Logger logger = LogManager.getLogger();

    public static String start(InputStream is,
                               String methodName,
                               String methodDesc,
                               StringBuilder builder) throws Exception {
        ClassReader cr = new ClassReader(is);
        ClassNode cn = new ClassNode();
        cr.accept(cn, Const.GlobalASMOptions);
        String owner = cn.name;
        List<MethodNode> methods = cn.methods;
        MethodNode method = null;
        for (MethodNode mn : methods) {
            if (mn.name.equals(methodName) && mn.desc.equals(methodDesc)) {
                method = mn;
            }
        }
        if (method != null) {
            return print(owner, method, builder);
        }
        return null;
    }

    private static String print(String owner, MethodNode mn, StringBuilder builder) {
        SimpleVerifier simpleVerifier = null;
        try {
            simpleVerifier = new SimpleVerifier();
            Field loaderField = simpleVerifier.getClass().getDeclaredField("loader");
            loaderField.setAccessible(true);

            List<URL> urls = new ArrayList<>();
            for (String path : MainForm.getEngine().getJarsPath()) {
                urls.add(new URL(String.format("%s:/%s", "file", path)));
                logger.info("add url: {}", Paths.get(path).getFileName());
            }

            URLClassLoader classLoader = new URLClassLoader(urls.toArray(new URL[0]),
                    ClassLoader.getSystemClassLoader());
            loaderField.set(simpleVerifier, classLoader);

            logger.info("change classloader finish");
        } catch (Exception ex) {
            logger.error("hack simple verifier error: {}", ex.getMessage());
        }
        Analyzer<BasicValue> analyzer = new Analyzer<>(simpleVerifier);
        FrameUtils.printGraph(owner, mn, analyzer, ValueUtils::fromBasicValue2String, builder);
        return null;
    }
}
