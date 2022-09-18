package org.yanhuang.base.filerepo.entity;

import lombok.Getter;
import lombok.Setter;
import org.yanhuang.base.filerepo.config.Consts;
import xyz.erupt.annotation.Erupt;
import xyz.erupt.annotation.EruptField;
import xyz.erupt.annotation.sub_erupt.Tree;
import xyz.erupt.annotation.sub_field.Edit;
import xyz.erupt.annotation.sub_field.EditType;
import xyz.erupt.annotation.sub_field.sub_edit.ReferenceTreeType;
import xyz.erupt.core.annotation.EruptDataProcessor;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
@Erupt(
		primaryKeyCol = "path",
		name = "文件目录",
		orderBy = "RepoDirectory.name",
		tree = @Tree(id = "path", label = "name", pid = "parent.path",expandLevel = 1)
)
@EruptDataProcessor(Consts.DATA_SERVICE_DIR)
@Getter
@Setter
public class RepoDirectory extends RepoFile {

	@ManyToOne
	@EruptField(
			edit = @Edit(
					title = "父级目录",
					type = EditType.REFERENCE_TREE,
					referenceTreeType = @ReferenceTreeType(id = "path", pid = "parent.path", expandLevel = 1)
			)
	)
	private RepoDirectory parent;

}
