package stsc.database.service.schemas.optimizer.trading.strategies;

import java.util.Date;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "integer_arguments")
public final class OrmliteOptimizerIntegerArgument {

	@DatabaseField(generatedId = true, columnName = "id", canBeNull = false)
	private Integer id;

	@DatabaseField(columnName = "execution_instance_id", canBeNull = false)
	private int executionInstanceId;

	@DatabaseField(columnName = "parameter_name", canBeNull = false)
	private String parameterName;

	@DatabaseField(columnName = "parameter_value", canBeNull = false)
	private Integer parameterValue;

	@DatabaseField(columnName = "created_at", dataType = DataType.DATE)
	private Date createdAt;

	@DatabaseField(columnName = "updated_at", dataType = DataType.DATE)
	private Date updatedAt;

	private OrmliteOptimizerIntegerArgument() {
	}

	public OrmliteOptimizerIntegerArgument(int executionInstanceId) {
		this();
		// this.userId = 0;
		this.executionInstanceId = executionInstanceId;
	}

	public Integer getId() {
		return id;
	}

	public String getParameterName() {
		return parameterName;
	}

	public void setParameterName(String parameterName) {
		this.parameterName = parameterName;
	}

	public int getParameterValue() {
		return parameterValue;
	}

	public void setParameterValue(int parameterValue) {
		this.parameterValue = parameterValue;
	}

	public int getExecutionInstanceId() {
		return executionInstanceId;
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
