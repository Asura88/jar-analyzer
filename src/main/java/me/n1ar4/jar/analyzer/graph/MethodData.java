/*
 * GPLv3 License
 *
 * Copyright (c) 2023-2025 4ra1n (Jar Analyzer Team)
 *
 * This project is distributed under the GPLv3 license.
 *
 * https://github.com/jar-analyzer/jar-analyzer/blob/master/LICENSE
 */

package me.n1ar4.jar.analyzer.graph;

import java.util.List;

public class MethodData {
    private String uuid;
    private String methodString;
    private List<String> callerIds;
    private List<String> calleeIds;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getMethodString() {
        return methodString;
    }

    public void setMethodString(String methodString) {
        this.methodString = methodString;
    }

    public List<String> getCallerIds() {
        return callerIds;
    }

    public void setCallerIds(List<String> callerIds) {
        this.callerIds = callerIds;
    }

    public List<String> getCalleeIds() {
        return calleeIds;
    }

    public void setCalleeIds(List<String> calleeIds) {
        this.calleeIds = calleeIds;
    }
}
