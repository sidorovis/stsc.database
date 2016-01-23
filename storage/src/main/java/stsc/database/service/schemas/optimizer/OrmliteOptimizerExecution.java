package stsc.database.service.schemas.optimizer;

import java.util.Date;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "executions")
public final class OrmliteOptimizerExecution {

	@DatabaseField(generatedId = true, columnName = "id", canBeNull = false)
	private Integer id;

	@DatabaseField(columnName = "experiment_id", canBeNull = false)
	private Integer experimentId;

	@DatabaseField(columnName = "index_number", canBeNull = false)
	private Integer indexNumber;

	@DatabaseField(columnName = "execution_name", canBeNull = false)
	private String executionName;

	@DatabaseField(columnName = "algorithm_name", canBeNull = false)
	private String algorithmName;

	@DatabaseField(columnName = "execution_type", canBeNull = false)
	private String executionType;

	@DatabaseField(columnName = "created_at", dataType = DataType.DATE)
	private Date createdAt;

	@DatabaseField(columnName = "updated_at", dataType = DataType.DATE)
	private Date updatedAt;

	private OrmliteOptimizerExecution() {
	}

	public OrmliteOptimizerExecution(int experimentId, int indexNumber) {
		this();
		this.experimentId = experimentId;
		this.indexNumber = indexNumber;
	}

	public String getExecutionName() {
		return executionName;
	}

	public void setExecutionName(String executionName) {
		this.executionName = executionName;
	}

	public String getAlgorithmName() {
		return algorithmName;
	}

	public void setAlgorithmName(String algorithmName) {
		this.algorithmName = algorithmName;
	}

	public String getExecutionType() {
		return executionType;
	}

	public void setExecutionType(String executionType) {
		this.executionType = executionType;
	}

	public Integer getId() {
		return id;
	}

	public Integer getExperimentId() {
		return experimentId;
	}

	public Integer getIndexNumber() {
		return indexNumber;
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
