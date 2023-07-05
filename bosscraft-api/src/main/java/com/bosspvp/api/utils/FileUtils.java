package com.bosspvp.api.utils;

import com.bosspvp.api.BossPlugin;
import com.bosspvp.api.tuples.Pair;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.net.URI;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileUtils {

    public static List<Pair<String,File>> loadFiles(@NotNull File parentDir, boolean recursive) {
        File[] files = parentDir.listFiles();
        if(files==null || files.length==0) return new ArrayList<>();
        List<Pair<String,File>> result = new ArrayList<>();
        for (File file : files) {
            if (file.isDirectory()) {
                if(!recursive) continue;
                result.addAll(loadFiles(file,true));
                continue;
            }
            String fileName = file.getName().split("\\.")[0];
            result.add(new Pair<>(fileName,file));
        }
        return result;
    }


    public static Set<String> getAllPathsInResourceFolder(BossPlugin plugin,
                                                          String dir){
        Set<String> files = new LinkedHashSet<>();

        try {
            URI uri = plugin.getClass().getProtectionDomain().getCodeSource().getLocation().toURI();
            FileSystem fileSystem = FileSystems.newFileSystem(URI.create("jar:" + uri), Collections.emptyMap());
            Stream<Path> streamFiles = Files.walk(fileSystem.getPath(dir));
            files = streamFiles
                    .filter(Objects::nonNull)
                    .map(Path::toString)
                    //to have dirs first
                    .sorted((o1, o2) -> {
                        if(o1.contains(".") && !o2.contains(".")){
                            return 1;
                        }
                        if(!o1.contains(".") && o2.contains(".")){
                            return -1;
                        }
                        return 0;
                    })
                    .collect(Collectors.toCollection(LinkedHashSet::new));
            streamFiles.close();
            fileSystem.close();
        } catch (Exception ex) {
            plugin.getLogger().warning("An error occurred while trying to load files: " + ex.getMessage());
        }

        return files;
    }


    private FileUtils() {
        throw new UnsupportedOperationException("This is an utility class and cannot be instantiated");
    }
}
