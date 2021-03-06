package com.vinctor;

import org.gradle.api.Project;

import java.util.ArrayList;
import java.util.List;

public class UatuConfig {

    boolean enable = false;

    TraceConfig traceConfig;
    ReplaceConfig replaceConfig;


    public UatuConfig(Project project) {
        project.getExtensions().create("traceConfig", TraceConfig.class);
        project.getExtensions().create("replaceConfig", ReplaceConfig.class, project);
    }

    void init() {
        boolean isRepplaceEnable = false;
        boolean isTraceEnable = false;
        List<String> replaceExcluedClass = new ArrayList<>();
        if (replaceConfig != null) {
            replaceConfig.init(enable);
            isRepplaceEnable = replaceConfig.enable;
            replaceExcluedClass.clear();
            replaceExcluedClass.addAll(replaceConfig.getNeedToExclude());
        }
        if (traceConfig != null) {
            traceConfig.addReplaceExcluedClass(replaceExcluedClass);
            traceConfig.init(enable);
            isTraceEnable = traceConfig.enable;
        }
        enable = isRepplaceEnable || isTraceEnable;
    }

    @Override
    public String toString() {
        return "UatuConfig{\n" +
                "enable=" + enable +
                ",\ntraceConfig=" + traceConfig +
                ",\nreplaceConfig=" + replaceConfig +
                "\n}";
    }
}
