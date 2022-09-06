package org.yanhuang.base.filerepo.entity;

import xyz.erupt.annotation.Erupt;
import xyz.erupt.annotation.EruptField;
import xyz.erupt.annotation.sub_erupt.Tree;
import xyz.erupt.annotation.sub_field.Edit;
import xyz.erupt.annotation.sub_field.EditType;
import xyz.erupt.annotation.sub_field.Readonly;
import xyz.erupt.annotation.sub_field.View;
import xyz.erupt.annotation.sub_field.sub_edit.ReferenceTreeType;
import xyz.erupt.jpa.model.BaseModel;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "file_meta")
@Erupt(
		name = "文件目录",
		orderBy = "FileMeta.updateTime desc",
		tree = @Tree(id = "path", label = "name", pid = "directory")
)
public class FileMeta  extends BaseModel {

	@EruptField(
			views = @View(title = "路径",sortable = true),
			edit = @Edit(title = "路径", readonly = @Readonly(add = false,edit = false), notNull = true)
	)
	private String path;

	@EruptField(
			views = @View(title = "名称",sortable = true),
			edit = @Edit(title = "名称", notNull = true)
	)
	private String name;

	@EruptField(
			views = @View(title = "大小",sortable = true),
			edit = @Edit(title = "大小", readonly = @Readonly(add = false,edit = false), notNull = true)
	)
	private long size;

	@EruptField(
			views = @View(title = "修改时间",sortable = true),
			edit = @Edit(title = "修改时间", readonly = @Readonly(add = false,edit = false), notNull = true)
	)
	private LocalDateTime updateTime;

	@EruptField(
			views = @View(title = "创建时间",sortable = true),
			edit = @Edit(title = "创建时间", readonly = @Readonly(add = false,edit = false), notNull = true)
	)
	private LocalDateTime createTime;

	@ManyToOne
	@EruptField(
			edit = @Edit(
					title = "所在目录",
					type = EditType.REFERENCE_TREE,
					referenceTreeType = @ReferenceTreeType(pid = "directory.path")
			)
	)
	private RepoDirectory directory;

}
