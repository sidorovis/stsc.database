package stsc.database.service.schemas.optimizer.experiments;

import java.util.Date;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "sub_execution_domens")
public final class OrmliteOptimizerSubExecutionDomen {

	@DatabaseField(generatedId = true, columnName = "id", canBeNull = false)
	private Integer id;

	@DatabaseField(columnName = "execution_id", canBeNull = false)
	private Integer executionId;

	@DatabaseField(columnName = "parameter_name", canBeNull = false)
	private String parameterName;

	@DatabaseField(columnName = "parameter_domen", canBeNull = false)
	private String parameterDomen;

	@DatabaseField(columnName = "created_at", dataType = DataType.DATE)
	private Date createdAt;

	@DatabaseField(columnName = "updated_at", dataType = DataType.DATE)
	private Date updatedAt;

	private OrmliteOptimizerSubExecutionDomen() {
	}

	public OrmliteOptimizerSubExecutionDomen(int executionId) {
		this();
		this.executionId = executionId;
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

	public String getParameterDomen() {
		return parameterDomen;
	}

	public void setParameterDomen(String parameterDomen) {
		this.parameterDomen = parameterDomen;
	}

	public Integer getExecutionId() {
		return executionId;
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
