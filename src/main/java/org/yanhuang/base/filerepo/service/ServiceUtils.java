package org.yanhuang.base.filerepo.service;

import org.yanhuang.base.filerepo.config.Consts;
import org.yanhuang.base.filerepo.entity.RepoDirectory;
import org.yanhuang.base.filerepo.entity.RepoFile;
import xyz.erupt.annotation.constant.AnnotationConst;
import xyz.erupt.core.view.EruptFieldModel;
import xyz.erupt.core.view.EruptModel;

import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileTime;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ServiceUtils {

    public static Map<String, Object> fileViewMap(RepoFile rf, EruptModel eruptModel) {
        final Map<String, EruptFieldModel> fieldMap = eruptModel.getEruptFieldMap();
        final Map<String, Object> viewMap = new HashMap<>();
        Arrays.stream(eruptModel.getClazz().getMethods())
                .filter(m -> m.getName().startsWith("get"))
                .filter(m -> fieldMap.containsKey(getPropertyName(m)))
                .forEach(m -> {
                    try {
                        viewMap.put(getPropertyName(m), m.invoke(rf));
                    } catch (Exception ignored) {
                    }
                });
        viewMap.put(AnnotationConst.ID, rf.getPath());
        return viewMap;
    }

    public static Map<String, Object> dirViewMap(RepoDirectory rd, EruptModel eruptModel) {
        final Map<String, Object> fieldValueMap = fileViewMap(rd, eruptModel);
        Optional.of(rd).map(RepoDirectory::getParent).ifPresent(p -> fieldValueMap.put(AnnotationConst.PID, p.getPath()));
        return fieldValueMap;
    }

    public static String getPropertyName(Method m) {
        return m.getName().substring(3, 4).toLowerCase() + m.getName().substring(4);
    }

    public static RepoFile createRepoFromPath(Path f) throws IOException {
        final RepoFile rf = Files.isDirectory(f) ? new RepoDirectory() : new RepoFile();
        rf.setFilePath(f);
        rf.setPath(f.toString());
        rf.setName(f.getFileName().toString());
        rf.setSize(Files.size(f));
        final FileTime modifiedTime = Files.getLastModifiedTime(f);
        //TODO should use client zone
        final LocalDateTime updateTime = modifiedTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        rf.setUpdateTime(updateTime);
        rf.setCreateTime(updateTime);
        //TODO set parent and directory
        if (Files.isDirectory(f)) {
            rf.setFileType(Consts.FileType.directory);
        }else{
            rf.setFileType(Consts.FileType.file);
        }
        return rf;
    }

}
