package stsc.database.service.schemas.optimizer;

import java.util.Date;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "experiments")
public final class OrmliteOptimizerExperiment {

	public final static String USER_ID_COLUMN_NAME = "user_id";

	@DatabaseField(generatedId = true, columnName = "id", canBeNull = false)
	private Integer id;

	@DatabaseField(columnName = USER_ID_COLUMN_NAME, canBeNull = false, defaultValue = "0")
	private int userId;

	@DatabaseField(columnName = "title", canBeNull = false)
	private String title;

	@DatabaseField(columnName = "description", canBeNull = false)
	private String description;

	@DatabaseField(columnName = "period_from", dataType = DataType.DATE, canBeNull = false)
	private Date periodFrom;

	@DatabaseField(columnName = "period_to", dataType = DataType.DATE, canBeNull = false)
	private Date periodTo;

	@DatabaseField(columnName = "commited", dataType = DataType.BOOLEAN, canBeNull = false)
	private boolean commited;

	@DatabaseField(columnName = "processed", dataType = DataType.BOOLEAN, canBeNull = false)
	private boolean processed;

	@DatabaseField(columnName = "created_at", dataType = DataType.DATE)
	private Date createdAt;

	@DatabaseField(columnName = "updated_at", dataType = DataType.DATE)
	private Date updatedAt;

	private OrmliteOptimizerExperiment() {
		// this.userId = 0;
		this.periodFrom = new Date();
		this.periodTo = new Date();
	}

	public OrmliteOptimizerExperiment(String title, String description) {
		this();
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

	public Date getPeriodFrom() {
		return periodFrom;
	}

	public void setPeriodFrom(Date periodFrom) {
		this.periodFrom = periodFrom;
	}

	public Date getPeriodTo() {
		return periodTo;
	}

	public void setPeriodTo(Date periodTo) {
		this.periodTo = periodTo;
	}

	public void setCommited() {
		this.commited = true;
	}

	public void setProcessed() {
		this.processed = true;
	}

	public boolean isProcessed() {
		return this.processed;
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
