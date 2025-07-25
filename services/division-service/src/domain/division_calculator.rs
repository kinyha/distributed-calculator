use rust_decimal::Decimal;

pub struct DivisionCalculator;

impl DivisionCalculator {
    pub fn new() -> Self {
        Self
    }

    pub fn divide(&self, operand1: Decimal, operand2: Decimal) -> Result<Decimal, String> {
        if operand2.is_zero() {
            return Err("Division by zero is not allowed".to_string());
        }
        Ok(operand1 / operand2)
    }
}