package stsc.database.service.schemas.optimizer.experiments;

import java.util.Date;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "experiment_locks")
public final class OrmliteOptimizerExperimentLock {

	@DatabaseField(generatedId = true, columnName = "id", canBeNull = false, unique = true)
	private Integer id;

	@DatabaseField(columnName = "experiment_id", canBeNull = false, unique = true)
	private Integer experimentId;

	@DatabaseField(columnName = "created_at", dataType = DataType.DATE)
	private Date createdAt;

	@DatabaseField(columnName = "updated_at", dataType = DataType.DATE)
	private Date updatedAt;

	private OrmliteOptimizerExperimentLock() {
	}

	public OrmliteOptimizerExperimentLock(int experimentId) {
		this();
		this.experimentId = experimentId;
	}

	public Integer getId() {
		return id;
	}

	public Integer getExperimentId() {
		return experimentId;
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
