package com.bosspvp.api.utils;

import com.bosspvp.api.tuples.Pair;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileUtils {

    public static List<Pair<String,File>> loadFiles(@NotNull File parentDir, boolean recursive) {
        File[] files = parentDir.listFiles();
        if(files==null || files.length==0) return new ArrayList<>();
        List<Pair<String,File>> result = new ArrayList<>();
        for (File file : files) {
            if (file.isDirectory() && recursive) {
                result.addAll(loadFiles(file,true));
                continue;
            }
            String fileName = file.getName().split("\\.")[0];
            result.add(new Pair<>(fileName,file));
        }
        return result;
    }


    private FileUtils() {
        throw new UnsupportedOperationException("This is an utility class and cannot be instantiated");
    }
}
