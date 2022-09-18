package org.yanhuang.base.filerepo.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.yanhuang.base.filerepo.config.Consts;
import xyz.erupt.annotation.Erupt;
import xyz.erupt.annotation.EruptField;
import xyz.erupt.annotation.sub_erupt.LinkTree;
import xyz.erupt.annotation.sub_field.*;
import xyz.erupt.annotation.sub_field.sub_edit.ReferenceTreeType;
import xyz.erupt.core.annotation.EruptDataProcessor;
import xyz.erupt.jpa.model.BaseModel;

import javax.persistence.*;
import java.nio.file.Path;
import java.time.LocalDateTime;

@Entity
@Erupt(
		primaryKeyCol = "path",
		name = "文件列表",
		orderBy = "RepoFile.name",
		linkTree = @LinkTree(field = "directory") //field 的值为类中支持树组件字段
)
@EruptDataProcessor(Consts.DATA_SERVICE_FILE)
@Getter
@Setter
public class RepoFile {

	/**
	 * 文件对应的系统path
	 */
	private transient Path filePath;

	@Id
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
			views = @View(title = "修改时间",sortable = true, type = ViewType.DATE_TIME),
			edit = @Edit(title = "修改时间", readonly = @Readonly(add = false,edit = false), notNull = true)
	)
	private LocalDateTime updateTime;

	@EruptField(
			views = @View(title = "创建时间", sortable = true, type = ViewType.DATE_TIME),
			edit = @Edit(title = "创建时间", readonly = @Readonly(add = false, edit = false), notNull = true)
	)
	private LocalDateTime createTime;

	@ManyToOne
	@EruptField(
			views = @View(title = "所在目录", column = "directory"),
			edit = @Edit(
					title = "所在目录",
					type = EditType.REFERENCE_TREE,
					referenceTreeType = @ReferenceTreeType(id = "path", label = "name", pid = "parent.path")
			)
	)
	private RepoDirectory directory;

	@EruptField(
			views = @View(title = "类型", sortable = true),
			edit = @Edit(title = "类型", readonly = @Readonly(add = false, edit = false), notNull = true)
	)
	private Consts.FileType fileType;

}
