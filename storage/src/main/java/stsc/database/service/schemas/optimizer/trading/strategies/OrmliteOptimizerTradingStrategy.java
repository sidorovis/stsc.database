package stsc.database.service.schemas.optimizer.trading.strategies;

import java.util.Date;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "trading_strategies")
public final class OrmliteOptimizerTradingStrategy {

	public final static String USER_ID_COLUMN_NAME = "user_id";

	@DatabaseField(generatedId = true, columnName = "id", canBeNull = false)
	private Integer id;

	@DatabaseField(columnName = USER_ID_COLUMN_NAME, canBeNull = false, defaultValue = "0")
	private int userId;

	@DatabaseField(columnName = "experiment_id", canBeNull = false)
	private Integer experimentId;

	@DatabaseField(columnName = "period_from", dataType = DataType.DATE, canBeNull = false)
	private Date periodFrom;

	@DatabaseField(columnName = "period_to", dataType = DataType.DATE, canBeNull = false)
	private Date periodTo;

	@DatabaseField(columnName = "created_at", dataType = DataType.DATE)
	private Date createdAt;

	@DatabaseField(columnName = "updated_at", dataType = DataType.DATE)
	private Date updatedAt;

	private OrmliteOptimizerTradingStrategy() {
		this.periodFrom = new Date();
		this.periodTo = new Date();
	}
	
	public OrmliteOptimizerTradingStrategy(int experimentId) {
		this();
		this.experimentId = experimentId;
	}

	public Integer getId() {
		return id;
	}

	public int getUserId() {
		return userId;
	}

	public int getExperimentId() {
		return experimentId;
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
