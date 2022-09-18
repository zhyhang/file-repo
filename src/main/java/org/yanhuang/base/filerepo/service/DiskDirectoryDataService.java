package org.yanhuang.base.filerepo.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.SystemUtils;
import org.yanhuang.base.filerepo.entity.RepoDirectory;
import org.yanhuang.base.filerepo.entity.RepoFile;
import xyz.erupt.core.query.Column;
import xyz.erupt.core.query.EruptQuery;
import xyz.erupt.core.service.IEruptDataService;
import xyz.erupt.core.view.EruptModel;
import xyz.erupt.core.view.Page;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileTime;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Stream;

@Slf4j
public class DiskDirectoryDataService implements IEruptDataService {

    @Override
    public Object findDataById(EruptModel eruptModel, Object id) {
        return null;
    }

    @Override
    public Page queryList(EruptModel eruptModel, Page page, EruptQuery eruptQuery) {
        return null;
    }

    private List<RepoDirectory> listDirectory(Path dir) throws Exception {
        final RepoDirectory currDir = (RepoDirectory)ServiceUtils.createRepoFromPath(dir);
        try (final Stream<Path> pathStream = Files.walk(dir, 1)) {
            return pathStream.filter(Files::isDirectory)
                    .map(f -> {
                        try {
                            final RepoDirectory repoDir = (RepoDirectory) ServiceUtils.createRepoFromPath(f);
                            if(!f.equals(dir)) {
                                repoDir.setParent(currDir);
                            }
                            return repoDir;
                        } catch (Exception e) {
                            log.warn("convert path {} to repo-file error", f, e);
                            return null;
                        }
                    }).filter(Objects::nonNull).toList();
        }
    }

    private List<Map<String,Object>> getFileViewList(EruptModel eruptModel)  throws Exception{
        final List<RepoDirectory> fileList = listDirectory(SystemUtils.getUserHome().toPath());
        final ArrayList<Map<String,Object>> pageMapList = new ArrayList<>(fileList.size());
        for (RepoDirectory rd : fileList) {
                pageMapList.add(ServiceUtils.dirViewMap(rd, eruptModel));
        }
        return pageMapList;
    }

    @Override
    public Collection<Map<String, Object>> queryColumn(EruptModel eruptModel, List<Column> columns, EruptQuery eruptQuery) {
        try {
            return getFileViewList(eruptModel);
        } catch (Exception e) {
            log.error("read directory list error", e);
            return List.of();
        }
    }

    @Override
    public void addData(EruptModel eruptModel, Object object) {

    }

    @Override
    public void editData(EruptModel eruptModel, Object object) {

    }

    @Override
    public void deleteData(EruptModel eruptModel, Object object) {

    }
}
