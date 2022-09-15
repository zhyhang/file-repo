package org.yanhuang.base.filerepo.service;

import org.yanhuang.base.filerepo.entity.RepoFile;
import xyz.erupt.core.query.Column;
import xyz.erupt.core.query.EruptQuery;
import xyz.erupt.core.service.IEruptDataService;
import xyz.erupt.core.view.EruptModel;
import xyz.erupt.core.view.Page;

import java.time.LocalDateTime;
import java.util.*;

public class DiskFileDataService  implements IEruptDataService {

    @Override
    public Object findDataById(EruptModel eruptModel, Object id) {
        if (eruptModel != null) {
            return eruptModel;
        }else{
            final RepoFile file = new RepoFile();
            file.setPath("/tmp/001.txt");
            file.setName("001.txt");
            file.setId(0L);
        }
        return null;
    }

    @Override
    public Page queryList(EruptModel eruptModel, Page page, EruptQuery eruptQuery) {
        final Page pageRet = new Page(0,10,"asc");
        final RepoFile f1 = new RepoFile();
        f1.setPath("/tmp/f1.txt");
        f1.setName("f1.txt");
        f1.setId(1L);
        final RepoFile f2 = new RepoFile();
        f2.setPath("/tmp/f2.txt");
        f2.setName("f2.txt");
        f2.setId(2L);
        pageRet.setList(List.of(new HashMap<>(Map.of("id", "1", "path", "/tmp/f1.txt", "name", "f1.txt", "size", "2048")),
                new HashMap<>(Map.of("id", "1", "path", "/tmp/f2.txt", "name", "f2.txt", "size", "4096"))));
        return pageRet;
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
