package org.yanhuang.base.filerepo.entity;

import xyz.erupt.annotation.Erupt;
import xyz.erupt.annotation.sub_erupt.Tree;
import xyz.erupt.jpa.model.BaseModel;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "repo_directory")
@Erupt(
		name = "文件目录",
		orderBy = "FileMeta.updateTime desc",
		tree = @Tree(id = "path", label = "name", pid = "directory")
)
public class DirectoryList extends BaseModel {
}
