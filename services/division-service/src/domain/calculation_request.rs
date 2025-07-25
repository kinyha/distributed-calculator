use rust_decimal::Decimal;
use serde::{Deserialize, Serialize};

#[derive(Debug, Clone, Deserialize, Serialize)]
pub struct CalculationRequest {
    #[serde(rename = "operand1")]
    pub operand1: Decimal,
    #[serde(rename = "operand2")]
    pub operand2: Decimal,
    #[serde(rename = "requestId")]
    pub request_id: String,
}