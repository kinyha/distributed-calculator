use chrono::{DateTime, Utc};
use rust_decimal::Decimal;
use serde::{Deserialize, Serialize};
use std::time::Duration;

#[derive(Debug, Clone, Deserialize, Serialize)]
pub struct CalculationResult {
    pub result: Decimal,
    #[serde(rename = "processingTime")]
    pub processing_time: String, // ISO 8601 duration format
    pub cached: bool,
    pub timestamp: DateTime<Utc>,
}

impl CalculationResult {
    pub fn new(result: Decimal, processing_time: Duration, cached: bool) -> Self {
        Self {
            result,
            processing_time: format!("PT{:.3}S", processing_time.as_secs_f64()),
            cached,
            timestamp: Utc::now(),
        }
    }
}