/*
 * GPLv3 License
 *
 * Copyright (c) 2023-2025 4ra1n (Jar Analyzer Team)
 *
 * This project is distributed under the GPLv3 license.
 *
 * https://github.com/jar-analyzer/jar-analyzer/blob/master/LICENSE
 */

package com.n1ar4.agent.service;

import arthas.VmTool;
import arthas.core.util.SearchUtils;
import com.n1ar4.agent.dto.SourceResult;

import java.lang.instrument.Instrumentation;
import java.util.ArrayList;

public abstract class ServerDiscovery {
    protected String serverClass;

    public ServerDiscovery(String serverClass) {
        this.serverClass = serverClass;
    }

    public boolean CanLoad(VmTool vmTool, Instrumentation inst) {
        ArrayList<Class<?>> matchedClasses = new ArrayList<>(SearchUtils.searchClassOnly(
                inst, serverClass, false, null));
        if (matchedClasses.isEmpty())
            return false;
        Object[] instances = vmTool.getInstances(matchedClasses.get(0));
        return instances.length > 0;
    }

    public Object[] getLoadedClasses(VmTool vmTool, Instrumentation inst) {
        ArrayList<Class<?>> matchedClasses = new ArrayList<>(SearchUtils.searchClassOnly(
                inst, serverClass, false, null));
        Class<?> contextClass = matchedClasses.get(0);
        return vmTool.getInstances(contextClass);
    }

    public ArrayList<SourceResult> getServerSources(VmTool vmTool, Instrumentation inst) {
        Object[] instances = getLoadedClasses(vmTool, inst);
        if (instances == null) {
            return new ArrayList<>();
        }
        ArrayList<SourceResult> sourceResults = this.getServerSourceInternal(instances);
        return sourceResults != null ? sourceResults : new ArrayList<>();
    }

    protected abstract ArrayList<SourceResult> getServerSourceInternal(Object[] instances);
}
