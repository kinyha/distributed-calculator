use crate::domain::{CalculationRequest, CalculationResult};

pub trait CalculationCache: Send + Sync {
    fn get(&self, request: &CalculationRequest) -> Option<CalculationResult>;
    fn store(&self, request: &CalculationRequest, result: &CalculationResult);
}