use rust_decimal::Decimal;
use crate::domain::CalculationRequest;

pub struct InputValidator;

impl InputValidator {
    pub fn new() -> Self {
        Self
    }

    pub fn validate(&self, request: &CalculationRequest) -> Result<(), String> {
        if request.request_id.trim().is_empty() {
            return Err("Request ID cannot be blank".to_string());
        }

        let scale_limit = 10;
        if request.operand1.scale() > scale_limit || request.operand2.scale() > scale_limit {
            return Err("Operands cannot have more than 10 decimal places".to_string());
        }

        let limit = Decimal::from(10_000_000_000_i64); // 10 billion
        if request.operand1.abs() > limit || request.operand2.abs() > limit {
            return Err("Operands cannot exceed 10 billion".to_string());
        }

        if request.operand2.is_zero() {
            return Err("Division by zero is not allowed".to_string());
        }

        Ok(())
    }
}