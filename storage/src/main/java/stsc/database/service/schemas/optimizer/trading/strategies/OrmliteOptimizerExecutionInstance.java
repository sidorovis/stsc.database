package stsc.database.service.schemas.optimizer.trading.strategies;

import java.util.Date;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "execution_instances")
public final class OrmliteOptimizerExecutionInstance {

	@DatabaseField(generatedId = true, columnName = "id", canBeNull = false)
	private Integer id;

	@DatabaseField(columnName = "trading_strategy_id", canBeNull = false)
	private Integer tradingStrategyId;

	@DatabaseField(columnName = "index_number", canBeNull = false)
	private Integer indexNumber;

	@DatabaseField(columnName = "execution_instance_name", canBeNull = false)
	private String executionInstanceName;

	@DatabaseField(columnName = "algorithm_name", canBeNull = false)
	private String algorithmName;

	@DatabaseField(columnName = "algorithm_type", canBeNull = false)
	private String algorithmType;

	@DatabaseField(columnName = "created_at", dataType = DataType.DATE)
	private Date createdAt;

	@DatabaseField(columnName = "updated_at", dataType = DataType.DATE)
	private Date updatedAt;

	private OrmliteOptimizerExecutionInstance() {
	}

	public OrmliteOptimizerExecutionInstance(int tradingStrategyId) {
		this();
		// this.userId = 0;
		this.tradingStrategyId = tradingStrategyId;
	}

	public Integer getId() {
		return id;
	}

	public int getTradingStrategyId() {
		return tradingStrategyId;
	}

	public int getIndexNumber() {
		return indexNumber;
	}

	public void setIndexNumber(int indexNumber) {
		this.indexNumber = indexNumber;
	}

	public String getExecutionInstanceName() {
		return executionInstanceName;
	}

	public void setExecutionInstanceName(String executionInstanceName) {
		this.executionInstanceName = executionInstanceName;
	}

	public String getAlgorithmName() {
		return algorithmName;
	}

	public void setAlgorithmName(String algorithmName) {
		this.algorithmName = algorithmName;
	}

	public String getAlgorithmType() {
		return algorithmType;
	}

	public void setAlgorithmType(String algorithmType) {
		this.algorithmType = algorithmType;
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
