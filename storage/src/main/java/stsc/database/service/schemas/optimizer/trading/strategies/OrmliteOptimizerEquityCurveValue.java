package stsc.database.service.schemas.optimizer.trading.strategies;

import java.util.Date;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "equity_curve_values")
public final class OrmliteOptimizerEquityCurveValue {

	@DatabaseField(generatedId = true, columnName = "id", canBeNull = false)
	private Integer id;

	@DatabaseField(columnName = "metrics_tuple_id", canBeNull = false)
	private Integer metricsTupleId;

	@DatabaseField(columnName = "timestamp", canBeNull = false, dataType = DataType.DATE)
	private Date timestamp;

	@DatabaseField(columnName = "value", canBeNull = false)
	private Double value;

	@DatabaseField(columnName = "created_at", dataType = DataType.DATE)
	private Date createdAt;

	@DatabaseField(columnName = "updated_at", dataType = DataType.DATE)
	private Date updatedAt;

	private OrmliteOptimizerEquityCurveValue() {
	}

	public OrmliteOptimizerEquityCurveValue(int metricsTupleId) {
		this();
		this.metricsTupleId = metricsTupleId;
	}

	public Integer getId() {
		return id;
	}

	public Integer getMetricsTupleId() {
		return metricsTupleId;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
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
