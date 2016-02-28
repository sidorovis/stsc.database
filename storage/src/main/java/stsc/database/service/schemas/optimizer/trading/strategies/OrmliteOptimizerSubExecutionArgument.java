package stsc.database.service.schemas.optimizer.trading.strategies;

import java.util.Date;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "sub_execution_arguments")
public final class OrmliteOptimizerSubExecutionArgument {

	@DatabaseField(generatedId = true, columnName = "id", canBeNull = false)
	private Integer id;

	@DatabaseField(columnName = "execution_instance_id", canBeNull = false)
	private int executionInstanceId;

	@DatabaseField(columnName = "sub_execution_domen_id", canBeNull = false)
	private Integer subExecutionDomenId;

	@DatabaseField(columnName = "argument_index", canBeNull = false)
	private Integer argumentIndex;

	@DatabaseField(columnName = "created_at", dataType = DataType.DATE)
	private Date createdAt;

	@DatabaseField(columnName = "updated_at", dataType = DataType.DATE)
	private Date updatedAt;

	private OrmliteOptimizerSubExecutionArgument() {
	}

	public OrmliteOptimizerSubExecutionArgument(int executionInstanceId) {
		this();
		// this.userId = 0;
		this.executionInstanceId = executionInstanceId;
	}

	public Integer getId() {
		return id;
	}

	public int getExecutionInstanceId() {
		return executionInstanceId;
	}

	public int getSubExecutionDomenId() {
		return subExecutionDomenId;
	}

	public void setSubExecutionDomenId(int subExecutionDomenId) {
		this.subExecutionDomenId = subExecutionDomenId;
	}

	public int getArgumentIndex() {
		return argumentIndex;
	}

	public void setArgumentIndex(int argumentIndex) {
		this.argumentIndex = argumentIndex;
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
