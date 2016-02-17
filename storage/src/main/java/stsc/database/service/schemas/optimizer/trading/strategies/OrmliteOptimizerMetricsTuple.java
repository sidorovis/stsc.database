package stsc.database.service.schemas.optimizer.trading.strategies;

import java.util.Date;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "metrics_tuples")
public final class OrmliteOptimizerMetricsTuple {

	@DatabaseField(generatedId = true, columnName = "id", canBeNull = false)
	private Integer id;

	@DatabaseField(columnName = "trading_strategy_id", canBeNull = false)
	private Integer tradingStrategyId;

	@DatabaseField(columnName = "created_at", dataType = DataType.DATE)
	private Date createdAt;

	@DatabaseField(columnName = "updated_at", dataType = DataType.DATE)
	private Date updatedAt;

	private OrmliteOptimizerMetricsTuple() {
	}

	public OrmliteOptimizerMetricsTuple(int tradingStrategyId) {
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
