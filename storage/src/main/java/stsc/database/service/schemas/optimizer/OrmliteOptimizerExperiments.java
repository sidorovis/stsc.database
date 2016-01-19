package stsc.database.service.schemas.optimizer;

import java.util.Date;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "experiments")
public class OrmliteOptimizerExperiments {

	@DatabaseField(generatedId = true, columnName = "id", canBeNull = false)
	private Integer id;

	@DatabaseField(columnName = "user_id", canBeNull = false)
	private Integer userId;

	@DatabaseField(columnName = "title", canBeNull = false)
	private String title;

	@DatabaseField(columnName = "description", canBeNull = false)
	private String description;

	@DatabaseField(columnName = "created_at", dataType = DataType.DATE)
	private Date createdAt;

	@DatabaseField(columnName = "updated_at", dataType = DataType.DATE)
	private Date updatedAt;

	@SuppressWarnings("unused")
	private OrmliteOptimizerExperiments() {
		// for ormlite
	}

	public OrmliteOptimizerExperiments(String title, String description) {
		setTitle(title);
		setDescription(description);
	}

	public Integer getId() {
		return id;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitle() {
		return title;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

	public void setCreatedAt() {
		if (getId() == null) {
			this.createdAt = new Date();
		}
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setUpdatedAt() {
		this.updatedAt = new Date();
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

}
