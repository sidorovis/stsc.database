package stsc.database.service.schemas.optimizer.trading.strategies;

import java.util.Date;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "integer_metrics")
public final class OrmliteOptimizerIntegerMetric {

	@DatabaseField(generatedId = true, columnName = "id", canBeNull = false)
	private Integer id;

	@DatabaseField(columnName = "metrics_tuple_id", canBeNull = false)
	private Integer metricsTupleId;

	@DatabaseField(columnName = "metric_type", canBeNull = false)
	private String metricType;

	@DatabaseField(columnName = "metric_value", canBeNull = false)
	private Integer metricValue;

	@DatabaseField(columnName = "created_at", dataType = DataType.DATE)
	private Date createdAt;

	@DatabaseField(columnName = "updated_at", dataType = DataType.DATE)
	private Date updatedAt;

	private OrmliteOptimizerIntegerMetric() {
	}

	public OrmliteOptimizerIntegerMetric(int metricsTupleId) {
		this();
		this.metricsTupleId = metricsTupleId;
	}

	public Integer getId() {
		return id;
	}

	public String getMetricType() {
		return metricType;
	}

	public void setMetricType(String metricType) {
		this.metricType = metricType;
	}

	public int getMetricValue() {
		return metricValue;
	}

	public void setMetricValue(int metricValue) {
		this.metricValue = metricValue;
	}

	public Integer getMetricsTupleId() {
		return metricsTupleId;
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
