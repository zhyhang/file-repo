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
import java.util.*;
import java.util.stream.Stream;

@Slf4j
public class DiskFileDataService implements IEruptDataService {

    @Override
    public Object findDataById(EruptModel eruptModel, Object id) {
        return null;
    }

    @Override
    public Page queryList(EruptModel eruptModel, Page page, EruptQuery eruptQuery) {
        final Path queryPath = queryPath(eruptQuery.getConditionStrings());
        final Path startDir = queryPath != null ? queryPath : SystemUtils.getUserHome().toPath();
        final Page pageRet = new Page(0, page.getPageSize(), page.getSort());
        try {
            pageRet.setList(getFileViewList(startDir, eruptModel, eruptQuery));
        } catch (Exception e) {
            log.error("read file list error", e);
        }
        return pageRet;
    }

    private Path queryPath(final List<String> conditions) {
        for (String condition : conditions) {
            if (condition.startsWith("directory.path")) {
                final String pathStr = condition.substring(condition.indexOf("'") + 1, condition.length() - 1);
                return Path.of(pathStr);
            }
        }
        return null;
    }

    private List<RepoFile> listDirectory(Path dir) throws Exception {
        final RepoDirectory currDir = (RepoDirectory) ServiceUtils.createRepoFromPath(dir);
        try (final Stream<Path> pathStream = Files.walk(dir, 1)) {
            return pathStream.filter(f -> !f.equals(dir)).map(f -> {
                try {
                    final RepoFile file = ServiceUtils.createRepoFromPath(f);
                    file.setDirectory(currDir);
                    return file;
                } catch (Exception e) {
                    log.warn("convert path {} to repo-file error", f, e);
                    return null;
                }
            }).filter(Objects::nonNull).toList();
        }
    }

    private List<Map<String, Object>> getFileViewList(Path startDir, EruptModel eruptModel, final EruptQuery eruptQuery) throws Exception {
        final List<RepoFile> fileList = listDirectory(startDir);
        final List<Map<String, Object>> pageMapList = new ArrayList<>(fileList.size());
        for (RepoFile file : fileList) {
            pageMapList.add(ServiceUtils.fileViewMap(file, eruptModel));
        }
        final String orderBy = eruptQuery.getOrderBy();
        orderBy(pageMapList, orderBy != null && orderBy.trim().length() > 0 ? orderBy : "name asc");
        return pageMapList;
    }

    private void orderBy(List<Map<String, Object>> viewMapList, String orderBy) {
        viewMapList.sort((x, y) -> compareOrderFields(x, y, orderBy.split(",")));
    }

    private int compareOrderFields(final Map<String, Object> x, final Map<String, Object> y, final String[] fieldOrders) {
        for (String fieldOrder : fieldOrders) {
            final int compared = comparedOrderField(x, y, fieldOrder);
            if (compared != 0) {
                return compared;
            }
        }
        return 0;
    }

    private int comparedOrderField(final Map<String, Object> x, final Map<String, Object> y, final String fieldOrder) {
        final String field = fieldOrder.substring(0, fieldOrder.indexOf(' '));
        final Object xf = x.get(field);
        final Object yf = y.get(field);
        if (xf != null && yf != null) {
            int compared;
            if ((xf instanceof Comparable) && (yf instanceof Comparable)) {
                compared = ((Comparable) xf).compareTo(yf);
            }else{
                compared = xf.toString().compareTo(yf.toString());
            }
            if (compared !=0) {
                return fieldOrder.endsWith("asc") ? compared : -compared;
            }
        } else if (xf != null) {
            return fieldOrder.endsWith("asc") ? -1 : 1;
        }
        return 0;
    }

    @Override
    public Collection<Map<String, Object>> queryColumn(EruptModel eruptModel, List<Column> columns, EruptQuery eruptQuery) {
        return null;
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
