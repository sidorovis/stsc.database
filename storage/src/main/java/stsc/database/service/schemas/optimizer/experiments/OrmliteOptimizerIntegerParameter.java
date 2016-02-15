package stsc.database.service.schemas.optimizer.experiments;

import java.util.Date;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "integer_parameters")
public final class OrmliteOptimizerIntegerParameter {

	@DatabaseField(generatedId = true, columnName = "id", canBeNull = false)
	private Integer id;

	@DatabaseField(columnName = "execution_id", canBeNull = false)
	private Integer executionId;

	@DatabaseField(columnName = "parameter_name", canBeNull = false)
	private String parameterName;

	@DatabaseField(columnName = "domen_from", canBeNull = false)
	private Integer from;

	@DatabaseField(columnName = "domen_step", canBeNull = false)
	private Integer step;

	@DatabaseField(columnName = "domen_to", canBeNull = false)
	private Integer to;

	@DatabaseField(columnName = "created_at", dataType = DataType.DATE)
	private Date createdAt;

	@DatabaseField(columnName = "updated_at", dataType = DataType.DATE)
	private Date updatedAt;

	private OrmliteOptimizerIntegerParameter() {
	}

	public OrmliteOptimizerIntegerParameter(int executionId) {
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

	public int getFrom() {
		return from;
	}

	public void setFrom(int from) {
		this.from = from;
	}

	public int getStep() {
		return step;
	}

	public void setStep(int step) {
		this.step = step;
	}

	public int getTo() {
		return to;
	}

	public void setTo(int to) {
		this.to = to;
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
